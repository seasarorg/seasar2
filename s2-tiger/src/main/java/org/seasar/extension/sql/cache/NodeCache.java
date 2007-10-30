/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.sql.cache;

import java.io.InputStream;
import java.io.Reader;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.parser.SqlParserImpl;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.ReaderUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author higa
 * 
 */
public final class NodeCache {

    private static volatile boolean initialized;

    private static ConcurrentHashMap<String, Node> nodeCache = new ConcurrentHashMap<String, Node>(
            200);

    static {
        initialize();
    }

    /**
     * キャッシュしているノードを返します。 まだ、解析していないときにはファイルからSQLを取得して解析し、 結果をキャッシュします。
     * 
     * @param path
     *            パス。examples/dao/EmployeeDao_selectXxx.sqlのような'/'区切りのパスです。
     * @param dbmsName
     *            DBMS名
     * @return キャッシュしているノード
     */
    public static Node getNode(String path, String dbmsName) {
        if (!initialized) {
            initialize();
        }
        if (path.endsWith(".sql")) {
            path = path.substring(0, path.length() - 4);
        }
        String s = null;
        Node node = null;
        if (dbmsName != null) {
            s = path + "_" + dbmsName;
            node = nodeCache.get(s);
            if (node != null) {
                return node;
            }
            node = createNode(s);
            if (node != null) {
                Node current = nodeCache.putIfAbsent(s, node);
                return current != null ? current : node;
            }
        }
        node = nodeCache.get(path);
        if (node != null) {
            return node;
        }
        node = createNode(path);
        if (node != null) {
            Node current = nodeCache.putIfAbsent(s != null ? s : path, node);
            return current != null ? current : node;
        }
        return null;
    }

    private static Node createNode(String path) {
        InputStream is = ResourceUtil.getResourceAsStreamNoException(path,
                "sql");
        if (is == null) {
            return null;
        }
        Reader reader = InputStreamReaderUtil.create(is, "UTF-8");
        String sql = ReaderUtil.readText(reader);
        return new SqlParserImpl(sql).parse();
    }

    /**
     * 初期化します。
     */
    public static void initialize() {
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    /**
     * キャッシュをクリアします。
     */
    public static void clear() {
        nodeCache.clear();
        initialized = false;
    }
}

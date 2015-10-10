/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import org.seasar.extension.sql.node.SqlNode;
import org.seasar.extension.sql.parser.SqlParserImpl;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.ReaderUtil;
import org.seasar.framework.util.ResourceUtil;

import static org.seasar.framework.util.tiger.CollectionsUtil.*;

/**
 * @author higa
 * 
 */
public class NodeCache {

    private static final Node NOT_FOUND = new SqlNode("NOT FOUND");

    private static volatile boolean initialized;

    private static ConcurrentHashMap<String, Node> nodeCache = newConcurrentHashMap(200);

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
        return getNode(path, dbmsName, true);
    }

    /**
     * キャッシュしているノードを返します。 まだ、解析していないときにはファイルからSQLを取得して解析し、 結果をキャッシュします。
     * 
     * @param path
     *            パス。examples/dao/EmployeeDao_selectXxx.sqlのような'/'区切りのパスです。
     * @param dbmsName
     *            DBMS名
     * @param allowVariableSql
     *            可変なSQLを許可する場合は<code>true</code>
     * @return キャッシュしているノード
     */
    public static Node getNode(String path, String dbmsName,
            boolean allowVariableSql) {
        if (!initialized) {
            initialize();
        }
        if (path.endsWith(".sql")) {
            path = path.substring(0, path.length() - 4);
        }
        String key = path;
        if (!allowVariableSql) {
            key = key + "_disallowVariableSql";
        }
        Node node = null;
        if (dbmsName != null) {
            String dbmsSpecificKey = key + "_" + dbmsName;
            node = nodeCache.get(dbmsSpecificKey);
            if (node != null) {
                if (node != NOT_FOUND) {
                    return node;
                }
            } else {
                String dbmsSpecificPath = path + "_" + dbmsName;
                node = createNode(dbmsSpecificPath, allowVariableSql);
                if (node != null) {
                    return putIfAbsent(nodeCache, dbmsSpecificKey, node);
                }
                putIfAbsent(nodeCache, dbmsSpecificKey, NOT_FOUND);
            }
        }
        node = nodeCache.get(key);
        if (node != null) {
            if (node != NOT_FOUND) {
                return node;
            }
        } else {
            node = createNode(path, allowVariableSql);
            if (node != null) {
                return putIfAbsent(nodeCache, key, node);
            }
            putIfAbsent(nodeCache, key, NOT_FOUND);
        }
        return null;
    }

    private static Node createNode(String path, boolean allowVariableSql) {
        InputStream is = ResourceUtil.getResourceAsStreamNoException(path,
                "sql");
        if (is == null) {
            return null;
        }
        Reader reader = InputStreamReaderUtil.create(is, "UTF-8");
        String sql = ReaderUtil.readText(reader);
        if (sql.length() > 0 && sql.charAt(0) == '\uFEFF') {
            sql = sql.substring(1);
        }
        return new SqlParserImpl(sql, allowVariableSql).parse();
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

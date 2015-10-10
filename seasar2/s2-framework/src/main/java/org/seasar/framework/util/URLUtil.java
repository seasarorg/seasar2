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
package org.seasar.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.SRuntimeException;

/**
 * {@link URL}を扱うユーティリティ・クラスです。
 * 
 * @author higa
 */
public class URLUtil {

    /** プロトコルを正規化するためのマップ */
    protected static final Map CANONICAL_PROTOCOLS = new HashMap();
    static {
        CANONICAL_PROTOCOLS.put("wsjar", "jar"); // WebSphereがJarファイルのために使用する固有のプロトコル
        CANONICAL_PROTOCOLS.put("vfsfile", "file"); // JBossAS5がファイルシステムのために使用する固有のプロトコル
    }

    /**
     * URLをオープンして{@link InputStream}を返します。
     * 
     * @param url
     *            URL
     * @return URLが表すリソースを読み込むための{@link InputStream}
     */
    public static InputStream openStream(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * URLが参照するリモートオブジェクトへの接続を表す{@link URLConnection}オブジェクトを返します。
     * 
     * @param url
     *            URL
     * @return URLへの{@link URLConnection}オブジェクト
     */
    public static URLConnection openConnection(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * <code>String</code>表現から<code>URL</code>オブジェクトを作成します。
     * 
     * @param spec
     *            <code>URL</code>として構文解析される<code>String</code>
     * @return <code>URL</code>
     */
    public static URL create(String spec) {
        try {
            return new URL(spec);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 指定されたコンテキスト内の指定された仕様で構文解析することによって、<code>URL</code>を生成します。
     * 
     * @param context
     *            仕様を構文解析するコンテキスト
     * @param spec
     *            <code>URL</code>として構文解析される<code>String</code>
     * @return <code>URL</code>
     */
    public static URL create(URL context, String spec) {
        try {
            return new URL(context, spec);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 特定の符号化方式を使用して文字列を<code>application/x-www-form-urlencoded</code>形式に変換します。
     * 
     * @param s
     *            変換対象の String
     * @param enc
     *            サポートされる文字エンコーディングの名前
     * @return 変換後の<code>String</code>
     */
    public static String encode(final String s, final String enc) {
        try {
            return URLEncoder.encode(s, enc);
        } catch (final UnsupportedEncodingException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 特別な符号化方式を使用して<code>application/x-www-form-urlencoded</code>文字列をデコードします。
     * 
     * @param s
     *            デコード対象の<code>String</code>
     * @param enc
     *            サポートされる文字エンコーディングの名前
     * @return 新しくデコードされた String
     */
    public static String decode(final String s, final String enc) {
        try {
            return URLDecoder.decode(s, enc);
        } catch (final UnsupportedEncodingException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * プロトコルを正規化して返します。
     * 
     * @param protocol
     *            プロトコル
     * @return 正規化されたプロトコル
     */
    public static String toCanonicalProtocol(final String protocol) {
        final String canonicalProtocol = (String) CANONICAL_PROTOCOLS
                .get(protocol);
        if (canonicalProtocol != null) {
            return canonicalProtocol;
        }
        return protocol;
    }

    /**
     * URLが示すJarファイルの{@link File}オブジェクトを返します。
     * 
     * @param fileUrl
     *            JarファイルのURL
     * @return Jarファイルの{@link File}
     */
    public static File toFile(final URL fileUrl) {
        try {
            final String path = URLDecoder.decode(fileUrl.getPath(), "UTF-8");
            return new File(path).getAbsoluteFile();
        } catch (final Exception e) {
            throw new SRuntimeException("ESSR0091", new Object[] { fileUrl }, e);
        }
    }

    /**
     * <a
     * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4167874">このバグ<
     * /a>に対する対応です。
     * 
     */
    public static void disableURLCaches() {
        BeanDesc bd = BeanDescFactory.getBeanDesc(URLConnection.class);
        FieldUtil.set(bd.getField("defaultUseCaches"), null, Boolean.FALSE);
    }

}

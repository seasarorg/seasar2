/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.j2ee;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.util.StringUtil;

/**
 * JNDIのリソースを解決するためのロケータです。
 * 
 * @author koichk
 * 
 */
public class JndiResourceLocator {
    /**
     * ENCプレフィックスです。
     */
    public static final String ENC_PREFIX = "java:comp/env/";

    /**
     * マジックコンポーネントです。
     */
    protected static final Map MAGIC_COMPONENTS = new HashMap();
    static {
        MAGIC_COMPONENTS
                .put("java:comp/UserTransaction", "jta/UserTransaction");
        MAGIC_COMPONENTS.put("java:comp/TransactionSynchronizationRegistry",
                "jta/TransactionSynchronizationRegistry");
    }

    /**
     * ルックアップします。
     * 
     * @param name
     *            名前
     * @return ルックアップの結果
     * @throws NamingException
     *             {@link NamingException}が発生した場合
     */
    public static Object lookup(final String name) throws NamingException {
        return lookup(name, null);
    }

    /**
     * ルックアップします。
     * 
     * @param name
     *            名前
     * @param env
     *            環境
     * @return ルックアップした結果
     * @throws NamingException
     *             {@link NamingException}が発生した場合
     */
    public static Object lookup(final String name, final Hashtable env)
            throws NamingException {
        final InitialContext context = new InitialContext(env);
        try {
            return context.lookup(name);
        } finally {
            context.close();
        }
    }

    /**
     * 名前を解決します。
     * 
     * @param name
     *            名前
     * @return 解決された名前
     */
    public static String resolveName(final Name name) {
        return resolveName(name.toString());
    }

    /**
     * 名前を解決します。
     * 
     * @param name
     *            名前
     * @return 解決された名前
     */
    public static String resolveName(final String name) {
        String n = name;
        if (MAGIC_COMPONENTS.containsKey(name)) {
            n = (String) MAGIC_COMPONENTS.get(name);
        }
        if (name.startsWith(ENC_PREFIX)) {
            n = name.substring(ENC_PREFIX.length());
        }
        return StringUtil.replace(n, "/", ContainerConstants.NS_SEP_STR);
    }
}

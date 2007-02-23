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
package org.seasar.extension.j2ee;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.util.StringUtil;

public class JndiResourceLocator {
    public static final String ENC_PREFIX = "java:comp/env/";

    protected static final Map MAGIC_COMPONENTS = new HashMap();
    static {
        MAGIC_COMPONENTS
                .put("java:comp/UserTransaction", "jta/UserTransaction");
        MAGIC_COMPONENTS.put("java:comp/TransactionSynchronizationRegistry",
                "jta/TransactionSynchronizationRegistry");
    }

    public static Object lookup(final String name) throws NamingException {
        return lookup(name, null);
    }

    public static Object lookup(final String name, final Hashtable env)
            throws NamingException {
        final InitialContext context = new InitialContext(env);
        try {
            return context.lookup(name);
        } finally {
            context.close();
        }
    }

    public static String resolveName(final Name name) {
        return resolveName(name.toString());
    }

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

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
package org.seasar.extension.dxo.util;

import java.lang.reflect.Method;

import org.seasar.extension.dxo.DxoConstants;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.Tokenizer;

/**
 * @author koichik
 * @author higa
 * 
 */
public class DxoUtil {

    protected static final Method GET_VALUE_TYPE_OF_TARGET_MAP_METHOD = getValueTypeOfTargetMapMethod();

    public static Class getElementTypeOfList(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 1 ? MethodUtil
                .getElementTypeOfListFromReturnType(method) : MethodUtil
                .getElementTypeOfListFromParameterType(method, 1);
    }

    public static Class getValueTypeOfTargetMap(final Method method) {
        if (GET_VALUE_TYPE_OF_TARGET_MAP_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(GET_VALUE_TYPE_OF_TARGET_MAP_METHOD,
                null, new Object[] { method });
    }

    protected static Method getValueTypeOfTargetMapMethod() {
        try {
            final Class clazz = Class
                    .forName("org.seasar.extension.dxo.util.DxoTigerUtil");
            return clazz.getMethod("getValueTypeOfTargetMap",
                    new Class[] { Method.class });
        } catch (final Throwable ignore) {
        }
        return null;
    }

    public static Object parseMap(final String expression) {
        if (StringUtil.isEmpty(expression)) {
            return null;
        }
        String s = expression;
        if (!expression.startsWith("'")) {
            s = addQuote(expression);
        }
        return OgnlUtil.parseExpression(DxoConstants.OGNL_MAP_PREFIX + s
                + DxoConstants.OGNL_MAP_SUFFIX);
    }

    protected static String addQuote(final String expr) {
        final MyTokenizer tokenizer = new MyTokenizer(expr);
        final StringBuffer buf = new StringBuffer(expr.length() + 10);
        for (int token = tokenizer.nextToken(); token != Tokenizer.TT_EOF;) {
            if (token == Tokenizer.TT_QUOTE || token == Tokenizer.TT_WORD) {
                buf.append('\'').append(tokenizer.getStringValue())
                        .append('\'');
            } else {
                throw new IllegalArgumentException(expr + "(" + token + ")");
            }
            token = tokenizer.nextToken();
            if (token == MyTokenizer.TT_COLON) {
                buf.append((char) token);
            } else {
                throw new IllegalArgumentException(expr + "(" + token + ")");
            }
            token = tokenizer.nextToken();
            if (token == Tokenizer.TT_QUOTE) {
                buf.append('\'').append(tokenizer.getStringValue())
                        .append('\'');
            } else if (token == Tokenizer.TT_WORD) {
                buf.append(tokenizer.getStringValue());
            } else {
                throw new IllegalArgumentException(expr + "(" + token + ")");
            }
            token = tokenizer.nextToken();
            if (token == MyTokenizer.TT_COMMA) {
                buf.append((char) token);
                token = tokenizer.nextToken();
            } else if (token == Tokenizer.TT_EOF) {
                break;
            } else {
                throw new IllegalArgumentException(expr + "(" + token + ")");
            }
        }
        return buf.toString();
    }

    protected static class MyTokenizer extends Tokenizer {

        public static final int TT_COLON = ':';

        public static final int TT_COMMA = ',';

        private static byte[] defaultCtype = new byte[256];

        static {
            setup(defaultCtype);
        }

        public MyTokenizer(final String str) {
            this(str, defaultCtype);
        }

        public MyTokenizer(final String str, final byte[] ctype) {
            super(str, ctype);
        }

        protected static void setup(final byte[] ctype2) {
            Tokenizer.setup(ctype2);
            ordinaryChar(ctype2, TT_COLON);
            ordinaryChar(ctype2, TT_COMMA);
        }

    }

}

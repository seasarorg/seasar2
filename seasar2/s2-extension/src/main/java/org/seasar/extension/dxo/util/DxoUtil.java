/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

    public static Class getElementTypeOfList(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 1 ? MethodUtil
                .getElementTypeOfListFromReturnType(method) : MethodUtil
                .getElementTypeOfListFromParameterType(method, 1);
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

    protected static String addQuote(String expr) {
        MyTokenizer tokenizer = new MyTokenizer(expr);
        StringBuffer buf = new StringBuffer(expr.length() + 10);
        for (int token = tokenizer.nextToken(); token != MyTokenizer.TT_EOF;) {
            if (token == MyTokenizer.TT_QUOTE || token == MyTokenizer.TT_WORD) {
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
            if (token == MyTokenizer.TT_QUOTE) {
                buf.append('\'').append(tokenizer.getStringValue())
                        .append('\'');
            } else if (token == MyTokenizer.TT_WORD) {
                buf.append(tokenizer.getStringValue());
            } else {
                throw new IllegalArgumentException(expr + "(" + token + ")");
            }
            token = tokenizer.nextToken();
            if (token == MyTokenizer.TT_COMMA) {
                buf.append((char) token);
                token = tokenizer.nextToken();
            } else if (token == MyTokenizer.TT_EOF) {
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

        public MyTokenizer(String str) {
            this(str, defaultCtype);
        }

        public MyTokenizer(String str, byte[] ctype) {
            super(str, ctype);
        }

        protected static void setup(byte[] ctype2) {
            Tokenizer.setup(ctype2);
            ordinaryChar(ctype2, TT_COLON);
            ordinaryChar(ctype2, TT_COMMA);
        }
    }
}

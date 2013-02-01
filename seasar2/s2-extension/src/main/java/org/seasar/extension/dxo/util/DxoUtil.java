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
package org.seasar.extension.dxo.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.Tokenizer;

/**
 * Dxoのユーティリティクラスです。
 * 
 * @author koichik
 * @author higa
 */
public class DxoUtil {

    /** genericな{@link Map}から値の型を取得するためのメソッドです。 */
    protected static final Method GET_VALUE_TYPE_OF_TARGET_MAP_METHOD = getValueTypeOfTargetMapMethod();

    /**
     * S2-Tigerが利用可能な場合、Dxoのメソッドからgenericな{@link List}である変換先の要素型を返します。
     * 
     * @param method
     *            Dxoのメソッド
     * @return Dxoのメソッドからgenericな{@link List}である変換先の要素型
     */
    public static Class getElementTypeOfList(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 1 ? MethodUtil
                .getElementTypeOfListFromReturnType(method) : MethodUtil
                .getElementTypeOfListFromParameterType(method, 1);
    }

    /**
     * S2-Tigerが利用可能な場合、Dxoのメソッドからgenericな{@link Map}である変換先の値の型を返します。
     * 
     * @param method
     *            Dxoのメソッド
     * @return Dxoのメソッドからgenericな{@link Map}である変換先の値の型
     */
    public static Class getValueTypeOfTargetMap(final Method method) {
        if (GET_VALUE_TYPE_OF_TARGET_MAP_METHOD == null) {
            return null;
        }
        return (Class) MethodUtil.invoke(GET_VALUE_TYPE_OF_TARGET_MAP_METHOD,
                null, new Object[] { method });
    }

    /**
     * S2-Tigerが利用可能な場合、Dxoのメソッドからgenericな{@link Map}である変換先の値の型を返すメソッドを返します。
     * 
     * @return Dxoのメソッドからgenericな{@link Map}である変換先の値の型を返すメソッド
     */
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

    /**
     * 変換ルールを解析した結果の式オブジェクトを返します。
     * 
     * @param rule
     *            変換ルール
     * @return 変換ルールを解析した結果の式オブジェクト
     */
    public static Expression parseRule(final String rule) {
        if (StringUtil.isEmpty(rule)) {
            return null;
        }
        Expression exp = SimpleExpressionParser.parse(rule);
        if (exp == null) {
            exp = new OgnlExpression(rule);
        }
        return exp;
    }

    /**
     * <code>CONVERSION_RULE</code>アノテーションで指定される簡素な{@link Map}表現を OGNLの{@link Map}リテラルに変換して返します。
     * <p>
     * 簡素な{@link Map}表現は次の形式の文字列です。
     * </p>
     * 
     * <pre>
     * key1 : value1, key2 : value2 ...
     * </pre>
     * 
     * <p>
     * これをOGNLの{@link Map}リテラルにするため，キーをシングルクオートで囲んだ文字列を返します。
     * 値に複雑な式が指定されると適切に変換できない場合があります。 そのような場合は簡素な{@link Map}表現ではなく、 OGNLの{@link Map}リテラルを使用してください。
     * </p>
     * 
     * @param expr
     *            <code>CONVERSION_RULE</code>アノテーションで指定される簡素な{@link Map}表現
     * @return OGNLの{@link Map}リテラル
     */
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

    /**
     * <code>CONVERSION_RULE</code>アノテーションで指定される簡素な{@link Map}表現からトークンを取り出すクラスです。
     */
    protected static class MyTokenizer extends Tokenizer {

        /** コロン */
        public static final int TT_COLON = ':';

        /** カンマ */
        public static final int TT_COMMA = ',';

        /** 単語の構成要素ではない文字の配列 */
        private static byte[] defaultCtype = new byte[256];

        static {
            setup(defaultCtype);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param str
         *            <code>CONVERSION_RULE</code>アノテーションで指定される簡素な{@link Map}表現
         */
        public MyTokenizer(final String str) {
            this(str, defaultCtype);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param str
         *            <code>CONVERSION_RULE</code>アノテーションで指定される簡素な{@link Map}表現
         * @param ctype
         */
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

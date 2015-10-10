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
package org.seasar.extension.jdbc.gen.internal.util;

import org.seasar.framework.util.StringUtil;

/**
 * 引数に関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class ArgumentUtil {

    /** 引用符 */
    public static String QUOTE = "'";

    /** char型としての引用符 */
    public static char QUOTE_CHAR = '\'';

    /** エスケープされた引用符 */
    public static String ESCAPED_QUOTE = "'";

    /** 区切り文字 */
    public static String DELIMITER = ",";

    /** char型としての区切り文字 */
    public static char DELIMITER_CHAR = ',';

    /**
     * 
     */
    protected ArgumentUtil() {
    }

    /**
     * エンコードします。
     * 
     * @param value
     *            値
     * @return エンコードされた値
     */
    public static String encode(String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        String s = value.replace(QUOTE, ESCAPED_QUOTE);
        return QUOTE + s + QUOTE;
    }

    /**
     * デコードします。
     * 
     * @param value
     *            値
     * @return デコードされた値
     */
    public static String decode(String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        String s = StringUtil.ltrim(value, QUOTE);
        s = StringUtil.rtrim(s, QUOTE);
        return s.replace(ESCAPED_QUOTE, QUOTE);
    }

}

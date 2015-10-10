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

import java.util.regex.Pattern;

import org.seasar.extension.jdbc.gen.internal.exception.IllegalDumpValueRuntimeException;

/**
 * ダンプに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class DumpUtil {

    /** 引用符 */
    protected static String QUOTE = "\"";

    /** エスケープされた引用符 */
    protected static String ESCAPED_QUOTE = "\"\"";

    /** デコード対象の文字列を判別するための正規表現 */
    protected static Pattern DECODE_TARGET_PATTERN = Pattern.compile(
            "^\".*\"$", Pattern.DOTALL);

    /**
     * 
     */
    protected DumpUtil() {
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
            return "";
        }
        String s = value.replace(QUOTE, ESCAPED_QUOTE);
        return quote(s);
    }

    /**
     * デコードします。
     * 
     * @param value
     *            値
     * @return デコードされた値
     */
    public static String decode(String value) {
        if (value == null || value.equals("")) {
            return null;
        }
        if (DECODE_TARGET_PATTERN.matcher(value).matches()) {
            String s = value.substring(1, value.length() - 1);
            return s.replace(ESCAPED_QUOTE, QUOTE);
        }
        for (char c : value.toCharArray()) {
            if (c == '"' || c == ',' || c == '\n' || c == '\r') {
                throw new IllegalDumpValueRuntimeException(value);
            }
        }
        return value;
    }

    /**
     * 引用符で囲みます。
     * 
     * @param value
     *            値
     * @return 引用符で囲まれた値
     */
    public static String quote(String value) {
        return QUOTE + value + QUOTE;
    }

}

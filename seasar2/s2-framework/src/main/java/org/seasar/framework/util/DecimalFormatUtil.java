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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * {@link DecimalFormat}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class DecimalFormatUtil {

    /**
     * インスタンスを構築します。
     */
    protected DecimalFormatUtil() {
    }

    /**
     * 数値の文字列での表記を正規化します。
     * 
     * @param s
     * @return 正規化された文字列
     * @see #normalize(String, Locale)
     */
    public static String normalize(String s) {
        return normalize(s, Locale.getDefault());
    }

    /**
     * 数値の文字列での表記をグルーピングセパレータを削除し、小数点を.であらわした標準形に正規化します。
     * 
     * @param s
     * @param locale
     * @return 正規化された文字列
     */
    public static String normalize(String s, Locale locale) {
        if (s == null) {
            return null;
        }
        DecimalFormatSymbols symbols = DecimalFormatSymbolsUtil
                .getDecimalFormatSymbols(locale);
        char decimalSep = symbols.getDecimalSeparator();
        char groupingSep = symbols.getGroupingSeparator();
        StringBuffer buf = new StringBuffer(20);
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == groupingSep) {
                continue;
            } else if (c == decimalSep) {
                c = '.';
            }
            buf.append(c);
        }
        return buf.toString();
    }
}

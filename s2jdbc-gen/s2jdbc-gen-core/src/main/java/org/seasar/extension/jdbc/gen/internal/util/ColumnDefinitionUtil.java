/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

/**
 * カラム定義に関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class ColumnDefinitionUtil {

    /**
     * 
     */
    protected ColumnDefinitionUtil() {
    }

    /**
     * カラム定義の文字列をフォーマットします。
     * <p>
     * カラム定義の文字列に含まる次の値が変換されます。
     * <ul>
     * <li>$lが長さで置き換えられます。</li>
     * <li>$pが精度で置き換えられます。</li>
     * <li>$sがスケールで置き換えられます。</li>
     * </ul>
     * </p>
     * 
     * @param columnDefinition
     *            カラム定義の文字列
     * @param length
     *            長さ
     * @param precision
     *            精度
     * @param scale
     *            スケール
     * @return フォーマットされた文字列
     */
    public static String format(String columnDefinition, int length,
            int precision, int scale) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < columnDefinition.length(); i++) {
            char c = columnDefinition.charAt(i);
            if (c == '$') {
                i++;
                if (i < columnDefinition.length()) {
                    c = columnDefinition.charAt(i);
                    switch (c) {
                    case 'l':
                        buf.append(length);
                        break;
                    case 'p':
                        buf.append(precision);
                        break;
                    case 's':
                        buf.append(scale);
                        break;
                    default:
                        buf.append('$');
                        buf.append(c);
                        break;
                    }
                } else {
                    buf.append(c);
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
}

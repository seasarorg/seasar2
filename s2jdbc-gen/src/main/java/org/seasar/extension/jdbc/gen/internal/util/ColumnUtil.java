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

/**
 * カラムに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class ColumnUtil {

    /**
     * 
     */
    protected ColumnUtil() {
    }

    /**
     * カラムのデータ型をフォーマットします。
     * <p>
     * カラムのデータ型に含まれる置換文字を置き換えます。
     * <ul>
     * <li>$l を長さで置き換えます。</li>
     * <li>$p を精度で置き換えます。</li>
     * <li>$s をスケールで置き換えます。</li>
     * </ul>
     * </p>
     * 
     * @param dataType
     *            カラムのデータ型
     * @param length
     *            長さ
     * @param precision
     *            精度
     * @param scale
     *            スケール
     * @return フォーマットされたカラムのデータ型
     */
    public static String formatDataType(String dataType, int length,
            int precision, int scale) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < dataType.length(); i++) {
            char c = dataType.charAt(i);
            if (c == '$') {
                i++;
                if (i < dataType.length()) {
                    c = dataType.charAt(i);
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

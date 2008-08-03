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
package org.seasar.extension.jdbc.gen.sqltype;

import org.seasar.extension.jdbc.gen.SqlType;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlType implements SqlType {

    /** カラム定義 */
    protected String columnDefinition;

    /**
     * インスタンスを構築します。
     */
    protected AbstractSqlType() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param columnDefinition
     *            カラム定義
     */
    protected AbstractSqlType(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public String getColumnDefinition(int length, int precision, int scale,
            boolean identity) {
        return format(columnDefinition, length, precision, scale);
    }

    protected static String format(String format, int length, int precision,
            int scale) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == '$') {
                i++;
                if (i < format.length()) {
                    c = format.charAt(i);
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

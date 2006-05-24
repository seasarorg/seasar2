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
package org.seasar.extension.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleResultSet extends ResultSetWrapper {

    public static final int WAVE_DASH = 0x301c;

    public static final int FULLWIDTH_TILDE = 0xff5e;

    public OracleResultSet(ResultSet resultSet) {
        super(resultSet);
    }

    public String getString(int columnIndex) throws SQLException {
        return convert(super.getString(columnIndex));
    }

    public String getString(String columnName) throws SQLException {
        return convert(super.getString(columnName));
    }

    protected String convert(String source) {
        if (source == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        char ch;

        for (int i = 0; i < source.length(); i++) {
            ch = source.charAt(i);

            switch (ch) {
            case WAVE_DASH: // WAVE DASH(U+301C) -> FULLWIDTH TILDE(U+FF5E)
                ch = FULLWIDTH_TILDE;
                break;
            default:
                break;
            }

            result.append(ch);
        }

        return result.toString();
    }
}
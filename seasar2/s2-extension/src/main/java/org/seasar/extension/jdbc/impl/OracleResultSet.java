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
package org.seasar.extension.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.framework.util.StringConversionUtil;

/**
 * Oracle用の {@link ResultSet}です。 WAVE DASH(U+301C)をFULLWIDTH
 * TILDE(U+FF5E)に変換します。
 * 
 * @author higa
 * 
 */
public class OracleResultSet extends ResultSetWrapper {

    /**
     * WAVE DASHです。
     */
    public static final int WAVE_DASH = 0x301c;

    /**
     * FULLWIDTH TILDEです。
     */
    public static final int FULLWIDTH_TILDE = 0xff5e;

    /**
     * {@link OracleResultSet}を作成します。
     * 
     * @param resultSet
     *            結果セット
     */
    public OracleResultSet(ResultSet resultSet) {
        super(resultSet);
    }

    public String getString(int columnIndex) throws SQLException {
        return convert(super.getString(columnIndex));
    }

    public String getString(String columnName) throws SQLException {
        return convert(super.getString(columnName));
    }

    /**
     * WAVE DASH(U+301C)をFULLWIDTH TILDE(U+FF5E)に変換します。
     * 
     * @param source
     *            ソース
     * @return 変換結果
     * @see StringConversionUtil#fromWaveDashToFullwidthTilde(String)
     */
    protected String convert(String source) {
        return StringConversionUtil.fromWaveDashToFullwidthTilde(source);
    }
}
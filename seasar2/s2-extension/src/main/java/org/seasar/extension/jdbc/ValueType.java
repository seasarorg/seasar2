/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * S2JDBC用の値の型をあらわすインターフェースです。
 * 
 * @author higa
 * 
 */
public interface ValueType {

    /**
     * カラムの値を返します。
     * 
     * @param resultSet
     *            結果セット
     * @param index
     *            位置
     * @return カラムの値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Object getValue(ResultSet resultSet, int index) throws SQLException;

    /**
     * カラムの値を返します。
     * 
     * @param resultSet
     *            結果セット
     * @param columnName
     *            カラム名
     * @return カラムの値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Object getValue(ResultSet resultSet, String columnName) throws SQLException;

    /**
     * 変数の値をバインドします。
     * 
     * @param ps
     *            準備されたSQL文
     * @param index
     *            位置
     * @param value
     *            値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException;
}
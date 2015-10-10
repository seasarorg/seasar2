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
package org.seasar.extension.jdbc;

import java.sql.CallableStatement;
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
     * パラメータの値を返します。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param index
     *            位置
     * @return パラメータの値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Object getValue(CallableStatement cs, int index) throws SQLException;

    /**
     * パラメータの値を返します。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param parameterName
     *            パラメータ名
     * @return パラメータの値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Object getValue(CallableStatement cs, String parameterName)
            throws SQLException;

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

    /**
     * 変数の値をバインドします。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param parameterName
     *            パラメータ名
     * @param value
     *            値
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    void bindValue(CallableStatement cs, String parameterName, Object value)
            throws SQLException;

    /**
     * OUTパラメータを登録します。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param index
     *            位置
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    void registerOutParameter(CallableStatement cs, int index)
            throws SQLException;

    /**
     * OUTパラメータを登録します。
     * 
     * @param cs
     *            ストアドプロシージャを表す文
     * @param parameterName
     *            パラメータ名
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    void registerOutParameter(CallableStatement cs, String parameterName)
            throws SQLException;

    /**
     * 変数の値を文字列表現に変換します。
     * 
     * @param value
     *            値
     * @return 値の文字列表現
     */
    String toText(Object value);

    /**
     * JDBCのSQL型を返します。
     * 
     * @return JDBCのSQL型
     */
    int getSqlType();
}
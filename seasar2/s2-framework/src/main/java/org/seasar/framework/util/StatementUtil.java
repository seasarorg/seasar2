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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * {@link Statement}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class StatementUtil {

    /**
     * インスタンスを構築します。
     */
    protected StatementUtil() {
    }

    /**
     * SQLを実行します。
     * 
     * @param statement
     * @param sql
     * @return 実行した結果
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#execute(String)
     */
    public static boolean execute(Statement statement, String sql)
            throws SQLRuntimeException {
        try {
            return statement.execute(sql);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * フェッチサイズを設定します。
     * 
     * @param statement
     * @param fetchSize
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setFetchSize(int)
     */
    public static void setFetchSize(Statement statement, int fetchSize)
            throws SQLRuntimeException {
        try {
            statement.setFetchSize(fetchSize);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 最大行数を設定します。
     * 
     * @param statement
     * @param maxRows
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setMaxRows(int)
     */
    public static void setMaxRows(Statement statement, int maxRows)
            throws SQLRuntimeException {
        try {
            statement.setMaxRows(maxRows);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * クエリタイムアウトを設定します。
     * 
     * @param statement
     * @param queryTimeout
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setQueryTimeout(int)
     */
    public static void setQueryTimeout(Statement statement, int queryTimeout)
            throws SQLRuntimeException {
        try {
            statement.setQueryTimeout(queryTimeout);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * {@link Statement}を閉じます。
     * 
     * @param statement
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#close()
     */
    public static void close(Statement statement) throws SQLRuntimeException {
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 結果セットを返します。
     * 
     * @param statement
     * @return 結果セット
     * @throws SQLRuntimeException
     */
    public static ResultSet getResultSet(Statement statement)
            throws SQLRuntimeException {
        try {
            return statement.getResultSet();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 更新カウントを返します。
     * 
     * @param statement
     *            ステートメント
     * @return 更新カウント
     * @see Statement#getUpdateCount()
     */
    public static int getUpdateCount(Statement statement) {
        try {
            return statement.getUpdateCount();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}

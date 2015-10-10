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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * {@link PreparedStatement}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class PreparedStatementUtil {

    /**
     * インスタンスを構築します。
     */
    protected PreparedStatementUtil() {
    }

    /**
     * クエリを実行します。
     * 
     * @param ps
     * @return {@link ResultSet}
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     */
    public static ResultSet executeQuery(PreparedStatement ps)
            throws SQLRuntimeException {
        try {
            return ps.executeQuery();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 更新を実行します。
     * 
     * @param ps
     * @return 更新した結果の行数
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     */
    public static int executeUpdate(PreparedStatement ps)
            throws SQLRuntimeException {
        try {
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 実行します。
     * 
     * @param ps
     * @return 結果セットを返すかどうか
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see PreparedStatement#execute()
     */
    public static boolean execute(PreparedStatement ps)
            throws SQLRuntimeException {
        try {
            return ps.execute();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * バッチ更新を行ないます。
     * 
     * @param ps
     * @return 更新した結果の行数の配列
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     */
    public static int[] executeBatch(PreparedStatement ps)
            throws SQLRuntimeException {
        try {
            return ps.executeBatch();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * バッチを追加します。
     * 
     * @param ps
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     */
    public static void addBatch(PreparedStatement ps)
            throws SQLRuntimeException {
        try {
            ps.addBatch();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}

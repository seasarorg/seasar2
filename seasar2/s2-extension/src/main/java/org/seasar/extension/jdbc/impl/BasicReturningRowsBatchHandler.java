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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.ReturningRowsBatchHandler;
import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * 一つのSQLに複数のパラメータを適用してバッチ実行するための基本的なクラスです。
 * <p>
 * {@link BasicBatchHandler}と異なり、このインターフェースのメソッドはバッチ実行された各コマンドが更新した行数を配列で返します。
 * </p>
 * 
 * @author higa
 * @see BasicBatchHandler
 */
public class BasicReturningRowsBatchHandler extends BasicHandler implements
        ReturningRowsBatchHandler {

    private static final int[] EMPTY_ARRAY = new int[0];

    private int batchSize = -1;

    /**
     * {@link BasicReturningRowsBatchHandler}を作成します。
     */
    public BasicReturningRowsBatchHandler() {
    }

    /**
     * {@link BasicReturningRowsBatchHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     */
    public BasicReturningRowsBatchHandler(final DataSource dataSource,
            final String sql) {
        this(dataSource, sql, -1);
    }

    /**
     * {@link BasicReturningRowsBatchHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     * @param batchSize
     *            バッチ数
     */
    public BasicReturningRowsBatchHandler(final DataSource dataSource,
            final String sql, final int batchSize) {
        this(dataSource, sql, batchSize, BasicStatementFactory.INSTANCE);
    }

    /**
     * {@link BasicReturningRowsBatchHandler}を作成します。
     * 
     * @param dataSource
     *            データソース
     * @param sql
     *            SQL
     * @param batchSize
     *            バッチ数
     * @param statementFactory
     *            ステートメントファクトリ
     */
    public BasicReturningRowsBatchHandler(final DataSource dataSource,
            final String sql, final int batchSize,
            final StatementFactory statementFactory) {

        setDataSource(dataSource);
        setSql(sql);
        setBatchSize(batchSize);
        setStatementFactory(statementFactory);
    }

    /**
     * バッチ数を返します。
     * 
     * @return バッチ数
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * バッチ数を設定します。
     * 
     * @param batchSize
     *            バッチ数
     */
    public void setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
    }

    public int[] execute(final List list) throws SQLRuntimeException {
        if (list.size() == 0) {
            return EMPTY_ARRAY;
        }
        final Object[] args = (Object[]) list.get(0);
        return execute(list, getArgTypes(args));
    }

    public int[] execute(final List list, final Class[] argTypes)
            throws SQLRuntimeException {
        final Connection connection = getConnection();
        try {
            return execute(connection, list, argTypes);
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    /**
     * 更新を実行します。
     * 
     * @param connection
     *            コネクション
     * @param list
     *            バッチ対象のデータ
     * @param argTypes
     *            引数の型のリスト
     * @return バッチ内のコマンドごとに 1 つの要素が格納されている更新カウントの配列。
     *         配列の要素はコマンドがバッチに追加された順序で並べられる
     */
    protected int[] execute(final Connection connection, final List list,
            final Class[] argTypes) {
        final int size = batchSize > 0 ? Math.min(batchSize, list.size())
                : list.size();
        if (size == 0) {
            return EMPTY_ARRAY;
        }
        final PreparedStatement ps = prepareStatement(connection);
        try {
            for (int i = 0; i < list.size(); ++i) {
                final Object[] args = (Object[]) list.get(i);
                logSql(args, argTypes);
                bindArgs(ps, args, argTypes);
                PreparedStatementUtil.addBatch(ps);
            }
            return PreparedStatementUtil.executeBatch(ps);
        } finally {
            StatementUtil.close(ps);
        }
    }

}
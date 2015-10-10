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
package org.seasar.framework.mock.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * {@link Connection}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockConnection implements Connection {

    private boolean closed = false;

    private boolean committed = false;

    private boolean rolledback = false;

    private boolean autoCommit = true;

    public void clearWarnings() throws SQLException {
    }

    public void close() throws SQLException {
        closed = true;
    }

    public void commit() throws SQLException {
        committed = true;
    }

    /**
     * コミットしているかどうかを返します。
     * 
     * @return コミットしているかどうか
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * コミットしているかどうかを設定します。
     * 
     * @param committed
     *            コミットしているかどうか
     */
    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public Statement createStatement() throws SQLException {
        return createMockStatement();
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement() {
        return new MockStatement(this);
    }

    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return createMockStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        return new MockStatement(this, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return createMockStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement(int resultSetType,
            int resultSetConcurrency) {
        return new MockStatement(this, resultSetType, resultSetConcurrency);
    }

    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    public String getCatalog() throws SQLException {
        return null;
    }

    public int getHoldability() throws SQLException {
        return 0;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    public int getTransactionIsolation() throws SQLException {
        return TRANSACTION_READ_COMMITTED;
    }

    public Map getTypeMap() throws SQLException {
        return null;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public boolean isClosed() throws SQLException {
        return closed;
    }

    /**
     * 閉じているかどうかを設定します。
     * 
     * @param closed
     *            閉じているかどうか
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isReadOnly() throws SQLException {
        return false;
    }

    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return prepareMockCall(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        return new MockCallableStatement(this, sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return prepareMockCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql, int resultSetType,
            int resultSetConcurrency) {
        return new MockCallableStatement(this, sql, resultSetType,
                resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareMockCall(sql);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql) {
        return new MockCallableStatement(this, sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return prepareMockStatement(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用の準備されたステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        return new MockPreparedStatement(this, sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return prepareMockStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * モック用の準備されたステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int resultSetType, int resultSetConcurrency) {
        return new MockPreparedStatement(this, sql, resultSetType,
                resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return prepareMockStatement(sql, autoGeneratedKeys);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param autoGeneratedKeys
     *            自動生成されたキーを返すかどうかのフラグ
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int autoGeneratedKeys) {
        return new MockPreparedStatement(this, sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndices)
            throws SQLException {
        return prepareMockStatement(sql, columnIndices);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param columnIndices
     *            自動生成された値を返して欲しいカラムの位置の配列
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int[] columnIndices) {
        return new MockPreparedStatement(this, sql, columnIndices);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return prepareMockStatement(sql, columnNames);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param columnNames
     *            自動生成された値を返して欲しいカラムの名前の配列
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            String[] columnNames) {
        return new MockPreparedStatement(this, sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareMockStatement(sql);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql) {
        return new MockPreparedStatement(this, sql);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    }

    public void rollback() throws SQLException {
        rolledback = true;
    }

    /**
     * ロールバックしたかどうかを返します。
     * 
     * @return Returns ロールバックしたかどうか
     */
    public boolean isRolledback() {
        return rolledback;
    }

    /**
     * ロールバックしたかどうかを設定します。
     * 
     * @param rolledback
     *            ロールバックしたかどうか
     */
    public void setRolledback(boolean rolledback) {
        this.rolledback = rolledback;
    }

    public void rollback(Savepoint savepoint) throws SQLException {
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
    }

    public void setCatalog(String catalog) throws SQLException {
    }

    public void setHoldability(int holdability) throws SQLException {
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
    }

    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    public void setTransactionIsolation(int level) throws SQLException {
    }

    public void setTypeMap(Map arg0) throws SQLException {
    }
}
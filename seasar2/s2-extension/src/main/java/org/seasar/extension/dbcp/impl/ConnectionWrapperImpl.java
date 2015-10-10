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
package org.seasar.extension.dbcp.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.XAConnection;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.jdbc.impl.PreparedStatementWrapper;
import org.seasar.framework.exception.SSQLException;
import org.seasar.framework.log.Logger;

/**
 * {@link ConnectionWrapper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class ConnectionWrapperImpl implements ConnectionWrapper,
        ConnectionEventListener {

    private static final Logger logger_ = Logger
            .getLogger(ConnectionWrapperImpl.class);

    private XAConnection xaConnection_;

    private Connection physicalConnection_;

    private XAResource xaResource_;

    private ConnectionPool connectionPool_;

    private boolean closed_ = false;

    private Transaction tx_;

    /**
     * {@link ConnectionWrapperImpl}を作成します。
     * 
     * @param xaConnection
     *            XAコネクション
     * @param physicalConnection
     *            物理コネクション
     * @param connectionPool
     *            コネクションプール
     * @param tx
     *            トランザクション
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public ConnectionWrapperImpl(final XAConnection xaConnection,
            final Connection physicalConnection,
            final ConnectionPool connectionPool, final Transaction tx)
            throws SQLException {
        xaConnection_ = xaConnection;
        physicalConnection_ = physicalConnection;
        xaResource_ = new XAResourceWrapperImpl(xaConnection.getXAResource(),
                this);
        connectionPool_ = connectionPool;
        tx_ = tx;
        xaConnection_.addConnectionEventListener(this);
    }

    public Connection getPhysicalConnection() {
        return physicalConnection_;
    }

    public XAResource getXAResource() {
        return xaResource_;
    }

    public XAConnection getXAConnection() {
        return xaConnection_;
    }

    public void init(final Transaction tx) {
        closed_ = false;
        tx_ = tx;
    }

    public void cleanup() {
        xaConnection_.removeConnectionEventListener(this);
        closed_ = true;
        xaConnection_ = null;
        physicalConnection_ = null;
        tx_ = null;
    }

    public void closeReally() {
        if (xaConnection_ == null) {
            return;
        }
        closed_ = true;
        try {
            if (!physicalConnection_.isClosed()) {
                if (!physicalConnection_.getAutoCommit()) {
                    try {
                        physicalConnection_.rollback();
                        physicalConnection_.setAutoCommit(true);
                    } catch (final SQLException ex) {
                        logger_.log(ex);
                    }
                }
                physicalConnection_.close();
            }
        } catch (final SQLException ex) {
            logger_.log(ex);
        } finally {
            physicalConnection_ = null;
        }

        try {
            xaConnection_.close();
            logger_.log("DSSR0001", null);
        } catch (final SQLException ex) {
            logger_.log(ex);
        } finally {
            xaConnection_ = null;
        }
    }

    private void assertOpened() throws SQLException {
        if (closed_) {
            throw new SSQLException("ESSR0062", null);
        }
    }

    private void assertLocalTx() throws SQLException {
        if (tx_ != null) {
            throw new SSQLException("ESSR0366", null);
        }
    }

    public void release() throws SQLException {
        if (!closed_) {
            connectionPool_.release(this);
        }
    }

    public void connectionClosed(final ConnectionEvent event) {
    }

    public void connectionErrorOccurred(final ConnectionEvent event) {
        try {
            release();
        } catch (final SQLException ignore) {
        }
    }

    public Statement createStatement() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.createStatement();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(final String sql)
            throws SQLException {
        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public CallableStatement prepareCall(final String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public String nativeSQL(final String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.nativeSQL(sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public boolean isClosed() throws SQLException {
        return closed_;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getMetaData();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setReadOnly(final boolean readOnly) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setReadOnly(readOnly);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public boolean isReadOnly() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.isReadOnly();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setCatalog(final String catalog) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setCatalog(catalog);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public String getCatalog() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getCatalog();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void close() throws SQLException {
        if (closed_) {
            return;
        }
        if (logger_.isDebugEnabled()) {
            logger_.log("DSSR0002", new Object[] { tx_ });
        }
        if (tx_ == null) {
            connectionPool_.checkIn(this);
        } else {
            connectionPool_.checkInTx(tx_);
        }
    }

    public void setTransactionIsolation(final int level) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTransactionIsolation(level);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public int getTransactionIsolation() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTransactionIsolation();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getWarnings();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void clearWarnings() throws SQLException {
        assertOpened();
        try {
            physicalConnection_.clearWarnings();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void commit() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.commit();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void rollback() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        assertOpened();
        if (autoCommit) {
            assertLocalTx();
        }
        try {
            physicalConnection_.setAutoCommit(autoCommit);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public boolean getAutoCommit() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getAutoCommit();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency) throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public Map getTypeMap() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTypeMap();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setTypeMap(final Map map) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTypeMap(map);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(
                    physicalConnection_.prepareStatement(sql, resultSetType,
                            resultSetConcurrency), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public void setHoldability(final int holdability) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setHoldability(holdability);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public int getHoldability() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getHoldability();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public Savepoint setSavepoint(final String name) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint(name);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void rollback(final Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback(savepoint);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.releaseSavepoint(savepoint);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency, final int resultSetHoldability)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, resultSetType, resultSetConcurrency,
                            resultSetHoldability), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(final String sql,
            final int autoGeneratedKeys) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, autoGeneratedKeys), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(final String sql,
            final int[] columnIndexes) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnIndexes), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(final String sql,
            final String[] columnNames) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnNames), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    private SQLException wrapException(final SQLException e, final String sql) {
        return new SSQLException("ESSR0072",
                new Object[] { sql, e.getMessage(),
                        new Integer(e.getErrorCode()), e.getSQLState() }, e
                        .getSQLState(), e.getErrorCode(), e, sql);
    }

}

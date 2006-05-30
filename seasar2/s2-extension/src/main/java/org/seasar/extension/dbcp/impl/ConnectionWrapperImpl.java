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
package org.seasar.extension.dbcp.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.framework.exception.SSQLException;
import org.seasar.framework.log.Logger;

public final class ConnectionWrapperImpl implements ConnectionWrapper {

    private static Logger logger_ = Logger
            .getLogger(ConnectionWrapperImpl.class);

    private XAConnection xaConnection_;

    private Connection physicalConnection_;

    private XAResource xaResource_;

    private ConnectionPool connectionPool_;

    private boolean closed_ = false;

    private boolean localTx_;

    public ConnectionWrapperImpl(XAConnection xaConnection,
            ConnectionPool connectionPool, boolean localTx) throws SQLException {

        xaConnection_ = xaConnection;
        physicalConnection_ = xaConnection.getConnection();
        xaResource_ = new XAResourceWrapperImpl(xaConnection.getXAResource(),
                this);
        connectionPool_ = connectionPool;
        localTx_ = localTx;
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

    public void init(boolean localTx) {
        closed_ = false;
        localTx_ = localTx;
    }

    public void cleanup() {
        closed_ = true;
    }

    public void closeReally() {
        if (xaConnection_ == null) {
            return;
        }
        closed_ = true;
        try {
            xaConnection_.close();
            logger_.log("DSSR0001", null);
        } catch (SQLException ex) {
            logger_.log(ex);
        } finally {
            xaConnection_ = null;
        }
        try {
            if (!physicalConnection_.isClosed()) {
                physicalConnection_.close();
            }
        } catch (SQLException ex) {
            logger_.log(ex);
        }

    }

    private void assertOpened() throws SQLException {
        if (closed_) {
            throw new SSQLException("ESSR0062", null);
        }
    }

    private void assertLocalTx() throws SQLException {
        if (!localTx_) {
            throw new SSQLException("ESSR0366", null);
        }
    }

    public void release() throws SQLException {
        connectionPool_.release(this);
    }

    public Statement createStatement() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.createStatement();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public String nativeSQL(String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.nativeSQL(sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public boolean isClosed() throws SQLException {
        return closed_;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getMetaData();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setReadOnly(readOnly);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public boolean isReadOnly() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.isReadOnly();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setCatalog(String catalog) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setCatalog(catalog);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public String getCatalog() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getCatalog();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void close() throws SQLException {
        if (closed_) {
            return;
        }
        if (localTx_) {
            connectionPool_.checkIn(this);
        }
        logger_.log("DSSR0002", null);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTransactionIsolation(level);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public int getTransactionIsolation() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTransactionIsolation();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public SQLWarning getWarnings() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getWarnings();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void clearWarnings() throws SQLException {
        assertOpened();
        try {
            physicalConnection_.clearWarnings();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void commit() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.commit();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void rollback() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        assertOpened();
        if (autoCommit) {
            assertLocalTx();
        }
        try {
            physicalConnection_.setAutoCommit(autoCommit);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public boolean getAutoCommit() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getAutoCommit();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public Map getTypeMap() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTypeMap();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void setTypeMap(final Map map) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTypeMap(map);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(
                    physicalConnection_.prepareStatement(sql, resultSetType,
                            resultSetConcurrency), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public void setHoldability(int holdability) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setHoldability(holdability);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public int getHoldability() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getHoldability();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public Savepoint setSavepoint() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint();
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint(name);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback(savepoint);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.releaseSavepoint(savepoint);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (SQLException ex) {
            release();
            throw ex;
        }
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, resultSetType, resultSetConcurrency,
                            resultSetHoldability), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, autoGeneratedKeys), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnIndexes), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnNames), sql);
        } catch (SQLException ex) {
            release();
            throw new SQLExceptionWrapper(ex, sql);
        }
    }

    private static class PreparedStatementWrapper implements PreparedStatement {

        private final PreparedStatement original;

        private String sql;

        public PreparedStatementWrapper(PreparedStatement original, String sql) {
            this.original = original;
            this.sql = sql;
        }

        private SQLException wrapException(SQLException e) {
            return wrapException(e, this.sql);
        }

        private SQLException wrapException(SQLException e, String sql) {
            if (sql != null) {
                return new SQLExceptionWrapper(e, sql);
            }
            return e;
        }

        public ResultSet executeQuery() throws SQLException {
            try {
                return original.executeQuery();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int executeUpdate() throws SQLException {
            try {
                return original.executeUpdate();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public ResultSetMetaData getMetaData() throws SQLException {
            try {
                return original.getMetaData();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public ParameterMetaData getParameterMetaData() throws SQLException {
            try {
                return original.getParameterMetaData();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getMaxRows() throws SQLException {
            try {
                return original.getMaxRows();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getQueryTimeout() throws SQLException {
            return original.getQueryTimeout();
        }

        public SQLWarning getWarnings() throws SQLException {
            try {
                return original.getWarnings();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public ResultSet getResultSet() throws SQLException {
            try {
                return original.getResultSet();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getUpdateCount() throws SQLException {
            try {
                return original.getUpdateCount();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public boolean getMoreResults() throws SQLException {
            try {
                return original.getMoreResults();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getFetchDirection() throws SQLException {
            try {
                return original.getFetchDirection();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getFetchSize() throws SQLException {
            try {
                return original.getFetchSize();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getResultSetConcurrency() throws SQLException {
            try {
                return original.getResultSetConcurrency();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int getResultSetType() throws SQLException {
            try {
                return original.getResultSetType();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int[] executeBatch() throws SQLException {
            try {
                return original.executeBatch();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public Connection getConnection() throws SQLException {
            try {
                return original.getConnection();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public boolean getMoreResults(int current) throws SQLException {
            try {
                return original.getMoreResults();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public ResultSet getGeneratedKeys() throws SQLException {
            try {
                return original.getGeneratedKeys();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public int executeUpdate(String sql, int autoGeneratedKeys)
                throws SQLException {
            try {
                return original.executeUpdate();
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public int getResultSetHoldability() throws SQLException {
            try {
                return original.getResultSetHoldability();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void addBatch() throws SQLException {
            try {
                original.addBatch();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void addBatch(String sql) throws SQLException {
            try {
                original.addBatch(sql);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public void cancel() throws SQLException {
            try {
                original.cancel();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void clearBatch() throws SQLException {
            try {
                original.clearBatch();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void clearParameters() throws SQLException {
            try {
                original.clearParameters();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void clearWarnings() throws SQLException {
            try {
                original.clearWarnings();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void close() throws SQLException {
            try {
                original.close();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public boolean execute() throws SQLException {
            try {
                return original.execute();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public boolean execute(String sql, int autoGeneratedKeys)
                throws SQLException {
            try {
                return original.execute(sql, autoGeneratedKeys);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public boolean execute(String sql, int[] columnIndexes)
                throws SQLException {
            try {
                return original.execute(sql, columnIndexes);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public boolean execute(String sql, String[] columnNames)
                throws SQLException {
            try {
                return original.execute(sql, columnNames);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public boolean execute(String sql) throws SQLException {
            try {
                return original.execute(sql);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public ResultSet executeQuery(String sql) throws SQLException {
            try {
                return original.executeQuery(sql);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public int executeUpdate(String sql, int[] columnIndexes)
                throws SQLException {
            try {
                return original.executeUpdate(sql, columnIndexes);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public int executeUpdate(String sql, String[] columnNames)
                throws SQLException {
            try {
                return original.executeUpdate(sql, columnNames);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public int executeUpdate(String sql) throws SQLException {
            try {
                return original.executeUpdate(sql);
            } catch (SQLException e) {
                throw wrapException(e, sql);
            }
        }

        public int getMaxFieldSize() throws SQLException {
            try {
                return original.getMaxFieldSize();
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setArray(int i, Array x) throws SQLException {
            try {
                original.setArray(i, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setAsciiStream(int parameterIndex, InputStream x, int length)
                throws SQLException {
            try {
                original.setAsciiStream(parameterIndex, x, length);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setBigDecimal(int parameterIndex, BigDecimal x)
                throws SQLException {
            try {
                original.setBigDecimal(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setBinaryStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            try {
                original.setBinaryStream(parameterIndex, x, length);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setBlob(int i, Blob x) throws SQLException {
            try {
                original.setBlob(i, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setBoolean(int parameterIndex, boolean x)
                throws SQLException {
            try {
                original.setBoolean(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setByte(int parameterIndex, byte x) throws SQLException {
            try {
                original.setByte(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setBytes(int parameterIndex, byte[] x) throws SQLException {
            try {
                original.setBytes(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setCharacterStream(int parameterIndex, Reader reader,
                int length) throws SQLException {
            try {
                original.setCharacterStream(parameterIndex, reader, length);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setClob(int i, Clob x) throws SQLException {
            try {
                original.setClob(i, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setCursorName(String name) throws SQLException {
            try {
                original.setCursorName(name);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setDate(int parameterIndex, Date x, Calendar cal)
                throws SQLException {
            try {
                original.setDate(parameterIndex, x, cal);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setDate(int parameterIndex, Date x) throws SQLException {
            try {
                original.setDate(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setDouble(int parameterIndex, double x) throws SQLException {
            try {
                original.setDouble(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setEscapeProcessing(boolean enable) throws SQLException {
            try {
                original.setEscapeProcessing(enable);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setFetchDirection(int direction) throws SQLException {
            try {
                original.setFetchDirection(direction);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setFetchSize(int rows) throws SQLException {
            try {
                original.setFetchSize(rows);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setFloat(int parameterIndex, float x) throws SQLException {
            try {
                original.setFloat(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setInt(int parameterIndex, int x) throws SQLException {
            try {
                original.setInt(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setLong(int parameterIndex, long x) throws SQLException {
            try {
                original.setLong(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setMaxFieldSize(int max) throws SQLException {
            try {
                original.setMaxFieldSize(max);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setMaxRows(int max) throws SQLException {
            try {
                original.setMaxRows(max);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setNull(int paramIndex, int sqlType, String typeName)
                throws SQLException {
            try {
                original.setNull(paramIndex, sqlType, typeName);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setNull(int parameterIndex, int sqlType)
                throws SQLException {
            try {
                original.setNull(parameterIndex, sqlType);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType,
                int scale) throws SQLException {
            try {
                original.setObject(parameterIndex, x, targetSqlType, scale);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType)
                throws SQLException {
            try {
                original.setObject(parameterIndex, x, targetSqlType);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setObject(int parameterIndex, Object x) throws SQLException {
            try {
                original.setObject(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setQueryTimeout(int seconds) throws SQLException {
            try {
                original.setQueryTimeout(seconds);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setRef(int i, Ref x) throws SQLException {
            try {
                original.setRef(i, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setShort(int parameterIndex, short x) throws SQLException {
            try {
                original.setShort(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setString(int parameterIndex, String x) throws SQLException {
            try {
                original.setString(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setTime(int parameterIndex, Time x, Calendar cal)
                throws SQLException {
            try {
                original.setTime(parameterIndex, x, cal);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setTime(int parameterIndex, Time x) throws SQLException {
            try {
                original.setTime(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
                throws SQLException {
            try {
                original.setTimestamp(parameterIndex, x, cal);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setTimestamp(int parameterIndex, Timestamp x)
                throws SQLException {
            try {
                original.setTimestamp(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setUnicodeStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            try {
                original.setUnicodeStream(parameterIndex, x, length);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }

        public void setURL(int parameterIndex, URL x) throws SQLException {
            try {
                original.setURL(parameterIndex, x);
            } catch (SQLException e) {
                throw wrapException(e);
            }
        }
    }

    private static class SQLExceptionWrapper extends SQLException {

        private static final long serialVersionUID = 1L;

        private String sql;

        public SQLExceptionWrapper(SQLException cause, String sql) {
            super(cause.getMessage(), cause.getSQLState(), cause.getErrorCode());
            initCause(cause);
            setNextException(cause);
            this.sql = sql;
        }

        public String getMessage() {
            if (sql != null) {
                return super.getMessage() + " (SQL={" + sql + "})";
            }
            return super.getMessage();
        }

    }

}

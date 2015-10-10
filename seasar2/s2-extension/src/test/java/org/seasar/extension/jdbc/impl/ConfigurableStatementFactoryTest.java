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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.seasar.extension.jdbc.StatementFactory;

/**
 * @author manhole
 */
public class ConfigurableStatementFactoryTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testCunstructorWithNull() throws Exception {
        try {
            new ConfigurableStatementFactory(null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testCreatePreparedStatement() throws Exception {
        // ## Arrange ##
        final Connection mockConnection = new MockConnection();
        final PreparedStatement mockPreparedStatement = new MockPreparedStatement();

        ConfigurableStatementFactory statementFactory = new ConfigurableStatementFactory(
                new MockStatementFactory() {

                    public PreparedStatement createPreparedStatement(
                            Connection con, String sql) {
                        assertSame(mockConnection, con);
                        assertEquals("some sql", sql);
                        return mockPreparedStatement;
                    }
                });

        // ## Act ##
        PreparedStatement preparedStatement = statementFactory
                .createPreparedStatement(mockConnection, "some sql");

        // ## Assert ##
        assertSame(mockPreparedStatement, preparedStatement);
    }

    /**
     * @throws Exception
     */
    public void testCreateCallableStatement() throws Exception {
        // ## Arrange ##
        final Connection mockConnection = new MockConnection();
        final CallableStatement mockCallableStatement = new MockCallableStatement();

        ConfigurableStatementFactory statementFactory = new ConfigurableStatementFactory(
                new MockStatementFactory() {

                    public CallableStatement createCallableStatement(
                            Connection con, String sql) {
                        assertSame(mockConnection, con);
                        assertEquals("some sql", sql);
                        return mockCallableStatement;
                    }

                });

        // ## Act ##
        CallableStatement preparedStatement = statementFactory
                .createCallableStatement(mockConnection, "some sql");

        // ## Assert ##
        assertSame(mockCallableStatement, preparedStatement);
    }

    /**
     * @throws Exception
     */
    public void testConfigurePreparedStatement() throws Exception {
        // ## Arrange ##
        final int[] fetchSize = new int[1];
        final int[] maxRows = new int[1];
        final int[] queryTimeout = new int[1];
        final PreparedStatement mockPreparedStatement = new MockPreparedStatement() {
            public void setFetchSize(int arg0) throws SQLException {
                fetchSize[0] = arg0;
            }

            public void setMaxRows(int arg0) throws SQLException {
                maxRows[0] = arg0;
            }

            public void setQueryTimeout(int arg0) throws SQLException {
                queryTimeout[0] = arg0;
            }
        };

        ConfigurableStatementFactory statementFactory = new ConfigurableStatementFactory(
                new MockStatementFactory() {

                    public PreparedStatement createPreparedStatement(
                            Connection con, String sql) {
                        return mockPreparedStatement;
                    }

                });

        statementFactory.setFetchSize(new Integer(123));
        statementFactory.setMaxRows(new Integer(221));
        statementFactory.setQueryTimeout(new Integer(321));

        // ## Act ##
        statementFactory.createPreparedStatement(new MockConnection(),
                "select ...");

        // ## Assert ##
        assertEquals(123, fetchSize[0]);
        assertEquals(221, maxRows[0]);
        assertEquals(321, queryTimeout[0]);
    }

    /**
     * @throws Exception
     */
    public void testConfigureCallableStatement() throws Exception {
        // ## Arrange ##
        final int[] fetchSize = new int[1];
        final int[] maxRows = new int[1];
        final CallableStatement mockCallableStatement = new MockCallableStatement() {
            public void setFetchSize(int arg0) throws SQLException {
                fetchSize[0] = arg0;
            }

            public void setMaxRows(int arg0) throws SQLException {
                maxRows[0] = arg0;
            }
        };

        ConfigurableStatementFactory statementFactory = new ConfigurableStatementFactory(
                new MockStatementFactory() {

                    public CallableStatement createCallableStatement(
                            Connection con, String sql) {
                        return mockCallableStatement;
                    }

                });

        statementFactory.setFetchSize(new Integer(1123));
        statementFactory.setMaxRows(new Integer(5535));

        // ## Act ##
        statementFactory.createCallableStatement(new MockConnection(),
                "select ...");

        // ## Assert ##
        assertEquals(1123, fetchSize[0]);
        assertEquals(5535, maxRows[0]);
    }

    private static class MockStatementFactory implements StatementFactory {

        public PreparedStatement createPreparedStatement(Connection con,
                String sql) {
            throw new AssertionFailedError();
        }

        public CallableStatement createCallableStatement(Connection con,
                String sql) {
            throw new AssertionFailedError();
        }
    }

    private static class MockPreparedStatement implements PreparedStatement {

        public ResultSet executeQuery() throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setNull(int parameterIndex, int sqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBoolean(int parameterIndex, boolean x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setByte(int parameterIndex, byte x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setShort(int parameterIndex, short x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setInt(int parameterIndex, int x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setLong(int parameterIndex, long x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFloat(int parameterIndex, float x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDouble(int parameterIndex, double x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBigDecimal(int parameterIndex, BigDecimal x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setString(int parameterIndex, String x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBytes(int parameterIndex, byte[] x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(int parameterIndex, Date x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTime(int parameterIndex, Time x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTimestamp(int parameterIndex, Timestamp x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setAsciiStream(int parameterIndex, InputStream x, int length)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setUnicodeStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBinaryStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearParameters() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType,
                int scale) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute() throws SQLException {
            throw new AssertionFailedError();
        }

        public void addBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCharacterStream(int parameterIndex, Reader reader,
                int length) throws SQLException {
            throw new AssertionFailedError();

        }

        public void setRef(int i, Ref x) throws SQLException {
            throw new AssertionFailedError();

        }

        public void setBlob(int i, Blob x) throws SQLException {
            throw new AssertionFailedError();

        }

        public void setClob(int i, Clob x) throws SQLException {
            throw new AssertionFailedError();

        }

        public void setArray(int i, Array x) throws SQLException {
            throw new AssertionFailedError();

        }

        public ResultSetMetaData getMetaData() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(int parameterIndex, Date x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();

        }

        public void setTime(int parameterIndex, Time x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();

        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();

        }

        public void setNull(int paramIndex, int sqlType, String typeName)
                throws SQLException {
            throw new AssertionFailedError();

        }

        public void setURL(int parameterIndex, URL x) throws SQLException {
            throw new AssertionFailedError();

        }

        public ParameterMetaData getParameterMetaData() throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet executeQuery(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public void close() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getMaxFieldSize() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setMaxFieldSize(int max) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getMaxRows() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setMaxRows(int max) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setEscapeProcessing(boolean enable) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getQueryTimeout() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setQueryTimeout(int seconds) throws SQLException {
            throw new AssertionFailedError();
        }

        public void cancel() throws SQLException {
            throw new AssertionFailedError();
        }

        public SQLWarning getWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCursorName(String name) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet getResultSet() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getUpdateCount() throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getMoreResults() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFetchDirection(int direction) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getFetchDirection() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFetchSize(int rows) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getFetchSize() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetConcurrency() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetType() throws SQLException {
            throw new AssertionFailedError();
        }

        public void addBatch(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public int[] executeBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public Connection getConnection() throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getMoreResults(int current) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet getGeneratedKeys() throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, int autoGeneratedKeys)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, int[] columnIndexes)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, String[] columnNames)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, int autoGeneratedKeys)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, int[] columnIndexes)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, String[] columnNames)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetHoldability() throws SQLException {
            throw new AssertionFailedError();
        }
    }

    private static class MockConnection implements Connection {

        public Statement createStatement() throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public String nativeSQL(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getAutoCommit() throws SQLException {
            throw new AssertionFailedError();
        }

        public void commit() throws SQLException {
            throw new AssertionFailedError();
        }

        public void rollback() throws SQLException {
            throw new AssertionFailedError();
        }

        public void close() throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean isClosed() throws SQLException {
            throw new AssertionFailedError();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean isReadOnly() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCatalog(String catalog) throws SQLException {
            throw new AssertionFailedError();
        }

        public String getCatalog() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTransactionIsolation(int level) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getTransactionIsolation() throws SQLException {
            throw new AssertionFailedError();
        }

        public SQLWarning getWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public Statement createStatement(int resultSetType,
                int resultSetConcurrency) throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql,
                int resultSetType, int resultSetConcurrency)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public CallableStatement prepareCall(String sql, int resultSetType,
                int resultSetConcurrency) throws SQLException {
            throw new AssertionFailedError();
        }

        public Map getTypeMap() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTypeMap(Map arg0) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setHoldability(int holdability) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getHoldability() throws SQLException {
            throw new AssertionFailedError();
        }

        public Savepoint setSavepoint() throws SQLException {
            throw new AssertionFailedError();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            throw new AssertionFailedError();
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            throw new AssertionFailedError();
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            throw new AssertionFailedError();
        }

        public Statement createStatement(int resultSetType,
                int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql,
                int resultSetType, int resultSetConcurrency,
                int resultSetHoldability) throws SQLException {
            throw new AssertionFailedError();
        }

        public CallableStatement prepareCall(String sql, int resultSetType,
                int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql,
                int autoGeneratedKeys) throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql,
                int[] columnIndexes) throws SQLException {
            throw new AssertionFailedError();
        }

        public PreparedStatement prepareStatement(String sql,
                String[] columnNames) throws SQLException {
            throw new AssertionFailedError();
        }
    }

    private static class MockCallableStatement implements CallableStatement {

        public void registerOutParameter(int parameterIndex, int sqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void registerOutParameter(int parameterIndex, int sqlType,
                int scale) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean wasNull() throws SQLException {
            throw new AssertionFailedError();
        }

        public String getString(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getBoolean(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public byte getByte(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public short getShort(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getInt(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public long getLong(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public float getFloat(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public double getDouble(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public BigDecimal getBigDecimal(int parameterIndex, int scale)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public byte[] getBytes(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public Date getDate(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public Time getTime(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public Timestamp getTimestamp(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public Object getObject(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public Object getObject(int arg0, Map arg1) throws SQLException {
            throw new AssertionFailedError();
        }

        public Ref getRef(int i) throws SQLException {
            throw new AssertionFailedError();
        }

        public Blob getBlob(int i) throws SQLException {
            throw new AssertionFailedError();
        }

        public Clob getClob(int i) throws SQLException {
            throw new AssertionFailedError();
        }

        public Array getArray(int i) throws SQLException {
            throw new AssertionFailedError();
        }

        public Date getDate(int parameterIndex, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public Time getTime(int parameterIndex, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public Timestamp getTimestamp(int parameterIndex, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void registerOutParameter(int paramIndex, int sqlType,
                String typeName) throws SQLException {
            throw new AssertionFailedError();
        }

        public void registerOutParameter(String parameterName, int sqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void registerOutParameter(String parameterName, int sqlType,
                int scale) throws SQLException {
            throw new AssertionFailedError();
        }

        public void registerOutParameter(String parameterName, int sqlType,
                String typeName) throws SQLException {
            throw new AssertionFailedError();
        }

        public URL getURL(int parameterIndex) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setURL(String parameterName, URL val) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setNull(String parameterName, int sqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBoolean(String parameterName, boolean x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setByte(String parameterName, byte x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setShort(String parameterName, short x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setInt(String parameterName, int x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setLong(String parameterName, long x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFloat(String parameterName, float x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDouble(String parameterName, double x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBigDecimal(String parameterName, BigDecimal x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setString(String parameterName, String x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBytes(String parameterName, byte[] x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(String parameterName, Date x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTime(String parameterName, Time x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTimestamp(String parameterName, Timestamp x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setAsciiStream(String parameterName, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBinaryStream(String parameterName, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(String parameterName, Object x,
                int targetSqlType, int scale) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(String parameterName, Object x, int targetSqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(String parameterName, Object x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCharacterStream(String parameterName, Reader reader,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(String parameterName, Date x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTime(String parameterName, Time x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setNull(String parameterName, int sqlType, String typeName)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public String getString(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getBoolean(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public byte getByte(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public short getShort(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getInt(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public long getLong(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public float getFloat(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public double getDouble(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public byte[] getBytes(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Date getDate(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Time getTime(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Timestamp getTimestamp(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Object getObject(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public BigDecimal getBigDecimal(String parameterName)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public Object getObject(String arg0, Map arg1) throws SQLException {
            throw new AssertionFailedError();
        }

        public Ref getRef(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Blob getBlob(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Clob getClob(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Array getArray(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public Date getDate(String parameterName, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public Time getTime(String parameterName, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public Timestamp getTimestamp(String parameterName, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public URL getURL(String parameterName) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet executeQuery() throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setNull(int parameterIndex, int sqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBoolean(int parameterIndex, boolean x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setByte(int parameterIndex, byte x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setShort(int parameterIndex, short x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setInt(int parameterIndex, int x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setLong(int parameterIndex, long x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFloat(int parameterIndex, float x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDouble(int parameterIndex, double x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBigDecimal(int parameterIndex, BigDecimal x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setString(int parameterIndex, String x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBytes(int parameterIndex, byte[] x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(int parameterIndex, Date x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTime(int parameterIndex, Time x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTimestamp(int parameterIndex, Timestamp x)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setAsciiStream(int parameterIndex, InputStream x, int length)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setUnicodeStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBinaryStream(int parameterIndex, InputStream x,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearParameters() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType,
                int scale) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x, int targetSqlType)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setObject(int parameterIndex, Object x) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute() throws SQLException {
            throw new AssertionFailedError();
        }

        public void addBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCharacterStream(int parameterIndex, Reader reader,
                int length) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setRef(int i, Ref x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setBlob(int i, Blob x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setClob(int i, Clob x) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setArray(int i, Array x) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSetMetaData getMetaData() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setDate(int parameterIndex, Date x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTime(int parameterIndex, Time x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setNull(int paramIndex, int sqlType, String typeName)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public void setURL(int parameterIndex, URL x) throws SQLException {
            throw new AssertionFailedError();
        }

        public ParameterMetaData getParameterMetaData() throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet executeQuery(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public void close() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getMaxFieldSize() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setMaxFieldSize(int max) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getMaxRows() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setMaxRows(int max) throws SQLException {
            throw new AssertionFailedError();
        }

        public void setEscapeProcessing(boolean enable) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getQueryTimeout() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setQueryTimeout(int seconds) throws SQLException {
            throw new AssertionFailedError();
        }

        public void cancel() throws SQLException {
            throw new AssertionFailedError();
        }

        public SQLWarning getWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearWarnings() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setCursorName(String name) throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet getResultSet() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getUpdateCount() throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getMoreResults() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFetchDirection(int direction) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getFetchDirection() throws SQLException {
            throw new AssertionFailedError();
        }

        public void setFetchSize(int rows) throws SQLException {
            throw new AssertionFailedError();
        }

        public int getFetchSize() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetConcurrency() throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetType() throws SQLException {
            throw new AssertionFailedError();
        }

        public void addBatch(String sql) throws SQLException {
            throw new AssertionFailedError();
        }

        public void clearBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public int[] executeBatch() throws SQLException {
            throw new AssertionFailedError();
        }

        public Connection getConnection() throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean getMoreResults(int current) throws SQLException {
            throw new AssertionFailedError();
        }

        public ResultSet getGeneratedKeys() throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, int autoGeneratedKeys)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, int[] columnIndexes)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int executeUpdate(String sql, String[] columnNames)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, int autoGeneratedKeys)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, int[] columnIndexes)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public boolean execute(String sql, String[] columnNames)
                throws SQLException {
            throw new AssertionFailedError();
        }

        public int getResultSetHoldability() throws SQLException {
            throw new AssertionFailedError();
        }
    }

}

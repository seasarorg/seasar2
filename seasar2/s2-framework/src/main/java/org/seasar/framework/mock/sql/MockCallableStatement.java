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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * {@link CallableStatement}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockCallableStatement extends MockPreparedStatement implements
        CallableStatement {

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     */
    public MockCallableStatement(MockConnection connection, String sql) {
        super(connection, sql);
    }

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     */
    public MockCallableStatement(MockConnection connection, String sql,
            int resultSetType, int resultSetConcurrency) {
        super(connection, sql, resultSetType, resultSetConcurrency);
    }

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     */
    public MockCallableStatement(MockConnection connection, String sql,
            int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        super(connection, sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    public Array getArray(int i) throws SQLException {
        return null;
    }

    public Array getArray(String parameterName) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {
        return null;
    }

    public Blob getBlob(int i) throws SQLException {
        return null;
    }

    public Blob getBlob(String parameterName) throws SQLException {
        return null;
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        return false;
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        return false;
    }

    public byte getByte(int parameterIndex) throws SQLException {
        return 0;
    }

    public byte getByte(String parameterName) throws SQLException {
        return 0;
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        return null;
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        return null;
    }

    public Clob getClob(int i) throws SQLException {
        return null;
    }

    public Clob getClob(String parameterName) throws SQLException {
        return null;
    }

    public Date getDate(int parameterIndex) throws SQLException {
        return null;
    }

    public Date getDate(String parameterName) throws SQLException {
        return null;
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return null;
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return null;
    }

    public double getDouble(int parameterIndex) throws SQLException {
        return 0;
    }

    public double getDouble(String parameterName) throws SQLException {
        return 0;
    }

    public float getFloat(int parameterIndex) throws SQLException {
        return 0;
    }

    public float getFloat(String parameterName) throws SQLException {
        return 0;
    }

    public int getInt(int parameterIndex) throws SQLException {
        return 0;
    }

    public int getInt(String parameterName) throws SQLException {
        return 0;
    }

    public long getLong(int parameterIndex) throws SQLException {
        return 0;
    }

    public long getLong(String parameterName) throws SQLException {
        return 0;
    }

    public Object getObject(int parameterIndex) throws SQLException {
        return null;
    }

    public Object getObject(String parameterName) throws SQLException {
        return null;
    }

    public Object getObject(int arg0, Map arg1) throws SQLException {
        return null;
    }

    public Object getObject(String arg0, Map arg1) throws SQLException {
        return null;
    }

    public Ref getRef(int i) throws SQLException {
        return null;
    }

    public Ref getRef(String parameterName) throws SQLException {
        return null;
    }

    public short getShort(int parameterIndex) throws SQLException {
        return 0;
    }

    public short getShort(String parameterName) throws SQLException {
        return 0;
    }

    public String getString(int parameterIndex) throws SQLException {
        return null;
    }

    public String getString(String parameterName) throws SQLException {
        return null;
    }

    public Time getTime(int parameterIndex) throws SQLException {
        return null;
    }

    public Time getTime(String parameterName) throws SQLException {
        return null;
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return null;
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {
        return null;
    }

    public URL getURL(int parameterIndex) throws SQLException {
        return null;
    }

    public URL getURL(String parameterName) throws SQLException {
        return null;
    }

    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {
    }

    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {
    }

    public void setAsciiStream(String parameterName, InputStream x, int length)
            throws SQLException {
    }

    public void setBigDecimal(String parameterName, BigDecimal x)
            throws SQLException {
    }

    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
    }

    public void setByte(String parameterName, byte x) throws SQLException {
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
    }

    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {
    }

    public void setDate(String parameterName, Date x) throws SQLException {
    }

    public void setDate(String parameterName, Date x, Calendar cal)
            throws SQLException {
    }

    public void setDouble(String parameterName, double x) throws SQLException {
    }

    public void setFloat(String parameterName, float x) throws SQLException {
    }

    public void setInt(String parameterName, int x) throws SQLException {
    }

    public void setLong(String parameterName, long x) throws SQLException {
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
    }

    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {
    }

    public void setObject(String parameterName, Object x) throws SQLException {
    }

    public void setObject(String parameterName, Object x, int targetSqlType)
            throws SQLException {
    }

    public void setObject(String parameterName, Object x, int targetSqlType,
            int scale) throws SQLException {
    }

    public void setShort(String parameterName, short x) throws SQLException {
    }

    public void setString(String parameterName, String x) throws SQLException {
    }

    public void setTime(String parameterName, Time x) throws SQLException {
    }

    public void setTime(String parameterName, Time x, Calendar cal)
            throws SQLException {
    }

    public void setTimestamp(String parameterName, Timestamp x)
            throws SQLException {
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
            throws SQLException {
    }

    public void setURL(String parameterName, URL val) throws SQLException {
    }

    public boolean wasNull() throws SQLException {
        return false;
    }
}
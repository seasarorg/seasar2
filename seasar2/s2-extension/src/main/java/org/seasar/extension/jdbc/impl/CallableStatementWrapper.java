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
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * {@link CallableStatement}のラッパです。
 * 
 * @author higa
 * 
 */
public class CallableStatementWrapper implements CallableStatement {

    private CallableStatement original;

    /**
     * {@link CallableStatementWrapper}を作成します。
     * 
     * @param original
     *            オリジナル
     * 
     */
    public CallableStatementWrapper(CallableStatement original) {
        this.original = original;
    }

    /**
     * @see java.sql.CallableStatement#wasNull()
     */
    public boolean wasNull() throws SQLException {
        return original.wasNull();
    }

    /**
     * @see java.sql.CallableStatement#getByte(int)
     */
    public byte getByte(int parameterIndex) throws SQLException {
        return original.getByte(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(int)
     */
    public double getDouble(int parameterIndex) throws SQLException {
        return original.getDouble(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(int)
     */
    public float getFloat(int parameterIndex) throws SQLException {
        return original.getFloat(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getInt(int)
     */
    public int getInt(int parameterIndex) throws SQLException {
        return original.getInt(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getLong(int)
     */
    public long getLong(int parameterIndex) throws SQLException {
        return original.getLong(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getShort(int)
     */
    public short getShort(int parameterIndex) throws SQLException {
        return original.getShort(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(int)
     */
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return original.getBoolean(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(int)
     */
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return original.getBytes(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int)
     */
    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {

        original.registerOutParameter(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
     */
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {

        original.registerOutParameter(parameterIndex, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int)
     */
    public Object getObject(int parameterIndex) throws SQLException {
        return original.getObject(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getString(int)
     */
    public String getString(int parameterIndex) throws SQLException {
        return original.getString(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int,
     *      java.lang.String)
     */
    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException {

        original.registerOutParameter(paramIndex, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#getByte(java.lang.String)
     */
    public byte getByte(String parameterName) throws SQLException {
        return original.getByte(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(java.lang.String)
     */
    public double getDouble(String parameterName) throws SQLException {
        return original.getDouble(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(java.lang.String)
     */
    public float getFloat(String parameterName) throws SQLException {
        return original.getFloat(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getInt(java.lang.String)
     */
    public int getInt(String parameterName) throws SQLException {
        return original.getInt(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getLong(java.lang.String)
     */
    public long getLong(String parameterName) throws SQLException {
        return original.getLong(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getShort(java.lang.String)
     */
    public short getShort(String parameterName) throws SQLException {
        return original.getShort(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String parameterName) throws SQLException {
        return original.getBoolean(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(java.lang.String)
     */
    public byte[] getBytes(String parameterName) throws SQLException {
        return original.getBytes(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
     */
    public void setByte(String parameterName, byte x) throws SQLException {
        original.setByte(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
     */
    public void setDouble(String parameterName, double x) throws SQLException {
        original.setDouble(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
     */
    public void setFloat(String parameterName, float x) throws SQLException {
        original.setFloat(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int)
     */
    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {

        original.registerOutParameter(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#setInt(java.lang.String, int)
     */
    public void setInt(String parameterName, int x) throws SQLException {
        original.setInt(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int)
     */
    public void setNull(String parameterName, int sqlType) throws SQLException {
        original.setNull(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, int)
     */
    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {

        original.registerOutParameter(parameterName, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#setLong(java.lang.String, long)
     */
    public void setLong(String parameterName, long x) throws SQLException {
        original.setLong(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setShort(java.lang.String, short)
     */
    public void setShort(String parameterName, short x) throws SQLException {
        original.setShort(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        original.setBoolean(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        original.setBytes(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return original.getBigDecimal(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int, int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {

        return original.getBigDecimal(parameterIndex, scale);
    }

    /**
     * @see java.sql.CallableStatement#getURL(int)
     */
    public URL getURL(int parameterIndex) throws SQLException {
        return original.getURL(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getArray(int)
     */
    public Array getArray(int i) throws SQLException {
        return original.getArray(i);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(int)
     */
    public Blob getBlob(int i) throws SQLException {
        return original.getBlob(i);
    }

    /**
     * @see java.sql.CallableStatement#getClob(int)
     */
    public Clob getClob(int i) throws SQLException {
        return original.getClob(i);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int)
     */
    public Date getDate(int parameterIndex) throws SQLException {
        return original.getDate(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getRef(int)
     */
    public Ref getRef(int i) throws SQLException {
        return original.getRef(i);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int)
     */
    public Time getTime(int parameterIndex) throws SQLException {
        return original.getTime(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int)
     */
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return original.getTimestamp(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void setAsciiStream(String parameterName, InputStream x, int length)
            throws SQLException {

        original.setAsciiStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {

        original.setBinaryStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     *      java.io.Reader, int)
     */
    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {

        original.setCharacterStream(parameterName, reader, length);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String)
     */
    public Object getObject(String parameterName) throws SQLException {
        return original.getObject(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object)
     */
    public void setObject(String parameterName, Object x) throws SQLException {
        original.setObject(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int)
     */
    public void setObject(String parameterName, Object x, int targetSqlType)
            throws SQLException {

        original.setObject(parameterName, x, targetSqlType);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int, int)
     */
    public void setObject(String parameterName, Object x, int targetSqlType,
            int scale) throws SQLException {

        original.setObject(parameterName, x, targetSqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int, java.util.Map)
     */
    public Object getObject(int i, Map map) throws SQLException {
        return original.getObject(i, map);
    }

    /**
     * @see java.sql.CallableStatement#getString(java.lang.String)
     */
    public String getString(String parameterName) throws SQLException {
        return original.getString(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, java.lang.String)
     */
    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {

        original.registerOutParameter(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int,
     *      java.lang.String)
     */
    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {

        original.setNull(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#setString(java.lang.String,
     *      java.lang.String)
     */
    public void setString(String parameterName, String x) throws SQLException {
        original.setString(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return original.getBigDecimal(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
     *      java.math.BigDecimal)
     */
    public void setBigDecimal(String parameterName, BigDecimal x)
            throws SQLException {

        original.setBigDecimal(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getURL(java.lang.String)
     */
    public URL getURL(String parameterName) throws SQLException {
        return original.getURL(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
     */
    public void setURL(String parameterName, URL val) throws SQLException {
        original.setURL(parameterName, val);
    }

    /**
     * @see java.sql.CallableStatement#getArray(java.lang.String)
     */
    public Array getArray(String parameterName) throws SQLException {
        return original.getArray(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(java.lang.String)
     */
    public Blob getBlob(String parameterName) throws SQLException {
        return original.getBlob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getClob(java.lang.String)
     */
    public Clob getClob(String parameterName) throws SQLException {
        return original.getClob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String)
     */
    public Date getDate(String parameterName) throws SQLException {
        return original.getDate(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
     */
    public void setDate(String parameterName, Date x) throws SQLException {
        original.setDate(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
     */
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return original.getDate(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getRef(java.lang.String)
     */
    public Ref getRef(String parameterName) throws SQLException {
        return original.getRef(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String)
     */
    public Time getTime(String parameterName) throws SQLException {
        return original.getTime(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
     */
    public void setTime(String parameterName, Time x) throws SQLException {
        original.setTime(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
     */
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return original.getTime(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return original.getTimestamp(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp)
     */
    public void setTimestamp(String parameterName, Timestamp x)
            throws SQLException {

        original.setTimestamp(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {

        return original.getTimestamp(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String,
     *      java.util.Map)
     */
    public Object getObject(String parameterName, Map map) throws SQLException {
        return original.getObject(parameterName, map);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String,
     *      java.util.Calendar)
     */
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return original.getDate(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String,
     *      java.util.Calendar)
     */
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return original.getTime(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
     *      java.util.Calendar)
     */
    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {

        return original.getTimestamp(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
     *      java.util.Calendar)
     */
    public void setDate(String parameterName, Date x, Calendar cal)
            throws SQLException {

        original.setDate(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
     *      java.util.Calendar)
     */
    public void setTime(String parameterName, Time x, Calendar cal)
            throws SQLException {

        original.setTime(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
            throws SQLException {

        original.setTimestamp(parameterName, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return original.executeUpdate();
    }

    /**
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        original.addBatch();
    }

    /**
     * @see java.sql.PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        original.clearParameters();
    }

    /**
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return original.execute();
    }

    /**
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        original.setByte(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        original.setDouble(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        original.setFloat(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        original.setInt(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        original.setNull(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        original.setLong(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        original.setShort(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        original.setBoolean(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        original.setBytes(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
     *      int)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
     *      int)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setUnicodeStream(int,
     *      java.io.InputStream, int)
     * @deprecated
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setUnicodeStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
     *      int)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {

        original.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        original.setObject(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType)
            throws SQLException {

        original.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int,
     *      int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType,
            int scale) throws SQLException {

        original.setObject(parameterIndex, x, targetSqlType, scale);
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException {

        original.setNull(paramIndex, sqlType, typeName);
    }

    /**
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        original.setString(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x)
            throws SQLException {

        original.setBigDecimal(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(int parameterIndex, URL x) throws SQLException {
        original.setURL(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int i, Array x) throws SQLException {
        original.setArray(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int i, Blob x) throws SQLException {
        original.setBlob(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int i, Clob x) throws SQLException {
        original.setClob(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        original.setDate(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return original.getParameterMetaData();
    }

    /**
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(int i, Ref x) throws SQLException {
        original.setRef(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return original.executeQuery();
    }

    /**
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return original.getMetaData();
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        original.setTime(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, Timestamp x)
            throws SQLException {

        original.setTimestamp(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
     *      java.util.Calendar)
     */
    public void setDate(int parameterIndex, Date x, Calendar cal)
            throws SQLException {

        original.setDate(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
     *      java.util.Calendar)
     */
    public void setTime(int parameterIndex, Time x, Calendar cal)
            throws SQLException {

        original.setTime(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
     *      java.util.Calendar)
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
            throws SQLException {

        original.setTimestamp(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return original.getFetchDirection();
    }

    /**
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return original.getFetchSize();
    }

    /**
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        return original.getMaxFieldSize();
    }

    /**
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        return original.getMaxRows();
    }

    /**
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        return original.getQueryTimeout();
    }

    /**
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        return original.getResultSetConcurrency();
    }

    /**
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        return original.getResultSetHoldability();
    }

    /**
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        return original.getResultSetType();
    }

    /**
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        return original.getUpdateCount();
    }

    /**
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        original.cancel();
    }

    /**
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        original.clearBatch();
    }

    /**
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        original.clearWarnings();
    }

    /**
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        original.close();
    }

    /**
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        return original.getMoreResults();
    }

    /**
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        return original.executeBatch();
    }

    /**
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        original.setFetchDirection(direction);
    }

    /**
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        original.setFetchSize(rows);
    }

    /**
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws SQLException {
        original.setMaxFieldSize(max);
    }

    /**
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        original.setMaxRows(max);
    }

    /**
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        original.setQueryTimeout(seconds);
    }

    /**
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int current) throws SQLException {
        return original.getMoreResults(current);
    }

    /**
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        original.setEscapeProcessing(enable);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String sql) throws SQLException {
        return original.executeUpdate(sql);
    }

    /**
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String sql) throws SQLException {
        original.addBatch(sql);
    }

    /**
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String name) throws SQLException {
        original.setCursorName(name);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String sql) throws SQLException {
        return original.execute(sql);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {

        return original.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {

        return original.execute(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {

        return original.executeUpdate(sql, columnIndexes);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return original.execute(sql, columnIndexes);
    }

    /**
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return original.getConnection();
    }

    /**
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return original.getGeneratedKeys();
    }

    /**
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        return original.getResultSet();
    }

    /**
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return original.getWarnings();
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String,
     *      java.lang.String[])
     */
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {

        return original.executeUpdate(sql, columnNames);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {

        return original.execute(sql, columnNames);
    }

    /**
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return original.executeQuery(sql);
    }
}
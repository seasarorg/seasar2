/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
 * @author higa
 *
 */
public class CallableStatementWrapper implements CallableStatement {

	private CallableStatement original_;
	
	/**
	 * 
	 */
	public CallableStatementWrapper(CallableStatement original) {
		original_ = original;
	}

	/**
	 * @see java.sql.CallableStatement#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		return original_.wasNull();
	}

	/**
	 * @see java.sql.CallableStatement#getByte(int)
	 */
	public byte getByte(int parameterIndex) throws SQLException {
		return original_.getByte(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getDouble(int)
	 */
	public double getDouble(int parameterIndex) throws SQLException {
		return original_.getDouble(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getFloat(int)
	 */
	public float getFloat(int parameterIndex) throws SQLException {
		return original_.getFloat(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getInt(int)
	 */
	public int getInt(int parameterIndex) throws SQLException {
		return original_.getInt(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getLong(int)
	 */
	public long getLong(int parameterIndex) throws SQLException {
		return original_.getLong(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getShort(int)
	 */
	public short getShort(int parameterIndex) throws SQLException {
		return original_.getShort(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getBoolean(int)
	 */
	public boolean getBoolean(int parameterIndex) throws SQLException {
		return original_.getBoolean(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getBytes(int)
	 */
	public byte[] getBytes(int parameterIndex) throws SQLException {
		return original_.getBytes(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {

		original_.registerOutParameter(parameterIndex, sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		
		original_.registerOutParameter(parameterIndex, sqlType, scale);
	}

	/**
	 * @see java.sql.CallableStatement#getObject(int)
	 */
	public Object getObject(int parameterIndex) throws SQLException {
		return original_.getObject(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getString(int)
	 */
	public String getString(int parameterIndex) throws SQLException {
		return original_.getString(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, java.lang.String)
	 */
	public void registerOutParameter(int paramIndex, int sqlType,
			String typeName) throws SQLException {
		
		original_.registerOutParameter(paramIndex, sqlType, typeName);
	}

	/**
	 * @see java.sql.CallableStatement#getByte(java.lang.String)
	 */
	public byte getByte(String parameterName) throws SQLException {
		return original_.getByte(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getDouble(java.lang.String)
	 */
	public double getDouble(String parameterName) throws SQLException {
		return original_.getDouble(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getFloat(java.lang.String)
	 */
	public float getFloat(String parameterName) throws SQLException {
		return original_.getFloat(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getInt(java.lang.String)
	 */
	public int getInt(String parameterName) throws SQLException {
		return original_.getInt(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getLong(java.lang.String)
	 */
	public long getLong(String parameterName) throws SQLException {
		return original_.getLong(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getShort(java.lang.String)
	 */
	public short getShort(String parameterName) throws SQLException {
		return original_.getShort(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String parameterName) throws SQLException {
		return original_.getBoolean(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String parameterName) throws SQLException {
		return original_.getBytes(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
	 */
	public void setByte(String parameterName, byte x) throws SQLException {
		original_.setByte(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
	 */
	public void setDouble(String parameterName, double x) throws SQLException {
		original_.setDouble(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
	 */
	public void setFloat(String parameterName, float x) throws SQLException {
		original_.setFloat(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int)
	 */
	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {

		original_.registerOutParameter(parameterName, sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#setInt(java.lang.String, int)
	 */
	public void setInt(String parameterName, int x) throws SQLException {
		original_.setInt(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	public void setNull(String parameterName, int sqlType) throws SQLException {
		original_.setNull(parameterName, sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, int)
	 */
	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {

		original_.registerOutParameter(parameterName, sqlType, scale);
	}

	/**
	 * @see java.sql.CallableStatement#setLong(java.lang.String, long)
	 */
	public void setLong(String parameterName, long x) throws SQLException {
		original_.setLong(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setShort(java.lang.String, short)
	 */
	public void setShort(String parameterName, short x) throws SQLException {
		original_.setShort(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		original_.setBoolean(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
	 */
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		original_.setBytes(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		return original_.getBigDecimal(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getBigDecimal(int, int)
	 * @deprecated
	 */
	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		
		return original_.getBigDecimal(parameterIndex, scale);
	}

	/**
	 * @see java.sql.CallableStatement#getURL(int)
	 */
	public URL getURL(int parameterIndex) throws SQLException {
		return original_.getURL(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getArray(int)
	 */
	public Array getArray(int i) throws SQLException {
		return original_.getArray(i);
	}

	/**
	 * @see java.sql.CallableStatement#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException {
		return original_.getBlob(i);
	}

	/**
	 * @see java.sql.CallableStatement#getClob(int)
	 */
	public Clob getClob(int i) throws SQLException {
		return original_.getClob(i);
	}

	/**
	 * @see java.sql.CallableStatement#getDate(int)
	 */
	public Date getDate(int parameterIndex) throws SQLException {
		return original_.getDate(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getRef(int)
	 */
	public Ref getRef(int i) throws SQLException {
		return original_.getRef(i);
	}

	/**
	 * @see java.sql.CallableStatement#getTime(int)
	 */
	public Time getTime(int parameterIndex) throws SQLException {
		return original_.getTime(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return original_.getTimestamp(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {

		original_.setAsciiStream(parameterName, x, length);
	}

	/**
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {

		original_.setBinaryStream(parameterName, x, length);
	}

	/**
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {

		original_.setCharacterStream(parameterName, reader, length);
	}

	/**
	 * @see java.sql.CallableStatement#getObject(java.lang.String)
	 */
	public Object getObject(String parameterName) throws SQLException {
		return original_.getObject(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object)
	 */
	public void setObject(String parameterName, Object x) throws SQLException {
		original_.setObject(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {

		original_.setObject(parameterName, x, targetSqlType);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {

		original_.setObject(parameterName, x, targetSqlType, scale);
	}

	/**
	 * @see java.sql.CallableStatement#getObject(int, java.util.Map)
	 */
	public Object getObject(int i, Map map) throws SQLException {
		return original_.getObject(i, map);
	}

	/**
	 * @see java.sql.CallableStatement#getString(java.lang.String)
	 */
	public String getString(String parameterName) throws SQLException {
		return original_.getString(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, java.lang.String)
	 */
	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {

		original_.registerOutParameter(parameterName, sqlType, typeName);
	}

	/**
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int, java.lang.String)
	 */
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {

		original_.setNull(parameterName, sqlType, typeName);
	}

	/**
	 * @see java.sql.CallableStatement#setString(java.lang.String, java.lang.String)
	 */
	public void setString(String parameterName, String x) throws SQLException {
		original_.setString(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		return original_.getBigDecimal(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {

		original_.setBigDecimal(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getURL(java.lang.String)
	 */
	public URL getURL(String parameterName) throws SQLException {
		return original_.getURL(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
	 */
	public void setURL(String parameterName, URL val) throws SQLException {
		original_.setURL(parameterName, val);
	}

	/**
	 * @see java.sql.CallableStatement#getArray(java.lang.String)
	 */
	public Array getArray(String parameterName) throws SQLException {
		return original_.getArray(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getBlob(java.lang.String)
	 */
	public Blob getBlob(String parameterName) throws SQLException {
		return original_.getBlob(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getClob(java.lang.String)
	 */
	public Clob getClob(String parameterName) throws SQLException {
		return original_.getClob(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getDate(java.lang.String)
	 */
	public Date getDate(String parameterName) throws SQLException {
		return original_.getDate(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate(String parameterName, Date x) throws SQLException {
		original_.setDate(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		return original_.getDate(parameterIndex, cal);
	}

	/**
	 * @see java.sql.CallableStatement#getRef(java.lang.String)
	 */
	public Ref getRef(String parameterName) throws SQLException {
		return original_.getRef(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#getTime(java.lang.String)
	 */
	public Time getTime(String parameterName) throws SQLException {
		return original_.getTime(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime(String parameterName, Time x) throws SQLException {
		original_.setTime(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		return original_.getTime(parameterIndex, cal);
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		return original_.getTimestamp(parameterName);
	}

	/**
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {

		original_.setTimestamp(parameterName, x);
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {

		return original_.getTimestamp(parameterIndex, cal);
	}

	/**
	 * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String parameterName, Map map) throws SQLException {
		return original_.getObject(parameterName, map);
	}

	/**
	 * @see java.sql.CallableStatement#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		return original_.getDate(parameterName, cal);
	}

	/**
	 * @see java.sql.CallableStatement#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		return original_.getTime(parameterName, cal);
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {

		return original_.getTimestamp(parameterName, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date, java.util.Calendar)
	 */
	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {

		original_.setDate(parameterName, x, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time, java.util.Calendar)
	 */
	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {

		original_.setTime(parameterName, x, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp, java.util.Calendar)
	 */
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {

		original_.setTimestamp(parameterName, x, cal);
	}

	/**
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return original_.executeUpdate();
	}

	/**
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException {
		original_.addBatch();
	}

	/**
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	public void clearParameters() throws SQLException {
		original_.clearParameters();
	}

	/**
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return original_.execute();
	}

	/**
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	public void setByte(int parameterIndex, byte x) throws SQLException {
		original_.setByte(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	public void setDouble(int parameterIndex, double x) throws SQLException {
		original_.setDouble(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	public void setFloat(int parameterIndex, float x) throws SQLException {
		original_.setFloat(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	public void setInt(int parameterIndex, int x) throws SQLException {
		original_.setInt(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		original_.setNull(parameterIndex, sqlType);
	}

	/**
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	public void setLong(int parameterIndex, long x) throws SQLException {
		original_.setLong(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	public void setShort(int parameterIndex, short x) throws SQLException {
		original_.setShort(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		original_.setBoolean(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		original_.setBytes(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {

		original_.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {

		original_.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
	 * @deprecated
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {

		original_.setUnicodeStream(parameterIndex, x, length);
	}

	/**
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {

		original_.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(int parameterIndex, Object x) throws SQLException {
		original_.setObject(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {

		original_.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		
		original_.setObject(parameterIndex, x, targetSqlType, scale);
	}

	/**
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {

		original_.setNull(paramIndex, sqlType, typeName);
	}

	/**
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(int parameterIndex, String x) throws SQLException {
		original_.setString(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {

		original_.setBigDecimal(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		original_.setURL(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	public void setArray(int i, Array x) throws SQLException {
		original_.setArray(i, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	public void setBlob(int i, Blob x) throws SQLException {
		original_.setBlob(i, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	public void setClob(int i, Clob x) throws SQLException {
		original_.setClob(i, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate(int parameterIndex, Date x) throws SQLException {
		original_.setDate(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return original_.getParameterMetaData();
	}

	/**
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	public void setRef(int i, Ref x) throws SQLException {
		original_.setRef(i, x);
	}

	/**
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		return original_.executeQuery();
	}

	/**
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return original_.getMetaData();
	}

	/**
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	public void setTime(int parameterIndex, Time x) throws SQLException {
		original_.setTime(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {

		original_.setTimestamp(parameterIndex, x);
	}

	/**
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
	 */
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {

		original_.setDate(parameterIndex, x, cal);
	}

	/**
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
	 */
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {

		original_.setTime(parameterIndex, x, cal);
	}

	/**
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {

		original_.setTimestamp(parameterIndex, x, cal);
	}

	/**
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return original_.getFetchDirection();
	}

	/**
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return original_.getFetchSize();
	}

	/**
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		return original_.getMaxFieldSize();
	}

	/**
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		return original_.getMaxRows();
	}

	/**
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		return original_.getQueryTimeout();
	}

	/**
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		return original_.getResultSetConcurrency();
	}

	/**
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return original_.getResultSetHoldability();
	}

	/**
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		return original_.getResultSetType();
	}

	/**
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		return original_.getUpdateCount();
	}

	/**
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		original_.cancel();
	}

	/**
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		original_.clearBatch();
	}

	/**
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		original_.clearWarnings();
	}

	/**
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		original_.close();
	}

	/**
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		return original_.getMoreResults();
	}

	/**
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		return original_.executeBatch();
	}

	/**
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		original_.setFetchDirection(direction);
	}

	/**
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		original_.setFetchSize(rows);
	}

	/**
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		original_.setMaxFieldSize(max);
	}

	/**
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		original_.setMaxRows(max);
	}

	/**
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		original_.setQueryTimeout(seconds);
	}

	/**
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return original_.getMoreResults(current);
	}

	/**
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		original_.setEscapeProcessing(enable);
	}

	/**
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		return original_.executeUpdate(sql);
	}

	/**
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		original_.addBatch(sql);
	}

	/**
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		original_.setCursorName(name);
	}

	/**
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
		return original_.execute(sql);
	}

	/**
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {

		return original_.executeUpdate(sql, autoGeneratedKeys);
	}

	/**
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {

		return original_.execute(sql, autoGeneratedKeys);
	}

	/**
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {

		return original_.executeUpdate(sql, columnIndexes);
	}

	/**
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return original_.execute(sql, columnIndexes);
	}

	/**
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return original_.getConnection();
	}

	/**
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return original_.getGeneratedKeys();
	}

	/**
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		return original_.getResultSet();
	}

	/**
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return original_.getWarnings();
	}

	/**
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {

		return original_.executeUpdate(sql, columnNames);
	}

	/**
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {

		return original_.execute(sql, columnNames);
	}

	/**
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return original_.executeQuery(sql);
	}
}
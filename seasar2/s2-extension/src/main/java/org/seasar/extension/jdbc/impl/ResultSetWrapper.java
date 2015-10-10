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
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * 結果セットのラッパです。
 * 
 * @author higa
 * 
 */
public class ResultSetWrapper implements ResultSet {

    private ResultSet original;

    /**
     * {@link ResultSetWrapper}を作成します。
     * 
     * @param original
     *            オリジナル
     */
    public ResultSetWrapper(ResultSet original) {
        this.original = original;
    }

    /**
     * @see java.sql.ResultSet#getConcurrency()
     */
    public int getConcurrency() throws SQLException {
        return original.getConcurrency();
    }

    /**
     * @see java.sql.ResultSet#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return original.getFetchDirection();
    }

    /**
     * @see java.sql.ResultSet#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return original.getFetchSize();
    }

    /**
     * @see java.sql.ResultSet#getRow()
     */
    public int getRow() throws SQLException {
        return original.getRow();
    }

    /**
     * @see java.sql.ResultSet#getType()
     */
    public int getType() throws SQLException {
        return original.getType();
    }

    /**
     * @see java.sql.ResultSet#afterLast()
     */
    public void afterLast() throws SQLException {
        original.afterLast();
    }

    /**
     * @see java.sql.ResultSet#beforeFirst()
     */
    public void beforeFirst() throws SQLException {
        original.beforeFirst();
    }

    /**
     * @see java.sql.ResultSet#cancelRowUpdates()
     */
    public void cancelRowUpdates() throws SQLException {
        original.cancelRowUpdates();
    }

    /**
     * @see java.sql.ResultSet#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        original.clearWarnings();
    }

    /**
     * @see java.sql.ResultSet#close()
     */
    public void close() throws SQLException {
        original.close();
    }

    /**
     * @see java.sql.ResultSet#deleteRow()
     */
    public void deleteRow() throws SQLException {
        original.deleteRow();
    }

    /**
     * @see java.sql.ResultSet#insertRow()
     */
    public void insertRow() throws SQLException {
        original.insertRow();
    }

    /**
     * @see java.sql.ResultSet#moveToCurrentRow()
     */
    public void moveToCurrentRow() throws SQLException {
        original.moveToCurrentRow();
    }

    /**
     * @see java.sql.ResultSet#moveToInsertRow()
     */
    public void moveToInsertRow() throws SQLException {
        original.moveToInsertRow();
    }

    /**
     * @see java.sql.ResultSet#refreshRow()
     */
    public void refreshRow() throws SQLException {
        original.refreshRow();
    }

    /**
     * @see java.sql.ResultSet#updateRow()
     */
    public void updateRow() throws SQLException {
        original.updateRow();
    }

    /**
     * @see java.sql.ResultSet#first()
     */
    public boolean first() throws SQLException {
        return original.first();
    }

    /**
     * @see java.sql.ResultSet#isAfterLast()
     */
    public boolean isAfterLast() throws SQLException {
        return original.isAfterLast();
    }

    /**
     * @see java.sql.ResultSet#isBeforeFirst()
     */
    public boolean isBeforeFirst() throws SQLException {
        return original.isBeforeFirst();
    }

    /**
     * @see java.sql.ResultSet#isFirst()
     */
    public boolean isFirst() throws SQLException {
        return original.isFirst();
    }

    /**
     * @see java.sql.ResultSet#isLast()
     */
    public boolean isLast() throws SQLException {
        return original.isLast();
    }

    /**
     * @see java.sql.ResultSet#last()
     */
    public boolean last() throws SQLException {
        return original.last();
    }

    /**
     * @see java.sql.ResultSet#next()
     */
    public boolean next() throws SQLException {
        return original.next();
    }

    /**
     * @see java.sql.ResultSet#previous()
     */
    public boolean previous() throws SQLException {
        return original.previous();
    }

    /**
     * @see java.sql.ResultSet#rowDeleted()
     */
    public boolean rowDeleted() throws SQLException {
        return original.rowDeleted();
    }

    /**
     * @see java.sql.ResultSet#rowInserted()
     */
    public boolean rowInserted() throws SQLException {
        return original.rowInserted();
    }

    /**
     * @see java.sql.ResultSet#rowUpdated()
     */
    public boolean rowUpdated() throws SQLException {
        return original.rowUpdated();
    }

    /**
     * @see java.sql.ResultSet#wasNull()
     */
    public boolean wasNull() throws SQLException {
        return original.wasNull();
    }

    /**
     * @see java.sql.ResultSet#getByte(int)
     */
    public byte getByte(int columnIndex) throws SQLException {
        return original.getByte(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getDouble(int)
     */
    public double getDouble(int columnIndex) throws SQLException {
        return original.getDouble(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getFloat(int)
     */
    public float getFloat(int columnIndex) throws SQLException {
        return original.getFloat(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getInt(int)
     */
    public int getInt(int columnIndex) throws SQLException {
        return original.getInt(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getLong(int)
     */
    public long getLong(int columnIndex) throws SQLException {
        return original.getLong(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getShort(int)
     */
    public short getShort(int columnIndex) throws SQLException {
        return original.getShort(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        original.setFetchDirection(direction);
    }

    /**
     * @see java.sql.ResultSet#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        original.setFetchSize(rows);
    }

    /**
     * @see java.sql.ResultSet#updateNull(int)
     */
    public void updateNull(int columnIndex) throws SQLException {
        original.updateNull(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#absolute(int)
     */
    public boolean absolute(int row) throws SQLException {
        return original.absolute(row);
    }

    /**
     * @see java.sql.ResultSet#getBoolean(int)
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        return original.getBoolean(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#relative(int)
     */
    public boolean relative(int rows) throws SQLException {
        return original.relative(rows);
    }

    /**
     * @see java.sql.ResultSet#getBytes(int)
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        return original.getBytes(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateByte(int, byte)
     */
    public void updateByte(int columnIndex, byte x) throws SQLException {
        original.updateByte(columnIndex, x);

    }

    /**
     * @see java.sql.ResultSet#updateDouble(int, double)
     */
    public void updateDouble(int columnIndex, double x) throws SQLException {
        original.updateDouble(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateFloat(int, float)
     */
    public void updateFloat(int columnIndex, float x) throws SQLException {
        original.updateFloat(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateInt(int, int)
     */
    public void updateInt(int columnIndex, int x) throws SQLException {
        original.updateInt(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateLong(int, long)
     */
    public void updateLong(int columnIndex, long x) throws SQLException {
        original.updateLong(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateShort(int, short)
     */
    public void updateShort(int columnIndex, short x) throws SQLException {
        original.updateShort(columnIndex, x);

    }

    /**
     * @see java.sql.ResultSet#updateBoolean(int, boolean)
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        original.updateBoolean(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateBytes(int, byte[])
     */
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        original.updateBytes(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getAsciiStream(int)
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return original.getAsciiStream(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getBinaryStream(int)
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return original.getBinaryStream(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getUnicodeStream(int)
     * @deprecated use <code>getCharacterStream</code> in place of
     *             <code>getUnicodeStream</code>
     */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return original.getUnicodeStream(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {

        original.updateAsciiStream(columnIndex, x, length);
    }

    /**
     * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {

        original.updateBinaryStream(columnIndex, x, length);
    }

    /**
     * @see java.sql.ResultSet#getCharacterStream(int)
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return original.getCharacterStream(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {

        original.updateCharacterStream(columnIndex, x, length);
    }

    /**
     * @see java.sql.ResultSet#getObject(int)
     */
    public Object getObject(int columnIndex) throws SQLException {
        return original.getObject(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
     */
    public void updateObject(int columnIndex, Object x) throws SQLException {
        original.updateObject(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
     */
    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException {

        original.updateObject(columnIndex, x, scale);
    }

    /**
     * @see java.sql.ResultSet#getCursorName()
     */
    public String getCursorName() throws SQLException {
        return original.getCursorName();
    }

    /**
     * @see java.sql.ResultSet#getString(int)
     */
    public String getString(int columnIndex) throws SQLException {
        return original.getString(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateString(int, java.lang.String)
     */
    public void updateString(int columnIndex, String x) throws SQLException {
        original.updateString(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getByte(java.lang.String)
     */
    public byte getByte(String columnName) throws SQLException {
        return original.getByte(columnName);
    }

    /**
     * @see java.sql.ResultSet#getDouble(java.lang.String)
     */
    public double getDouble(String columnName) throws SQLException {
        return original.getDouble(columnName);
    }

    /**
     * @see java.sql.ResultSet#getFloat(java.lang.String)
     */
    public float getFloat(String columnName) throws SQLException {
        return original.getFloat(columnName);
    }

    /**
     * @see java.sql.ResultSet#findColumn(java.lang.String)
     */
    public int findColumn(String columnName) throws SQLException {
        return original.findColumn(columnName);
    }

    /**
     * @see java.sql.ResultSet#getInt(java.lang.String)
     */
    public int getInt(String columnName) throws SQLException {
        return original.getInt(columnName);
    }

    /**
     * @see java.sql.ResultSet#getLong(java.lang.String)
     */
    public long getLong(String columnName) throws SQLException {
        return original.getLong(columnName);
    }

    /**
     * @see java.sql.ResultSet#getShort(java.lang.String)
     */
    public short getShort(String columnName) throws SQLException {
        return original.getShort(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateNull(java.lang.String)
     */
    public void updateNull(String columnName) throws SQLException {
        original.updateNull(columnName);
    }

    /**
     * @see java.sql.ResultSet#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String columnName) throws SQLException {
        return original.getBoolean(columnName);
    }

    /**
     * @see java.sql.ResultSet#getBytes(java.lang.String)
     */
    public byte[] getBytes(String columnName) throws SQLException {
        return original.getBytes(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
     */
    public void updateByte(String columnName, byte x) throws SQLException {
        original.updateByte(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
     */
    public void updateDouble(String columnName, double x) throws SQLException {
        original.updateDouble(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
     */
    public void updateFloat(String columnName, float x) throws SQLException {
        original.updateFloat(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateInt(java.lang.String, int)
     */
    public void updateInt(String columnName, int x) throws SQLException {
        original.updateInt(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateLong(java.lang.String, long)
     */
    public void updateLong(String columnName, long x) throws SQLException {
        original.updateLong(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateShort(java.lang.String, short)
     */
    public void updateShort(String columnName, short x) throws SQLException {
        original.updateShort(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
     */
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        original.updateBoolean(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
     */
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        original.updateBytes(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return original.getBigDecimal(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(int, int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {

        return original.getBigDecimal(columnIndex, scale);
    }

    /**
     * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {

        original.updateBigDecimal(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getURL(int)
     */
    public URL getURL(int columnIndex) throws SQLException {
        return original.getURL(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#getArray(int)
     */
    public Array getArray(int i) throws SQLException {
        return original.getArray(i);
    }

    /**
     * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
     */
    public void updateArray(int columnIndex, Array x) throws SQLException {
        original.updateArray(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getBlob(int)
     */
    public Blob getBlob(int i) throws SQLException {
        return original.getBlob(i);
    }

    /**
     * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
     */
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        original.updateBlob(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getClob(int)
     */
    public Clob getClob(int i) throws SQLException {
        return original.getClob(i);
    }

    /**
     * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
     */
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        original.updateClob(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getDate(int)
     */
    public Date getDate(int columnIndex) throws SQLException {
        return original.getDate(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
     */
    public void updateDate(int columnIndex, Date x) throws SQLException {
        original.updateDate(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getRef(int)
     */
    public Ref getRef(int i) throws SQLException {
        return original.getRef(i);
    }

    /**
     * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
     */
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        original.updateRef(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return original.getMetaData();
    }

    /**
     * @see java.sql.ResultSet#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return original.getWarnings();
    }

    /**
     * @see java.sql.ResultSet#getStatement()
     */
    public Statement getStatement() throws SQLException {
        return original.getStatement();
    }

    /**
     * @see java.sql.ResultSet#getTime(int)
     */
    public Time getTime(int columnIndex) throws SQLException {
        return original.getTime(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
     */
    public void updateTime(int columnIndex, Time x) throws SQLException {
        original.updateTime(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(int)
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return original.getTimestamp(columnIndex);
    }

    /**
     * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
     */
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {

        original.updateTimestamp(columnIndex, x);
    }

    /**
     * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
     */
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return original.getAsciiStream(columnName);
    }

    /**
     * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
     */
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return original.getBinaryStream(columnName);
    }

    /**
     * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
     * @deprecated use <code>getCharacterStream</code> instead
     */
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return original.getUnicodeStream(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateAsciiStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void updateAsciiStream(String columnName, InputStream x, int length)
            throws SQLException {

        original.updateAsciiStream(columnName, x, length);
    }

    /**
     * @see java.sql.ResultSet#updateBinaryStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void updateBinaryStream(String columnName, InputStream x, int length)
            throws SQLException {

        original.updateBinaryStream(columnName, x, length);
    }

    /**
     * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
     */
    public Reader getCharacterStream(String columnName) throws SQLException {
        return original.getCharacterStream(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateCharacterStream(java.lang.String,
     *      java.io.Reader, int)
     */
    public void updateCharacterStream(String columnName, Reader reader,
            int length) throws SQLException {

        original.updateCharacterStream(columnName, reader, length);
    }

    /**
     * @see java.sql.ResultSet#getObject(java.lang.String)
     */
    public Object getObject(String columnName) throws SQLException {
        return original.getObject(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
     */
    public void updateObject(String columnName, Object x) throws SQLException {
        original.updateObject(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object,
     *      int)
     */
    public void updateObject(String columnName, Object x, int scale)
            throws SQLException {

        original.updateObject(columnName, x, scale);
    }

    /**
     * @see java.sql.ResultSet#getObject(int, java.util.Map)
     */
    public Object getObject(int i, Map map) throws SQLException {
        return original.getObject(i, map);
    }

    /**
     * @see java.sql.ResultSet#getString(java.lang.String)
     */
    public String getString(String columnName) throws SQLException {
        return original.getString(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
     */
    public void updateString(String columnName, String x) throws SQLException {
        original.updateString(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return original.getBigDecimal(columnName);
    }

    /**
     * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException {

        return original.getBigDecimal(columnName, scale);
    }

    /**
     * @see java.sql.ResultSet#updateBigDecimal(java.lang.String,
     *      java.math.BigDecimal)
     */
    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException {

        original.updateBigDecimal(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getURL(java.lang.String)
     */
    public URL getURL(String columnName) throws SQLException {
        return original.getURL(columnName);
    }

    /**
     * @see java.sql.ResultSet#getArray(java.lang.String)
     */
    public Array getArray(String colName) throws SQLException {
        return original.getArray(colName);
    }

    /**
     * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
     */
    public void updateArray(String columnName, Array x) throws SQLException {
        original.updateArray(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getBlob(java.lang.String)
     */
    public Blob getBlob(String colName) throws SQLException {
        return original.getBlob(colName);
    }

    /**
     * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
     */
    public void updateBlob(String columnName, Blob x) throws SQLException {
        original.updateBlob(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getClob(java.lang.String)
     */
    public Clob getClob(String colName) throws SQLException {
        return original.getClob(colName);
    }

    /**
     * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
     */
    public void updateClob(String columnName, Clob x) throws SQLException {
        original.updateClob(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getDate(java.lang.String)
     */
    public Date getDate(String columnName) throws SQLException {
        return original.getDate(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
     */
    public void updateDate(String columnName, Date x) throws SQLException {
        original.updateDate(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return original.getDate(columnIndex, cal);
    }

    /**
     * @see java.sql.ResultSet#getRef(java.lang.String)
     */
    public Ref getRef(String colName) throws SQLException {
        return original.getRef(colName);
    }

    /**
     * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
     */
    public void updateRef(String columnName, Ref x) throws SQLException {
        original.updateRef(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getTime(java.lang.String)
     */
    public Time getTime(String columnName) throws SQLException {
        return original.getTime(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
     */
    public void updateTime(String columnName, Time x) throws SQLException {
        original.updateTime(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return original.getTime(columnIndex, cal);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return original.getTimestamp(columnName);
    }

    /**
     * @see java.sql.ResultSet#updateTimestamp(java.lang.String,
     *      java.sql.Timestamp)
     */
    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException {

        original.updateTimestamp(columnName, x);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {

        return original.getTimestamp(columnIndex, cal);
    }

    /**
     * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
     */
    public Object getObject(String colName, Map map) throws SQLException {
        return original.getObject(colName, map);
    }

    /**
     * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return original.getDate(columnName, cal);
    }

    /**
     * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return original.getTime(columnName, cal);
    }

    /**
     * @see java.sql.ResultSet#getTimestamp(java.lang.String,
     *      java.util.Calendar)
     */
    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException {

        return original.getTimestamp(columnName, cal);
    }
}
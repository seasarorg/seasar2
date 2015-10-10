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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.BigDecimalConversionUtil;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.ByteConversionUtil;
import org.seasar.framework.util.DoubleConversionUtil;
import org.seasar.framework.util.FloatConversionUtil;
import org.seasar.framework.util.IntegerConversionUtil;
import org.seasar.framework.util.LongConversionUtil;
import org.seasar.framework.util.ShortConversionUtil;
import org.seasar.framework.util.SqlDateConversionUtil;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * {@link ResultSet}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockResultSet implements ResultSet {

    private MockResultSetMetaData metaData;

    private List rowDataList = new ArrayList();

    private int row = 0;

    private int type;

    private int concurrency;

    private boolean closed = false;

    private int fetchSize;

    private MockStatement statement;

    /**
     * {@link MockResultSet}を作成します。
     * 
     */
    public MockResultSet() {
        this(null);
    }

    /**
     * {@link MockResultSet}を作成します。
     * 
     * @param metaData
     *            結果セットメタデータ
     */
    public MockResultSet(MockResultSetMetaData metaData) {
        this(metaData, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
    }

    /**
     * {@link MockResultSet}を作成します。
     * 
     * @param metaData
     *            結果セットメタデータ
     * @param type
     *            結果セットタイプ
     * @param concurrency
     *            結果セット同時並行性
     */
    public MockResultSet(MockResultSetMetaData metaData, int type,
            int concurrency) {
        setMockMetaData(metaData);
        setType(type);
        setConcurrency(concurrency);
    }

    /**
     * 現在の行データを返します。
     * 
     * @return 現在の行データ
     */
    public ArrayMap getRowData() {
        return getRowData(row);
    }

    /**
     * <p>
     * 特定の行のデータを返します。
     * </p>
     * <p>
     * 行番号は1からはじまります。
     * </p>
     * 
     * @param index
     *            行番号
     * @return 特定の行のデータ
     */
    public ArrayMap getRowData(int index) {
        return (ArrayMap) rowDataList.get(index - 1);
    }

    /**
     * <p>
     * 特定のカラムのデータを返します。
     * </p>
     * <p>
     * カラム番号は1からはじまります。
     * </p>
     * 
     * @param index
     *            カラム番号
     * @return 特定のカラムのデータ
     */
    public Object getColumnData(int index) {
        return getRowData().get(index - 1);
    }

    /**
     * <p>
     * 特定のカラムのデータを返します。
     * </p>
     * 
     * @param columnName
     *            カラム名
     * @return 特定のカラムのデータ
     */
    public Object getColumnData(String columnName) {
        return getRowData().get(columnName);
    }

    /**
     * 行データを追加します。
     * 
     * @param rowData
     *            行データ
     */
    public void addRowData(ArrayMap rowData) {
        rowDataList.add(rowData);
    }

    public boolean absolute(int row) throws SQLException {
        assertCursor();
        if (row > 0 && row <= rowDataList.size()) {
            this.row = row;
            return true;
        }
        return false;
    }

    /**
     * カーソルが使えることを表明します。
     * 
     * @throws SQLException
     *             カーソルが使えない場合。
     */
    protected void assertCursor() throws SQLException {
        if (type == TYPE_FORWARD_ONLY) {
            throw new SQLException("cursor not supported");
        }
    }

    public void afterLast() throws SQLException {
        assertCursor();
        row = rowDataList.size() + 1;
    }

    public void beforeFirst() throws SQLException {
        assertCursor();
        row = 0;
    }

    public void cancelRowUpdates() throws SQLException {
        assertUpdatable();
    }

    /**
     * 更新可能であることを表明します。
     * 
     * @throws SQLException
     *             更新可能でない場合。
     */
    protected void assertUpdatable() throws SQLException {
        if (concurrency == CONCUR_READ_ONLY) {
            throw new SQLException("update not supported");
        }
    }

    public void clearWarnings() throws SQLException {
    }

    public void close() throws SQLException {
        closed = true;
    }

    /**
     * 閉じているかどうかを返します。
     * 
     * @return 閉じているかどうか
     */
    public boolean isClosed() {
        return closed;
    }

    public void deleteRow() throws SQLException {
        assertUpdatable();
    }

    public int findColumn(String columnName) throws SQLException {
        return metaData.findColumn(columnName);
    }

    public boolean first() throws SQLException {
        return absolute(1);
    }

    public Array getArray(int columnIndex) throws SQLException {
        return (Array) getColumnData(columnIndex);
    }

    public Array getArray(String columnName) throws SQLException {
        return (Array) getColumnData(columnName);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return BigDecimalConversionUtil
                .toBigDecimal(getColumnData(columnIndex));
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return BigDecimalConversionUtil.toBigDecimal(getColumnData(columnName));
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        BigDecimal value = getBigDecimal(columnIndex);
        if (value == null) {
            return null;
        }
        value = value.setScale(scale);
        return value;
    }

    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException {
        BigDecimal value = getBigDecimal(columnName);
        if (value == null) {
            return null;
        }
        value = value.setScale(scale);
        return value;
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return (Blob) getColumnData(columnIndex);
    }

    public Blob getBlob(String columnName) throws SQLException {
        return (Blob) getColumnData(columnName);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return BooleanConversionUtil
                .toPrimitiveBoolean(getColumnData(columnIndex));
    }

    public boolean getBoolean(String columnName) throws SQLException {
        return BooleanConversionUtil
                .toPrimitiveBoolean(getColumnData(columnName));
    }

    public byte getByte(int columnIndex) throws SQLException {
        return ByteConversionUtil.toPrimitiveByte(getColumnData(columnIndex));
    }

    public byte getByte(String columnName) throws SQLException {
        return ByteConversionUtil.toPrimitiveByte(getColumnData(columnName));
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return (byte[]) getColumnData(columnIndex);
    }

    public byte[] getBytes(String columnName) throws SQLException {
        return (byte[]) getColumnData(columnName);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return (Reader) getColumnData(columnIndex);
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        return (Reader) getColumnData(columnName);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return (Clob) getColumnData(columnIndex);
    }

    public Clob getClob(String columnName) throws SQLException {
        return (Clob) getColumnData(columnName);
    }

    public int getConcurrency() throws SQLException {
        return concurrency;
    }

    /**
     * 結果セット同時並行性を設定します。
     * 
     * @param concurrency
     *            結果セット同時並行性
     */
    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public String getCursorName() throws SQLException {
        return null;
    }

    public Date getDate(int columnIndex) throws SQLException {
        return SqlDateConversionUtil.toDate(getColumnData(columnIndex));
    }

    public Date getDate(String columnName) throws SQLException {
        return SqlDateConversionUtil.toDate(getColumnData(columnName));
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDate(columnIndex);
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return getDate(columnName);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return DoubleConversionUtil
                .toPrimitiveDouble(getColumnData(columnIndex));
    }

    public double getDouble(String columnName) throws SQLException {
        return DoubleConversionUtil
                .toPrimitiveDouble(getColumnData(columnName));
    }

    public int getFetchDirection() throws SQLException {
        return 0;
    }

    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    public float getFloat(int columnIndex) throws SQLException {
        return FloatConversionUtil.toPrimitiveFloat(getColumnData(columnIndex));
    }

    public float getFloat(String columnName) throws SQLException {
        return FloatConversionUtil.toPrimitiveFloat(getColumnData(columnName));
    }

    public int getInt(int columnIndex) throws SQLException {
        return IntegerConversionUtil.toPrimitiveInt(getColumnData(columnIndex));
    }

    public int getInt(String columnName) throws SQLException {
        return IntegerConversionUtil.toPrimitiveInt(getColumnData(columnName));
    }

    public long getLong(int columnIndex) throws SQLException {
        return LongConversionUtil.toPrimitiveLong(getColumnData(columnIndex));
    }

    public long getLong(String columnName) throws SQLException {
        return LongConversionUtil.toPrimitiveLong(getColumnData(columnName));
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return getMockMetaData();
    }

    /**
     * {@link ResultSetMetaData}用のモックを返します。
     * 
     * @return {@link ResultSetMetaData}用のモック
     */
    public MockResultSetMetaData getMockMetaData() {
        return metaData;
    }

    /**
     * {@link ResultSetMetaData}用のモックを設定します。
     * 
     * @param metaData
     *            {@link ResultSetMetaData}用のモック
     */
    public void setMockMetaData(MockResultSetMetaData metaData) {
        this.metaData = metaData;
    }

    public Object getObject(int columnIndex) throws SQLException {
        return getColumnData(columnIndex);
    }

    public Object getObject(String columnName) throws SQLException {
        return getColumnData(columnName);
    }

    public Object getObject(int columnIndex, Map arg1) throws SQLException {
        return getObject(columnIndex);
    }

    public Object getObject(String columnName, Map arg1) throws SQLException {
        return getObject(columnName);
    }

    public Ref getRef(int columnIndex) throws SQLException {
        return (Ref) getColumnData(columnIndex);
    }

    public Ref getRef(String columnName) throws SQLException {
        return (Ref) getColumnData(columnName);
    }

    public int getRow() throws SQLException {
        return row;
    }

    public short getShort(int columnIndex) throws SQLException {
        return ShortConversionUtil.toPrimitiveShort(getColumnData(columnIndex));
    }

    public short getShort(String columnName) throws SQLException {
        return ShortConversionUtil.toPrimitiveShort(getColumnData(columnName));
    }

    public Statement getStatement() throws SQLException {
        return getMockStatement();
    }

    /**
     * ステートメント用のモックを返します。
     * 
     * @return ステートメント用のモック
     */
    public MockStatement getMockStatement() {
        return statement;
    }

    /**
     * ステートメント用のモックを設定します。
     * 
     * @param statement
     *            ステートメント用のモック
     */
    public void setMockStatement(MockStatement statement) {
        this.statement = statement;
    }

    public String getString(int columnIndex) throws SQLException {
        return StringConversionUtil.toString(getColumnData(columnIndex));
    }

    public String getString(String columnName) throws SQLException {
        return StringConversionUtil.toString(getColumnData(columnName));
    }

    public Time getTime(int columnIndex) throws SQLException {
        return TimeConversionUtil.toTime(getColumnData(columnIndex));
    }

    public Time getTime(String columnName) throws SQLException {
        return TimeConversionUtil.toTime(getColumnData(columnName));
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTime(columnIndex);
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return getTime(columnName);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return TimestampConversionUtil.toTimestamp(getColumnData(columnIndex));
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        return TimestampConversionUtil.toTimestamp(getColumnData(columnName));
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException {
        return getTimestamp(columnName);
    }

    public int getType() throws SQLException {
        return type;
    }

    /**
     * 結果セットタイプを設定します。
     * 
     * @param type
     *            結果セットタイプ
     */
    public void setType(int type) {
        this.type = type;
    }

    public URL getURL(int columnIndex) throws SQLException {
        return (URL) getColumnData(columnIndex);
    }

    public URL getURL(String columnName) throws SQLException {
        return (URL) getColumnData(columnName);
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void insertRow() throws SQLException {
        assertUpdatable();
    }

    public boolean isAfterLast() throws SQLException {
        return row > rowDataList.size();
    }

    public boolean isBeforeFirst() throws SQLException {
        return row == 0;
    }

    public boolean isFirst() throws SQLException {
        return row == 1;
    }

    public boolean isLast() throws SQLException {
        return row == rowDataList.size();
    }

    public boolean last() throws SQLException {
        return absolute(rowDataList.size());
    }

    public void moveToCurrentRow() throws SQLException {
    }

    public void moveToInsertRow() throws SQLException {
    }

    public boolean next() throws SQLException {
        if (row < rowDataList.size()) {
            ++row;
            return true;
        } else if (row == rowDataList.size()) {
            ++row;
            return false;
        }
        return false;
    }

    public boolean previous() throws SQLException {
        assertCursor();
        if (row > 1) {
            --row;
            return true;
        } else if (row == 1) {
            --row;
            return false;
        }
        return false;
    }

    public void refreshRow() throws SQLException {
    }

    public boolean relative(int rows) throws SQLException {
        return absolute(row + rows);
    }

    public boolean rowDeleted() throws SQLException {
        return false;
    }

    public boolean rowInserted() throws SQLException {
        return false;
    }

    public boolean rowUpdated() throws SQLException {
        return false;
    }

    public void setFetchDirection(int direction) throws SQLException {
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        this.fetchSize = fetchSize;
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        assertUpdatable();
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        assertUpdatable();
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    public void updateAsciiStream(String columnName, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
        assertUpdatable();
    }

    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException {
        assertUpdatable();
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    public void updateBinaryStream(String columnName, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        assertUpdatable();
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        assertUpdatable();
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        assertUpdatable();
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
        assertUpdatable();
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        assertUpdatable();
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        assertUpdatable();
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        assertUpdatable();
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException {
        assertUpdatable();
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
        assertUpdatable();
    }

    public void updateCharacterStream(String columnName, Reader reader,
            int length) throws SQLException {
        assertUpdatable();
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        assertUpdatable();
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        assertUpdatable();
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        assertUpdatable();
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        assertUpdatable();
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        assertUpdatable();
    }

    public void updateDouble(String columnName, double x) throws SQLException {
        assertUpdatable();
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        assertUpdatable();
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        assertUpdatable();
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        assertUpdatable();
    }

    public void updateInt(String columnName, int x) throws SQLException {
        assertUpdatable();
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        assertUpdatable();
    }

    public void updateLong(String columnName, long x) throws SQLException {
        assertUpdatable();
    }

    public void updateNull(int columnIndex) throws SQLException {
        assertUpdatable();
    }

    public void updateNull(String columnName) throws SQLException {
        assertUpdatable();
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        assertUpdatable();
    }

    public void updateObject(String columnName, Object x) throws SQLException {
        assertUpdatable();
    }

    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException {
        assertUpdatable();
    }

    public void updateObject(String columnName, Object x, int scale)
            throws SQLException {
        assertUpdatable();
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        assertUpdatable();
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        assertUpdatable();
    }

    public void updateRow() throws SQLException {
        assertUpdatable();
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        assertUpdatable();
    }

    public void updateShort(String columnName, short x) throws SQLException {
        assertUpdatable();
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        assertUpdatable();
    }

    public void updateString(String columnName, String x) throws SQLException {
        assertUpdatable();
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        assertUpdatable();
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        assertUpdatable();
    }

    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
        assertUpdatable();
    }

    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException {
        assertUpdatable();
    }

    public boolean wasNull() throws SQLException {
        return false;
    }
}
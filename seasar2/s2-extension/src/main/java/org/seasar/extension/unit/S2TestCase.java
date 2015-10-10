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
package org.seasar.extension.unit;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.impl.SqlDeleteTableWriter;
import org.seasar.extension.dataset.impl.SqlReader;
import org.seasar.extension.dataset.impl.SqlReloadReader;
import org.seasar.extension.dataset.impl.SqlReloadTableReader;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.extension.dataset.impl.XlsReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * トランザクションやデータソースを使うためのTestCaseです。
 * 
 * @author higa
 */
public abstract class S2TestCase extends S2FrameworkTestCase {

    private static final String DATASOURCE_NAME = "j2ee"
            + ContainerConstants.NS_SEP + "dataSource";

    private DataSource dataSource;

    private Connection connection;

    private DatabaseMetaData dbMetaData;

    /**
     * {@link S2TestCase}を作成します。
     */
    public S2TestCase() {
    }

    /**
     * {@link S2TestCase}を作成します。
     * 
     * @param name
     *            名前
     */
    public S2TestCase(String name) {
        super(name);
    }

    protected void doRunTest() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = (TransactionManager) getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            super.doRunTest();
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }

    /**
     * トランザクション処理が必要かどうかを返します。
     * 
     * @return トランザクション処理が必要かどうか
     */
    protected boolean needTransaction() {
        return getName().endsWith("Tx");
    }

    protected void setUpAfterContainerInit() throws Throwable {
        super.setUpAfterContainerInit();
        setupDataSource();
    }

    protected void tearDownBeforeContainerDestroy() throws Throwable {
        tearDownDataSource();
        super.tearDownBeforeContainerDestroy();
    }

    /**
     * 
     */
    protected void setupDataSource() {
        S2Container container = getContainer();
        try {
            if (container.hasComponentDef(DATASOURCE_NAME)) {
                dataSource = (DataSource) container
                        .getComponent(DATASOURCE_NAME);
            } else if (container.hasComponentDef(DataSource.class)) {
                dataSource = (DataSource) container
                        .getComponent(DataSource.class);
            }
        } catch (Throwable t) {
            System.err.println(t);
        }
    }

    /**
     * データソースの終了処理を行ないます。
     */
    protected void tearDownDataSource() {
        dbMetaData = null;
        if (connection != null) {
            ConnectionUtil.close(connection);
            connection = null;
        }
        dataSource = null;
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new EmptyRuntimeException("dataSource");
        }
        return dataSource;
    }

    /**
     * コネクションを返します。
     * 
     * @return コネクション
     */
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        connection = DataSourceUtil.getConnection(getDataSource());
        return connection;
    }

    /**
     * データベースメタデータを返します。
     * 
     * @return データベースメタデータ
     */
    public DatabaseMetaData getDatabaseMetaData() {
        if (dbMetaData != null) {
            return dbMetaData;
        }
        dbMetaData = ConnectionUtil.getMetaData(getConnection());
        return dbMetaData;
    }

    /**
     * エクセルを読み込みます。
     * 
     * @param path
     *            パス
     * @return エクセルのデータ
     */
    public DataSet readXls(String path) {
        return readXls(path, true);
    }

    /**
     * エクセルを読み込みます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     * @return エクセルのデータ
     */
    public DataSet readXls(String path, boolean trimString) {
        DataReader reader = new XlsReader(convertPath(path), trimString);
        return reader.read();
    }

    /**
     * エクセルに書き込みます。
     * 
     * @param path
     *            パス
     * @param dataSet
     *            データセット
     */
    public void writeXls(String path, DataSet dataSet) {
        File dir = ResourceUtil.getBuildDir(getClass());
        File file = new File(dir, convertPath(path));
        DataWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
        writer.write(dataSet);
    }

    /**
     * データベースに書き込みます。
     * 
     * @param dataSet
     *            データセット
     */
    public void writeDb(DataSet dataSet) {
        SqlWriter writer = getSqlWriter();
        writer.write(dataSet);
    }

    /**
     * {@link SqlWriter}を返します。
     * 
     * @return {@link SqlWriter}
     */
    protected SqlWriter getSqlWriter() {
        S2Container container = getContainer();
        if (container.hasComponentDef(SqlWriter.class)) {
            return (SqlWriter) container.getComponent(SqlWriter.class);
        }
        return new SqlWriter(getDataSource());
    }

    /**
     * データベースの内容を読み込みます。
     * 
     * @param dataSet
     *            データセット
     * @return データベースの内容
     */
    public DataSet readDb(DataSet dataSet) {
        SqlReader reader = new SqlReader(getDataSource());
        reader.addDataSet(dataSet);
        return reader.read();
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param table
     *            テーブル
     * @return テーブルの内容
     */
    public DataTable readDbByTable(String table) {
        return readDbByTable(table, null);
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param table
     *            テーブル名
     * @param condition
     *            条件
     * @return テーブルの内容
     */
    public DataTable readDbByTable(String table, String condition) {
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setTable(table, condition);
        return reader.read();
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param sql
     *            SQL
     * @param tableName
     *            テーブル名
     * @return テーブルの内容
     */
    public DataTable readDbBySql(String sql, String tableName) {
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setSql(sql, tableName);
        return reader.read();
    }

    /**
     * エクセルから読み込んだデータをデータベースに書き出します。
     * 
     * @param path
     *            パス
     */
    public void readXlsWriteDb(String path) {
        readXlsWriteDb(path, true);
    }

    /**
     * エクセルから読み込んだデータをデータベースに書き出します。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readXlsWriteDb(String path, boolean trimString) {
        writeDb(readXls(path, trimString));
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を置き換えます。
     * 
     * @param path
     *            パス
     */
    public void readXlsReplaceDb(String path) {
        readXlsReplaceDb(path, true);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を置き換えます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readXlsReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readXls(path, trimString);
        deleteDb(dataSet);
        writeDb(dataSet);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を全部置き換えます。
     * 
     * @param path
     *            パス
     */
    public void readXlsAllReplaceDb(String path) {
        readXlsAllReplaceDb(path, true);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を全部置き換えます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readXlsAllReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readXls(path, trimString);
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            deleteTable(dataSet.getTable(i).getTableName());
        }
        writeDb(dataSet);
    }

    /**
     * リロードします。
     * 
     * @param dataSet
     *            データセット
     * @return リロードした結果
     */
    public DataSet reload(DataSet dataSet) {
        return new SqlReloadReader(getDataSource(), dataSet).read();
    }

    /**
     * @param table
     * @return
     */
    public DataTable reload(DataTable table) {
        return new SqlReloadTableReader(getDataSource(), table).read();
    }

    /**
     * テーブルの内容をリロードもしくは読み込みます。
     * 
     * @param dataSet
     *            データセット
     * @return 読み込んだ結果
     */
    public DataSet reloadOrReadDb(DataSet dataSet) {
        DataSet newDataSet = new DataSetImpl();
        outer: for (int i = 0; i < dataSet.getTableSize(); i++) {
            DataTable table = dataSet.getTable(i);
            if (!table.hasMetaData()) {
                table.setupMetaData(getDatabaseMetaData());
            }
            for (int j = 0; j < table.getColumnSize(); j++) {
                DataColumn column = table.getColumn(j);
                if (column.isPrimaryKey()) {
                    newDataSet.addTable(reload(table));
                    continue outer;
                }
            }
            newDataSet.addTable(readDbByTable(table.getTableName()));
        }
        return newDataSet;
    }

    /**
     * データベースからデータを削除します。
     * 
     * @param dataSet
     *            データセット
     */
    public void deleteDb(DataSet dataSet) {
        SqlDeleteTableWriter writer = new SqlDeleteTableWriter(getDataSource());
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            writer.write(dataSet.getTable(i));
        }
    }

    /**
     * テーブルのデータを削除します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void deleteTable(String tableName) {
        UpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                "DELETE FROM " + tableName);
        handler.execute(null);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(DataSet expected, DataSet actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(String message, DataSet expected, DataSet actual) {
        message = message == null ? "" : message;
        assertEquals(message + ":TableSize", expected.getTableSize(), actual
                .getTableSize());
        for (int i = 0; i < expected.getTableSize(); ++i) {
            assertEquals(message, expected.getTable(i), actual.getTable(i));
        }
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(DataTable expected, DataTable actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(String message, DataTable expected,
            DataTable actual) {

        message = message == null ? "" : message;
        message = message + ":TableName=" + expected.getTableName();
        assertEquals(message + ":RowSize", expected.getRowSize(), actual
                .getRowSize());
        for (int i = 0; i < expected.getRowSize(); ++i) {
            DataRow expectedRow = expected.getRow(i);
            DataRow actualRow = actual.getRow(i);
            List errorMessages = new ArrayList();
            for (int j = 0; j < expected.getColumnSize(); ++j) {
                try {
                    String columnName = expected.getColumnName(j);
                    Object expectedValue = expectedRow.getValue(columnName);
                    ColumnType ct = ColumnTypes.getColumnType(expectedValue);
                    Object actualValue = actualRow.getValue(columnName);
                    if (!ct.equals(expectedValue, actualValue)) {
                        assertEquals(message + ":Row=" + i + ":columnName="
                                + columnName, expectedValue, actualValue);
                    }
                } catch (AssertionFailedError e) {
                    errorMessages.add(e.getMessage());
                }
            }
            if (!errorMessages.isEmpty()) {
                fail(message + errorMessages);
            }
        }
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(DataSet expected, Object actual) {
        assertEquals(null, expected, actual);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    public void assertEquals(String message, DataSet expected, Object actual) {
        if (expected == null || actual == null) {
            Assert.assertEquals(message, expected, actual);
            return;
        }
        if (actual instanceof List) {
            List actualList = (List) actual;
            Assert.assertFalse(actualList.isEmpty());
            Object actualItem = actualList.get(0);
            if (actualItem instanceof Map) {
                assertMapListEquals(message, expected, actualList);
            } else {
                assertBeanListEquals(message, expected, actualList);
            }
        } else if (actual instanceof Object[]) {
            assertEquals(message, expected, Arrays.asList((Object[]) actual));
        } else {
            if (actual instanceof Map) {
                assertMapEquals(message, expected, (Map) actual);
            } else {
                assertBeanEquals(message, expected, actual);
            }
        }
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param map
     *            実際の値
     */
    protected void assertMapEquals(String message, DataSet expected, Map map) {

        MapReader reader = new MapReader(map);
        assertEquals(message, expected, reader.read());
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param list
     *            実際の値
     */
    protected void assertMapListEquals(String message, DataSet expected,
            List list) {

        MapListReader reader = new MapListReader(list);
        assertEquals(message, expected, reader.read());
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param bean
     *            実際の値
     */
    protected void assertBeanEquals(String message, DataSet expected,
            Object bean) {

        BeanReader reader = new BeanReader(bean);
        assertEquals(message, expected, reader.read());
    }

    /**
     * 等しいことを表明します。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param list
     *            実際の値
     */
    protected void assertBeanListEquals(String message, DataSet expected,
            List list) {

        BeanListReader reader = new BeanListReader(list);
        assertEquals(message, expected, reader.read());
    }
}
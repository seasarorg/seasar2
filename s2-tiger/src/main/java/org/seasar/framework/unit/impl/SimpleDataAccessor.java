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
package org.seasar.framework.unit.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataReader;
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
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.unit.DataAccessor;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * Java EE のAPIに依存しない{@link DataAccessor}のシンプルな実装です。
 * 
 * @author taedium
 */
public class SimpleDataAccessor implements DataAccessor {

    /** テストコンテキスト */
    protected TestContext testContext;

    private DataSource dataSource;

    private Connection connection;

    private DatabaseMetaData dbMetaData;

    private SqlWriter sqlWriter;

    /**
     * テストコンテキストを設定します。
     * 
     * @param testContext
     *            テストコンテキスト
     */
    @Binding(bindingType = BindingType.MUST)
    public void setTestContext(final TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * データソースを設定します。
     * 
     * @param dataSource
     *            データソース
     */
    @Binding(bindingType = BindingType.SHOULD)
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * SQLライターを設定します。
     * 
     * @param sqlWriter
     *            SQLライター
     */
    @Binding(bindingType = BindingType.MAY)
    public void setSqlWriter(final SqlWriter sqlWriter) {
        this.sqlWriter = sqlWriter;
    }

    /**
     * データソースを取得します。
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
     * コネクションを取得します。
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
     * データベースに関するメタデータを取得します。
     * 
     * @return データベースに関するメタデータ
     */
    public DatabaseMetaData getDatabaseMetaData() {
        if (dbMetaData != null) {
            return dbMetaData;
        }
        dbMetaData = ConnectionUtil.getMetaData(getConnection());
        return dbMetaData;
    }

    /**
     * SQLライターを取得します。
     * 
     * @return SQLライター
     */
    protected SqlWriter getSqlWriter() {
        return sqlWriter != null ? sqlWriter : new SqlWriter(getDataSource());
    }

    public DataSet readXls(String path) {
        return readXls(path, true);
    }

    public DataSet readXls(String path, boolean trimString) {
        DataReader reader = new XlsReader(convertPath(path), trimString);
        return reader.read();
    }

    public void writeXls(String path, DataSet dataSet) {
        File dir = ResourceUtil.getBuildDir(getClass());
        File file = new File(dir, convertPath(path));
        DataWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
        writer.write(dataSet);
    }

    public void writeDb(DataSet dataSet) {
        DataWriter writer = getSqlWriter();
        writer.write(dataSet);
    }

    public DataSet readDb(DataSet dataSet) {
        SqlReader reader = new SqlReader(getDataSource());
        reader.addDataSet(dataSet);
        return reader.read();
    }

    public DataTable readDbByTable(String table) {
        return readDbByTable(table, null);
    }

    public DataTable readDbByTable(String table, String condition) {
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setTable(table, condition);
        return reader.read();
    }

    public DataTable readDbBySql(String sql, String tableName) {
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setSql(sql, tableName);
        return reader.read();
    }

    public void readXlsWriteDb(String path) {
        readXlsWriteDb(path, true);
    }

    public void readXlsWriteDb(String path, boolean trimString) {
        writeDb(readXls(path, trimString));
    }

    public void readXlsReplaceDb(String path) {
        readXlsReplaceDb(path, true);
    }

    public void readXlsReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readXls(path, trimString);
        deleteDb(dataSet);
        writeDb(dataSet);
    }

    public void readXlsAllReplaceDb(String path) {
        readXlsAllReplaceDb(path, true);
    }

    public void readXlsAllReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readXls(path, trimString);
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            deleteTable(dataSet.getTable(i).getTableName());
        }
        writeDb(dataSet);
    }

    public DataSet reload(DataSet dataSet) {
        return new SqlReloadReader(getDataSource(), dataSet).read();
    }

    public DataTable reload(DataTable table) {
        return new SqlReloadTableReader(getDataSource(), table).read();
    }

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

    public void deleteDb(DataSet dataSet) {
        SqlDeleteTableWriter writer = new SqlDeleteTableWriter(getDataSource());
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            writer.write(dataSet.getTable(i));
        }
    }

    public void deleteTable(String tableName) {
        UpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                "DELETE FROM " + tableName);
        handler.execute(null);
    }

    /**
     * パスを適切に変換して返します。
     * 
     * @param path
     *            パス
     * @return 指定されたパスのリソースが存在すればそのパス、存在しなければテストコンテキストに応じたパス
     */
    protected String convertPath(String path) {
        if (ResourceUtil.isExist(path)) {
            return path;
        }
        return testContext.getTestClassPackagePath() + "/" + path;
    }

}

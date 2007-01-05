/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

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
import org.seasar.framework.util.TransactionManagerUtil;

/**
 * @author taedium
 * 
 */
public class DataAccessorImpl implements DataAccessor {

    protected TestContext testContext;

    protected TransactionManager tm;

    private DataSource dataSource;

    protected EntityManager em;

    private Connection connection;

    private DatabaseMetaData dbMetaData;

    @Binding(bindingType = BindingType.MUST)
    public void setTestContext(final TestContext testContext) {
        this.testContext = testContext;
    }

    public void setTransactionManager(final TransactionManager tm) {
        this.tm = tm;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setEntityManager(final EntityManager em) {
        this.em = em;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new EmptyRuntimeException("dataSource");
        }
        return dataSource;
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        connection = DataSourceUtil.getConnection(getDataSource());
        return connection;
    }

    public DatabaseMetaData getDatabaseMetaData() {
        if (dbMetaData != null) {
            return dbMetaData;
        }
        dbMetaData = ConnectionUtil.getMetaData(getConnection());
        return dbMetaData;
    }

    public DataSet readXls(String path) {
        DataReader reader = new XlsReader(convertPath(path));
        return reader.read();
    }

    public void writeXls(String path, DataSet dataSet) {
        File dir = ResourceUtil.getBuildDir(getClass());
        File file = new File(dir, convertPath(path));
        DataWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
        writer.write(dataSet);
    }

    public void writeDb(DataSet dataSet) {
        flushIfNecessary();
        DataWriter writer = new SqlWriter(getDataSource());
        writer.write(dataSet);
    }

    public DataSet readDb(DataSet dataSet) {
        flushIfNecessary();
        SqlReader reader = new SqlReader(getDataSource());
        reader.addDataSet(dataSet);
        return reader.read();
    }

    public DataTable readDbByTable(String table) {
        flushIfNecessary();
        return readDbByTable(table, null);
    }

    public DataTable readDbByTable(String table, String condition) {
        flushIfNecessary();
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setTable(table, condition);
        return reader.read();
    }

    public DataTable readDbBySql(String sql, String tableName) {
        flushIfNecessary();
        SqlTableReader reader = new SqlTableReader(getDataSource());
        reader.setSql(sql, tableName);
        return reader.read();
    }

    public void readXlsWriteDb(String path) {
        flushIfNecessary();
        writeDb(readXls(path));
    }

    public void readXlsReplaceDb(String path) {
        flushIfNecessary();
        DataSet dataSet = readXls(path);
        deleteDb(dataSet);
        writeDb(dataSet);
    }

    public void readXlsAllReplaceDb(String path) {
        flushIfNecessary();
        DataSet dataSet = readXls(path);
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            deleteTable(dataSet.getTable(i).getTableName());
        }
        writeDb(dataSet);
    }

    public DataSet reload(DataSet dataSet) {
        flushIfNecessary();
        return new SqlReloadReader(getDataSource(), dataSet).read();
    }

    public DataTable reload(DataTable table) {
        flushIfNecessary();
        return new SqlReloadTableReader(getDataSource(), table).read();
    }

    public DataSet reloadOrReadDb(DataSet dataSet) {
        flushIfNecessary();
        DataSet newDataSet = new DataSetImpl();
        outer: for (int i = 0; i < dataSet.getTableSize(); i++) {
            DataTable table = dataSet.getTable(i);
            if (!table.hasMetaData()) {
                table.setupMetaData(getDatabaseMetaData());
            }
            for (int j = 0; i < table.getColumnSize(); j++) {
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
        flushIfNecessary();
        SqlDeleteTableWriter writer = new SqlDeleteTableWriter(getDataSource());
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            writer.write(dataSet.getTable(i));
        }
    }

    public void deleteTable(String tableName) {
        flushIfNecessary();
        UpdateHandler handler = new BasicUpdateHandler(getDataSource(),
                "DELETE FROM " + tableName);
        handler.execute(null);
    }

    protected String convertPath(String path) {
        if (ResourceUtil.isExist(path)) {
            return path;
        }
        return testContext.getTestClassPackagePath() + "/" + path;
    }

    protected void flushIfNecessary() {
        if (em != null && TransactionManagerUtil.isActive(tm)) {
            em.flush();
        }
    }

}

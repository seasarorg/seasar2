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
package org.seasar.extension.dataset.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.RowState;
import org.seasar.extension.dataset.TableWriter;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;

/**
 * SQL用の {@link TableWriter}です。
 * 
 * @author higa
 * 
 */
public class SqlTableWriter implements TableWriter {

    private DataSource dataSource;

    /**
     * {@link SqlTableWriter}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public SqlTableWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    public void write(DataTable table) {
        if (!table.hasMetaData()) {
            setupMetaData(table);
        }
        doWrite(table);
    }

    /**
     * データを書き込みます。
     * 
     * @param table
     *            テーブル
     */
    protected void doWrite(DataTable table) {
        for (int i = 0; i < table.getRowSize(); ++i) {
            DataRow row = table.getRow(i);
            RowState state = row.getState();
            state.update(dataSource, row);
        }
    }

    private void setupMetaData(DataTable table) {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            table.setupMetaData(ConnectionUtil.getMetaData(con));
        } finally {
            ConnectionUtil.close(con);
        }
    }
}
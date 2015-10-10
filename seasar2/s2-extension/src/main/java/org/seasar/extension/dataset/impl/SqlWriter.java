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

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.TableWriter;

/**
 * SQL用の {@link DataWriter}です。
 * 
 * @author higa
 * 
 */
public class SqlWriter implements DataWriter {

    private DataSource dataSource;

    /**
     * {@link SqlWriter}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public SqlWriter(DataSource dataSource) {
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

    /**
     * @see org.seasar.extension.dataset.DataWriter#write(org.seasar.extension.dataset.DataSet)
     */
    public void write(DataSet dataSet) {
        TableWriter writer = new SqlTableWriter(getDataSource());
        for (int i = 0; i < dataSet.getTableSize(); ++i) {
            writer.write(dataSet.getTable(i));
        }
    }

}

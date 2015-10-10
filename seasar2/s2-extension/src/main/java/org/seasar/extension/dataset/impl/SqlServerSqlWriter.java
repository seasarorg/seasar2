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
import org.seasar.extension.dataset.TableWriter;

/**
 * SqlServer用の {@link SqlWriter}です。
 * 
 * @author taedium
 * 
 */
public class SqlServerSqlWriter extends SqlWriter {

    /**
     * {@link SqlServerSqlWriter}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public SqlServerSqlWriter(final DataSource dataSource) {
        super(dataSource);
    }

    public void write(final DataSet dataSet) {
        final TableWriter writer = new SqlServerSqlTableWriter(getDataSource());
        for (int i = 0; i < dataSet.getTableSize(); ++i) {
            writer.write(dataSet.getTable(i));
        }
    }
}

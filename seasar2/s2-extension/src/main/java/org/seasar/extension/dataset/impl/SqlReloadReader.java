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
package org.seasar.extension.dataset.impl;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.TableReader;

/**
 * @author higa
 * 
 */
public class SqlReloadReader implements DataReader {

    private DataSource dataSource_;

    private DataSet dataSet_;

    public SqlReloadReader(DataSource dataSource, DataSet dataSet) {
        dataSource_ = dataSource;
        dataSet_ = dataSet;
    }

    public DataSource getDataSource() {
        return dataSource_;
    }

    public DataSet getDataSet() {
        return dataSet_;
    }

    /**
     * @see org.seasar.extension.dataset.DataReader#read()
     */
    public DataSet read() {
        DataSet newDataSet = new DataSetImpl();
        for (int i = 0; i < dataSet_.getTableSize(); ++i) {
            TableReader reader = new SqlReloadTableReader(dataSource_, dataSet_
                    .getTable(i));
            newDataSet.addTable(reader.read());
        }
        return newDataSet;
    }

}
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
package org.seasar.framework.unit;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * @author taedium
 * 
 */
public interface DataAccessor {

    DataSet readXls(String path);

    void writeXls(String path, DataSet dataSet);

    void writeDb(DataSet dataSet);

    DataSet readDb(DataSet dataSet);

    DataTable readDbByTable(String table);

    DataTable readDbByTable(String table, String condition);

    DataTable readDbBySql(String sql, String tableName);

    void readXlsWriteDb(String path);

    void readXlsReplaceDb(String path);

    void readXlsAllReplaceDb(String path);

    DataSet reload(DataSet dataSet);

    DataTable reload(DataTable table);

    DataSet reloadOrReadDb(DataSet dataSet);

    void deleteDb(DataSet dataSet);

    void deleteTable(String tableName);

}

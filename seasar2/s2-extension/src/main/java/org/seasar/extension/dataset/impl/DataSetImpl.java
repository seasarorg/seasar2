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

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableNotFoundRuntimeException;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * {@link DataSet}の実装です。
 * 
 * @author higa
 * 
 */
public class DataSetImpl implements DataSet {

    private ArrayMap tables = new CaseInsensitiveMap();

    /**
     * {@link DataSetImpl}を作成します。
     */
    public DataSetImpl() {
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#getTableSize()
     */
    public int getTableSize() {
        return tables.size();
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#getTableName(int)
     */
    public String getTableName(int index) {
        return getTable(index).getTableName();
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#getTable(int)
     */
    public DataTable getTable(int index) {
        return (DataTable) tables.get(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#hasTable(java.lang.String)
     */
    public boolean hasTable(String tableName) {
        return tables.containsKey(tableName);
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#getTable(java.lang.String)
     */
    public DataTable getTable(String tableName)
            throws TableNotFoundRuntimeException {

        DataTable table = (DataTable) tables.get(tableName);
        if (table == null) {
            throw new TableNotFoundRuntimeException(tableName);
        }
        return table;
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#addTable(java.lang.String)
     */
    public DataTable addTable(String tableName) {
        return addTable(new DataTableImpl(tableName));
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#addTable(org.seasar.extension.dataset.DataTable)
     */
    public DataTable addTable(DataTable table) {
        tables.put(table.getTableName(), table);
        return table;
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#removeTable(org.seasar.extension.dataset.DataTable)
     */
    public DataTable removeTable(DataTable table) {
        return removeTable(table.getTableName());
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#removeTable(int)
     */
    public DataTable removeTable(int index) {
        return (DataTable) tables.remove(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataSet#removeTable(java.lang.String)
     */
    public DataTable removeTable(String tableName) {
        DataTable table = (DataTable) tables.remove(tableName);
        if (table == null) {
            throw new TableNotFoundRuntimeException(tableName);
        }
        return table;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        for (int i = 0; i < getTableSize(); ++i) {
            buf.append(getTable(i));
            buf.append("\n");
        }
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataSet)) {
            return false;
        }
        DataSet other = (DataSet) o;
        if (getTableSize() != other.getTableSize()) {
            return false;
        }
        for (int i = 0; i < getTableSize(); ++i) {
            if (!getTable(i).equals(other.getTable(i))) {
                return false;
            }
        }
        return true;
    }
}

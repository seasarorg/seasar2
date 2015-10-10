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

import java.util.Iterator;
import java.util.Map;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.RowState;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.ColumnNotFoundRuntimeException;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * {@link DataRow}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class DataRowImpl implements DataRow {

    private DataTable table_;

    private ArrayMap values_ = new CaseInsensitiveMap();

    private RowState state_ = RowStates.UNCHANGED;

    /**
     * {@link DataRowImpl}を作成します。
     * 
     * @param table
     */
    public DataRowImpl(DataTable table) {
        table_ = table;
        initValues();
    }

    private void initValues() {
        for (int i = 0; i < table_.getColumnSize(); ++i) {
            values_.put(table_.getColumnName(i), null);
        }
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#getValue(int)
     */
    public Object getValue(int index) {
        return values_.get(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#getValue(java.lang.String)
     */
    public Object getValue(String columnName)
            throws ColumnNotFoundRuntimeException {

        DataColumn column = table_.getColumn(columnName);
        return values_.get(column.getColumnIndex());
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#setValue(java.lang.String,
     *      java.lang.Object)
     */
    public void setValue(String columnName, Object value)
            throws ColumnNotFoundRuntimeException {

        DataColumn column = table_.getColumn(columnName);
        values_.put(columnName, column.convert(value));
        modify();
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#setValue(int, java.lang.Object)
     */
    public void setValue(int index, Object value) {
        DataColumn column = table_.getColumn(index);
        values_.set(index, column.convert(value));
        modify();
    }

    private void modify() {
        if (state_.equals(RowStates.UNCHANGED)) {
            state_ = RowStates.MODIFIED;
        }
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#remove()
     */
    public void remove() {
        state_ = RowStates.REMOVED;
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#getTable()
     */
    public DataTable getTable() {
        return table_;
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#getState()
     */
    public RowState getState() {
        return state_;
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#setState(org.seasar.extension.dataset.RowState)
     */
    public void setState(RowState state) {
        state_ = state;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append("{");
        for (int i = 0; i < values_.size(); ++i) {
            buf.append(getValue(i));
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append('}');
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataRow)) {
            return false;
        }
        DataRow other = (DataRow) o;
        for (int i = 0; i < table_.getColumnSize(); ++i) {
            String columnName = table_.getColumnName(i);
            Object value = values_.get(i);
            Object otherValue = other.getValue(columnName);
            ColumnType ct = ColumnTypes.getColumnType(value);
            if (ct.equals(value, otherValue)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * @see org.seasar.extension.dataset.DataRow#copyFrom(java.lang.Object)
     */
    public void copyFrom(Object source) {
        if (source instanceof Map) {
            copyFromMap((Map) source);
        } else if (source instanceof DataRow) {
            copyFromRow((DataRow) source);
        } else {
            copyFromBean(source);
        }

    }

    private void copyFromMap(Map source) {
        for (Iterator i = source.keySet().iterator(); i.hasNext();) {
            String columnName = (String) i.next();
            if (table_.hasColumn(columnName)) {
                Object value = source.get(columnName);
                setValue(columnName, convertValue(value));
            }
        }
    }

    private void copyFromRow(DataRow source) {
        for (int i = 0; i < source.getTable().getColumnSize(); ++i) {
            String columnName = source.getTable().getColumnName(i);
            if (table_.hasColumn(columnName)) {
                Object value = source.getValue(i);
                setValue(columnName, convertValue(value));
            }
        }
    }

    private void copyFromBean(Object source) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(source.getClass());
        for (int i = 0; i < table_.getColumnSize(); ++i) {
            String columnName = table_.getColumnName(i);
            String propertyName = StringUtil.replace(columnName, "_", "");
            if (beanDesc.hasPropertyDesc(propertyName)) {
                PropertyDesc pd = beanDesc.getPropertyDesc(propertyName);
                Object value = pd.getValue(source);
                setValue(columnName, convertValue(value));
            }
        }
    }

    private Object convertValue(Object value) {
        if (value == null) {
            return null;
        }
        ColumnType columnType = ColumnTypes.getColumnType(value.getClass());
        return columnType.convert(value, null);
    }
}
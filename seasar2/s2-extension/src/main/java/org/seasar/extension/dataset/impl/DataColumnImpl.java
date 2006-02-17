/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;

/**
 * @author higa
 * 
 */
public class DataColumnImpl implements DataColumn {

    private String columnName;

    private ColumnType columnType;

    private int columnIndex;

    private boolean primaryKey = false;

    private boolean writable = true;

    private String formatPattern;

    public DataColumnImpl(String columnName, ColumnType columnType,
            int columnIndex) {

        setColumnName(columnName);
        setColumnType(columnType);
        setColumnIndex(columnIndex);
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#getColumnName()
     */
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#getColumnType()
     */
    public ColumnType getColumnType() {
        return columnType;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#setColumnType(org.seasar.extension.dataset.ColumnType)
     */
    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#getColumnIndex()
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#isPrimaryKey()
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#setPrimaryKey(boolean)
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#isWritable()
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#setWritable(boolean)
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#getFormatPattern()
     */
    public String getFormatPattern() {
        return formatPattern;
    }

    /**
     * @see org.seasar.extension.dataset.DataColumn#setFormatPattern(java.lang.String)
     */
    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public Object convert(Object value) {
        return columnType.convert(value, formatPattern);
    }
}
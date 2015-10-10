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

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;

/**
 * {@link DataColumn}の実装クラスです。
 * 
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

    /**
     * {@link DataColumnImpl}を作成します。
     * 
     * @param columnName
     *            カラム名
     * @param columnType
     *            カラムの型
     * @param columnIndex
     *            カラムの位置
     */
    public DataColumnImpl(String columnName, ColumnType columnType,
            int columnIndex) {

        setColumnName(columnName);
        setColumnType(columnType);
        setColumnIndex(columnIndex);
    }

    public String getColumnName() {
        return columnName;
    }

    /**
     * カラム名を設定します。
     * 
     * @param columnName
     *            カラム名
     */
    private void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * カラムの位置を設定します。
     * 
     * @param columnIndex
     *            カラムの位置
     */
    private void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public Object convert(Object value) {
        return columnType.convert(value, formatPattern);
    }
}
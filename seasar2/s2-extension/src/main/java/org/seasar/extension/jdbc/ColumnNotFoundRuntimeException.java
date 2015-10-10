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
package org.seasar.extension.jdbc;

import org.seasar.framework.exception.SRuntimeException;

/**
 * カラムが見つらない場合の例外です。
 * 
 * @author higa
 * 
 */
public class ColumnNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 0L;

    private String tableName;

    private String columnName;

    /**
     * {@link ColumnNotFoundRuntimeException}を作成します。
     * 
     * @param tableName
     *            テーブル名
     * @param columnName
     *            カラム名
     */
    public ColumnNotFoundRuntimeException(String tableName, String columnName) {
        super("ESSR0068", new Object[] { tableName, columnName });
        this.tableName = tableName;
        this.columnName = columnName;
    }

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    public String getColumnName() {
        return columnName;
    }
}
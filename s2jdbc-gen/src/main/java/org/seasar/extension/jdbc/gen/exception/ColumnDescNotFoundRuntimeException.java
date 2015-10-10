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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.framework.exception.SRuntimeException;

/**
 * {@link ColumnDesc}が見つからない場合にスローされる例外です。
 * 
 * @author taedium
 */
public class ColumnDescNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** カラム名 */
    protected String columnName;

    /** 完全なテーブル名 */
    protected String fullTableName;

    /**
     * インスタンスを構築します。
     * 
     * @param columnName
     *            カラム名
     * @param fullTableName
     *            完全なテーブル名
     */
    public ColumnDescNotFoundRuntimeException(String columnName,
            String fullTableName) {
        super("ES2JDBCGen0013", new Object[] { columnName, fullTableName });
        this.columnName = columnName;
        this.fullTableName = fullTableName;
    }

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 完全なテーブル名を返します。
     * 
     * @return 完全なテーブル名
     */
    public String getFullTableName() {
        return fullTableName;
    }

}

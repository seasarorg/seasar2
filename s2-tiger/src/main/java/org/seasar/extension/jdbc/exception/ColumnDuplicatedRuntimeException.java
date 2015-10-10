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
package org.seasar.extension.jdbc.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * カラム名が重複していた場合の例外です。
 * 
 * @author higa
 * 
 */
public class ColumnDuplicatedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String propertyName;

    private String propertyName2;

    private String columnName;

    /**
     * {@link ColumnDuplicatedRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param propertyName2
     *            プロパティ名2
     * @param columnName
     *            カラム名
     */
    public ColumnDuplicatedRuntimeException(String entityName,
            String propertyName, String propertyName2, String columnName) {
        super("ESSR0724", new Object[] { entityName, propertyName,
                propertyName2, columnName });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.propertyName2 = propertyName2;
        this.columnName = columnName;
    }

    /**
     * エンティティ名を返します。
     * 
     * @return エンティティ名
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * プロパティ名2を返します。
     * 
     * @return プロパティ名2
     */
    public String getPropertyName2() {
        return propertyName2;
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
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
 * カラムが見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class EntityColumnNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String columnName;

    /**
     * {@link EntityColumnNotFoundRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param columnName
     *            カラム名
     */
    public EntityColumnNotFoundRuntimeException(String entityName, String columnName) {
        super("ESSR0725", new Object[] { entityName, columnName });
        this.entityName = entityName;
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
     * カラム名を返します。
     * 
     * @return カラム名
     */
    public String getColumnName() {
        return columnName;
    }
}
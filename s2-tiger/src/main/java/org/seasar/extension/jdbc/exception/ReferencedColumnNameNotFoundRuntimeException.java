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
 * 結合カラムの参照されるカラム名が見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class ReferencedColumnNameNotFoundRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String propertyName;

    private String inverseEntityName;

    private String primaryKey;

    /**
     * {@link ReferencedColumnNameNotFoundRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param inverseEntityName
     *            逆側のエンティティ名
     * @param primaryKey
     *            主キー
     */
    public ReferencedColumnNameNotFoundRuntimeException(String entityName,
            String propertyName, String inverseEntityName, String primaryKey) {
        super("ESSR0731", new Object[] { entityName, propertyName,
                inverseEntityName, primaryKey });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.inverseEntityName = inverseEntityName;
        this.primaryKey = primaryKey;
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
     * 逆側のエンティティ名を返します。
     * 
     * @return 逆側のエンティティ名
     */
    public String getInverseEntityName() {
        return inverseEntityName;
    }

    /**
     * 主キーを返します。
     * 
     * @return 主キー
     */
    public String getPrimaryKey() {
        return primaryKey;
    }
}
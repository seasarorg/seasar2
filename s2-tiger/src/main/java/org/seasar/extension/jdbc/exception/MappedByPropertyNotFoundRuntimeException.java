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
 * MappedByで指定されたプロパティが見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class MappedByPropertyNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String propertyName;

    private String mappedBy;

    private Class<?> inverseRelationshipClass;

    /**
     * {@link MappedByPropertyNotFoundRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param mappedBy
     *            MappedBy名
     * @param inverseRelationshipClass
     *            逆側の関連クラス
     * 
     */
    public MappedByPropertyNotFoundRuntimeException(String entityName,
            String propertyName, String mappedBy,
            Class<?> inverseRelationshipClass) {
        super("ESSR0719", new Object[] { entityName, propertyName, mappedBy,
                inverseRelationshipClass.getName() });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.mappedBy = mappedBy;
        this.inverseRelationshipClass = inverseRelationshipClass;
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
     * MappedBy名を返します。
     * 
     * @return MappedBy名
     */
    public String getMappedBy() {
        return mappedBy;
    }

    /**
     * 逆側の関連クラスを返します。
     * 
     * @return 逆側の関連クラス
     */
    public Class<?> getInverseRelationshipClass() {
        return inverseRelationshipClass;
    }
}
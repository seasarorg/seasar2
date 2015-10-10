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
 * MappedByで指定されたプロパティが元のエンティティと異なる場合の例外です。
 * 
 * @author higa
 * 
 */
public class MappedByNotIdenticalRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String propertyName;

    private String mappedBy;

    private Class<?> entityClass;

    private Class<?> inverseRelationshipClass;

    private String inversePropertyName;

    private Class<?> inversePropertyClass;

    /**
     * {@link MappedByNotIdenticalRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param mappedBy
     *            MappedBy名
     * @param entityClass
     *            エンティティクラス
     * @param inverseRelationshipClass
     *            逆側の関連クラス
     * @param inversePropertyName
     *            逆側のプロパティ名
     * @param inversePropertyClass
     *            逆側のプロパティクラス
     * 
     */
    public MappedByNotIdenticalRuntimeException(String entityName,
            String propertyName, String mappedBy, Class<?> entityClass,
            Class<?> inverseRelationshipClass, String inversePropertyName,
            Class<?> inversePropertyClass) {
        super("ESSR0713", new Object[] { entityName, propertyName, mappedBy,
                entityClass.getName(), inverseRelationshipClass.getName(),
                inversePropertyName, inversePropertyClass.getName() });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.mappedBy = mappedBy;
        this.entityClass = entityClass;
        this.inverseRelationshipClass = inverseRelationshipClass;
        this.inversePropertyName = inversePropertyName;
        this.inversePropertyClass = inversePropertyClass;
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
     * エンティティクラスを返します。
     * 
     * @return エンティティクラス
     */
    public Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * 逆側の関連クラスを返します。
     * 
     * @return 逆側の関連クラス
     */
    public Class<?> getInverseRelationshipClass() {
        return inverseRelationshipClass;
    }

    /**
     * 逆側のプロパティ名を返します。
     * 
     * @return 逆側のプロパティ名
     */
    public String getInversePropertyName() {
        return inversePropertyName;
    }

    /**
     * 逆側のプロパティクラスを返します。
     * 
     * @return 逆側のプロパティクラス
     */
    public Class<?> getInversePropertyClass() {
        return inversePropertyClass;
    }
}
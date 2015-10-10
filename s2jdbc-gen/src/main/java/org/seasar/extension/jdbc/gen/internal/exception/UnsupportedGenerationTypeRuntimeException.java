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
package org.seasar.extension.jdbc.gen.internal.exception;

import javax.persistence.GenerationType;

import org.seasar.framework.exception.SRuntimeException;

/**
 * サポートされていない{@link GenerationType}が指定された場合にスローされる例外です。
 * 
 * @author taedium
 */
public class UnsupportedGenerationTypeRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** 識別子を生成する方法を示す列挙型 */
    protected GenerationType generationType;

    /** エンティティ名 */
    protected String entityName;

    /** プロパティ名 */
    protected String propertyName;

    /**
     * インスタンスを構築します。
     * 
     * @param generationType
     *            識別子を生成する方法を示す列挙型
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     */
    public UnsupportedGenerationTypeRuntimeException(
            GenerationType generationType, String entityName,
            String propertyName) {
        super("ES2JDBCGen0020", new Object[] { generationType.name(),
                entityName, propertyName });
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    /**
     * 識別子を生成する方法を示す列挙型を返します。
     * 
     * @return 識別子を生成する方法を示す列挙型
     */
    public GenerationType getGenerationType() {
        return generationType;
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

}

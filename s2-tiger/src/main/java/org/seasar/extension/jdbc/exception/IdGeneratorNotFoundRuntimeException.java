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

import javax.persistence.GeneratedValue;

import org.seasar.framework.exception.SRuntimeException;

/**
 * {@link GeneratedValue#generator()}で指定されたIDジェネレータが見つからなかった場合の例外です。
 * 
 * @author koichik
 */
public class IdGeneratorNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** エンティティ名 */
    protected String entityName;

    /** 識別子のプロパティ名 */
    protected String propertyName;

    /** ジェネレータ名 */
    protected String generatorName;

    /**
     * {@link IdGeneratorNotFoundRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param generatorName
     *            ジェネレータ名
     */
    public IdGeneratorNotFoundRuntimeException(final String entityName,
            final String propertyName, final String generatorName) {
        super("ESSR0737", new Object[] { entityName, propertyName,
                generatorName });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.generatorName = generatorName;
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
     * ジェネレータ名を返します。
     * 
     * @return ジェネレータ名
     */
    public String getGeneratorName() {
        return generatorName;
    }

}

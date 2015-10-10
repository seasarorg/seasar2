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

import javax.persistence.FetchType;

import org.seasar.framework.exception.SRuntimeException;

/**
 * フェッチタイプに{@link FetchType#LAZY}が指定された場合の例外です。
 * 
 * @author koichik
 */
public class LazyFetchSpecifiedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** エンティティ名 */
    protected String entityName;

    /** 識別子のプロパティ名 */
    protected String propertyName;

    /**
     * インスタンスを構築します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     */
    public LazyFetchSpecifiedRuntimeException(final String entityName,
            final String propertyName) {
        super("ESSR0764", new Object[] { entityName, propertyName });
        this.entityName = entityName;
        this.propertyName = propertyName;
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

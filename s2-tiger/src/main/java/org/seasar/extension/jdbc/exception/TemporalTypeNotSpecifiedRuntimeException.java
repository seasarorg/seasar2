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

import java.util.Calendar;

import javax.persistence.TemporalType;

import org.seasar.framework.exception.SRuntimeException;

/**
 * {@link Date} や {@link Calendar} 型のプロパティに {@link TemporalType}
 * が設定されていない場合にスローされる例外です。
 * 
 * @author taedium
 */
public class TemporalTypeNotSpecifiedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String propertyName;

    /**
     * インスタンスを作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     */
    public TemporalTypeNotSpecifiedRuntimeException(String entityName,
            String propertyName) {
        super("ESSR0753", new Object[] { entityName, propertyName });
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

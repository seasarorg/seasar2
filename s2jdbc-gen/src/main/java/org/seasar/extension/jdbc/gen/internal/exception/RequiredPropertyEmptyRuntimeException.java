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

import org.seasar.framework.exception.SRuntimeException;

/**
 * 必須のプロパティが空の場合にスローされる例外です。
 * 
 * @author taedium
 */
public class RequiredPropertyEmptyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** プロパティ名 */
    protected String propertyName;

    /**
     * インスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     */
    public RequiredPropertyEmptyRuntimeException(String propertyName) {
        super("ES2JDBCGen0004", new Object[] { propertyName });
        this.propertyName = propertyName;
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

/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 * パラメータの型が不正な場合の例外です。
 * 
 * @author taedium
 * 
 */
public class IllegalParamTypeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private Class<?> expectedClass;

    private Class<?> actualClass;

    /**
     * {@link IllegalParamTypeRuntimeException}を作成します。
     * 
     * @param expectedType
     *            期待されるパラメータの型
     * @param actualType
     *            実際のパラメータの型
     */
    public IllegalParamTypeRuntimeException(Class<?> expectedType,
            Class<?> actualType) {
        super("ESSR0734", new Object[] {
                expectedType == null ? null : expectedType.getName(),
                actualType == null ? null : actualType.getName() });
        this.expectedClass = expectedType;
        this.actualClass = actualType;
    }

    /**
     * 期待されるパラメータの型を返します。
     * 
     * @return 期待されるパラメータの型
     */
    public Class<?> getExpectedType() {
        return expectedClass;
    }

    /**
     * 実際のパラメータの型を返します。
     * 
     * @return 実際のパラメータの型
     */
    public Class<?> getActualType() {
        return actualClass;
    }

}

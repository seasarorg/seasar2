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
 * ある型を期待する型でキャストできない場合にスローされる例外です。
 * 
 * @author taedium
 */
public class ClassUnmatchRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** 期待するクラス */
    protected Class<?> expectedClass;

    /** 実際のクラス */
    protected Class<?> actualClass;

    /**
     * インスタンスを構築します。
     * 
     * @param expectedClass
     *            期待するクラス
     * @param actualClass
     *            実際のクラス
     */
    public ClassUnmatchRuntimeException(Class<?> expectedClass,
            Class<?> actualClass) {
        super("ES2JDBCGen0023", new Object[] { actualClass.getName(),
                expectedClass.getName(), });
        this.expectedClass = expectedClass;
        this.actualClass = actualClass;
    }

    /**
     * 期待するクラスを返します。
     * 
     * @return 期待するクラス
     */
    public Class<?> getExpectedClass() {
        return expectedClass;
    }

    /**
     * 実際のクラスを返します。
     * 
     * @return 実際のクラス
     */
    public Class<?> getActualClass() {
        return actualClass;
    }

}

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
 * ダンプファイルの値が不正である場合にスローされます。
 * 
 * @author taedium
 */
public class IllegalDumpValueRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** 値 */
    protected String value;

    /**
     * インスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public IllegalDumpValueRuntimeException(String value) {
        super("ES2JDBCGen0028", new Object[] { value });
        this.value = value;
    }

    /**
     * 値を返します。
     * 
     * @return 値
     */
    public String getValue() {
        return value;
    }

}

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
 * バージョン番号の値が不正な場合にスローされる例外です。
 * 
 * @author taedium
 */
public class IllegalVersionRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** バージョン */
    protected String version;

    /** 不正な値 */
    protected String illegalValue;

    /**
     * インスタンスを構築します。
     * 
     * @param version
     *            バージョン
     * @param illegalValue
     *            不正な値
     */
    public IllegalVersionRuntimeException(String version, String illegalValue) {
        super("ES2JDBCGen0011", new Object[] { illegalValue });
        this.version = version;
        this.illegalValue = illegalValue;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param version
     *            バージョン
     * @param illegalValue
     *            不正な値
     */
    public IllegalVersionRuntimeException(String version, long illegalValue) {
        super("ES2JDBCGen0031", new Object[] { version, illegalValue });
        this.version = version;
        this.illegalValue = String.valueOf(illegalValue);
    }

    /**
     * バージョンを返します。
     * 
     * @return バージョン
     */
    public String getVersion() {
        return version;
    }

    /**
     * 不正な値を返します。
     * 
     * @return 不正な値
     */
    public String getIllegalValue() {
        return illegalValue;
    }

}

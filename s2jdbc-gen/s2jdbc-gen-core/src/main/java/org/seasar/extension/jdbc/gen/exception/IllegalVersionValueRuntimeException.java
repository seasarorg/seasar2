/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * バージョン番号の値が不正な場合にスローされる例外です。
 * 
 * @author taedium
 */
public class IllegalVersionValueRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** バージョン番号を保持するファイル名 */
    protected String versionFileName;

    /** 不正な値 */
    protected String illegalValue;

    /**
     * インスタンスを構築します。
     * 
     * @param versionFileName
     *            バージョン番号を保持するファイル名
     * @param illegalValue
     *            不正な値
     */
    public IllegalVersionValueRuntimeException(String versionFileName,
            String illegalValue) {
        super("ES2JDBCGen0006", new Object[] { versionFileName, illegalValue });
        this.versionFileName = versionFileName;
        this.illegalValue = illegalValue;
    }

    /**
     * バージョン番号を保持するファイル名を返します。
     * 
     * @return バージョン番号を保持するファイル名
     */
    public String getVersionFileName() {
        return versionFileName;
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

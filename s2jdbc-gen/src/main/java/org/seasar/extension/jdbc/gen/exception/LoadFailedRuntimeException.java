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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * データのロードに失敗した場合にスローされる例外です。
 * 
 * @author taedium
 */
public class LoadFailedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** ダンプファイルのパス */
    protected String dumpFilePath;

    /** 行番号 */
    protected int lineNumber;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param dumpFilePath
     *            ダンプファイルのパス
     * @param lineNumber
     *            行番号
     */
    public LoadFailedRuntimeException(Throwable cause, String dumpFilePath,
            int lineNumber) {
        super("ES2JDBCGen0015", new Object[] { dumpFilePath,
                String.valueOf(lineNumber), cause }, cause);
        this.dumpFilePath = dumpFilePath;
        this.lineNumber = lineNumber;
    }

    /**
     * ダンプファイルのパスを返します。
     * 
     * @return ダンプファイルのパス
     */
    public String getDumpFilePath() {
        return dumpFilePath;
    }

    /**
     * 行番号を返します。
     * 
     * @return 行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

}

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
 * ヘッダー行の列数とデータ行の列数が一致していないダンプファイルをロードした場合にスローされます。
 * 
 * @author taedium
 */
public class IllegalDumpColumnSizeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** ダンプファイルのパス */
    protected String dumpFilePath;

    /** 行番号 */
    protected int lineNumber;

    /** データ行の列数 */
    protected int lineColumnSize;

    /** ヘッダー行の列数 */
    protected int headerColumnSize;

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFilePath
     *            ダンプファイルのパス
     * @param lineNumber
     *            行番号
     * @param lineColumnSize
     *            データ行の列数
     * @param headerColumnSize
     *            ヘッダー行の列数
     */
    public IllegalDumpColumnSizeRuntimeException(String dumpFilePath,
            int lineNumber, int lineColumnSize, int headerColumnSize) {
        super("ES2JDBCGen0012", new Object[] { dumpFilePath, lineNumber,
                lineColumnSize, headerColumnSize });
        this.dumpFilePath = dumpFilePath;
        this.lineNumber = lineNumber;
        this.lineColumnSize = lineColumnSize;
        this.headerColumnSize = headerColumnSize;
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

    /**
     * データ行の列数を返します。
     * 
     * @return データ行の列数
     */
    public int getLineColumnSize() {
        return lineColumnSize;
    }

    /**
     * ヘッダー行の列数を返します。
     * 
     * @return ヘッダー行の列数
     */
    public int getHeaderColumnSize() {
        return headerColumnSize;
    }

}

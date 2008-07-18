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

import java.sql.SQLException;

import org.seasar.framework.exception.SRuntimeException;

/**
 * SQLの実行に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 */
public class SqlFailedException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** ファイルのパス */
    protected String filePath;

    /** SQL */
    protected String sql;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param filePath
     *            ファイルのパス
     * @param sql
     *            SQL
     */
    public SqlFailedException(SQLException cause, String filePath, String sql) {
        super("ES2JDBCGen0003", new Object[] { filePath, sql, cause }, cause);
        this.filePath = filePath;
        this.sql = sql;
    }

    /**
     * ファイルのパスを返します。
     * 
     * @return ファイルのパス
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

}

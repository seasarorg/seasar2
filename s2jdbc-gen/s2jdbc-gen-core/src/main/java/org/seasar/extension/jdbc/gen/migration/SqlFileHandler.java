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
package org.seasar.extension.jdbc.gen.migration;

import java.io.File;

import org.seasar.extension.jdbc.gen.MigrationFileHandler;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlFileExecutor;

/**
 * SQLファイルを処理します。
 * 
 * @author taedium
 */
public class SqlFileHandler implements MigrationFileHandler {

    /** SQLファイル */
    protected File sqlFile;

    /** SQLファイルの実行者 */
    protected SqlFileExecutor sqlFileExecutor;

    /**
     * インスタンスを構築します。
     * 
     * @param sqlFile
     *            SQLファイル
     * @param sqlFileExecutor
     *            SQLファイルの実行者
     */
    public SqlFileHandler(File sqlFile, SqlFileExecutor sqlFileExecutor) {
        if (sqlFile == null) {
            throw new NullPointerException("sqlFile");
        }
        if (sqlFileExecutor == null) {
            throw new NullPointerException("sqlFileExecutor");
        }
        this.sqlFile = sqlFile;
        this.sqlFileExecutor = sqlFileExecutor;
    }

    public void handle(SqlExecutionContext sqlExecutionContext) {
        sqlFileExecutor.execute(sqlExecutionContext, sqlFile);
    }
}

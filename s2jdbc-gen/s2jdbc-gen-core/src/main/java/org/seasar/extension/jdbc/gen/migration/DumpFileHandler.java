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

import org.seasar.extension.jdbc.gen.DatabaseDesc;
import org.seasar.extension.jdbc.gen.Loader;
import org.seasar.extension.jdbc.gen.MigrationFileHandler;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;

/**
 * ダンプファイルを処理します。
 * 
 * @author taedium
 */
public class DumpFileHandler implements MigrationFileHandler {

    /** データベース記述 */
    protected DatabaseDesc databaseDesc;

    /** ダンプファイル */
    protected File dumpFile;

    /** ローダ */
    protected Loader loader;

    /**
     * インスタンスを構築します。
     * 
     * @param databaseDesc
     *            データベース記述
     * @param dumpFile
     *            ダンプファイル
     * @param loader
     *            ローダ
     */
    public DumpFileHandler(DatabaseDesc databaseDesc, File dumpFile,
            Loader loader) {
        if (databaseDesc == null) {
            throw new NullPointerException("databaseDesc");
        }
        if (dumpFile == null) {
            throw new NullPointerException("dumpFile");
        }
        if (loader == null) {
            throw new NullPointerException("loader");
        }
        this.databaseDesc = databaseDesc;
        this.dumpFile = dumpFile;
        this.loader = loader;
    }

    public void handle(SqlExecutionContext sqlExecutionContext) {
        loader.load(sqlExecutionContext, databaseDesc, dumpFile);
    }

}

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
package org.seasar.extension.jdbc.gen.data;

import java.io.File;

import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;

/**
 * データベースのデータをダンプするインタフェースです。
 * 
 * @author taedium
 */
public interface Dumper {

    /**
     * ダンプします。
     * 
     * @param sqlExecutionContext
     *            SQL実行コンテキスト
     * @param databaseDesc
     *            データベース記述
     * @param dumpDir
     *            ダンプ先のディレクトリ
     */
    void dump(SqlExecutionContext sqlExecutionContext,
            DatabaseDesc databaseDesc, File dumpDir);
}

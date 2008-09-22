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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.internal.util.EnvAwareFileComparator;
import org.seasar.extension.jdbc.gen.internal.util.EnvAwareFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.version.DdlVersionBaseDirectory;
import org.seasar.extension.jdbc.gen.version.Migrater;
import org.seasar.extension.jdbc.gen.version.SchemaInfoTable;
import org.seasar.framework.log.Logger;

/**
 * {@link Migrater}の実装クラスです。
 * 
 * @author taedium
 */
public class MigraterImpl implements Migrater {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(MigraterImpl.class);

    /** SQLのひとまとまりの実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /** スキーマのバージョン */
    protected SchemaInfoTable schemaInfoTable;

    /** DDLのバージョンを管理するディレクトリ */
    protected DdlVersionBaseDirectory ddlVersionBaseDirectory;

    /** 環境名 */
    protected String env;

    /** バージョン */
    protected String version;

    /**
     * インスタンスを構築します。
     * 
     * @param sqlUnitExecutor
     *            SQLのひとまとまりの実行者
     * @param schemaInfoTable
     *            スキーマのバージョン
     * @param ddlVersionBaseDirectory
     *            DDLをバージョン管理するディレクトリ
     * @param version
     *            バージョン
     * @param env
     *            環境名
     */
    public MigraterImpl(SqlUnitExecutor sqlUnitExecutor,
            SchemaInfoTable schemaInfoTable,
            DdlVersionBaseDirectory ddlVersionBaseDirectory, String version, String env) {
        if (sqlUnitExecutor == null) {
            throw new NullPointerException("sqlUnitExecutor");
        }
        if (schemaInfoTable == null) {
            throw new NullPointerException("schemaVersion");
        }
        if (ddlVersionBaseDirectory == null) {
            throw new NullPointerException("versionDirectories");
        }
        if (version == null) {
            throw new NullPointerException("version");
        }
        if (env == null) {
            throw new NullPointerException("env");
        }
        this.sqlUnitExecutor = sqlUnitExecutor;
        this.schemaInfoTable = schemaInfoTable;
        this.ddlVersionBaseDirectory = ddlVersionBaseDirectory;
        this.version = version;
        this.env = env;
    }

    public void migrate(Callback callback) {
        int from = schemaInfoTable.getVersionNo();
        int to = ddlVersionBaseDirectory.getDdlInfoFile().getVersionNo(version);

        logger.log("IS2JDBCGen0005", new Object[] { from, to });
        migrateInternal(callback, from, to);
        logger.log("IS2JDBCGen0006", new Object[] { from, to });
    }

    /**
     * マイグレーション処理を実行します。
     * 
     * @param callback
     *            コールバック
     * @param from
     *            マイグレーション元のバージョン番号
     * @param to
     *            マイグレーション先のバージョン番号
     */
    protected void migrateInternal(final Callback callback, int from, int to) {
        File dropVersionDir = ddlVersionBaseDirectory.getVersionDir(from);
        File dropDir = ddlVersionBaseDirectory.getDropDir(dropVersionDir);
        final List<File> dropFileList = getFileList(dropDir);

        File createVersionDir = ddlVersionBaseDirectory.getVersionDir(to);
        File createDir = ddlVersionBaseDirectory.getCreateDir(createVersionDir);
        final List<File> createFileList = getFileList(createDir);

        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                for (File file : dropFileList) {
                    callback.drop(context, file);
                }
                for (File file : createFileList) {
                    callback.create(context, file);
                }
            }
        });
    }

    /**
     * ディレクトリに含まれるファイルを返します。
     * 
     * @param dir
     *            ディレクトリ
     * @return ファイルのリスト
     */
    protected List<File> getFileList(File dir) {
        final List<File> fileList = new ArrayList<File>();
        FileUtil.traverseDirectory(dir, new EnvAwareFilenameFilter(env),
                new EnvAwareFileComparator(env), new FileUtil.FileHandler() {

                    public void handle(File file) {
                        fileList.add(file);
                    }
                });
        return fileList;
    }

}

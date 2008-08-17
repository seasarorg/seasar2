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
package org.seasar.extension.jdbc.gen.version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.Migrater;
import org.seasar.extension.jdbc.gen.SchemaInfoTable;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.util.EnvAwareFileComparator;
import org.seasar.extension.jdbc.gen.util.ExcludeFilenameFilter;
import org.seasar.extension.jdbc.gen.util.FileUtil;
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
    protected DdlVersionDirectory ddlVersionDirectory;

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
     * @param ddlVersionDirectory
     *            DDLをバージョン管理するディレクトリ
     * @param version
     *            バージョン
     * @param env
     *            環境名
     */
    public MigraterImpl(SqlUnitExecutor sqlUnitExecutor,
            SchemaInfoTable schemaInfoTable,
            DdlVersionDirectory ddlVersionDirectory, String version, String env) {
        if (sqlUnitExecutor == null) {
            throw new NullPointerException("sqlUnitExecutor");
        }
        if (schemaInfoTable == null) {
            throw new NullPointerException("schemaVersion");
        }
        if (ddlVersionDirectory == null) {
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
        this.ddlVersionDirectory = ddlVersionDirectory;
        this.version = version;
        this.env = env;
    }

    public void migrate(Callback callback) {
        int from = schemaInfoTable.getVersionNo();
        int to = ddlVersionDirectory.getDdlInfoFile().getVersionNo(version);

        logger.log("IS2JDBCGen0005", new Object[] { from, to });
        drop(callback, from);
        create(callback, to);
        logger.log("IS2JDBCGen0006", new Object[] { from, to });
    }

    /**
     * drop処理を実行します。
     * 
     * @param callback
     *            コールバック
     * @param versionNo
     *            バージョン番号
     */
    protected void drop(final Callback callback, int versionNo) {
        File versionDir = ddlVersionDirectory.getVersionDir(versionNo);
        File dropDir = ddlVersionDirectory.getDropDir(versionDir);
        final List<File> fileList = getFileList(dropDir);

        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                for (File file : fileList) {
                    callback.drop(context, file);
                }
            }
        });
    }

    /**
     * create処理を実行します。
     * 
     * @param callback
     *            コールバック
     * @param versionNo
     *            バージョン番号
     */
    protected void create(final Callback callback, int versionNo) {
        File versionDir = ddlVersionDirectory.getVersionDir(versionNo);
        File createDir = ddlVersionDirectory.getCreateDir(versionDir);
        final List<File> fileList = getFileList(createDir);

        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                for (File file : fileList) {
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
        FileUtil.traverseDirectory(dir, new ExcludeFilenameFilter(),
                new EnvAwareFileComparator(env), new FileUtil.FileHandler() {

                    public void handle(File file) {
                        fileList.add(file);
                    }
                });
        return fileList;
    }

}

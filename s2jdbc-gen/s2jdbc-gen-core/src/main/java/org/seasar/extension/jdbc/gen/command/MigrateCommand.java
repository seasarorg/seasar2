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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.DdlVersion;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SchemaVersion;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlExecutor;
import org.seasar.extension.jdbc.gen.SqlScriptReader;
import org.seasar.extension.jdbc.gen.SqlScriptTokenizer;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContextImpl;
import org.seasar.extension.jdbc.gen.sql.SqlExecutorImpl;
import org.seasar.extension.jdbc.gen.sql.SqlScriptReaderImpl;
import org.seasar.extension.jdbc.gen.sql.SqlScriptTokenizerImpl;
import org.seasar.extension.jdbc.gen.util.ExclusionFilenameFilter;
import org.seasar.extension.jdbc.gen.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.gen.version.DdlVersionImpl;
import org.seasar.extension.jdbc.gen.version.SchemaVersionImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringConversionUtil;

/**
 * @author taedium
 * 
 */
public class MigrateCommand extends AbstractCommand {

    /** ロガー */
    protected Logger logger = Logger.getLogger(MigrateCommand.class);

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter = null;

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** 環境名 */
    protected String env = "ut";

    /** エラー発生時に処理を中止する場合{@code true} */
    protected boolean haltOnError = false;

    /** スキーマ情報を格納するテーブル名 */
    protected String schemaInfoFullTableName = "SCHEMA_INFO";

    /** スキーマのバージョン番号を格納するカラム名 */
    protected String schemaInfoColumnName = "VERSION";

    /** テーブルを作成するDDLファイル名 */
    protected String createTableDdlFileName = "010-create-table.sql";

    /** DDLファイルのエンコーディング */
    protected String ddlFileEncoding = "UTF-8";

    /** マイグレーションのルートとなるディレクトリ */
    protected File migrationRootDir = new File("db");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** DDLのバージョンファイル名 */
    protected String ddlVersionFileName = "ddl-version.txt";

    /** マイグレーション元となるバージョン番号 */
    protected Integer from = null;

    /** マイグレーション先となるバージョン番号 */
    protected Integer to = null;

    protected String createDirName = "create";

    protected String dropDirName = "drop";

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** SQLスクリプトのリーダ */
    protected SqlScriptReader sqlScriptReader;

    /** SQLスクリプトのトークナイザ */
    protected SqlScriptTokenizer sqlScriptTokenizer;

    /** スキーマのバージョン */
    protected SchemaVersion schemaVersion;

    /** DDLのバージョン */
    protected DdlVersion ddlVersion;

    /**
     * @return Returns the statementDelimiter.
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * @param statementDelimiter
     *            The statementDelimiter to set.
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * @return Returns the blockDelimiter.
     */
    public String getBlockDelimiter() {
        return blockDelimiter;
    }

    /**
     * @param blockDelimiter
     *            The blockDelimiter to set.
     */
    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    /**
     * @return Returns the configPath.
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * @param configPath
     *            The configPath to set.
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @return Returns the jdbcManagerName.
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * @param jdbcManagerName
     *            The jdbcManagerName to set.
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * @return Returns the env.
     */
    public String getEnv() {
        return env;
    }

    /**
     * @param env
     *            The env to set.
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * @return Returns the haltOnError.
     */
    public boolean isHaltOnError() {
        return haltOnError;
    }

    /**
     * @param haltOnError
     *            The haltOnError to set.
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * @return Returns the schemaInfoFullTableName.
     */
    public String getSchemaInfoFullTableName() {
        return schemaInfoFullTableName;
    }

    /**
     * @param schemaInfoFullTableName
     *            The schemaInfoFullTableName to set.
     */
    public void setSchemaInfoFullTableName(String schemaInfoFullTableName) {
        this.schemaInfoFullTableName = schemaInfoFullTableName;
    }

    /**
     * @return Returns the schemaInfoColumnName.
     */
    public String getSchemaInfoColumnName() {
        return schemaInfoColumnName;
    }

    /**
     * @param schemaInfoColumnName
     *            The schemaInfoColumnName to set.
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        this.schemaInfoColumnName = schemaInfoColumnName;
    }

    /**
     * @return Returns the createTableDdlFileName.
     */
    public String getCreateTableDdlFileName() {
        return createTableDdlFileName;
    }

    /**
     * @param createTableDdlFileName
     *            The createTableDdlFileName to set.
     */
    public void setCreateTableDdlFileName(String createTableDdlFileName) {
        this.createTableDdlFileName = createTableDdlFileName;
    }

    /**
     * @return Returns the ddlFileEncoding.
     */
    public String getDdlFileEncoding() {
        return ddlFileEncoding;
    }

    /**
     * @param ddlFileEncoding
     *            The ddlFileEncoding to set.
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        this.ddlFileEncoding = ddlFileEncoding;
    }

    /**
     * @return Returns the migrationRootDir.
     */
    public File getMigrationRootDir() {
        return migrationRootDir;
    }

    /**
     * @param migrationRootDir
     *            The migrationRootDir to set.
     */
    public void setMigrationRootDir(File migrationRootDir) {
        this.migrationRootDir = migrationRootDir;
    }

    /**
     * @return Returns the versionNoPattern.
     */
    public String getVersionNoPattern() {
        return versionNoPattern;
    }

    /**
     * @param versionNoPattern
     *            The versionNoPattern to set.
     */
    public void setVersionNoPattern(String versionNoPattern) {
        this.versionNoPattern = versionNoPattern;
    }

    /**
     * @return Returns the ddlVersionFileName.
     */
    public String getDdlVersionFileName() {
        return ddlVersionFileName;
    }

    /**
     * @param ddlVersionFileName
     *            The ddlVersionFileName to set.
     */
    public void setDdlVersionFileName(String ddlVersionFileName) {
        this.ddlVersionFileName = ddlVersionFileName;
    }

    /**
     * @return Returns the from.
     */
    public Integer getFrom() {
        return from;
    }

    /**
     * @param from
     *            The from to set.
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * @return Returns the to.
     */
    public Integer getTo() {
        return to;
    }

    /**
     * @param to
     *            The to to set.
     */
    public void setTo(Integer to) {
        this.to = to;
    }

    @Override
    protected void doValidate() {
    }

    @Override
    protected void doInit() {
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        schemaVersion = createSchemaVersion();
        ddlVersion = createDdlVersion();
        sqlScriptTokenizer = createScriptTokenizer();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
    }

    @Override
    protected void doExecute() {
        int fromVersionNo = from != null ? from : schemaVersion.getVersionNo();
        int toVersionNo = to != null ? to : ddlVersion.getVersionNo();
        drop(fromVersionNo);
        create(toVersionNo);
        logger.log("IS2JDBCGen0005",
                new Object[] { fromVersionNo, toVersionNo });
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    /**
     * スキーマからオブジェクトを削除します。
     * 
     * @param versionNo
     *            バージョン番号
     */
    protected void drop(int versionNo) {
        String versionName = convertVersionNoToVersionName(versionNo);
        File versionDir = new File(migrationRootDir, versionName);
        File dropDir = new File(versionDir, dropDirName);
        SqlExecutionContext context = createSqlExecutionContext();
        try {
            try {
                dump(context);
                for (File sqlFile : getSqlFileList(dropDir)) {
                    executeSqlFile(context, sqlFile);
                }
            } finally {
                if (!context.getExceptionList().isEmpty()) {
                    throw context.getExceptionList().get(0);
                }
            }
        } finally {
            context.destroy();
        }
    }

    /**
     * データをダンプします。
     * 
     * @param context
     *            SQLの実行コンテキスト
     */
    protected void dump(SqlExecutionContext context) {
    }

    /**
     * スキーマにオブジェクトを作成します。
     * 
     * @param versionNo
     *            バージョン番号
     */
    protected void create(int versionNo) {
        String versionDirName = convertVersionNoToVersionName(versionNo);
        File versionDir = new File(migrationRootDir, versionDirName);
        File createDir = new File(versionDir, createDirName);
        SqlExecutionContext context = createSqlExecutionContext();
        try {
            try {
                for (File sqlFile : getSqlFileList(createDir)) {
                    executeSqlFile(context, sqlFile);
                    if (sqlFile.getName().equals(createTableDdlFileName)) {
                        load(context);
                    }
                }
            } finally {
                if (!context.getExceptionList().isEmpty()) {
                    throw context.getExceptionList().get(0);
                }
            }
        } finally {
            context.destroy();
        }
    }

    /**
     * データをロードします。
     * 
     * @param context
     */
    protected void load(SqlExecutionContext context) {
    }

    /**
     * SQLファイルを実行します。
     * 
     * @param context
     *            SQLの実行コンテキスト
     * @param sqlFile
     *            SQLファイル
     */
    protected void executeSqlFile(SqlExecutionContext context, File sqlFile) {
        SqlScriptReader reader = createSqlScriptReader(sqlFile);
        try {
            for (String sql = reader.readSql(); sql != null; sql = reader
                    .readSql()) {
                SqlExecutor sqlExecutor = createSqlExecutor(sqlFile, sql);
                sqlExecutor.execute(context);
            }
        } finally {
            reader.close();
        }
    }

    /**
     * バージョン番号をバージョン名に変換します。
     * 
     * @param versionNo
     *            バージョン番号
     * @return バージョン名
     */
    protected String convertVersionNoToVersionName(int versionNo) {
        return StringConversionUtil.toString(versionNo, versionNoPattern);
    }

    /**
     * ディレクトリに存在するSQLファイルをリストとして返します。
     * 
     * @param dir
     *            ディレクトリ
     * @return SQLファイルのリスト
     */
    protected List<File> getSqlFileList(File dir) {
        if (!dir.exists()) {
            logger.log("DS2JDBCGen0010", new Object[] { dir.getPath() });
            return Collections.emptyList();
        }
        logger.log("DS2JDBCGen0011", new Object[] { dir.getPath() });

        File[] files = dir.listFiles(new ExclusionFilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (super.accept(dir, name)) {
                    return name.endsWith(".sql") || name.endsWith(".ddl");
                }
                return false;
            }
        });

        List<File> list = Arrays.asList(files);
        Collections.sort(list, new Comparator<File>() {

            public int compare(File file1, File file2) {
                return file1.getName().compareTo(file2.getName());
            }
        });
        return list;
    }

    /**
     * {@link SchemaVersion}の実装を作成します。
     * 
     * @return {@link SchemaVersion}の実装
     */
    protected SchemaVersion createSchemaVersion() {
        return new SchemaVersionImpl(dataSource, dialect,
                schemaInfoFullTableName, schemaInfoColumnName);
    }

    /**
     * {@link DdlVersion}の実装を作成します。
     * 
     * @return {@link DdlVersion}の実装
     */
    protected DdlVersion createDdlVersion() {
        return new DdlVersionImpl(
                new File(migrationRootDir, ddlVersionFileName));
    }

    /**
     * {@link SqlScriptTokenizer}の実装を作成します。
     * 
     * @return {@link SqlScriptTokenizer}の実装
     */
    protected SqlScriptTokenizer createScriptTokenizer() {
        String blockDelimiter = this.blockDelimiter != null ? this.blockDelimiter
                : dialect.getSqlBlockDelimiter();
        return new SqlScriptTokenizerImpl(statementDelimiter, blockDelimiter);
    }

    /**
     * {@link SqlExecutionContext}の実装を作成します。
     * 
     * @return {@link SqlExecutionContext}の実装
     */
    protected SqlExecutionContext createSqlExecutionContext() {
        return new SqlExecutionContextImpl(DataSourceUtil
                .getConnection(dataSource), haltOnError);
    }

    /**
     * {@link SqlScriptReader}の実装を作成します。
     * 
     * @param sqlFile
     *            SQLファイル
     * 
     * @return {@link SqlScriptReader}の実装
     */
    protected SqlScriptReader createSqlScriptReader(File sqlFile) {
        return new SqlScriptReaderImpl(sqlFile, ddlFileEncoding,
                sqlScriptTokenizer, dialect);
    }

    /**
     * {@link SqlExecutor}の実装を作成します。
     * 
     * @param sqlFile
     *            SQLファイル
     * @param sql
     *            SQL
     * @return {@link SqlExecutor}の実装
     */
    protected SqlExecutor createSqlExecutor(File sqlFile, String sql) {
        return new SqlExecutorImpl(sqlFile.getPath(), sql);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

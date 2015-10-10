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
package org.seasar.extension.jdbc.gen.internal.command;

import java.io.File;

import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.data.Loader;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlFileExecutor;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.Migrater;
import org.seasar.extension.jdbc.gen.version.SchemaInfoTable;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * データベースのスキーマとデータを移行する{@link Command}の実装クラスです。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからデータベースの情報を取得します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * 
 * @author taedium
 */
public class MigrateCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(MigrateCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** スキーマ情報を格納するテーブル名 */
    protected String schemaInfoFullTableName = "SCHEMA_INFO";

    /** スキーマのバージョン番号を格納するカラム名 */
    protected String schemaInfoColumnName = "VERSION";

    /** マイグレーションのディレクトリ */
    protected File migrateDir = new File("db", "migrate");

    /** DDL情報ファイル */
    protected File ddlInfoFile = new File("db", "ddl-info.txt");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** マイグレーション先のバージョン */
    protected String version = "latest";

    /** 環境名をバージョンに適用する場合{@code true} */
    protected boolean applyEnvToVersion;

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter = null;

    /** DDLファイルのエンコーディング */
    protected String ddlFileEncoding = "UTF-8";

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    /** エラー発生時に処理を中止する場合{@code true} */
    protected boolean haltOnError = true;

    /** データをロードする際のバッチサイズ */
    protected int loadBatchSize = 10;

    /** トランザクション内で実行する場合{@code true}、そうでない場合{@code false} */
    protected boolean transactional = false;

    /** {@link GenDialect}の実装クラス名 */
    protected String genDialectClassName = null;

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** SQLファイルの実行者 */
    protected SqlFileExecutor sqlFileExecutor;

    /** スキーマのバージョン */
    protected SchemaInfoTable schemaInfoTable;

    /** バージョン管理のディレクトリツリー */
    protected DdlVersionDirectoryTree ddlVersionDirectoryTree;

    /** マイグレータ */
    protected Migrater migrater;

    /** データベース記述のファクトリ */
    protected DatabaseDescFactory databaseDescFactory;

    /** SQLのひとまとまりの実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /** ローダ */
    protected Loader loader;

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * SQLブロックの区切り文字を返します。
     * 
     * @return SQLブロックの区切り文字
     */
    public String getBlockDelimiter() {
        return blockDelimiter;
    }

    /**
     * SQLブロックの区切り文字を設定します。
     * 
     * @param blockDelimiter
     *            SQLブロックの区切り文字
     */
    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    /**
     * エラー発生時に処理を中止する場合{@code true}、中止しない場合{@code false}を返します。
     * 
     * @return エラー発生時に処理を中止する場合{@code true}、中止しない場合{@code false}
     */
    public boolean isHaltOnError() {
        return haltOnError;
    }

    /**
     * エラー発生時に処理を中止する場合{@code true}、中止しない場合{@code false}を設定します。
     * 
     * @param haltOnError
     *            エラー発生時に処理を中止する場合{@code true}、中止しない場合{@code false}
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * スキーマ情報を格納するテーブル名を返します。
     * 
     * @return スキーマ情報を格納するテーブル名
     */
    public String getSchemaInfoFullTableName() {
        return schemaInfoFullTableName;
    }

    /**
     * スキーマ情報を格納するテーブル名を設定します。
     * 
     * @param schemaInfoFullTableName
     *            スキーマ情報を格納するテーブル名
     */
    public void setSchemaInfoFullTableName(String schemaInfoFullTableName) {
        this.schemaInfoFullTableName = schemaInfoFullTableName;
    }

    /**
     * スキーマのバージョン番号を格納するカラム名を返します。
     * 
     * @return スキーマのバージョン番号を格納するカラム名
     */
    public String getSchemaInfoColumnName() {
        return schemaInfoColumnName;
    }

    /**
     * スキーマのバージョン番号を格納するカラム名を設定します。
     * 
     * @param schemaInfoColumnName
     *            スキーマのバージョン番号を格納するカラム名
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        this.schemaInfoColumnName = schemaInfoColumnName;
    }

    /**
     * DDLファイルのエンコーディングを返します。
     * 
     * @return DDLファイルのエンコーディング
     */
    public String getDdlFileEncoding() {
        return ddlFileEncoding;
    }

    /**
     * DDLファイルのエンコーディングを設定します。
     * 
     * @param ddlFileEncoding
     *            DDLファイルのエンコーディング
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        this.ddlFileEncoding = ddlFileEncoding;
    }

    /**
     * マイグレーションのディレクトリを返します。
     * 
     * @return マイグレーションのディレクトリ
     */
    public File getMigrateDir() {
        return migrateDir;
    }

    /**
     * マイグレーションのディレクトリを設定します。
     * 
     * @param migrateDir
     *            マイグレーションのディレクトリ
     */
    public void setMigrateDir(File migrateDir) {
        this.migrateDir = migrateDir;
    }

    /**
     * バージョン番号のパターンを返します。
     * 
     * @return バージョン番号のパターン
     */
    public String getVersionNoPattern() {
        return versionNoPattern;
    }

    /**
     * バージョン番号のパターンを設定します。
     * 
     * @param versionNoPattern
     *            バージョン番号のパターン
     */
    public void setVersionNoPattern(String versionNoPattern) {
        this.versionNoPattern = versionNoPattern;
    }

    /**
     * DDL情報ファイルを返します。
     * 
     * @return DDL情報ファイル
     */
    public File getDdlInfoFile() {
        return ddlInfoFile;
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        this.ddlInfoFile = ddlInfoFile;
    }

    /**
     * マイグレーション先のバージョンを返します。
     * 
     * @return マイグレーション先のバージョン
     */
    public String getVersion() {
        return version;
    }

    /**
     * マイグレーション先のバージョンを設定します。
     * 
     * @param version
     *            マイグレーション先のバージョン
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * クラスパスのディレクトリを返します。
     * 
     * @return クラスパスのディレクトリ
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        this.classpathDir = classpathDir;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を返します。
     * 
     * @return エンティティクラスのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を返します。
     * 
     * @return 対象とするエンティティクラス名の正規表現
     */
    public String getEntityClassNamePattern() {
        return entityClassNamePattern;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        this.entityClassNamePattern = entityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を返します。
     * 
     * @return 対象としないエンティティクラス名の正規表現
     */
    public String getIgnoreEntityClassNamePattern() {
        return ignoreEntityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        this.ignoreEntityClassNamePattern = ignoreEntityClassNamePattern;
    }

    /**
     * ダンプファイルのエンコーディングを返します。
     * 
     * @return ダンプファイルのエンコーディング
     */
    public String getDumpFileEncoding() {
        return dumpFileEncoding;
    }

    /**
     * ダンプファイルのエンコーディングを設定します。
     * 
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        this.dumpFileEncoding = dumpFileEncoding;
    }

    /**
     * データをロードする際のバッチサイズを返します。
     * 
     * @return データをロードする際のバッチサイズ
     */
    public int getLoadBatchSize() {
        return loadBatchSize;
    }

    /**
     * データをロードする際のバッチサイズを設定します。
     * 
     * @param loadBatchSize
     *            データをロードする際のバッチサイズ
     */
    public void setLoadBatchSize(int loadBatchSize) {
        this.loadBatchSize = loadBatchSize;
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public boolean isTransactional() {
        return transactional;
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    /**
     * {@link GenDialect}の実装クラス名を返します。
     * 
     * @return {@link GenDialect}の実装クラス名
     */
    public String getGenDialectClassName() {
        return genDialectClassName;
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        this.genDialectClassName = genDialectClassName;
    }

    /**
     * 環境名をバージョンに適用する場合{@code true}を返します。
     * 
     * @return 環境名をバージョンに適用する場合{@code true}
     */
    public boolean isApplyEnvToVersion() {
        return applyEnvToVersion;
    }

    /**
     * 環境名をバージョンに適用する場合{@code true}を設定します。
     * 
     * @param applyEnvToVersion
     *            環境名をバージョンに適用する場合{@code true}
     */
    public void setApplyEnvToVersion(boolean applyEnvToVersion) {
        this.applyEnvToVersion = applyEnvToVersion;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        dialect = getGenDialect(genDialectClassName);
        valueTypeProvider = createValueTypeProvider();
        if (transactional) {
            userTransaction = SingletonS2Container
                    .getComponent(UserTransaction.class);
        }
        sqlFileExecutor = createSqlFileExecutor();
        schemaInfoTable = createSchemaInfoTable();
        ddlVersionDirectoryTree = createDdlVersionDirectoryTree();
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        loader = createLoader();
        migrater = createMigrater();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() {
        final DatabaseDesc databaseDesc = databaseDescFactory.getDatabaseDesc();

        migrater.migrate(new Migrater.Callback() {

            public void drop(SqlExecutionContext sqlExecutionContext, File file) {
                if (sqlFileExecutor.isTarget(databaseDesc, file)) {
                    boolean haltOnError = sqlExecutionContext.isHaltOnError();
                    try {
                        sqlExecutionContext.setHaltOnError(false);
                        sqlFileExecutor.execute(sqlExecutionContext, file);
                    } finally {
                        sqlExecutionContext.setHaltOnError(haltOnError);
                    }
                }
            }

            public void create(SqlExecutionContext sqlExecutionContext,
                    File file) {
                if (sqlFileExecutor.isTarget(databaseDesc, file)) {
                    sqlFileExecutor.execute(sqlExecutionContext, file);
                }
                if (loader.isTarget(databaseDesc, file)) {
                    loader.load(sqlExecutionContext, databaseDesc, file);
                }
            }
        });
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return factory.createEntityMetaReader(this, classpathDir, ClassUtil
                .concatName(rootPackageName, entityPackageName), jdbcManager
                .getEntityMetaFactory(), entityClassNamePattern,
                ignoreEntityClassNamePattern, false, null, null);
    }

    /**
     * {@link DatabaseDescFactory}の実装を作成します。
     * 
     * @return {@link DatabaseDescFactory}の実装
     */
    protected DatabaseDescFactory createDatabaseDescFactory() {
        return factory.createDatabaseDescFactory(this, jdbcManager
                .getEntityMetaFactory(), entityMetaReader, dialect,
                valueTypeProvider, true);
    }

    /**
     * {@link SchemaInfoTable}の実装を作成します。
     * 
     * @return {@link SchemaInfoTable}の実装
     */
    protected SchemaInfoTable createSchemaInfoTable() {
        return factory.createSchemaInfoTable(this, jdbcManager.getDataSource(),
                dialect, schemaInfoFullTableName, schemaInfoColumnName);
    }

    /**
     * {@link DdlVersionDirectoryTree}の実装を作成します。
     * 
     * @return {@link DdlVersionDirectoryTree}の実装
     */
    protected DdlVersionDirectoryTree createDdlVersionDirectoryTree() {
        return factory.createDdlVersionDirectoryTree(this, migrateDir,
                ddlInfoFile, versionNoPattern, env, applyEnvToVersion);
    }

    /**
     * {@link Migrater}の実装を作成します。
     * 
     * @return {@link Migrater}の実装
     */
    protected Migrater createMigrater() {
        return factory.createMigrater(this, sqlUnitExecutor, schemaInfoTable,
                ddlVersionDirectoryTree, version, env);
    }

    /**
     * {@link SqlFileExecutor}の実装を作成します。
     * 
     * @return {@link SqlFileExecutor}の実装
     */
    protected SqlFileExecutor createSqlFileExecutor() {
        return factory.createSqlFileExecutor(this, dialect, ddlFileEncoding,
                statementDelimiter, blockDelimiter);
    }

    /**
     * {@link SqlUnitExecutor}の実装を作成します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return factory.createSqlUnitExecutor(this, jdbcManager.getDataSource(),
                userTransaction, haltOnError);
    }

    /**
     * {@link Loader}の実装を作成します。
     * 
     * @return {@link Loader}の実装
     */
    protected Loader createLoader() {
        return factory.createLoader(this, dialect, dumpFileEncoding,
                loadBatchSize, false);
    }

    /**
     * {@link ValueTypeProvider}の実装を作成します。
     * 
     * @return {@link ValueTypeProvider}の実装
     */
    protected ValueTypeProvider createValueTypeProvider() {
        return factory.createValueTypeProvider(this, jdbcManager.getDialect());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

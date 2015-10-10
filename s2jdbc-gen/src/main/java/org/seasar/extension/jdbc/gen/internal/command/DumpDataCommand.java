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
import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * エンティティに対応するデータベースのデータをテーブルごとにダンプする{@link Command}の実装です。
 * 
 * @author taedium
 */
public class DumpDataCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DumpDataCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir = null;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** ダンプディレクトリ */
    protected File dumpDir = null;

    /** ダンプディレクトリ名 */
    protected String dumpDirName = "040-dump";

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    /** マイグレーションのディレクトリ */
    protected File migrateDir = new File("db", "migrate");

    /** DDLのバージョンファイル */
    protected File ddlInfoFile = new File("db", "ddl-info.txt");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** 環境名をバージョンに適用する場合{@code true} */
    protected boolean applyEnvToVersion;

    /** {@link GenDialect}の実装クラス名 */
    protected String genDialectClassName = null;

    /** トランザクション内で実行する場合{@code true}、そうでない場合{@code false} */
    protected boolean transactional = false;

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** データベース記述ファクトリ */
    protected DatabaseDescFactory databaseDescFactory;

    /** SQLのひとまとまりの処理の実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /** ダンパ */
    protected Dumper dumper;

    /** DDLのバージョンを管理するディレクトリツリー */
    protected DdlVersionDirectoryTree ddlVersionDirectoryTree;

    /**
     * インスタンスを構築します。
     */
    public DumpDataCommand() {
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
     * ダンプディレクトリを返します。
     * 
     * @return ダンプディレクトリ
     */
    public File getDumpDir() {
        return dumpDir;
    }

    /**
     * ダンプディレクトリを設定します。
     * 
     * @param dumpDir
     *            ダンプディレクトリ
     */
    public void setDumpDir(File dumpDir) {
        this.dumpDir = dumpDir;
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
     * DDL情報ファイル名を返します。
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
     * ダンプディレクトリ名を返します。
     * 
     * @return ダンプディレクトリ名
     */
    public String getDumpDirName() {
        return dumpDirName;
    }

    /**
     * ダンプディレクトリ名を設定します。
     * 
     * @param dumpDirName
     *            ダンプディレクトリ名
     */
    public void setDumpDirName(String dumpDirName) {
        this.dumpDirName = dumpDirName;
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

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        dialect = getGenDialect(genDialectClassName);
        if (transactional) {
            userTransaction = SingletonS2Container
                    .getComponent(UserTransaction.class);
        }
        valueTypeProvider = createValueTypeProvider();
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        dumper = createDumper();
        ddlVersionDirectoryTree = createDdlVersionDirectoryTree();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() {
        final DatabaseDesc databaseDesc = databaseDescFactory.getDatabaseDesc();
        ManagedFile createDir = ddlVersionDirectoryTree
                .getCurrentVersionDirectory().getCreateDirectory();
        final File dir = dumpDir != null ? dumpDir : createDir.createChild(
                dumpDirName).asFile();

        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                dumper.dump(context, databaseDesc, dir);
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
     * {@link Dumper}の実装を作成します。
     * 
     * @return {@link Dumper}の実装
     */
    protected Dumper createDumper() {
        return factory.createDumper(this, dialect, dumpFileEncoding);
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
     * {@link SqlUnitExecutor}の実装を作成します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return factory.createSqlUnitExecutor(this, jdbcManager.getDataSource(),
                userTransaction, true);
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
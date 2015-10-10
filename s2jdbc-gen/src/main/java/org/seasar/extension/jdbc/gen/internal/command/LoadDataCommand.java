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
import java.util.ArrayList;
import java.util.List;

import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.data.Loader;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileComparetor;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
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
 * ダンプファイルをロードする{@link Command}の実装クラスです。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからデータベースの情報を取得します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * 
 * @author taedium
 */
public class LoadDataCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(LoadDataCommand.class);

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

    /** ダンプファイルのディレクトリ */
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
    protected boolean applyEnvToVersion = false;

    /** データをロードする際のバッチサイズ */
    protected int loadBatchSize = 10;

    /** ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false} */
    protected boolean delete = false;

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

    /** データベース記述のファクトリ */
    protected DatabaseDescFactory databaseDescFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** SQLのひとまとまりの処理の実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /** ローダ */
    protected Loader loader;

    /** DDLのバージョンを管理するディレクトリツリー */
    protected DdlVersionDirectoryTree ddlVersionDirectoryTree;

    /**
     * インスタンスを構築します。
     */
    public LoadDataCommand() {
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
     * ダンプファイルのディレクトリを設定します。
     * 
     * @return ダンプファイルのディレクトリ
     */
    public File getDumpDir() {
        return dumpDir;
    }

    /**
     * ダンプファイルのディレクトリを返します。
     * 
     * @param dumpDir
     *            ダンプファイルのディレクトリ
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
     * ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}を返します。
     * 
     * @return ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}を設定します。
     * 
     * @param delete
     *            ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
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
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        loader = createLoader();
        ddlVersionDirectoryTree = createDdlVersionDirectoryTree();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() {
        final DatabaseDesc databaseDesc = databaseDescFactory.getDatabaseDesc();
        final List<File> fileList = new ArrayList<File>();
        if (dumpDir != null) {
            FileUtil.traverseDirectory(dumpDir,
                    new DefaultExcludesFilenameFilter(), new FileComparetor(),
                    new FileUtil.FileHandler() {

                        public void handle(File file) {
                            fileList.add(file);
                        }
                    });
        } else {
            ManagedFile createDir = ddlVersionDirectoryTree
                    .getCurrentVersionDirectory().getCreateDirectory();
            ManagedFile managedDumpDir = createDir.createChild(dumpDirName);
            fileList.addAll(managedDumpDir.listAllFiles());
        }

        sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

            public void execute(SqlExecutionContext context) {
                for (File file : fileList) {
                    if (loader.isTarget(databaseDesc, file)) {
                        loader.load(context, databaseDesc, file);
                    }
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
     * {@link SqlUnitExecutor}の実装を作成します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return factory.createSqlUnitExecutor(this, jdbcManager.getDataSource(),
                userTransaction, true);
    }

    /**
     * {@link Loader}の実装を作成します。
     * 
     * @return {@link Loader}の実装
     */
    protected Loader createLoader() {
        return factory.createLoader(this, dialect, dumpFileEncoding,
                loadBatchSize, delete);
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
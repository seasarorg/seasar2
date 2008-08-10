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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.DatabaseDesc;
import org.seasar.extension.jdbc.gen.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.Loader;
import org.seasar.extension.jdbc.gen.MigrationFileHandler;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.SqlUnitExecutor.ExecutionUnit;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.migration.DumpFileHandler;
import org.seasar.extension.jdbc.gen.sql.LoaderImpl;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutorImpl;
import org.seasar.extension.jdbc.gen.util.ExclusionFilenameFilter;
import org.seasar.extension.jdbc.gen.util.FileUtil;
import org.seasar.extension.jdbc.gen.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.gen.util.FileUtil.FileHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class LoadDataCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(LoadDataCommand.class);

    /** クラスパスのルートとなるディレクトリ */
    protected File classpathDir;

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティ名の正規表現 */
    protected String entityNamePattern = ".*";

    /** 対象としないエンティティ名の正規表現 */
    protected String ignoreEntityNamePattern = "";

    /** 環境名 */
    protected String env = "ut";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    protected File dumpDir = new File("db", "dump");

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** 方言 */
    protected GenDialect dialect;

    protected DataSource dataSource;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    protected DatabaseDescFactory databaseDescFactory;

    /** ジェネレータ */
    protected Generator generator;

    protected SqlUnitExecutor sqlUnitExecutor;

    protected Loader loader;

    /**
     * インスタンスを構築します。
     */
    public LoadDataCommand() {
    }

    /**
     * クラスパスのルートとなるディレクトリを返します。
     * 
     * @return クラスパスのルートとなるディレクトリ
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
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
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
     * 対象とするエンティティ名の正規表現を返します。
     * 
     * @return 対象とするエンティティ名の正規表現
     */
    public String getEntityNamePattern() {
        return entityNamePattern;
    }

    /**
     * 対象とするエンティティ名の正規表現を設定します。
     * 
     * @param entityNamePattern
     *            対象とするエンティティ名の正規表現
     */
    public void setEntityNamePattern(String entityNamePattern) {
        this.entityNamePattern = entityNamePattern;
    }

    /**
     * 対象としないエンティティ名の正規表現を返します。
     * 
     * @return 対象としないエンティティ名の正規表現
     */
    public String getIgnoreEntityNamePattern() {
        return ignoreEntityNamePattern;
    }

    /**
     * 対象としないエンティティ名の正規表現を設定します。
     * 
     * @param ignoreEntityNamePattern
     *            対象としないエンティティ名の正規表現
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        this.ignoreEntityNamePattern = ignoreEntityNamePattern;
    }

    /**
     * 環境名を返します。
     * 
     * @return 環境名
     */
    public String getEnv() {
        return env;
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
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
     * @return Returns the dumpDir.
     */
    public File getDumpDir() {
        return dumpDir;
    }

    /**
     * @param dumpDir
     *            The dumpDir to set.
     */
    public void setDumpDir(File dumpDir) {
        this.dumpDir = dumpDir;
    }

    /**
     * @return Returns the dumpFileEncoding.
     */
    public String getDumpFileEncoding() {
        return dumpFileEncoding;
    }

    /**
     * @param dumpFileEncoding
     *            The dumpFileEncoding to set.
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        this.dumpFileEncoding = dumpFileEncoding;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dataSource = jdbcManager.getDataSource();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        loader = createLoader();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
    }

    @Override
    protected void doExecute() {
        final DatabaseDesc databaseDesc = databaseDescFactory.getDatabaseDesc();
        final List<MigrationFileHandler> handlerList = new ArrayList<MigrationFileHandler>();

        FileUtil.traverseDirectory(dumpDir, new ExclusionFilenameFilter(),
                new FileHandler() {

                    public void handle(File file) {
                        if (file.getName().endsWith(".csv")) {
                            handlerList.add(new DumpFileHandler(databaseDesc,
                                    file, loader));
                        }
                    }
                });

        sqlUnitExecutor.execute(new ExecutionUnit<Void>() {

            public Void execute(SqlExecutionContext context) {
                for (MigrationFileHandler handler : handlerList) {
                    handler.handle(context);
                }
                return null;
            }
        });
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), entityMetaFactory,
                entityNamePattern, ignoreEntityNamePattern);
    }

    protected DatabaseDescFactory createDatabaseDescFactory() {
        return new DatabaseDescFactoryImpl(entityMetaFactory, entityMetaReader,
                dialect);
    }

    protected SqlUnitExecutor createSqlUnitExecutor() {
        return new SqlUnitExecutorImpl(dataSource, false);
    }

    protected Loader createLoader() {
        return new LoaderImpl(dialect, dumpFileEncoding);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.ProductInfo;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialectRegistry;
import org.seasar.extension.jdbc.gen.exception.CommandFailedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;
import org.seasar.extension.jdbc.gen.internal.factory.FactoryImpl;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;
import org.seasar.extension.jdbc.gen.internal.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;

/**
 * コマンドの抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractCommand implements Command {

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** 環境名 */
    protected String env = "ut";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** {@link Factory}の実装クラス名 */
    protected String factoryClassName = FactoryImpl.class.getName();

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** 内部的なJDBCマネージャ */
    protected JdbcManagerImplementor jdbcManager;

    /** S2JDBC-Genのインタフェースの実装を作成するファクトリ */
    protected Factory factory;

    /**
     * インスタンスを構築します。
     */
    public AbstractCommand() {
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
     * {@link Factory}の実装クラス名を返します。
     * 
     * @return {@link Factory}の実装クラス名
     */
    public String getFactoryClassName() {
        return factoryClassName;
    }

    /**
     * {@link Factory}の実装クラス名を設定します。
     * 
     * @param factoryClassName
     *            {@link Factory}の実装クラス名
     */
    public void setFactoryClassName(String factoryClassName) {
        this.factoryClassName = factoryClassName;
    }

    public final void execute() {
        ProductInfo info = ProductInfo.getInstance();
        getLogger().log("IS2JDBCGen0008",
                new Object[] { info.getName(), info.getVersion() });
        String commandClassName = getClass().getName();
        getLogger().log("DS2JDBCGen0003", new Object[] { commandClassName });
        logWritableProperties();
        validate();
        init();
        try {
            doExecute();
        } catch (Throwable t) {
            throw new CommandFailedRuntimeException(t, commandClassName);
        } finally {
            destroy();
        }
        getLogger().log("DS2JDBCGen0008", new Object[] { commandClassName });
    }

    /**
     * 設定可能なプロパティの値をログ出力します。
     */
    protected void logWritableProperties() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (propertyDesc.hasWriteMethod()) {
                getLogger().log(
                        "DS2JDBCGen0001",
                        new Object[] { propertyDesc.getPropertyName(),
                                propertyDesc.getValue(this) });
            }
        }
    }

    /**
     * 検証します。
     */
    protected final void validate() {
        doValidate();
    }

    /**
     * 初期化します。
     */
    protected final void init() {
        factory = ReflectUtil.newInstance(Factory.class, factoryClassName);
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();
        jdbcManager = SingletonS2Container.getComponent(jdbcManagerName);
        doInit();
    }

    /**
     * 破棄します。
     */
    protected final void destroy() {
        doDestroy();
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    /**
     * RDBMSとRDBMSに対する方言をログ出力します。
     * 
     * @param dialect
     */
    protected void logRdbmsAndGenDialect(GenDialect dialect) {
        getLogger().log("DS2JDBCGen0005",
                new Object[] { getRdbmsName(), dialect.getClass().getName() });
    }

    /**
     * RDBMSの名前を返します。
     * 
     * @return RDBMSの名前
     */
    protected String getRdbmsName() {
        Connection conn = DataSourceUtil.getConnection(jdbcManager
                .getDataSource());
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(conn);
            return DatabaseMetaDataUtil.getDatabaseProductName(metaData);
        } finally {
            ConnectionUtil.close(conn);
        }
    }

    /**
     * {@link GenDialect}の実装クラスを返します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     * @return {@link GenDialect}の実装クラス
     */
    protected GenDialect getGenDialect(String genDialectClassName) {
        if (genDialectClassName != null) {
            return ReflectUtil.newInstance(GenDialect.class,
                    genDialectClassName);
        }
        return GenDialectRegistry.getGenDialect(jdbcManager.getDialect());
    }

    /**
     * サブクラスで検証します。
     */
    protected abstract void doValidate();

    /**
     * サブクラスで初期化します。
     */
    protected abstract void doInit();

    /**
     * サブクラスで実行します。
     * 
     * @throws Throwable
     */
    protected abstract void doExecute() throws Throwable;

    /**
     * サブクラスで破棄します。
     */
    protected abstract void doDestroy();

    /**
     * ロガーを返します。
     * 
     * @return ロガー
     */
    protected abstract Logger getLogger();

}

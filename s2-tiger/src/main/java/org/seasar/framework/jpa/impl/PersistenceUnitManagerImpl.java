/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.env.Env;
import org.seasar.framework.jpa.EntityManagerProvider;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitManagerLocater;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.jpa.exception.PersistenceUnitNodFoundException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 永続ユニットを管理するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
@Component
public class PersistenceUnitManagerImpl implements PersistenceUnitManager {

    /** staticなコンテキストマップ */
    protected static final ContextMap staticContextMap = new ContextMap();

    /** コンテキストマップ */
    protected ContextMap contextMap;

    /** staticなコンテキストマップを使用する場合は<code>true</code> */
    protected boolean useStaticContext = Env.getValue().startsWith("ut");

    /** デフォルトの永続ユニット名 */
    protected String defaultPersistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

    /** デフォルトの{@link PersistenceUnitProvider} */
    @Binding(bindingType = BindingType.MUST)
    protected PersistenceUnitProvider defaultUnitProvider;

    /** エンティティマネージャのプロバイダ */
    @Binding(bindingType = BindingType.MUST)
    protected EntityManagerProvider entityManagerProvider;

    /** ネーミング規約 */
    @Binding(bindingType = BindingType.MAY)
    protected NamingConvention convention;

    /**
     * インスタンスを構築します。
     * 
     */
    public PersistenceUnitManagerImpl() {
    }

    /**
     * staticなコンテキストマップを使用する場合は<code>true</code>を設定します。
     * 
     * @param useStaticContext
     *            staticなコンテキストマップを使用する場合は<code>true</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setUseStaticContext(final boolean useStaticContext) {
        this.useStaticContext = useStaticContext;
    }

    /**
     * デフォルトの永続ユニット名を設定します。
     * 
     * @param defaultPersistenceUnitName
     *            デフォルトの永続ユニット名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDefaultPersistenceUnitName(
            final String defaultPersistenceUnitName) {
        this.defaultPersistenceUnitName = defaultPersistenceUnitName;
    }

    /**
     * 永続ユニットマネージャをオープンします。
     */
    @InitMethod
    public void open() {
        contextMap = useStaticContext ? staticContextMap : new ContextMap();
        PersistenceUnitManagerLocater.setInstance(this);
    }

    /**
     * 永続ユニットマネージャをクローズします。
     * <p>
     * staticなコンテキストマップを使用していない場合、管理下にある{@link EntityManagerFactory}を全てクローズします。
     * </p>
     * 
     */
    @DestroyMethod
    public void close() {
        PersistenceUnitManagerLocater.setInstance(null);
        synchronized (contextMap) {
            if (!useStaticContext) {
                contextMap.close();
            }
        }
    }

    public EntityManagerFactory getEntityManagerFactory(final String unitName) {
        return getEntityManagerFactory(unitName, unitName);
    }

    public EntityManagerFactory getEntityManagerFactory(final String unitName,
            final PersistenceUnitProvider provider) {
        return getEntityManagerFactory(unitName, unitName, provider);
    }

    public EntityManagerFactory getEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName) {
        return getEntityManagerFactory(abstractUnitName, concreteUnitName,
                defaultUnitProvider);
    }

    public EntityManagerFactory getEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName,
            final PersistenceUnitProvider provider) {
        synchronized (contextMap) {
            final EntityManagerFactory emf = contextMap
                    .getEntityManagerFactory(concreteUnitName);
            if (emf != null) {
                return emf;
            }
            if (provider == null) {
                throw new PersistenceUnitNodFoundException(concreteUnitName);
            }
            return createEntityManagerFactory(abstractUnitName,
                    concreteUnitName, provider);
        }
    }

    /**
     * 指定されたユニット名を持つ{@link EntityManagerFactory}を指定の{PersistenceUnitProvider}から作成して返します。
     * 
     * @param abstractUnitName
     *            抽象ユニット名
     * @param concreteUnitName
     *            具象ユニット名
     * @param provider
     *            {@link EntityManagerFactory}を作成する{PersistenceUnitProvider}
     * @return 指定されたユニット名を持ち、指定の{PersistenceUnitProvider}から作成された{@link EntityManagerFactory}
     */
    protected EntityManagerFactory createEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName,
            final PersistenceUnitProvider provider) {
        final EntityManagerFactory emf = provider.createEntityManagerFactory(
                abstractUnitName, concreteUnitName);
        if (emf != null) {
            contextMap.addEntityManagerFactory(concreteUnitName, provider, emf);
            return emf;
        }
        throw new PersistenceUnitNodFoundException(concreteUnitName);
    }

    public String getAbstractPersistenceUnitName(final Class<?> entityClass) {
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        final String path = ClassUtil.getResourcePath(entityClass);
        return getAbstractPersistenceUnitName(entityPackageName, path);
    }

    public String getAbstractPersistenceUnitName(final String mappingFile) {
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        if (mappingFile.lastIndexOf("/" + entityPackageName + "/") > -1) {
            return getAbstractPersistenceUnitName(entityPackageName,
                    mappingFile);
        }
        final String daoPackageName = convention.getDaoPackageName();
        return getAbstractPersistenceUnitName(daoPackageName, mappingFile);
    }

    public String getConcretePersistenceUnitName(final Class<?> entityClass) {
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        final String path = ClassUtil.getResourcePath(entityClass);
        return getConcretePersistenceUnitName(entityPackageName, path);
    }

    public String getConcretePersistenceUnitName(final String mappingFile) {
        if (convention == null) {
            return defaultPersistenceUnitName;
        }
        final String entityPackageName = convention.getEntityPackageName();
        if (mappingFile.lastIndexOf("/" + entityPackageName + "/") > -1) {
            return getConcretePersistenceUnitName(entityPackageName,
                    mappingFile);
        }
        final String daoPackageName = convention.getDaoPackageName();
        return getConcretePersistenceUnitName(daoPackageName, mappingFile);
    }

    public PersistenceUnitProvider getPersistenceUnitProvider(
            final Class<?> entityClass) {
        if (convention == null) {
            return getPersistenceUnitProvider(defaultPersistenceUnitName);
        }
        final String packageName = convention.getEntityPackageName();
        final String path = ClassUtil.getResourcePath(entityClass);
        final String unitName = getConcretePersistenceUnitName(packageName,
                path);
        return getPersistenceUnitProvider(unitName);
    }

    public PersistenceUnitProvider getPersistenceUnitProvider(
            final String unitName) {
        return contextMap.getPersistenceUnitProvider(unitName);
    }

    /**
     * リソースのパス名から抽象永続ユニット名を求めて返します。
     * 
     * @param packageName
     *            エンティティのパッケージ名
     * @param path
     *            リソースのパス名
     * @return リソースのパス名から求めた抽象永続ユニット名
     */
    protected String getAbstractPersistenceUnitName(final String packageName,
            final String path) {
        final String prefix = getAbstractPersistenceUnitPrefix(packageName,
                path);
        if (StringUtil.isEmpty(prefix)) {
            return defaultPersistenceUnitName;
        }
        return prefix + StringUtil.capitalize(defaultPersistenceUnitName);
    }

    /**
     * リソースのパス名から具象永続ユニット名を求めて返します。
     * 
     * @param packageName
     *            エンティティのパッケージ名
     * @param path
     *            リソースのパス名
     * @return リソースのパス名から求めた具象永続ユニット名
     */
    protected String getConcretePersistenceUnitName(final String packageName,
            final String path) {
        final String prefix = getConcretePersistenceUnitPrefix(packageName,
                path);
        if (StringUtil.isEmpty(prefix)) {
            return defaultPersistenceUnitName;
        }
        return prefix + StringUtil.capitalize(defaultPersistenceUnitName);
    }

    /**
     * リソースのパス名から抽象永続ユニット名のプレフィックスを求めて返します。
     * 
     * @param packageName
     *            エンティティのパッケージ名
     * @param path
     *            リソースのパス名
     * @return リソースのパス名から求めた永続ユニット名のプレフィックス
     */
    protected String getAbstractPersistenceUnitPrefix(final String packageName,
            final String path) {
        final String key = "/" + packageName + "/";
        final int pos = path.lastIndexOf(key);
        if (pos < 0) {
            return null;
        }
        final int pos2 = path.lastIndexOf('/');
        if (pos + key.length() - 1 == pos2) {
            return null;
        }
        return path.substring(pos + key.length(), pos2);
    }

    /**
     * リソースのパス名から具象永続ユニット名のプレフィックスを求めて返します。
     * 
     * @param packageName
     *            エンティティのパッケージ名
     * @param path
     *            リソースのパス名
     * @return リソースのパス名から求めた具象永続ユニット名のプレフィックス
     */
    protected String getConcretePersistenceUnitPrefix(final String packageName,
            final String path) {
        final String abstractPrefix = getAbstractPersistenceUnitPrefix(
                packageName, path);
        if (!StringUtil.isEmpty(abstractPrefix)) {
            return abstractPrefix;
        }
        return entityManagerProvider.getSelectableEntityManagerPrefix();
    }

    /**
     * 永続ユニットに関する情報を保持するためのクラスです。
     * 
     * @author koichik
     */
    public static class ContextMap {

        /** 永続ユニット名と永続ユニットプロバイダのマップ */
        protected final Map<String, PersistenceUnitProvider> persistenceUnitProviders = CollectionsUtil
                .newHashMap();

        /** 永続ユニット名と{@link EntityManagerFactory}のマップ */
        protected final Map<String, EntityManagerFactory> entityManagerFactories = CollectionsUtil
                .newHashMap();

        /**
         * ユニット名に関連づけられた永続ユニットプロバイダを返します。
         * 
         * @param unitName
         *            ユニット名
         * @return ユニット名に関連づけられた永続ユニットプロバイダ
         */
        public PersistenceUnitProvider getPersistenceUnitProvider(
                final String unitName) {
            return persistenceUnitProviders.get(unitName);
        }

        /**
         * ユニット名に関連づけられた{@link EntityManagerFactory}を返します。
         * 
         * @param unitName
         *            ユニット名
         * @return ユニット名に関連づけられた{@link EntityManagerFactory}
         */
        public EntityManagerFactory getEntityManagerFactory(
                final String unitName) {
            return entityManagerFactories.get(unitName);
        }

        /**
         * ユニット名に関連づけられた{@link EntityManagerFactory}を登録します。
         * 
         * @param unitName
         *            ユニット名
         * @param persistenceUnitProvider
         *            永続ユニットプロバイダ
         * @param emf
         *            {@link EntityManagerFactory}
         */
        public void addEntityManagerFactory(final String unitName,
                final PersistenceUnitProvider persistenceUnitProvider,
                final EntityManagerFactory emf) {
            persistenceUnitProviders.put(unitName, persistenceUnitProvider);
            entityManagerFactories.put(unitName, emf);
        }

        /**
         * 保持している{@link EntityManagerFactory}をクローズします。
         */
        public void close() {
            for (final EntityManagerFactory emf : entityManagerFactories
                    .values()) {
                emf.close();
            }
            entityManagerFactories.clear();
        }

    }

}

/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.jpa.PersistenceClassTransformer;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.jpa.PersistenceUnitInfoRegistry;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * コンテナ管理の{@link EntityManagerFactory} を提供するクラスです。
 * 
 * @author taedium
 */
public class ContainerPersistenceUnitProvider extends
        AbstractPersistenceUnitProvider {

    private static final Logger logger = Logger
            .getLogger(ContainerPersistenceUnitProvider.class);

    /** 永続ユニットのコンフィギュレーション */
    protected PersistenceUnitConfiguration persistenceUnitConfiguration;

    /** 永続ユニット情報のレジストリ */
    protected PersistenceUnitInfoRegistry persistenceUnitInfoRegistry;

    /** 永続クラスのトランスフォーマ */
    protected PersistenceClassTransformer persistenceClassTransformer;

    /** 永続プロバイダのクラス名 */
    protected String providerClassName;

    /** 永続プロバイダのプロパティ */
    protected Map<String, String> properties = CollectionsUtil.newHashMap();

    /**
     * 永続ユニットのコンフィギュレーションを設定します。
     * 
     * @param persistenceUnitConfiguration
     *            永続ユニットのコンフィギュレーション
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitConfiguration(
            final PersistenceUnitConfiguration persistenceUnitConfiguration) {
        this.persistenceUnitConfiguration = persistenceUnitConfiguration;
    }

    /**
     * 永続ユニット情報のレジストリを設定します。
     * 
     * @param persistenceUnitInfoRegistry
     *            永続ユニット情報のレジストリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitInfoRegistry(
            final PersistenceUnitInfoRegistry persistenceUnitInfoRegistry) {
        this.persistenceUnitInfoRegistry = persistenceUnitInfoRegistry;
    }

    /**
     * 永続クラスのトランスフォーマを設定します。
     * 
     * @param persistenceClassTransformer
     *            永続クラスのトランスフォーマ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceClassTransformer(
            PersistenceClassTransformer persistenceClassTransformer) {
        this.persistenceClassTransformer = persistenceClassTransformer;
    }

    /**
     * 永続プロバイダのクラス名を設定します。
     * <p>
     * このプロパティに値が設定されると<code>persistence.xml</code>に設定されている情報を上書きします。
     * </p>
     * 
     * @param providerClassName
     *            永続プロバイダのクラス名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setProviderClassName(final String providerClassName) {
        this.providerClassName = providerClassName;
    }

    /**
     * 永続ユニット情報のプロパティを設定します。
     * <p>
     * このプロパティに値が設定されると<code>persistence.xml</code>に設定されている情報に追加されます。
     * </p>
     * 
     * @param properties
     *            永続ユニット情報のプロパティ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setProperties(final Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    public EntityManagerFactory createEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName) {
        final PersistenceUnitInfo unitInfo = persistenceUnitInfoRegistry
                .getPersistenceUnitInfo(concreteUnitName);
        if (unitInfo == null) {
            throw new IllegalArgumentException(concreteUnitName); // TODO
        }
        overrideUnitInfo(PersistenceUnitInfoImpl.class.cast(unitInfo));

        addMappingFiles(abstractUnitName, unitInfo);
        addPersistenceClasses(abstractUnitName, unitInfo);

        final PersistenceProvider provider = createPersistenceProvider(unitInfo);
        final EntityManagerFactory emf = provider
                .createContainerEntityManagerFactory(unitInfo, null);

        transform(PersistenceUnitInfoImpl.class.cast(unitInfo));

        return emf;
    }

    /**
     * 永続ユニット情報の設定を上書きします。
     * 
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void overrideUnitInfo(final PersistenceUnitInfoImpl unitInfo) {
        if (!StringUtil.isEmpty(providerClassName)) {
            unitInfo.setPersistenceProviderClassName(providerClassName);
        }
        for (final Entry<String, String> entry : properties.entrySet()) {
            unitInfo.addProperties(entry.getKey(), entry.getValue());
        }
    }

    /**
     * マッピングファイルを永続ユニット情報に登録します。
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void addMappingFiles(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        persistenceUnitConfiguration.detectMappingFiles(abstractUnitName,
                new MappingFileHandler(unitInfo));
    }

    /**
     * 永続クラスを永続ユニット情報に登録します。
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void addPersistenceClasses(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        final ClassLoader original = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
                unitInfo.getNewTempClassLoader());
        try {
            persistenceUnitConfiguration.detectPersistenceClasses(
                    abstractUnitName, new PersistenceClassHandler(unitInfo));
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * 永続ユニット情報から永続プロバイダを作成します。
     * 
     * @param unitInfo
     *            永続ユニット情報
     * @return 永続プロバイダ
     */
    protected PersistenceProvider createPersistenceProvider(
            final PersistenceUnitInfo unitInfo) {
        final String providerClassName = unitInfo
                .getPersistenceProviderClassName();
        final Class<PersistenceProvider> providerClass = ReflectionUtil
                .forName(providerClassName);
        return ReflectionUtil.newInstance(providerClass);
    }

    /**
     * 永続ユニット情報で管理されるクラスにトランスフォーマ{@link ClassTransformer}を適用します。
     * 
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void transform(final PersistenceUnitInfoImpl unitInfo) {
        final List<ClassTransformer> trasformers = unitInfo.getTransformers();
        final ClassLoader classLoader = unitInfo.getClassLoader();

        persistenceClassTransformer.transformClasses(trasformers, classLoader,
                unitInfo.getManagedClassNames());

        persistenceClassTransformer.transformJarFiles(trasformers, classLoader,
                unitInfo.getJarFileUrls());
    }

    /**
     * マッピングファイルを永続ユニット情報に登録するクラスです。
     * 
     * @author taedium
     */
    public static class MappingFileHandler implements ResourceHandler {

        protected PersistenceUnitInfo unitInfo;

        /**
         * インスタンスを構築します。
         * 
         * @param unitInfo
         *            永続ユニット
         */
        public MappingFileHandler(final PersistenceUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        public void processResource(final String path, final InputStream is) {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0112", new Object[] { path,
                        unitInfo.getPersistenceUnitName() });
            }
            unitInfo.getMappingFileNames().add(path);
        }

    }

    /**
     * 永続クラスを永続ユニット情報に登録するクラスです。
     * 
     * @author taedium
     */
    public static class PersistenceClassHandler implements ClassHandler {

        protected PersistenceUnitInfo unitInfo;

        protected final Set<String> packageNames = CollectionsUtil.newHashSet();

        /**
         * インスタンスを構築します。
         * 
         * @param unitInfo
         *            永続ユニット
         */
        public PersistenceClassHandler(final PersistenceUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        public void processClass(final String packageName,
                final String shortClassName) {
            final String className = ClassUtil.concatName(packageName,
                    shortClassName);
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0113", new Object[] { className,
                        unitInfo.getPersistenceUnitName() });
            }
            unitInfo.getManagedClassNames().add(className);
            if (!packageNames.contains(packageName)) {
                addPackageInfo(packageName);
            }
        }

        protected void addPackageInfo(final String packageName) {
            packageNames.add(packageName);
            final String pkgInfoName = ClassUtil.concatName(packageName,
                    "package-info");
            final Class<?> pkgInfoClass = ReflectionUtil
                    .forNameNoException(pkgInfoName);
            if (pkgInfoClass != null) {
                unitInfo.getManagedClassNames().add(packageName);
            }
        }

    }

}

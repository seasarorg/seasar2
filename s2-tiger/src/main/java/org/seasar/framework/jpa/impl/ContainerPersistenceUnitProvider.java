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
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.jpa.PersistenceUnitInfoFactory;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * コンテナ管理の{@link EntityManagerFactory} を提供するクラスです。
 * 
 * @author taedium
 */
public class ContainerPersistenceUnitProvider implements
        PersistenceUnitProvider {

    /** <code>persistence.xml</code>のパス名 */
    public static final String PERSISTENCE_XML = "META-INF/persistence.xml";

    private static final Logger logger = Logger
            .getLogger(ContainerPersistenceUnitProvider.class);

    protected PersistenceUnitConfiguration persistenceUnitConfiguration;

    protected PersistenceUnitInfoFactory persistenceUnitInfoFactory;

    protected Map<String, PersistenceUnitInfo> unitInfoMap = CollectionsUtil
            .newHashMap();

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
     * 永続ユニット情報のファクトリを設定します。
     * 
     * @param persistenceUnitInfoFactory
     *            永続ユニット情報のファクトリを設定します。
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitInfoFactory(
            final PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }

    /**
     * <code>META-INF/persistence.xml</code>をロードします。
     */
    @InitMethod
    public void load() {
        @SuppressWarnings("unchecked")
        final Iterator<URL> it = ClassLoaderUtil.getResources(PERSISTENCE_XML);
        while (it.hasNext()) {
            for (final PersistenceUnitInfo unitInfo : persistenceUnitInfoFactory
                    .createPersistenceUnitInfo(it.next())) {
                final String unitName = unitInfo.getPersistenceUnitName();
                if (!unitInfoMap.containsKey(unitName)) {
                    unitInfoMap.put(unitName, unitInfo);
                }
            }
        }
    }

    public EntityManagerFactory createEntityManagerFactory(final String unitName) {
        return createEntityManagerFactory(unitName, unitName);
    }

    public EntityManagerFactory createEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName) {
        final PersistenceUnitInfo unitInfo = unitInfoMap.get(concreteUnitName);
        if (unitInfo == null) {
            throw new IllegalArgumentException(concreteUnitName);
        }

        addMappingFiles(abstractUnitName, unitInfo);
        addPersistenceClasses(abstractUnitName, unitInfo);

        final String providerClassName = unitInfo
                .getPersistenceProviderClassName();
        final Class<PersistenceProvider> providerClass = ReflectionUtil
                .forName(providerClassName);
        final PersistenceProvider provider = ReflectionUtil
                .newInstance(providerClass);
        return provider.createContainerEntityManagerFactory(unitInfo, null);
    }

    protected void addMappingFiles(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        persistenceUnitConfiguration.detectMappingFiles(abstractUnitName,
                new MappingFileHandler(unitInfo));
    }

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

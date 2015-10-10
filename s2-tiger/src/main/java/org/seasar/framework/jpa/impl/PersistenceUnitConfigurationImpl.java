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
package org.seasar.framework.jpa.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link PersistenceUnitConfiguration}の実装クラスです。
 * <p>
 * 永続クラスやマッピングファイルを手動または自動で永続ユニットに登録するために使用します。
 * </p>
 * 
 * @author taedium
 */
public class PersistenceUnitConfigurationImpl implements
        PersistenceUnitConfiguration {

    /** 永続ユニットマネージャ */
    protected PersistenceUnitManager persistenceUnitManager;

    /** 永続ユニット名をキー、マッピングファイル名のリストを値とするマップ */
    protected Map<String, List<String>> mappingFiles = CollectionsUtil
            .newHashMap();

    /** 永続ユニット名をキー、永続クラスのリストを値とするマップ */
    protected Map<String, List<Class<?>>> persistenceClasses = CollectionsUtil
            .newHashMap();

    /** 永続ユニット名をキー、{@link ResourceAutoDetector}のリストを値とするマップ */
    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors = CollectionsUtil
            .newHashMap();

    /** 永続ユニット名をキー、{@link ClassAutoDetector}のリストを値とするマップ */
    protected Map<String, List<ClassAutoDetector>> persistenceClassAutoDetectors = CollectionsUtil
            .newHashMap();

    /**
     * 永続ユニットマネージャを設定します。
     * 
     * @param persistenceUnitManager
     *            永続ユニットマネージャ
     */
    public void setPersistenceUnitManager(
            final PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    /**
     * {@link ResourceAutoDetector}の配列を設定します。
     * 
     * @param detectors
     *            {@link ResourceAutoDetector}の配列
     */
    public void setMappingFileAutoDetector(
            final ResourceAutoDetector[] detectors) {
        for (final ResourceAutoDetector detector : detectors) {
            addMappingFileAutoDetector(detector);
        }
    }

    /**
     * {@link ClassAutoDetector}の配列を設定します。
     * 
     * @param detectors
     *            {@link ClassAutoDetector}の配列
     */
    public void setPersistenceClassAutoDetector(
            final ClassAutoDetector[] detectors) {
        for (final ClassAutoDetector detector : detectors) {
            addPersistenceClassAutoDetector(detector);
        }
    }

    public void addMappingFile(final String fileName) {
        addMappingFile(null, fileName);
    }

    public void addMappingFile(final String unitName, final String fileName) {
        if (!mappingFiles.containsKey(unitName)) {
            mappingFiles.put(unitName, new ArrayList<String>());
        }
        mappingFiles.get(unitName).add(fileName);
    }

    public void addPersistenceClass(final Class<?> clazz) {
        addPersistenceClass(null, clazz);
    }

    public void addPersistenceClass(final String unitName, final Class<?> clazz) {
        if (!persistenceClasses.containsKey(unitName)) {
            persistenceClasses.put(unitName, new ArrayList<Class<?>>());
        }
        persistenceClasses.get(unitName).add(clazz);
    }

    public void addMappingFileAutoDetector(final ResourceAutoDetector detector) {
        addMappingFileAutoDetector(null, detector);
    }

    public void addMappingFileAutoDetector(final String unitName,
            final ResourceAutoDetector detector) {
        if (!mappingFileAutoDetectors.containsKey(unitName)) {
            mappingFileAutoDetectors.put(unitName,
                    new ArrayList<ResourceAutoDetector>());
        }
        mappingFileAutoDetectors.get(unitName).add(detector);
    }

    public void addPersistenceClassAutoDetector(final ClassAutoDetector detector) {
        addPersistenceClassAutoDetector(null, detector);
    }

    public void addPersistenceClassAutoDetector(final String unitName,
            final ClassAutoDetector detector) {
        if (!persistenceClassAutoDetectors.containsKey(unitName)) {
            persistenceClassAutoDetectors.put(unitName,
                    new ArrayList<ClassAutoDetector>());
        }
        persistenceClassAutoDetectors.get(unitName).add(detector);
    }

    public void detectMappingFiles(final String unitName,
            final ResourceHandler handler) {
        for (final String mappingFile : getMappingFileList(unitName)) {
            handler.processResource(mappingFile, null);
        }
        for (final String mappingFile : getMappingFileList(null)) {
            if (isTarget(unitName, mappingFile)) {
                handler.processResource(mappingFile, null);
            }
        }
        for (final ResourceAutoDetector detector : getMappingFileAutoDetectorList(unitName)) {
            detector.detect(handler);
        }
        for (final ResourceAutoDetector detector : getMappingFileAutoDetectorList(null)) {
            detector.detect(new UnitNameAwareHandler(unitName, handler));
        }
    }

    public void detectPersistenceClasses(final String unitName,
            final ClassHandler handler) {
        for (final Class<?> clazz : getPersistenceClassList(unitName)) {
            invokeHandler(handler, clazz);
        }
        for (final Class<?> clazz : getPersistenceClassList(null)) {
            if (isTarget(unitName, clazz)) {
                invokeHandler(handler, clazz);
            }
        }
        for (final ClassAutoDetector detector : getPersistenceClassAutoDetectorList(unitName)) {
            detector.detect(handler);
        }
        for (final ClassAutoDetector detector : getPersistenceClassAutoDetectorList(null)) {
            detector.detect(new UnitNameAwareHandler(unitName, handler));
        }
    }

    public boolean isAutoDetection() {
        return persistenceClassAutoDetectors.size() > 0
                || mappingFileAutoDetectors.size() > 0;
    }

    /**
     * マッピングファイルのリストを返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return マッピングファイルのリスト
     */
    protected List<String> getMappingFileList(final String unitName) {
        final List<String> result = mappingFiles.get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * {@link ResourceAutoDetector}のリストを返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return {@link ResourceAutoDetector}のリスト
     */
    protected List<ResourceAutoDetector> getMappingFileAutoDetectorList(
            final String unitName) {
        final List<ResourceAutoDetector> result = mappingFileAutoDetectors
                .get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * 永続クラスのリストを返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return 永続クラスのリスト
     */
    protected List<Class<?>> getPersistenceClassList(final String unitName) {
        final List<Class<?>> result = persistenceClasses.get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * {@link ClassAutoDetector}のリストを返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return {@link ClassAutoDetector}のリスト
     */
    protected List<ClassAutoDetector> getPersistenceClassAutoDetectorList(
            final String unitName) {
        final List<ClassAutoDetector> result = persistenceClassAutoDetectors
                .get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * <code>handler</code>を実行します。
     * 
     * @param handler
     *            クラスを処理するためのハンドラ
     * @param clazz
     *            処理対象のクラス
     */
    protected void invokeHandler(final ClassHandler handler,
            final Class<?> clazz) {
        handler.processClass(ClassUtil.getPackageName(clazz), ClassUtil
                .getShortClassName(clazz));
    }

    /**
     * <code>unitName</code>と<code>mappingFile</code>を管理する永続ユニットの名前が等しい場合
     * <code>true</code>を返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param mappingFile
     *            マッピングファイル
     * @return <code>unitName</code>と<code>mappingFile</code>を
     *         管理する永続ユニットの名前が等しい場合<code>true</code>、等しくない場合<code>false</code>
     */
    protected boolean isTarget(final String unitName, final String mappingFile) {
        return unitName.equals(persistenceUnitManager
                .getAbstractPersistenceUnitName(mappingFile));
    }

    /**
     * <code>unitName</code>と<code>clazz</code>を管理する永続ユニットの名前が等しい場合
     * <code>true</code>を返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param clazz
     *            永続クラス
     * @return <code>unitName</code>と<code>clazz</code>を
     *         管理する永続ユニットの名前が等しい場合<code>true</code>、等しくない場合<code>false</code>
     */
    protected boolean isTarget(final String unitName, final Class<?> clazz) {
        return unitName.equals(persistenceUnitManager
                .getAbstractPersistenceUnitName(clazz));
    }

    /**
     * 扱うリソースやクラスが指定された永続ユニットで管理されている場合に限り処理を指定されたハンドラに委譲するラッパです。
     * 
     * @author taedium
     */
    public class UnitNameAwareHandler implements ResourceHandler, ClassHandler {

        /** 永続ユニット名 */
        protected String unitName;

        /** 委譲先のリソースハンドラ */
        protected ResourceHandler delegateResourceHandler;

        /** 委譲先のクラスハンドラ */
        protected ClassHandler delegateClassHandler;

        /**
         * インスタンスを構築します。
         * 
         * @param unitName
         *            永続ユニット名
         * @param delegateResourceHandler
         *            委譲先のリソースハンドラ
         */
        public UnitNameAwareHandler(final String unitName,
                final ResourceHandler delegateResourceHandler) {
            this.unitName = unitName;
            this.delegateResourceHandler = delegateResourceHandler;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param unitName
         *            永続ユニット名
         * @param delegateClassHandler
         *            委譲先のクラスハンドラ
         */
        public UnitNameAwareHandler(final String unitName,
                final ClassHandler delegateClassHandler) {
            this.unitName = unitName;
            this.delegateClassHandler = delegateClassHandler;
        }

        public void processResource(final String path, final InputStream is) {
            if (isTarget(unitName, path)) {
                delegateResourceHandler.processResource(path, is);
            }
        }

        public void processClass(final String packageName,
                final String shortClassName) {
            final String className = ClassUtil.concatName(packageName,
                    shortClassName);
            final Class<?> clazz = ReflectionUtil.forName(className);
            if (isTarget(unitName, clazz)) {
                delegateClassHandler.processClass(packageName, shortClassName);
            }
        }

    }
}

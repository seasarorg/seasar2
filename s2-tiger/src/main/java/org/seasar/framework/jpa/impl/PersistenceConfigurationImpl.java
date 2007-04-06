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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.jpa.PersistenceConfiguration;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class PersistenceConfigurationImpl implements PersistenceConfiguration {

    protected PersistenceUnitManager persistenceUnitManager;

    protected Map<String, List<String>> mappingFiles = CollectionsUtil
            .newHashMap();

    protected Map<String, List<Class<?>>> persistenceClasses = CollectionsUtil
            .newHashMap();

    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors = CollectionsUtil
            .newHashMap();

    protected Map<String, List<ClassAutoDetector>> persistenceClassAutoDetectors = CollectionsUtil
            .newHashMap();

    public void setPersistenceUnitManager(
            final PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
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

    public void setMappingFileAutoDetector(
            final ResourceAutoDetector[] resourceAutoDetectors) {
        for (final ResourceAutoDetector detector : resourceAutoDetectors) {
            addMappingFileAutoDetector(detector);
        }
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

    public void setPersistenceClassAutoDetector(
            final ClassAutoDetector[] detectors) {
        for (final ClassAutoDetector detector : detectors) {
            addPersistenceClassAutoDetector(detector);
        }
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

    @SuppressWarnings("unchecked")
    protected List<String> getMappingFileList(final String unitName) {
        final List<String> result = mappingFiles.get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    protected List<ResourceAutoDetector> getMappingFileAutoDetectorList(
            final String unitName) {
        final List<ResourceAutoDetector> result = mappingFileAutoDetectors
                .get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    protected List<Class<?>> getPersistenceClassList(final String unitName) {
        final List<Class<?>> result = persistenceClasses.get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    protected List<ClassAutoDetector> getPersistenceClassAutoDetectorList(
            final String unitName) {
        final List<ClassAutoDetector> result = persistenceClassAutoDetectors
                .get(unitName);
        if (result != null) {
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    protected void invokeHandler(final ClassHandler handler,
            final Class<?> clazz) {
        handler.processClass(ClassUtil.getPackageName(clazz), ClassUtil
                .getShortClassName(clazz));
    }

    protected boolean isTarget(final String unitName, final String mappingFile) {
        return unitName.equals(persistenceUnitManager
                .getPersistenceUnitName(mappingFile));
    }

    protected boolean isTarget(final String unitName, final Class<?> clazz) {
        return unitName.equals(persistenceUnitManager
                .getPersistenceUnitName(clazz));
    }

    public class UnitNameAwareHandler implements ResourceHandler, ClassHandler {

        protected String unitName;

        protected ResourceHandler delegateResourceHandler;

        protected ClassHandler delegateClassHandler;

        public UnitNameAwareHandler(final String unitName,
                final ResourceHandler delegateResourceHandler) {
            this.unitName = unitName;
            this.delegateResourceHandler = delegateResourceHandler;
        }

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

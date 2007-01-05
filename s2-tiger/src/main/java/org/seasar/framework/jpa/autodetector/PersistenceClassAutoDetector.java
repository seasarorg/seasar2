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
package org.seasar.framework.jpa.autodetector;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.seasar.framework.autodetector.impl.AbstractClassAutoDetector;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 */
@Component
public class PersistenceClassAutoDetector extends AbstractClassAutoDetector {

    protected final List<Class<? extends Annotation>> annotations = CollectionsUtil
            .newArrayList();

    protected NamingConvention namingConvention;

    protected ClassLoader classLoader;

    public PersistenceClassAutoDetector() {
        annotations.add(Entity.class);
        annotations.add(MappedSuperclass.class);
        annotations.add(Embeddable.class);
    }

    @Binding(bindingType = BindingType.MAY)
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @InitMethod
    public void init() {
        if (namingConvention != null) {
            final String entityPackageName = namingConvention
                    .getEntityPackageName();
            for (final String rootPackageName : namingConvention
                    .getRootPackageNames()) {
                final String packageName = ClassUtil.concatName(
                        rootPackageName, entityPackageName);
                addTargetPackageName(packageName);
            }
        }
    }

    public void addAnnotation(final Class<? extends Annotation> annotation) {
        annotations.add(annotation);
    }

    @SuppressWarnings("unchecked")
    public void detect(final ClassHandler handler) {
        for (int i = 0; i < getTargetPackageNameSize(); i++) {
            final String packageName = getTargetPackageName(i);
            for (final Iterator<URL> it = ClassLoaderUtil
                    .getResources(packageName.replace('.', '/')); it.hasNext();) {
                detect(handler, packageName, it.next());
            }
        }
    }

    protected void detect(final ClassHandler handler,
            final String entityPackageName, final URL url) {
        final Strategy strategy = getStrategy(url.getProtocol());
        strategy.detect(entityPackageName, url, new ClassHandler() {
            public void processClass(final String packageName,
                    final String shortClassName) {
                if (packageName.startsWith(entityPackageName)
                        && isEntity(packageName, shortClassName)) {
                    handler.processClass(packageName, shortClassName);
                }
            }
        });
    }

    protected boolean isEntity(final String packageName,
            final String shortClassName) {
        final String name = ClassUtil.concatName(packageName, shortClassName);
        final Class<?> clazz = getClass(name);
        for (final Annotation ann : clazz.getAnnotations()) {
            if (annotations.contains(ann.annotationType())) {
                return true;
            }
        }
        return false;
    }

    protected Class<?> getClass(final String className) {
        if (classLoader != null) {
            return ReflectionUtil.forName(className, classLoader);
        }
        return ReflectionUtil.forNameNoException(className);
    }

}

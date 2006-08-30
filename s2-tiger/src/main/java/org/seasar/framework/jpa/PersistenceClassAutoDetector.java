/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.seasar.framework.autodetector.impl.AbstractClassAutoDetector;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
@Component
public class PersistenceClassAutoDetector extends AbstractClassAutoDetector {

    protected final List<Class<? extends Annotation>> annotations = CollectionsUtil
            .newArrayList();

    protected NamingConvention namingConvention;

    public PersistenceClassAutoDetector() {
        annotations.add(Entity.class);
        annotations.add(MappedSuperclass.class);
        annotations.add(Embeddable.class);
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

    public void addAnnotation(Class<? extends Annotation> annotation) {
        this.annotations.add(annotation);
    }

    @Binding(bindingType = BindingType.MAY)
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public Class[] detect() {
        final Set<Class<?>> result = CollectionsUtil.newHashSet();

        for (int i = 0; i < getTargetPackageNameSize(); i++) {
            final String packageName = getTargetPackageName(i);
            final URL url = ResourceUtil.getResourceNoException(packageName
                    .replace(".", "/"));

            if (url != null) {
                detect(result, packageName, url);
            }
        }

        return result.toArray(new Class<?>[result.size()]);
    }

    protected void detect(final Set<Class<?>> result, final String packageName,
            final URL url) {
        for (final String rootPackageName : namingConvention
                .getRootPackageNames()) {
            final URL rootUrl = ResourceUtil.getResource(rootPackageName
                    .replace(".", "/"));
            if (!rootUrl.getProtocol().equals(url.getProtocol())) {
                continue;
            }
            final Strategy strategy = getStrategy(url.getProtocol());
            final String rootBaseName = strategy.getBaseName(rootPackageName,
                    rootUrl);
            final String baseName = strategy.getBaseName(packageName, url);
            if (!rootBaseName.equals(baseName)) {
                continue;
            }

            strategy.detect(packageName, url, new ClassHandler() {
                public void processClass(final String packageName,
                        final String shortClassName) {
                    final String name = ClassUtil.concatName(packageName,
                            shortClassName);
                    final Class<?> clazz = ReflectionUtil.forName(name);
                    for (final Annotation ann : clazz.getAnnotations()) {
                        if (annotations.contains(ann.annotationType())) {
                            result.add(clazz);
                            return;
                        }
                    }
                }
            });
        }
    }
}

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
package org.seasar.framework.autodetector.impl;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;

import org.seasar.framework.traverser.ClassTraverser;
import org.seasar.framework.traverser.Traverser;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class AnnotatedClassAutoDetector extends AbstractClassAutoDetector {
    
    private List<Class<? extends Annotation>> annotations = CollectionsUtil.newArrayList();

    public void addAnnotation(Class<? extends Annotation> annotation) {
        annotations.add(annotation);
    }

    public List<Class<? extends Annotation>> getAnnotations() {
        return annotations;
    }
    
    public Class<?>[] detect() {
        final List<Class<?>> result = CollectionsUtil.newArrayList();
        
        for (int i = 0; i < getReferenceClassSize(); i++) {
            final Class<?> referenceClass = getReferenceClass(i);
            final String baseClassPath = ResourceUtil
                    .getResourcePath(referenceClass);
            final URL url = ResourceUtil.getResource(baseClassPath);
            final Strategy strategy = getStrategy(url.getProtocol());
            
            final Traverser traverser = new ClassTraverser(
                    new ClassHandler() {
                public void processClass(String packageName,
                        String shortClassName) {
                    final String name = ClassUtil.concatName(packageName,
                            shortClassName);
                    final Class<?> clazz = ReflectionUtil.forName(name);
                    for (Annotation annotation : clazz.getAnnotations()) {
                        if (annotations.contains(annotation.annotationType())) {
                            result.add(clazz);
                            return;
                        }
                    }
                }
            });
            
            strategy.detect(referenceClass, url, traverser);
        }
        
        return result.toArray(new Class<?>[]{});
    }
}

/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.customizer;

import java.lang.reflect.Method;

import org.seasar.framework.aop.annotation.DependencyLookup;
import org.seasar.framework.aop.interceptors.DependencyLookupInterceptor;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.factory.AspectDefFactory;

/**
 * {@link DependencyLookup}が注釈されたメソッドに{@link DependencyLookupInterceptor}
 * を適用するカスタマイザです。
 * 
 * @author koichik
 */
public class DependencyLookupCustomizer extends AbstractCustomizer {

    @Override
    protected void doCustomize(ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        for (final Method method : componentClass.getMethods()) {
            if (method.isSynthetic() || method.isBridge()) {
                continue;
            }
            final DependencyLookup annotation = method
                    .getAnnotation(DependencyLookup.class);
            if (annotation == null) {
                continue;
            }
            componentDef.addAspectDef(AspectDefFactory.createAspectDef(
                    "aop.dependencyLookupInterceptor", method));
        }
    }

}

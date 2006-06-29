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
package org.seasar.framework.container.factory.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.factory.AnnotationHandler;

/**
 * @author koichik
 * 
 */
public class AspectAnnotationAspectDefBuilder extends AbstractAspectDefBuilder {

    public void appendAspectDef(final AnnotationHandler annotationHandler,
            final ComponentDef componentDef) {
        processClass(componentDef);
        processMethod(componentDef);
    }

    protected void processClass(final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final Aspect aspect = componentClass.getAnnotation(Aspect.class);
        if (aspect != null) {
            String interceptor = aspect.value();
            String pointcut = aspect.pointcut();
            appendAspect(componentDef, interceptor, pointcut);
        }
    }

    protected void processMethod(final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final Method[] methods = componentClass.getMethods();
        for (final Method method : methods) {
            final int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)
                    || Modifier.isFinal(modifiers)) {
                continue;
            }
            final Aspect aspect = method.getAnnotation(Aspect.class);
            if (aspect != null) {
                String interceptor = aspect.value();
                appendAspect(componentDef, interceptor, method);
            }
        }
    }

}

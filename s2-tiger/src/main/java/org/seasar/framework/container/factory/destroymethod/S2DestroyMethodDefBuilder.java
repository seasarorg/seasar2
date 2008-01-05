/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory.destroymethod;

import java.lang.reflect.Method;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.IllegalDestroyMethodAnnotationRuntimeException;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.DestroyMethodDefBuilder;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;

/**
 * {@link DestroyMethod}アノテーションを読み取り{@link DestroyMethodDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class S2DestroyMethodDefBuilder implements DestroyMethodDefBuilder {

    public void appendDestroyMethodDef(AnnotationHandler annotationHandler,
            ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        for (final Method method : componentClass.getMethods()) {
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final DestroyMethod destroyMethod = method
                    .getAnnotation(DestroyMethod.class);
            if (destroyMethod == null) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                throw new IllegalDestroyMethodAnnotationRuntimeException(
                        componentClass, method.getName());
            }
            if (!annotationHandler.isDestroyMethodRegisterable(componentDef,
                    method.getName())) {
                continue;
            }
            componentDef.addDestroyMethodDef(new DestroyMethodDefImpl(method));
        }
    }

}

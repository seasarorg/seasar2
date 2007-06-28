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
package org.seasar.framework.container.factory.initmethod;

import java.lang.reflect.Method;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.InitMethodDefBuilder;
import org.seasar.framework.container.impl.InitMethodDefImpl;

/**
 * {@link InitMethod}アノテーションを読み取り{@link InitMethodDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class S2InitMethodDefBuilder implements InitMethodDefBuilder {

    public void appendInitMethodDef(AnnotationHandler annotationHandler,
            ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        for (final Method method : componentClass.getMethods()) {
            final InitMethod initMethod = method
                    .getAnnotation(InitMethod.class);
            if (initMethod == null) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                throw new IllegalInitMethodAnnotationRuntimeException(
                        componentClass, method.getName());
            }
            if (!annotationHandler.isInitMethodRegisterable(componentDef,
                    method.getName())) {
                continue;
            }
            componentDef.addInitMethodDef(new InitMethodDefImpl(method));
        }
    }

}

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
package org.seasar.framework.container.factory.initmethod;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.InitMethodDefBuilder;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3DescFactory;

/**
 * EJB3の{@link PostConstruct}アノテーションを読み取り{@link DestroyMethodDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class EJB3InitMethodDefBuilder implements InitMethodDefBuilder {

    public void appendInitMethodDef(AnnotationHandler annotationHandler,
            ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final EJB3Desc ejb3Desc = EJB3DescFactory.getEJB3Desc(componentClass);
        if (ejb3Desc == null) {
            return;
        }

        for (final Method method : ejb3Desc.getPostConstructMethods()) {
            if (!annotationHandler.isInitMethodRegisterable(componentDef,
                    method.getName())) {
                continue;
            }
            componentDef.addInitMethodDef(new InitMethodDefImpl(method));
        }
    }

}

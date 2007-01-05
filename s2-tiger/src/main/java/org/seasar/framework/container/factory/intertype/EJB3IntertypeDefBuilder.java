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
package org.seasar.framework.container.factory.intertype;

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.IntertypeDefBuilder;
import org.seasar.framework.container.impl.InterTypeDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.ejb.EJB3BusinessMethodDesc;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3DescFactory;
import org.seasar.framework.ejb.EJB3InterceptorDesc;
import org.seasar.framework.ejb.impl.EJB3InterceptorSupportInterType;

/**
 * @author koichik
 * 
 */
public class EJB3IntertypeDefBuilder implements IntertypeDefBuilder {

    public void appendIntertypeDef(final AnnotationHandler annotationHandler,
            final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final EJB3Desc ejb3Desc = EJB3DescFactory.getEJB3Desc(componentClass);
        if (ejb3Desc == null) {
            return;
        }

        final EJB3InterceptorSupportInterType interType = new EJB3InterceptorSupportInterType();
        for (final Class<?> interceptorClass : getInterceptorClasses(ejb3Desc)) {
            interType.addInterceptor(interceptorClass);
            componentDef.addPropertyDef(createPropertyDef(annotationHandler,
                    interceptorClass));
        }
        componentDef.addInterTypeDef(new InterTypeDefImpl(interType));
    }

    protected Set<Class<?>> getInterceptorClasses(final EJB3Desc ejb3desc) {
        final Set<Class<?>> interceptorClasses = new HashSet<Class<?>>();
        for (final EJB3InterceptorDesc interceptorDesc : ejb3desc
                .getInterceptors()) {
            interceptorClasses.add(interceptorDesc.getInterceptorClass());
        }
        for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                .getBusinessMethods()) {
            for (final EJB3InterceptorDesc interceptorDesc : methodDesc
                    .getInterceptors()) {
                interceptorClasses.add(interceptorDesc.getInterceptorClass());
            }
        }
        return interceptorClasses;
    }

    protected PropertyDef createPropertyDef(
            final AnnotationHandler annotationHandler,
            final Class<?> interceptorClass) {
        final PropertyDefImpl propDef = new PropertyDefImpl(
                EJB3InterceptorSupportInterType.getFieldName(interceptorClass));
        propDef.setChildComponentDef(createInterceptorComonentDef(
                annotationHandler, interceptorClass));
        return propDef;
    }

    protected ComponentDef createInterceptorComonentDef(
            final AnnotationHandler annotationHandler,
            final Class<?> interceptorClass) {
        final ComponentDef interceptorCd = annotationHandler
                .createComponentDef(interceptorClass, null);
        annotationHandler.appendDI(interceptorCd);
        annotationHandler.appendInitMethod(interceptorCd);
        annotationHandler.appendDestroyMethod(interceptorCd);
        annotationHandler.appendAspect(interceptorCd);
        annotationHandler.appendInterType(interceptorCd);
        return interceptorCd;
    }

}

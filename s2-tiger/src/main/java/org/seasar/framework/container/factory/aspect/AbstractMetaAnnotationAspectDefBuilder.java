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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.annotation.Interceptor;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 * 
 */
public abstract class AbstractMetaAnnotationAspectDefBuilder extends
        AbstractAspectDefBuilder {

    protected abstract Class<? extends Annotation> getMetaAnnotationType();

    protected abstract String getInterceptorNamespace();

    protected abstract String getInterceptorSuffix();

    public void appendAspectDef(final AnnotationHandler annotationHandler,
            final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        processClass(componentDef, componentClass);
        processMethod(componentDef, componentClass);
    }

    protected void processClass(final ComponentDef componentDef,
            final Class<?> componentClass) {
        for (final Annotation annotation : componentClass.getAnnotations()) {
            final Class<? extends Annotation> annotationType = annotation
                    .annotationType();
            final Annotation metaAnnotation = annotationType
                    .getAnnotation(getMetaAnnotationType());
            if (metaAnnotation == null) {
                continue;
            }
            final String pointcut = getPointcut(annotation);
            final AspectDef aspectDef = StringUtil.isEmpty(pointcut) ? new AspectDefImpl()
                    : new AspectDefImpl(new PointcutImpl(StringUtil.split(
                            pointcut, ", ")));
            aspectDef.setExpression(new ExpressionImpl(annotation));
            componentDef.addAspectDef(aspectDef);
        }
    }

    protected void processMethod(final ComponentDef componentDef,
            final Class<?> componentClass) {
        for (final Method method : componentClass.getMethods()) {
            final int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)
                    || Modifier.isFinal(modifiers)) {
                continue;
            }
            for (final Annotation annotation : method.getAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation
                        .annotationType();
                final Annotation metaAnnotation = annotationType
                        .getAnnotation(getMetaAnnotationType());
                if (metaAnnotation == null) {
                    continue;
                }
                final AspectDef aspectDef = new AspectDefImpl(new PointcutImpl(
                        method));
                aspectDef.setExpression(new ExpressionImpl(annotation));
                componentDef.addAspectDef(aspectDef);
            }
        }
    }

    protected String getPointcut(final Annotation annotation) {
        for (final Method method : annotation.getClass().getMethods()) {
            if ("pointcut".equals(method.getName())
                    && method.getReturnType() == String.class) {
                return String.class.cast(MethodUtil.invoke(method, annotation,
                        null));
            }
        }
        return null;
    }

    protected String getInterceptorName(final Annotation annotation) {
        final Class<? extends Annotation> annotationType = annotation
                .annotationType();
        final Interceptor interceptor = annotationType
                .getAnnotation(Interceptor.class);
        final String value = interceptor.value();
        if (!StringUtil.isEmpty(value)) {
            return value;
        }
        final String namespace = getInterceptorNamespace();
        final String interceptorName = StringUtil.decapitalize(annotationType
                .getSimpleName());
        final String suffix = getInterceptorSuffix();
        if (namespace != null) {
            return namespace + "." + interceptorName + suffix;
        }
        return interceptorName + suffix;
    }

    public class ExpressionImpl implements Expression {
        protected Annotation annotation;

        public ExpressionImpl(final Annotation annotation) {
            this.annotation = annotation;
        }

        public Object evaluate(final S2Container container, final Map context) {
            final MethodInterceptor interceptor = MethodInterceptor.class
                    .cast(container
                            .getComponent(getInterceptorName(annotation)));
            final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(interceptor
                    .getClass());
            for (final Method method : annotation.annotationType().getMethods()) {
                final String propertyName = method.getName();
                if ("pointcut".equals(propertyName)
                        || !beanDesc.hasPropertyDesc(propertyName)) {
                    continue;
                }
                final PropertyDesc propertyDesc = beanDesc
                        .getPropertyDesc(propertyName);
                if (!propertyDesc.hasWriteMethod()) {
                    continue;
                }
                propertyDesc.setValue(interceptor, MethodUtil.invoke(method,
                        annotation, null));
            }
            return interceptor;
        }
    }

}

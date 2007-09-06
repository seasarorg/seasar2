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
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * メタアノテーションで注釈されたアノテーションを読み取り{@link AspectDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class MetaAnnotationAspectDefBuilder extends AbstractAspectDefBuilder {

    /** メタアノテーションの型 */
    protected Class<? extends Annotation> metaAnnotationType;

    /** インターセプタの名前空間 */
    protected String interceptorNamespace;

    /** インターセプタの接尾辞 */
    protected String interceptorSuffix;

    /**
     * インスタンスを構築します。
     */
    public MetaAnnotationAspectDefBuilder() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param metaAnnotationType
     *            メタアノテーションの型
     * @param interceptorSuffix
     *            インターセプタの接尾辞
     */
    public MetaAnnotationAspectDefBuilder(
            final Class<? extends Annotation> metaAnnotationType,
            final String interceptorSuffix) {
        this.metaAnnotationType = metaAnnotationType;
        this.interceptorNamespace = null;
        this.interceptorSuffix = interceptorSuffix;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param metaAnnotationType
     *            メタアノテーションの型
     * @param interceptorNamespace
     *            インターセプタの名前空間
     * @param interceptorSuffix
     *            インターセプタの接尾辞
     */
    public MetaAnnotationAspectDefBuilder(
            final Class<? extends Annotation> metaAnnotationType,
            final String interceptorNamespace, final String interceptorSuffix) {
        this.metaAnnotationType = metaAnnotationType;
        this.interceptorNamespace = interceptorNamespace;
        this.interceptorSuffix = interceptorSuffix;
    }

    /**
     * メタアノテーションの型を返します。
     * 
     * @return メタアノテーションの型
     */
    public Class<? extends Annotation> getMetaAnnotationType() {
        return metaAnnotationType;
    }

    /**
     * メタアノテーションの型を設定します。
     * 
     * @param metaAnnotationType
     *            メタアノテーションの型
     */
    public void setMetaAnnotationType(
            final Class<? extends Annotation> metaAnnotationType) {
        this.metaAnnotationType = metaAnnotationType;
    }

    /**
     * インターセプタの名前空間を返します。
     * 
     * @return インターセプタの名前空間
     */
    public String getInterceptorNamespace() {
        return interceptorNamespace;
    }

    /**
     * インターセプタの名前空間を設定します。
     * 
     * @param interceptorNamespace
     *            インターセプタの名前空間
     */
    public void setInterceptorNamespace(final String interceptorNamespace) {
        this.interceptorNamespace = interceptorNamespace;
    }

    /**
     * インターセプタの接尾辞を返します。
     * 
     * @return インターセプタの接尾辞
     */
    public String getInterceptorSuffix() {
        return interceptorSuffix;
    }

    /**
     * インターセプタの接尾辞を設定します。
     * 
     * @param interceptorSuffix
     *            インターセプタの接尾辞
     */
    public void setInterceptorSuffix(final String interceptorSuffix) {
        this.interceptorSuffix = interceptorSuffix;
    }

    public void appendAspectDef(final AnnotationHandler annotationHandler,
            final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        processClass(componentDef, componentClass);
        processMethod(componentDef, componentClass);
    }

    /**
     * クラスに付けられたメタアノテーションで注釈されたアノテーションを読み取り{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param componentClass
     *            コンポーネントの型
     */
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
            final AspectDef aspectDef = new AspectDefImpl();
            if (!StringUtil.isEmpty(pointcut)) {
                aspectDef
                        .setPointcut(AspectDefFactory.createPointcut(pointcut));
            }
            aspectDef.setExpression(new ExpressionImpl(annotation));
            componentDef.addAspectDef(aspectDef);
        }
    }

    /**
     * メソッドに付けられたメタアノテーションで注釈されたアノテーションを読み取り{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param componentClass
     *            コンポーネントの型
     */
    protected void processMethod(final ComponentDef componentDef,
            final Class<?> componentClass) {
        for (final Method method : componentClass.getMethods()) {
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
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

    /**
     * アノテーションに指定されているポイントカットを返します。
     * 
     * @param annotation
     *            アノテーション
     * @return アノテーションに指定されているポイントカット
     */
    protected String getPointcut(final Annotation annotation) {
        for (final Method method : annotation.getClass().getMethods()) {
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            if ("pointcut".equals(method.getName())
                    && method.getReturnType() == String.class) {
                return String.class.cast(MethodUtil.invoke(method, annotation,
                        null));
            }
        }
        return null;
    }

    /**
     * アノテーションに指定されているインターセプタのコンポーネント名を返します。
     * 
     * @param annotation
     *            アノテーション
     * @return インターセプタのコンポーネント名
     */
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

    /**
     * 評価されると指定されたアノテーションで指定された名前のコンポーネント (インターセプタ) をコンテナから取得して返す{@link Expression}の実装です。
     * <p>
     * コンテナから取得したインターセプタのインスタンスにアノテーションの要素と名前の一致するプロパティがあれば、
     * アノテーションの要素の値がインターセプタのプロパティにコピーされます。
     * </p>
     * 
     * @author koichik
     */
    public class ExpressionImpl implements Expression {

        /** アノテーション */
        protected Annotation annotation;

        /**
         * インスタンスを構築します。
         * 
         * @param annotation
         *            アノテーション
         */
        public ExpressionImpl(final Annotation annotation) {
            this.annotation = annotation;
        }

        @SuppressWarnings("unchecked")
        public Object evaluate(final S2Container container, final Map context) {
            final MethodInterceptor interceptor = MethodInterceptor.class
                    .cast(container
                            .getComponent(getInterceptorName(annotation)));
            final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(interceptor
                    .getClass());
            for (final Method method : annotation.annotationType().getMethods()) {
                if (method.isBridge() || method.isSynthetic()) {
                    continue;
                }
                final String propertyName = method.getName();
                if ("pointcut".equals(propertyName)
                        || !beanDesc.hasPropertyDesc(propertyName)) {
                    continue;
                }
                final PropertyDesc propertyDesc = beanDesc
                        .getPropertyDesc(propertyName);
                if (!propertyDesc.isWritable()) {
                    continue;
                }
                propertyDesc.setValue(interceptor, MethodUtil.invoke(method,
                        annotation, null));
            }
            return interceptor;
        }

    }

}

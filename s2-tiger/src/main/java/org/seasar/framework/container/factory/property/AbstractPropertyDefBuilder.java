/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.factory.PropertyDefBuilder;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.StringUtil;

/**
 * Tigerアノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @param <T>
 *            アノテーションの型
 * @author koichik
 */
public abstract class AbstractPropertyDefBuilder<T extends Annotation>
        implements PropertyDefBuilder {

    /** アノテーションハンドラ */
    protected AnnotationHandler handler = AnnotationHandlerFactory
            .getAnnotationHandler();

    /**
     * インスタンスを構築します
     */
    public AbstractPropertyDefBuilder() {
    }

    public PropertyDef createPropertyDef(
            final AnnotationHandler annotationHandler, BeanDesc beanDesc,
            PropertyDesc propertyDesc) {
        if (!propertyDesc.hasWriteMethod()) {
            return null;
        }
        final Method method = propertyDesc.getWriteMethod();
        final T annotation = method.getAnnotation(getAnnotationType());
        if (annotation == null) {
            return null;
        }
        return createPropertyDef(propertyDesc.getPropertyName(),
                AccessTypeDefFactory.PROPERTY, annotation);
    }

    public PropertyDef createPropertyDef(
            final AnnotationHandler annotationHandler, BeanDesc beanDesc,
            Field field) {
        final T annotation = field.getAnnotation(getAnnotationType());
        if (annotation == null) {
            return null;
        }
        return createPropertyDef(field.getName(), AccessTypeDefFactory.FIELD,
                annotation);
    }

    /**
     * 参照するアノテーションの型を返します。
     * 
     * @return 参照するアノテーションの型
     */
    protected abstract Class<T> getAnnotationType();

    /**
     * {@link PropertyDef}を作成して返します。
     * 
     * @param name
     *            プロパティ名
     * @param accessTypeDef
     *            アクセスタイプ定義
     * @param annotation
     *            アノテーション
     * @return {@link PropertyDef}
     */
    protected abstract PropertyDef createPropertyDef(String name,
            AccessTypeDef accessTypeDef, T annotation);

    /**
     * {@link InstanceDefFactory#SINGLETON シングルトン}の{@link ComponentDef}を作成して返します。
     * 
     * @param componentClass
     *            コンポーネントのクラス
     * @return {@link ComponentDef}
     */
    protected ComponentDef createComponentDef(final Class<?> componentClass) {
        return createComponentDef(componentClass, InstanceDefFactory.SINGLETON);
    }

    /**
     * 指定のインスタンス定義で{@link ComponentDef}を作成して返します。
     * 
     * @param componentClass
     *            コンポーネントのクラス
     * @param instanceDef
     *            インスタンス定義
     * @return {@link ComponentDef}
     */
    protected ComponentDef createComponentDef(final Class<?> componentClass,
            final InstanceDef instanceDef) {
        final ComponentDef componentDef = handler.createComponentDef(
                componentClass, instanceDef);
        handler.appendDI(componentDef);
        handler.appendAspect(componentDef);
        handler.appendInterType(componentDef);
        handler.appendInitMethod(componentDef);
        handler.appendDestroyMethod(componentDef);
        return componentDef;
    }

    /**
     * {@link PropertyDef}を作成して返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param accessTypeDef
     *            アクセスタイプ定義
     * @return {@link PropertyDef}
     */
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef) {
        return createPropertyDef(propertyName, accessTypeDef, "");
    }

    /**
     * {@link PropertyDef}を作成して返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param accessTypeDef
     *            アクセスタイプ定義
     * @param expression
     *            式
     * @return {@link PropertyDef}
     */
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final String expression) {
        return createPropertyDef(propertyName, accessTypeDef, expression, null);
    }

    /**
     * {@link PropertyDef}を作成して返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param accessTypeDef
     *            アクセスタイプ定義
     * @param child
     *            {@link PropertyDefImpl#setChildComponentDef(ComponentDef)}に設定されるコンポーネント
     * @return {@link PropertyDef}
     */
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final ComponentDef child) {
        return createPropertyDef(propertyName, accessTypeDef, "", child);
    }

    /**
     * {@link PropertyDef}を作成して返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param accessTypeDef
     *            アクセスタイプ定義
     * @param expression
     *            式
     * @param child
     *            {@link PropertyDefImpl#setChildComponentDef(ComponentDef)}に設定されるコンポーネント
     * @return {@link PropertyDef}
     */
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final String expression,
            final ComponentDef child) {
        final PropertyDef propertyDef = new PropertyDefImpl(propertyName);
        propertyDef.setAccessTypeDef(accessTypeDef);
        propertyDef.setBindingTypeDef(BindingTypeDefFactory.MUST);
        if (!StringUtil.isEmpty(expression)) {
            propertyDef.setExpression(new OgnlExpression(expression));
        }
        if (child != null) {
            propertyDef.setChildComponentDef(child);
        }
        return propertyDef;
    }

}

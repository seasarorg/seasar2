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
package org.seasar.framework.container.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.aop.annotation.Interceptor;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.factory.aspect.AspectAnnotationAspectDefBuilder;
import org.seasar.framework.container.factory.aspect.EJB3AnnotationAspectDefBuilder;
import org.seasar.framework.container.factory.aspect.MetaAnnotationAspectDefBuilder;
import org.seasar.framework.container.factory.component.EJB3ComponentDefBuilder;
import org.seasar.framework.container.factory.component.PojoComponentDefBuilder;
import org.seasar.framework.container.factory.destroymethod.S2DestroyMethodDefBuilder;
import org.seasar.framework.container.factory.initmethod.EJB3InitMethodDefBuilder;
import org.seasar.framework.container.factory.initmethod.S2InitMethodDefBuilder;
import org.seasar.framework.container.factory.intertype.EJB3IntertypeDefBuilder;
import org.seasar.framework.container.factory.intertype.S2IntertypeDefBuilder;
import org.seasar.framework.container.factory.property.BindingPropertyDefBuilder;
import org.seasar.framework.container.factory.property.EJBPropertyDefBuilder;
import org.seasar.framework.container.factory.property.PersistenceContextPropertyDefBuilder;
import org.seasar.framework.container.factory.property.PersistenceUnitPropertyDefBuilder;
import org.seasar.framework.container.factory.property.ResourcePropertyDefBuilder;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * クラスに指定されたTigerアノテーションからコンポーネント定義を作成する実装クラスです。
 * 
 * @author higa
 */
public class TigerAnnotationHandler extends ConstantAnnotationHandler {

    /** イニシャライズ済みなら<code>true</code> */
    protected static boolean initialized;

    /** EJB3が有効なら<code>true</code> */
    protected static final boolean enableEJB3;
    static {
        boolean enable = false;
        try {
            Class.forName("javax.ejb.Stateless"); // geronimo-ejb_3.0_spec-1.0.jar
            Class.forName("javax.interceptor.Interceptors"); // geronimo-interceptor_3.0_spec-1.0.jar
            enable = true;
        } catch (final Throwable ignore) {
        }
        enableEJB3 = enable;
    }

    /** JPAが有効なら<code>true</code> */
    protected static final boolean enableJPA;
    static {
        boolean enable = false;
        try {
            Class.forName("javax.persistence.PersistenceContext"); // geronimo-jpa_3.0_spec-1.0.jar
            enable = true;
        } catch (final Throwable ignore) {
        }
        enableJPA = enable;
    }

    /** Common Annotationsが有効なら<code>true</code> */
    protected static final boolean enableCommonAnnotations;
    static {
        boolean enable = false;
        try {
            Class.forName("javax.annotation.Resource"); // geronimo-annotation_1.0_spec-1.0.jar
            enable = true;
        } catch (final Throwable ignore) {
        }
        enableCommonAnnotations = enable;
    }

    /** {@link ComponentDefBuilder}の配列 */
    protected static final List<ComponentDefBuilder> componentDefBuilders = Collections
            .synchronizedList(new ArrayList<ComponentDefBuilder>());

    /** {@link PropertyDefBuilder}の配列 */
    protected static final List<PropertyDefBuilder> propertyDefBuilders = Collections
            .synchronizedList(new ArrayList<PropertyDefBuilder>());

    /** {@link AspectDefBuilder}の配列 */
    protected static final List<AspectDefBuilder> aspectDefBuilders = Collections
            .synchronizedList(new ArrayList<AspectDefBuilder>());

    /** {@link IntertypeDefBuilder}の配列 */
    protected static final List<IntertypeDefBuilder> intertypeDefBuilders = Collections
            .synchronizedList(new ArrayList<IntertypeDefBuilder>());

    /** {@link InitMethodDefBuilder}の配列 */
    protected static final List<InitMethodDefBuilder> initMethodDefBuilders = Collections
            .synchronizedList(new ArrayList<InitMethodDefBuilder>());

    /** {@link DestroyMethodDefBuilder}の配列 */
    protected static final List<DestroyMethodDefBuilder> destroyMethodDefBuilders = Collections
            .synchronizedList(new ArrayList<DestroyMethodDefBuilder>());

    static {
        initialize();
    }

    /**
     * インスタンスの状態を初期化します。
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        loadDefaultComponentDefBuilder();
        loadDefaultPropertyDefBuilder();
        loadDefaultAspectDefBuilder();
        loadDefaultIntertypeDefBuilder();
        loadDefaultInitMethodDefBuilder();
        loadDefaultDestroyMethodDefBuilder();
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                TigerAnnotationHandler.dispose();
            }
        });
        initialized = true;
    }

    /**
     * インスタンスの状態を破棄します。
     */
    public static void dispose() {
        clearComponentDefBuilder();
        clearPropertyDefBuilder();
        clearAspectDefBuilder();
        clearIntertypeDefBuilder();
        clearInitMethodDefBuilder();
        clearDestroyMethodDefBuilder();
        initialized = false;
    }

    /**
     * デフォルトの{@link ComponentDefBuilder}を追加します。
     */
    public static void loadDefaultComponentDefBuilder() {
        if (enableEJB3) {
            componentDefBuilders.add(new EJB3ComponentDefBuilder());
        }
        componentDefBuilders.add(new PojoComponentDefBuilder());
    }

    /**
     * {@link ComponentDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link ComponentDefBuilder}
     */
    public static void addComponentDefBuilder(final ComponentDefBuilder builder) {
        componentDefBuilders.add(builder);
    }

    /**
     * {@link ComponentDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link ComponentDefBuilder}
     */
    public static void removeComponentDefBuilder(
            final ComponentDefBuilder builder) {
        componentDefBuilders.remove(builder);
    }

    /**
     * {@link ComponentDefBuilder}をクリアします。
     */
    public static void clearComponentDefBuilder() {
        componentDefBuilders.clear();
    }

    /**
     * デフォルトの{@link PropertyDefBuilder}を追加します。
     */
    public static void loadDefaultPropertyDefBuilder() {
        clearPropertyDefBuilder();
        propertyDefBuilders.add(new BindingPropertyDefBuilder());
        if (enableEJB3) {
            propertyDefBuilders.add(new EJBPropertyDefBuilder());
        }
        if (enableJPA) {
            propertyDefBuilders.add(new PersistenceContextPropertyDefBuilder());
            propertyDefBuilders.add(new PersistenceUnitPropertyDefBuilder());
        }
        if (enableCommonAnnotations) {
            propertyDefBuilders.add(new ResourcePropertyDefBuilder());
        }
    }

    /**
     * {@link PropertyDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link PropertyDefBuilder}
     */
    public static void addPropertyDefBuilder(final PropertyDefBuilder builder) {
        propertyDefBuilders.add(builder);
    }

    /**
     * {@link PropertyDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link PropertyDefBuilder}
     */
    public static void removePropertyDefBuilder(final PropertyDefBuilder builder) {
        propertyDefBuilders.remove(builder);
    }

    /**
     * {@link PropertyDefBuilder}をクリアします。
     */
    public static void clearPropertyDefBuilder() {
        propertyDefBuilders.clear();
    }

    /**
     * デフォルトの{@link AspectDefBuilder}を追加します。
     */
    public static void loadDefaultAspectDefBuilder() {
        if (enableEJB3) {
            aspectDefBuilders.add(new EJB3AnnotationAspectDefBuilder());
        }
        aspectDefBuilders.add(new AspectAnnotationAspectDefBuilder());
        aspectDefBuilders.add(new MetaAnnotationAspectDefBuilder(
                Interceptor.class, "Interceptor"));
    }

    /**
     * {@link AspectDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link AspectDefBuilder}
     */
    public static void addAspectDefBuilder(final AspectDefBuilder builder) {
        aspectDefBuilders.add(builder);
    }

    /**
     * {@link AspectDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link AspectDefBuilder}
     */
    public static void removeAspectDefBuilder(final AspectDefBuilder builder) {
        aspectDefBuilders.remove(builder);
    }

    /**
     * {@link AspectDefBuilder}をクリアします。
     */
    public static void clearAspectDefBuilder() {
        aspectDefBuilders.clear();
    }

    /**
     * デフォルトの{@link IntertypeDefBuilder}を追加します。
     */
    public static void loadDefaultIntertypeDefBuilder() {
        if (enableEJB3) {
            intertypeDefBuilders.add(new EJB3IntertypeDefBuilder());
        }
        intertypeDefBuilders.add(new S2IntertypeDefBuilder());
    }

    /**
     * {@link IntertypeDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link IntertypeDefBuilder}
     */
    public static void addIntertypeDefBuilder(final IntertypeDefBuilder builder) {
        intertypeDefBuilders.add(builder);
    }

    /**
     * {@link IntertypeDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link IntertypeDefBuilder}
     */
    public static void removeIntertypeDefBuilder(
            final IntertypeDefBuilder builder) {
        intertypeDefBuilders.remove(builder);
    }

    /**
     * {@link IntertypeDefBuilder}をクリアします。
     */
    public static void clearIntertypeDefBuilder() {
        intertypeDefBuilders.clear();
    }

    /**
     * デフォルトの{@link InitMethodDefBuilder}を追加します。
     */
    public static void loadDefaultInitMethodDefBuilder() {
        if (enableEJB3) {
            initMethodDefBuilders.add(new EJB3InitMethodDefBuilder());
        }
        initMethodDefBuilders.add(new S2InitMethodDefBuilder());
    }

    /**
     * {@link InitMethodDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link InitMethodDefBuilder}
     */
    public static void addInitMethodDefBuilder(
            final InitMethodDefBuilder builder) {
        initMethodDefBuilders.add(builder);
    }

    /**
     * {@link InitMethodDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link InitMethodDefBuilder}
     */
    public static void removeInitMethodDefBuilder(
            final InitMethodDefBuilder builder) {
        initMethodDefBuilders.remove(builder);
    }

    /**
     * {@link InitMethodDefBuilder}をクリアします。
     */
    public static void clearInitMethodDefBuilder() {
        initMethodDefBuilders.clear();
    }

    /**
     * デフォルトの{@link DestroyMethodDefBuilder}を追加します。
     */
    public static void loadDefaultDestroyMethodDefBuilder() {
        destroyMethodDefBuilders.add(new S2DestroyMethodDefBuilder());
    }

    /**
     * {@link DestroyMethodDefBuilder}を追加します。
     * 
     * @param builder
     *            {@link DestroyMethodDefBuilder}
     */
    public static void addDestroyMethodDefBuilder(
            final DestroyMethodDefBuilder builder) {
        destroyMethodDefBuilders.add(builder);
    }

    /**
     * {@link DestroyMethodDefBuilder}を削除します。
     * 
     * @param builder
     *            {@link DestroyMethodDefBuilder}
     */
    public static void removeDestroyMethodDefBuilder(
            final DestroyMethodDefBuilder builder) {
        destroyMethodDefBuilders.remove(builder);
    }

    /**
     * {@link DestroyMethodDefBuilder}をクリアします。
     */
    public static void clearDestroyMethodDefBuilder() {
        destroyMethodDefBuilders.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ComponentDef createComponentDef(final Class componentClass,
            final InstanceDef defaultInstanceDef,
            final AutoBindingDef defaultAutoBindingDef,
            final boolean defaultExternalBinding) {
        initialize();
        for (final ComponentDefBuilder builder : componentDefBuilders) {
            final ComponentDef componentDef = builder.createComponentDef(this,
                    componentClass, defaultInstanceDef, defaultAutoBindingDef,
                    defaultExternalBinding);
            if (componentDef != null) {
                return componentDef;
            }
        }
        return super.createComponentDef(componentClass, defaultInstanceDef,
                defaultAutoBindingDef, defaultExternalBinding);
    }

    @Override
    public PropertyDef createPropertyDef(final BeanDesc beanDesc,
            PropertyDesc propertyDesc) {
        if (propertyDesc.isWritable()) {
            for (final PropertyDefBuilder builder : propertyDefBuilders) {
                final PropertyDef propertyDef = builder.createPropertyDef(this,
                        beanDesc, propertyDesc);
                if (propertyDef != null) {
                    return propertyDef;
                }
            }
        }
        return super.createPropertyDef(beanDesc, propertyDesc);
    }

    @Override
    public PropertyDef createPropertyDef(final BeanDesc beanDesc,
            final Field field) {
        for (final PropertyDefBuilder builder : propertyDefBuilders) {
            final PropertyDef propertyDef = builder.createPropertyDef(this,
                    beanDesc, field);
            if (propertyDef != null) {
                return propertyDef;
            }
        }
        return super.createPropertyDef(beanDesc, field);
    }

    @Override
    public void appendAspect(final ComponentDef componentDef) {
        for (final AspectDefBuilder builder : aspectDefBuilders) {
            builder.appendAspectDef(this, componentDef);
        }
        super.appendAspect(componentDef);
    }

    @Override
    public void appendInterType(final ComponentDef componentDef) {
        for (final IntertypeDefBuilder builder : intertypeDefBuilders) {
            builder.appendIntertypeDef(this, componentDef);
        }
        super.appendInterType(componentDef);
    }

    @Override
    public void appendInitMethod(final ComponentDef componentDef) {
        Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        for (final InitMethodDefBuilder builder : initMethodDefBuilders) {
            builder.appendInitMethodDef(this, componentDef);
        }
        super.appendInitMethod(componentDef);
    }

    @Override
    public void appendDestroyMethod(final ComponentDef componentDef) {
        Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        for (final DestroyMethodDefBuilder builder : destroyMethodDefBuilders) {
            builder.appendDestroyMethodDef(this, componentDef);
        }
        super.appendDestroyMethod(componentDef);
    }

}

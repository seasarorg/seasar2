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
package org.seasar.framework.container.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.factory.aspect.AspectAnnotationAspectDefBuilder;
import org.seasar.framework.container.factory.aspect.EJB3AnnotationAspectDefBuilder;
import org.seasar.framework.container.factory.aspect.InterceptorAnnotationAspectDefBuilder;
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
 * @author higa
 * 
 */
public class TigerAnnotationHandler extends ConstantAnnotationHandler {

    protected static boolean initialized;

    protected static final List<ComponentDefBuilder> componentDefBuilders = Collections
            .synchronizedList(new ArrayList());

    protected static final List<PropertyDefBuilder> propertyDefBuilders = Collections
            .synchronizedList(new ArrayList());

    protected static final List<AspectDefBuilder> aspectDefBuilders = Collections
            .synchronizedList(new ArrayList());

    protected static final List<IntertypeDefBuilder> intertypeDefBuilders = Collections
            .synchronizedList(new ArrayList());

    protected static final List<InitMethodDefBuilder> initMethodDefBuilders = Collections
            .synchronizedList(new ArrayList());

    protected static final List<DestroyMethodDefBuilder> destroyMethodDefBuilders = Collections
            .synchronizedList(new ArrayList());

    public static synchronized void initialize() {
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

    public static synchronized void dispose() {
        clearComponentDefBuilder();
        clearPropertyDefBuilder();
        clearAspectDefBuilder();
        clearIntertypeDefBuilder();
        initialized = false;
    }

    public static synchronized void loadDefaultComponentDefBuilder() {
        clearComponentDefBuilder();
        componentDefBuilders.add(new EJB3ComponentDefBuilder());
        componentDefBuilders.add(new PojoComponentDefBuilder());
    }

    public static synchronized void addComponentDefBuilder(
            final ComponentDefBuilder builder) {
        componentDefBuilders.add(builder);
    }

    public static synchronized void removeComponentDefBuilder(
            final ComponentDefBuilder factory) {
        componentDefBuilders.remove(factory);
    }

    public static synchronized void clearComponentDefBuilder() {
        componentDefBuilders.clear();
    }

    public static synchronized void loadDefaultPropertyDefBuilder() {
        clearPropertyDefBuilder();
        propertyDefBuilders.add(new BindingPropertyDefBuilder());
        propertyDefBuilders.add(new EJBPropertyDefBuilder());
        propertyDefBuilders.add(new PersistenceContextPropertyDefBuilder());
        propertyDefBuilders.add(new PersistenceUnitPropertyDefBuilder());
        propertyDefBuilders.add(new ResourcePropertyDefBuilder());
    }

    public static synchronized void addPropertyDefBuilder(
            final PropertyDefBuilder builder) {
        propertyDefBuilders.add(builder);
    }

    public static synchronized void removePropertyDefBuilder(
            final PropertyDefBuilder builder) {
        propertyDefBuilders.remove(builder);
    }

    public static synchronized void clearPropertyDefBuilder() {
        propertyDefBuilders.clear();
    }

    public static synchronized void loadDefaultAspectDefBuilder() {
        aspectDefBuilders.add(new EJB3AnnotationAspectDefBuilder());
        aspectDefBuilders.add(new AspectAnnotationAspectDefBuilder());
        aspectDefBuilders.add(new InterceptorAnnotationAspectDefBuilder());
    }

    public static synchronized void addAspectDefBuilder(
            final AspectDefBuilder builder) {
        aspectDefBuilders.add(builder);
    }

    public static synchronized void removeAspectDefBuilder(
            final AspectDefBuilder builder) {
        aspectDefBuilders.remove(builder);
    }

    public static synchronized void clearAspectDefBuilder() {
        aspectDefBuilders.clear();
    }

    public static synchronized void loadDefaultIntertypeDefBuilder() {
        clearIntertypeDefBuilder();
        intertypeDefBuilders.add(new EJB3IntertypeDefBuilder());
        intertypeDefBuilders.add(new S2IntertypeDefBuilder());
    }

    public static synchronized void addIntertypeDefBuilder(
            final IntertypeDefBuilder builder) {
        intertypeDefBuilders.add(builder);
    }

    public static synchronized void removeIntertypeDefBuilder(
            final IntertypeDefBuilder builder) {
        intertypeDefBuilders.remove(builder);
    }

    public static synchronized void clearIntertypeDefBuilder() {
        intertypeDefBuilders.clear();
    }

    public static synchronized void loadDefaultInitMethodDefBuilder() {
        clearInitMethodDefBuilder();
        initMethodDefBuilders.add(new EJB3InitMethodDefBuilder());
        initMethodDefBuilders.add(new S2InitMethodDefBuilder());
    }

    public static synchronized void addInitMethodDefBuilder(
            final InitMethodDefBuilder builder) {
        initMethodDefBuilders.add(builder);
    }

    public static synchronized void removeInitMethodDefBuilder(
            final InitMethodDefBuilder factory) {
        initMethodDefBuilders.remove(factory);
    }

    public static synchronized void clearInitMethodDefBuilder() {
        initMethodDefBuilders.clear();
    }

    public static synchronized void loadDefaultDestroyMethodDefBuilder() {
        clearDestroyMethodDefBuilder();
        destroyMethodDefBuilders.add(new S2DestroyMethodDefBuilder());
    }

    public static synchronized void addDestroyMethodDefBuilder(
            final DestroyMethodDefBuilder builder) {
        destroyMethodDefBuilders.add(builder);
    }

    public static synchronized void removeDestroyMethodDefBuilder(
            final DestroyMethodDefBuilder factory) {
        destroyMethodDefBuilders.remove(factory);
    }

    public static synchronized void clearDestroyMethodDefBuilder() {
        destroyMethodDefBuilders.clear();
    }

    public ComponentDef createComponentDef(final Class componentClass,
            final InstanceDef defaultInstanceDef,
            final AutoBindingDef defaultAutoBindingDef,
            final boolean defaultExternalBinding) {
        if (!initialized) {
            initialize();
        }
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

    public PropertyDef createPropertyDef(final BeanDesc beanDesc,
            PropertyDesc propertyDesc) {
        if (propertyDesc.hasWriteMethod()) {
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

    public void appendAspect(final ComponentDef componentDef) {
        for (final AspectDefBuilder builder : aspectDefBuilders) {
            builder.appendAspectDef(this, componentDef);
        }
        super.appendAspect(componentDef);
    }

    public void appendInterType(final ComponentDef componentDef) {
        for (final IntertypeDefBuilder builder : intertypeDefBuilders) {
            builder.appendIntertypeDef(this, componentDef);
        }
        super.appendInterType(componentDef);
    }

    public void appendInitMethod(final ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        for (final InitMethodDefBuilder builder : initMethodDefBuilders) {
            builder.appendInitMethodDef(this, componentDef);
        }
        super.appendInitMethod(componentDef);
    }

    public void appendDestroyMethod(final ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        for (final DestroyMethodDefBuilder builder : destroyMethodDefBuilders) {
            builder.appendDestroyMethodDef(this, componentDef);
        }
        super.appendDestroyMethod(componentDef);
    }

}

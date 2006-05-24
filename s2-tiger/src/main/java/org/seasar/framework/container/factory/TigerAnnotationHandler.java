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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PersistenceContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalDestroyMethodAnnotationRuntimeException;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.container.annotation.tiger.InterType;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.InterTypeDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.ejb.AroundInvokeSupportInterceptor;
import org.seasar.framework.ejb.EJB3BusinessMethodDesc;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3InterceptorDesc;
import org.seasar.framework.ejb.EJB3InterceptorSupportInterType;
import org.seasar.framework.ejb.EJB3InterceptorSupportInterceptor;
import org.seasar.framework.jpa.TxScopedEntityManagerProxy;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class TigerAnnotationHandler extends ConstantAnnotationHandler {

    private static final Map<TransactionAttributeType, String> TX_ATTRS = new HashMap<TransactionAttributeType, String>();
    static {
        TX_ATTRS.put(TransactionAttributeType.MANDATORY, "ejb3tx.mandatoryTx");
        TX_ATTRS.put(TransactionAttributeType.REQUIRED, "ejb3tx.requiredTx");
        TX_ATTRS.put(TransactionAttributeType.REQUIRES_NEW,
                "ejb3tx.requiresNewTx");
        TX_ATTRS.put(TransactionAttributeType.NOT_SUPPORTED,
                "ejb3tx.notSupportedTx");
        TX_ATTRS.put(TransactionAttributeType.NEVER, "ejb3tx.neverTx");
    }

    public ComponentDef createComponentDef(Class componentClass,
            InstanceDef defaultInstanceDef, AutoBindingDef defaultAutoBindingDef) {

        String name = null;
        InstanceDef instanceDef = null;
        AutoBindingDef autoBindingDef = null;
        Class<?> clazz = componentClass;
        EJB3Desc ejb3Desc = EJB3Desc.getEJB3Desc(clazz);
        if (ejb3Desc.isStateless()) {
            name = ejb3Desc.getName();
            instanceDef = defaultInstanceDef != null ? defaultInstanceDef
                    : InstanceDefFactory.PROTOTYPE;
            autoBindingDef = defaultAutoBindingDef != null ? defaultAutoBindingDef
                    : AutoBindingDefFactory.SEMIAUTO;
        } else if (ejb3Desc.isStateful()) {
            name = ejb3Desc.getName();
            instanceDef = defaultInstanceDef != null ? defaultInstanceDef
                    : InstanceDefFactory.PROTOTYPE;
            autoBindingDef = defaultAutoBindingDef != null ? defaultAutoBindingDef
                    : AutoBindingDefFactory.SEMIAUTO;
        }
        Component component = clazz.getAnnotation(Component.class);
        if (component != null) {
            if (!StringUtil.isEmpty(component.name())) {
                name = component.name();
            }
            InstanceType instanceType = component.instance();
            if (instanceType != null) {
                instanceDef = getInstanceDef(instanceType.getName(),
                        instanceDef);
            }
            AutoBindingType autoBindingType = component.autoBinding();
            if (autoBindingType != null) {
                autoBindingDef = getAutoBindingDef(autoBindingType.getName());
            }
        }
        if (!ejb3Desc.isEJB3() && component == null) {
            return super.createComponentDef(componentClass, defaultInstanceDef,
                    defaultAutoBindingDef);
        }
        return createComponentDef(componentClass, name, instanceDef,
                autoBindingDef);
    }

    public PropertyDef createPropertyDef(BeanDesc beanDesc,
            PropertyDesc propertyDesc) {

        String propName = propertyDesc.getPropertyName();
        if (propertyDesc.hasWriteMethod()) {
            Method method = propertyDesc.getWriteMethod();
            Binding binding = method.getAnnotation(Binding.class);
            if (binding != null) {
                String bindingTypeName = binding.bindingType().getName();
                String expression = binding.value();
                return createPropertyDef(propName, expression, bindingTypeName,
                        AccessTypeDef.PROPERTY_NAME);
            }

            EJB ejb = method.getAnnotation(EJB.class);
            if (ejb != null) {
                return createPropertyDef(propName, getExpression(ejb), null,
                        AccessTypeDef.PROPERTY_NAME);
            }

            PersistenceContext persistenceContext = method
                    .getAnnotation(PersistenceContext.class);
            if (persistenceContext != null) {
                return createPropertyDef(beanDesc, propName,
                        AccessTypeDefFactory.PROPERTY, persistenceContext);
            }
        }
        return super.createPropertyDef(beanDesc, propertyDesc);
    }

    protected String getExpression(EJB ejb) {
        String name = ejb.beanName();
        if (StringUtil.isEmpty(name)) {
            name = ejb.name();
        }
        return name.replace('/', '.');
    }

    public PropertyDef createPropertyDef(BeanDesc beanDesc, Field field) {
        EJB ejb = field.getAnnotation(EJB.class);
        if (ejb != null) {
            return createPropertyDef(field.getName(), getExpression(ejb), null,
                    AccessTypeDef.FIELD_NAME);
        }

        PersistenceContext persistenceContext = field
                .getAnnotation(PersistenceContext.class);
        if (persistenceContext != null) {
            return createPropertyDef(beanDesc, field.getName(),
                    AccessTypeDefFactory.FIELD, persistenceContext);
        }

        return super.createPropertyDef(beanDesc, field);
    }

    public void appendAspect(ComponentDef componentDef) {
        Class<?> clazz = componentDef.getComponentClass();
        Aspect aspect = clazz.getAnnotation(Aspect.class);
        if (aspect != null) {
            String interceptor = aspect.value();
            String pointcut = aspect.pointcut();
            appendAspect(componentDef, interceptor, pointcut);
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Aspect mAspect = method.getAnnotation(Aspect.class);
            if (mAspect != null) {
                String interceptor = mAspect.value();
                appendAspect(componentDef, interceptor, method);
            }
        }

        appendEJB3Aspect(componentDef);
        super.appendAspect(componentDef);
    }

    public void appendInterType(ComponentDef componentDef) {
        Class<?> clazz = componentDef.getComponentClass();
        InterType interType = clazz.getAnnotation(InterType.class);
        if (interType != null) {
            for (String interTypeName : interType.value()) {
                appendInterType(componentDef, interTypeName);
            }
        }
        appendEJB3InterType(componentDef);
        super.appendInterType(componentDef);
    }

    public void appendInitMethod(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        Method[] methods = componentClass.getMethods();
        for (Method method : methods) {
            InitMethod initMethod = method.getAnnotation(InitMethod.class);
            if (initMethod == null) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                throw new IllegalInitMethodAnnotationRuntimeException(
                        componentClass, method.getName());
            }
            if (!isInitMethodRegisterable(componentDef, method.getName())) {
                continue;
            }
            appendInitMethod(componentDef, method.getName());
        }
        appendEJB3InitMethod(componentDef);
        super.appendInitMethod(componentDef);
    }

    public void appendDestroyMethod(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        Method[] methods = componentClass.getMethods();
        for (Method method : methods) {
            DestroyMethod destroyMethod = method
                    .getAnnotation(DestroyMethod.class);
            if (destroyMethod == null) {
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                throw new IllegalDestroyMethodAnnotationRuntimeException(
                        componentClass, method.getName());
            }
            if (!isDestroyMethodRegisterable(componentDef, method.getName())) {
                continue;
            }
            appendDestroyMethod(componentDef, method.getName());
        }
        super.appendDestroyMethod(componentDef);
    }

    protected void appendEJB3Aspect(final ComponentDef componentDef) {
        final EJB3Desc ejb3desc = EJB3Desc.getEJB3Desc(componentDef
                .getComponentClass());
        if (!ejb3desc.isEJB3()) {
            return;
        }
        appendEJB3TxAspect(componentDef, ejb3desc);
        appendEJB3InterceptorsAspect(componentDef, ejb3desc);
        appendEJB3AroundInvokeAspect(componentDef, ejb3desc);
    }

    protected void appendEJB3TxAspect(final ComponentDef componentDef,
            final EJB3Desc ejb3desc) {
        if (!ejb3desc.isCMT()) {
            return;
        }

        for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                .getBusinessMethods()) {
            final String txInterceptor = TX_ATTRS.get(methodDesc
                    .getTransactionAttributeType());
            if (txInterceptor == null) {
                continue;
            }
            appendAspect(componentDef, txInterceptor, methodDesc.getMethod());
        }
    }

    protected void appendEJB3InterceptorsAspect(
            final ComponentDef componentDef, final EJB3Desc ejb3desc) {
        for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                .getBusinessMethods()) {
            for (final EJB3InterceptorDesc interceptorDesc : methodDesc
                    .getInterceptors()) {
                for (final Method interceptorMethod : interceptorDesc
                        .getInterceptorMethods()) {
                    final EJB3InterceptorSupportInterceptor interceptor = new EJB3InterceptorSupportInterceptor(
                            interceptorDesc.getInterceptorClass(),
                            interceptorMethod);
                    final AspectDef aspectDef = AspectDefFactory
                            .createAspectDef(interceptor, new PointcutImpl(
                                    methodDesc.getMethod()));
                    componentDef.addAspectDef(aspectDef);
                }
            }
        }
    }

    protected void appendEJB3AroundInvokeAspect(
            final ComponentDef componentDef, final EJB3Desc ejb3desc) {
        for (final Method aroundInvokeMethod : ejb3desc
                .getAroundInvokeMethods()) {
            final MethodInterceptor interceptor = new AroundInvokeSupportInterceptor(
                    aroundInvokeMethod);
            for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                    .getBusinessMethods()) {
                final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                        interceptor, new PointcutImpl(methodDesc.getMethod()));
                componentDef.addAspectDef(aspectDef);
            }
        }
    }

    protected void appendEJB3InterType(final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        final EJB3Desc ejb3desc = EJB3Desc.getEJB3Desc(componentClass);
        if (!ejb3desc.isEJB3()) {
            return;
        }

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
        final EJB3InterceptorSupportInterType interType = new EJB3InterceptorSupportInterType();
        for (final Class<?> interceptorClass : interceptorClasses) {
            interType.addInterceptor(interceptorClass);
            PropertyDefImpl propDef = new PropertyDefImpl(
                    EJB3InterceptorSupportInterType
                            .getFieldName(interceptorClass));
            final ComponentDef interceptorCd = createComponentDef(
                    interceptorClass, null);
            appendDI(interceptorCd);
            appendAspect(interceptorCd);
            appendInterType(interceptorCd);
            propDef.setChildComponentDef(interceptorCd);
            componentDef.addPropertyDef(propDef);
        }
        componentDef.addInterTypeDef(new InterTypeDefImpl(interType));
    }

    protected void appendEJB3InitMethod(final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        final EJB3Desc ejb3desc = EJB3Desc.getEJB3Desc(componentClass);
        if (!ejb3desc.isEJB3()) {
            return;
        }

        for (final Method method : ejb3desc.getPostConstructMethods()) {
            if (!isInitMethodRegisterable(componentDef, method.getName())) {
                continue;
            }
            appendInitMethod(componentDef, method);
        }
    }

    protected PropertyDef createPropertyDef(final BeanDesc beanDesc,
            final String propertyName, final AccessTypeDef accessTypeDef,
            final PersistenceContext persistenceContext) {
        final String name = persistenceContext.name();
        if (!StringUtil.isEmpty(name)) {
            return createPropertyDef(propertyName, name,
                    BindingTypeDef.MUST_NAME, accessTypeDef.getName());
        }

        final String unitName = persistenceContext.unitName();
        if (StringUtil.isEmpty(unitName)) {
            return createPropertyDef(propertyName, null,
                    BindingTypeDef.MUST_NAME, accessTypeDef.getName());
        }

        final ComponentDef componentDef = new ComponentDefImpl(
                TxScopedEntityManagerProxy.class);
        componentDef.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        componentDef.addPropertyDef(createPropertyDef("entityManagerFactory",
                unitName, BindingTypeDef.MUST_NAME, AccessTypeDef.PROPERTY_NAME));
        final PropertyDef propertyDef = new PropertyDefImpl(propertyName);
        propertyDef.setAccessTypeDef(accessTypeDef);
        propertyDef.setChildComponentDef(componentDef);
        return propertyDef;
    }
}

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
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.annotation.tiger.Aspect;
import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.container.annotation.tiger.InterType;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.ejb.AroundInvokeSupportInterceptor;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.SEJBException;
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
            InstanceDef defaultInstanceDef) {

        String name = null;
        InstanceDef instanceDef = null;
        AutoBindingDef autoBindingDef = null;
        Class<?> clazz = componentClass;
        Stateless stateless = clazz.getAnnotation(Stateless.class);
        if (stateless != null) {
            name = stateless.name();
            instanceDef = defaultInstanceDef != null ? defaultInstanceDef
                    : InstanceDefFactory.PROTOTYPE;
            autoBindingDef = AutoBindingDefFactory.NONE;
        }
        Stateful stateful = clazz.getAnnotation(Stateful.class);
        if (stateful != null) {
            name = stateful.name();
            instanceDef = defaultInstanceDef != null ? defaultInstanceDef
                    : InstanceDefFactory.PROTOTYPE;
            autoBindingDef = AutoBindingDefFactory.NONE;
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
        if (stateless == null && stateful == null && component == null) {
            return super.createComponentDef(componentClass, defaultInstanceDef);
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
                return createPropertyDef(propName, expression, bindingTypeName);
            }
            EJB ejb = method.getAnnotation(EJB.class);
            if (ejb != null) {
                return createPropertyDef(propName, getExpression(ejb), null);
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
            return createPropertyDef(field.getName(), getExpression(ejb), null);
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

    protected void appendEJB3Aspect(final ComponentDef componentDef) {
        final EJB3Desc ejb3desc = EJB3Desc.getEJB3Desc(componentDef
                .getComponentClass());
        if (!ejb3desc.isEJB3()) {
            return;
        }
        appendEJB3TxnAspect(componentDef, ejb3desc);
        appendEJB3AroundInvokeAspect(componentDef, ejb3desc);
    }

    protected void appendEJB3TxnAspect(final ComponentDef componentDef,
            final EJB3Desc ejb3desc) {
        if (!ejb3desc.isCMT()) {
            return;
        }

        final TransactionAttribute classAttr = ejb3desc.getBeanClass()
                .getAnnotation(TransactionAttribute.class);
        final TransactionAttributeType classType = (classAttr != null) ? classAttr
                .value()
                : null;

        for (final Method method : ejb3desc.getBusinessMethods()) {
            final TransactionAttribute methodAttr = method
                    .getAnnotation(TransactionAttribute.class);
            final TransactionAttributeType methodType = (methodAttr != null) ? methodAttr
                    .value()
                    : classType;
            if (methodType == null) {
                continue;
            }

            final String txInterceptor = TX_ATTRS.get(methodType);
            if (txInterceptor == null) {
                continue;
            }
            appendAspect(componentDef, txInterceptor, method);
        }
    }

    protected void appendEJB3AroundInvokeAspect(
            final ComponentDef componentDef, final EJB3Desc ejb3desc) {
        final Method aroundInvokeMethod = ejb3desc.getAroundInvokeMethod();
        if (aroundInvokeMethod == null) {
            return;
        }

        aroundInvokeMethod.setAccessible(true);
        final MethodInterceptor interceptor = new AroundInvokeSupportInterceptor(
                aroundInvokeMethod);
        for (final Method method : ejb3desc.getBusinessMethods()) {
            final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                    interceptor, new PointcutImpl(method));
            componentDef.addAspectDef(aspectDef);
        }
    }

    protected void appendEJB3InitMethod(final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        final EJB3Desc ejb3desc = EJB3Desc.getEJB3Desc(componentClass);
        if (!ejb3desc.isEJB3()) {
            return;
        }

        for (final Method method : ejb3desc.getAllMethods()) {
            final PostConstruct annotation = method
                    .getAnnotation(PostConstruct.class);
            if (annotation == null) {
                continue;
            }
            final int modifiers = method.getModifiers();
            if (method.getParameterTypes().length != 0
                    || Modifier.isStatic(modifiers)
                    || Modifier.isFinal(modifiers)) {
                throw new SEJBException("ESSR0409", "PostConstruct",
                        componentClass.getName(), method.getName());
            }
            if (!isInitMethodRegisterable(componentDef, method.getName())) {
                continue;
            }
            appendInitMethod(componentDef, method);
        }
    }
}

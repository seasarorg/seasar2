/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;

import javax.ejb.Stateless;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
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
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class TigerAnnotationHandler extends ConstantAnnotationHandler {

	public ComponentDef createComponentDef(Class componentClass,
			InstanceDef defaultInstanceDef) {
		
		String name = null;
		InstanceDef instanceDef = null;
		AutoBindingDef autoBindingDef = null;
		Class<?> clazz = componentClass;
        Stateless stateless = clazz.getAnnotation(Stateless.class);
        if (stateless != null) {
        	name = stateless.name();
        	instanceDef = defaultInstanceDef != null ? defaultInstanceDef : InstanceDefFactory.PROTOTYPE;
        	autoBindingDef = AutoBindingDefFactory.NONE;
        }
		Component component = clazz.getAnnotation(Component.class);
		if (component != null) {
			if (!StringUtil.isEmpty(component.name())) {
				name = component.name();
			}
			InstanceType instanceType = component.instance();
			if (instanceType != null) {
				instanceDef = getInstanceDef(instanceType.getName(), instanceDef);
			}
			AutoBindingType autoBindingType = component.autoBinding();
			if (autoBindingType != null) {
				autoBindingDef = getAutoBindingDef(autoBindingType.getName());
			}
		} else if (stateless == null) {
			return super.createComponentDef(componentClass, defaultInstanceDef);
		}
		return createComponentDef(componentClass, name,
				instanceDef, autoBindingDef);
	}
	
	public PropertyDef createPropertyDef(BeanDesc beanDesc,
			PropertyDesc propertyDesc) {

		if (!propertyDesc.hasWriteMethod()) {
			return super.createPropertyDef(beanDesc, propertyDesc);
		}
		Method method = propertyDesc.getWriteMethod();
		Binding binding = method.getAnnotation(Binding.class);
		if (binding != null) {
			String bindingTypeName = binding.bindingType().getName();
			String expression = binding.value();
			String propName = propertyDesc.getPropertyName();
			return createPropertyDef(propName, expression, bindingTypeName);
		}
		return super.createPropertyDef(beanDesc, propertyDesc);
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
		super.appendInitMethod(componentDef);
	}
}

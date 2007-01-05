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
package org.seasar.framework.container.assembler;

import java.lang.reflect.Field;

import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.StringUtil;

/**
 * バインディングタイプ定義の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractBindingTypeDef implements BindingTypeDef {

    private String name;

    protected AbstractBindingTypeDef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BindingTypeDef)) {
            return false;
        }
        BindingTypeDef other = (BindingTypeDef) o;
        return name == null ? other.getName() == null : name.equals(other
                .getName());
    }

    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    public void bind(ComponentDef componentDef, PropertyDef propertyDef,
            PropertyDesc propertyDesc, Object component) {
        if (propertyDef != null && propertyDef.isValueGettable()) {
            if (propertyDesc != null && propertyDesc.hasWriteMethod()) {
                bindManual(componentDef, propertyDef, propertyDesc, component);
            }
        } else {
            if (propertyDesc != null && propertyDesc.hasWriteMethod()) {
                doBind(componentDef, propertyDesc, component);
            }
        }
    }

    public void bind(ComponentDef componentDef, PropertyDef propertyDef,
            Field field, Object component) {
        if (propertyDef != null && propertyDef.isValueGettable()) {
            if (field != null) {
                bindManual(componentDef, propertyDef, field, component);
            }
        } else {
            if (propertyDef != null && field != null) {
                doBind(componentDef, field, component);
            }
        }
    }

    protected void bindManual(ComponentDef componentDef,
            PropertyDef propertyDef, Field field, Object component) {

        Object value = getValue(componentDef, propertyDef, component);
        setValue(componentDef, field, component, value);
    }

    protected void bindManual(ComponentDef componentDef,
            PropertyDef propertyDef, PropertyDesc propertyDesc, Object component) {

        Object value = getValue(componentDef, propertyDef, component);
        setValue(componentDef, propertyDesc, component, value);
    }

    protected boolean bindAuto(ComponentDef componentDef, Field field,
            Object component) {

        S2Container container = componentDef.getContainer();
        String propName = field.getName();
        Class propType = field.getType();
        if (container.hasComponentDef(propType)) {
            ComponentDef cd = container.getComponentDef(propType);
            if (cd.getComponentName() != null
                    && (cd.getComponentName().equalsIgnoreCase(propName) || StringUtil
                            .endsWithIgnoreCase(cd.getComponentName(),
                                    ContainerConstants.PACKAGE_SEP + propName))) {
                Object value = getValue(componentDef, propType, component,
                        propName);
                setValue(componentDef, field, component, value);
                return true;
            }
        }
        if (container.hasComponentDef(propName)) {
            Object value = getValue(componentDef, propName, component, propName);
            if (propType.isInstance(value)) {
                setValue(componentDef, field, component, value);
                return true;
            }
        }
        if (BindingUtil.isAutoBindable(propType)) {
            if (container.hasComponentDef(propType)) {
                Object value = getValue(componentDef, propType, component,
                        propName);
                setValue(componentDef, field, component, value);
                return true;
            }
            if (propType.isAssignableFrom(ComponentDef.class)) {
                setValue(componentDef, field, component, componentDef);
                return true;
            }
        }
        if (BindingUtil.isAutoBindableArray(propType)) {
            Class clazz = propType.getComponentType();
            Object[] values = container.findAllComponents(clazz);
            if (values.length > 0) {
                setValue(componentDef, field, component, values);
                return true;
            }
        }
        return false;
    }

    protected boolean bindAuto(ComponentDef componentDef,
            PropertyDesc propertyDesc, Object component) {

        S2Container container = componentDef.getContainer();
        String propName = propertyDesc.getPropertyName();
        Class propType = propertyDesc.getPropertyType();
        if (container.hasComponentDef(propType)) {
            ComponentDef cd = container.getComponentDef(propType);
            if (cd.getComponentName() != null
                    && (cd.getComponentName().equalsIgnoreCase(propName) || StringUtil
                            .endsWithIgnoreCase(cd.getComponentName(),
                                    ContainerConstants.PACKAGE_SEP + propName))) {
                Object value = getValue(componentDef, propType, component,
                        propName);
                setValue(componentDef, propertyDesc, component, value);
                return true;
            }
        }
        if (container.hasComponentDef(propName)) {
            Object value = getValue(componentDef, propName, component, propName);
            if (propType.isInstance(value)) {
                setValue(componentDef, propertyDesc, component, value);
                return true;
            }
        }
        if (BindingUtil.isAutoBindable(propType)) {
            if (container.hasComponentDef(propType)) {
                Object value = getValue(componentDef, propType, component,
                        propName);
                setValue(componentDef, propertyDesc, component, value);
                return true;
            }
            if (propType.isAssignableFrom(ComponentDef.class)) {
                setValue(componentDef, propertyDesc, component, componentDef);
                return true;
            }
        }
        if (BindingUtil.isAutoBindableArray(propType)) {
            Class clazz = propType.getComponentType();
            Object[] values = container.findAllComponents(clazz);
            if (values.length > 0) {
                setValue(componentDef, propertyDesc, component, values);
                return true;
            }
        }
        return false;
    }

    protected Object getValue(ComponentDef componentDef,
            PropertyDef propertyDef, Object component) {
        try {
            return propertyDef.getValue();
        } catch (RuntimeException cause) {
            throw new IllegalPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), propertyDef
                    .getPropertyName(), cause);
        }

    }

    protected Object getValue(ComponentDef componentDef, Object key,
            Object component, String propertyName) {
        try {
            return componentDef.getContainer().getComponent(key);
        } catch (RuntimeException cause) {
            throw new IllegalPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), propertyName,
                    cause);
        }

    }

    protected void setValue(ComponentDef componentDef,
            PropertyDesc propertyDesc, Object component, Object value)
            throws IllegalPropertyRuntimeException {

        if (value == null) {
            return;
        }
        try {
            propertyDesc.setValue(component, value);
        } catch (NumberFormatException ex) {
            throw new IllegalPropertyRuntimeException(componentDef
                    .getComponentClass(), propertyDesc.getPropertyName(), ex);
        }
    }

    protected void setValue(ComponentDef componentDef, Field field,
            Object component, Object value)
            throws IllegalPropertyRuntimeException {

        if (value == null) {
            return;
        }
        try {
            FieldUtil.set(field, component, value);
        } catch (NumberFormatException ex) {
            throw new IllegalPropertyRuntimeException(componentDef
                    .getComponentClass(), field.getName(), ex);
        }
    }

    protected abstract void doBind(ComponentDef componentDef,
            PropertyDesc propertyDesc, Object component);

    protected abstract void doBind(ComponentDef componentDef, Field field,
            Object component);
}
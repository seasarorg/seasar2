/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

    /**
     * {@link AbstractBindingTypeDef}を作成します。
     * 
     * @param name
     */
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
            if (propertyDesc != null && propertyDesc.isWritable()) {
                bindManual(componentDef, propertyDef, propertyDesc, component);
            }
        } else {
            if (propertyDesc != null && propertyDesc.isWritable()) {
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

    /**
     * 明示的な設定にもとづいてオブジェクトを結び付けます。
     * 
     * @param componentDef
     * @param propertyDef
     * @param field
     * @param component
     * @see #getValue(ComponentDef, PropertyDef, Object)
     * @see #setValue(ComponentDef, Field, Object, Object)
     */
    protected void bindManual(ComponentDef componentDef,
            PropertyDef propertyDef, Field field, Object component) {

        Object value = getValue(componentDef, propertyDef, component);
        setValue(componentDef, field, component, value);
    }

    /**
     * 明示的な設定にもとづいてオブジェクトを結び付けます。
     * 
     * @param componentDef
     * @param propertyDef
     * @param propertyDesc
     * @param component
     * @see #getValue(ComponentDef, PropertyDef, Object)
     * @see #setValue(ComponentDef, PropertyDesc, Object, Object)
     */
    protected void bindManual(ComponentDef componentDef,
            PropertyDef propertyDef, PropertyDesc propertyDesc, Object component) {

        Object value = getValue(componentDef, propertyDef, component);
        setValue(componentDef, propertyDesc, component, value);
    }

    /**
     * 自動的にオブジェクトを結び付けます。
     * 
     * @param componentDef
     * @param field
     * @param component
     * @return オブジェクトを結び付けたかどうか
     * @see #getValue(ComponentDef, Object, Object, String)
     * @see #setValue(ComponentDef, Field, Object, Object)
     */
    protected boolean bindAuto(ComponentDef componentDef, Field field,
            Object component) {

        S2Container container = componentDef.getContainer();
        String propName = field.getName();
        Class propType = field.getType();
        if (container.hasComponentDef(propType)) {
            ComponentDef cd = container.getComponentDef(propType);
            if (isAutoBindable(propName, propType, cd)) {
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

    /**
     * 自動的にオブジェクトを結び付けます。
     * 
     * @param componentDef
     * @param propertyDesc
     * @param component
     * @return オブジェクトを結び付けたかどうか
     */
    protected boolean bindAuto(ComponentDef componentDef,
            PropertyDesc propertyDesc, Object component) {

        S2Container container = componentDef.getContainer();
        String propName = propertyDesc.getPropertyName();
        Class propType = propertyDesc.getPropertyType();
        if (container.hasComponentDef(propType)) {
            ComponentDef cd = container.getComponentDef(propType);
            if (isAutoBindable(propName, propType, cd)) {
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

    /**
     * プロパティにコンポーネントを自動バインディング可能なら<code>true</code>を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param propertyType
     *            プロパティの型
     * @param cd
     *            コンポーネント定義
     * @return プロパティにコンポーネントを自動バインディング可能なら<code>true</code>
     */
    protected boolean isAutoBindable(final String propertyName,
            final Class propertyType, final ComponentDef cd) {
        return cd.getComponentName() != null
                && (cd.getComponentName().equalsIgnoreCase(propertyName) || StringUtil
                        .endsWithIgnoreCase(cd.getComponentName(),
                                ContainerConstants.PACKAGE_SEP + propertyName));
    }

    /**
     * プロパティの値を返します。
     * 
     * @param componentDef
     * @param propertyDef
     * @param component
     * @return プロパティの値
     * @throws IllegalPropertyRuntimeException
     *             {@link RuntimeException}が発生した場合
     */
    protected Object getValue(ComponentDef componentDef,
            PropertyDef propertyDef, Object component)
            throws IllegalPropertyRuntimeException {
        try {
            return propertyDef.getValue();
        } catch (RuntimeException cause) {
            throw new IllegalPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), propertyDef
                    .getPropertyName(), cause);
        }

    }

    /**
     * コンポーネントを返します。
     * 
     * @param componentDef
     * @param key
     * @param component
     * @param propertyName
     * @return コンポーネント
     * @throws IllegalPropertyRuntimeException
     *             {@link RuntimeException}が発生した場合
     */
    protected Object getValue(ComponentDef componentDef, Object key,
            Object component, String propertyName)
            throws IllegalPropertyRuntimeException {
        try {
            return componentDef.getContainer().getComponent(key);
        } catch (RuntimeException cause) {
            throw new IllegalPropertyRuntimeException(BindingUtil
                    .getComponentClass(componentDef, component), propertyName,
                    cause);
        }

    }

    /**
     * プロパティに値を設定します。
     * 
     * @param componentDef
     * @param propertyDesc
     * @param component
     * @param value
     * @throws IllegalPropertyRuntimeException
     *             {@link NumberFormatException}が発生した場合
     */
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

    /**
     * {@link Field}に値を設定します。
     * 
     * @param componentDef
     * @param field
     * @param component
     * @param value
     * @throws IllegalPropertyRuntimeException
     *             {@link NumberFormatException}が発生した場合
     */
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

    /**
     * オブジェクトを結びつけるためのメソッドです。
     * 
     * @param componentDef
     * @param propertyDesc
     * @param component
     */
    protected abstract void doBind(ComponentDef componentDef,
            PropertyDesc propertyDesc, Object component);

    /**
     * オブジェクトを結びつけるためのメソッドです。
     * 
     * @param componentDef
     * @param field
     * @param component
     */
    protected abstract void doBind(ComponentDef componentDef, Field field,
            Object component);
}
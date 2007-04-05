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
package org.seasar.extension.persistence.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.persistence.ColumnMetaFactory;
import org.seasar.extension.persistence.PropertyMeta;
import org.seasar.extension.persistence.PropertyMetaFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

/**
 * @author higa
 * 
 */
public class PropertyMetaFactoryImpl implements PropertyMetaFactory {

    private ColumnMetaFactory columnMetaFactory;

    /**
     * <code>ColumnMetaFactory</code>を返します。
     * 
     * @return columnMetaFactory.
     */
    public ColumnMetaFactory getColumnMetaFactory() {
        return columnMetaFactory;
    }

    /**
     * <code>ColumnMetaFactory</code>を設定します。
     * 
     * @param columnMetaFactory
     */
    @Binding(bindingType = BindingType.MUST)
    public void setColumnMetaFactory(ColumnMetaFactory columnMetaFactory) {
        this.columnMetaFactory = columnMetaFactory;
    }

    public PropertyMeta createPropertyMeta(Field field) {
        PropertyMeta propertyMeta = new PropertyMeta();
        doName(propertyMeta, field);
        doColumnMeta(propertyMeta, field);
        doId(propertyMeta, field);
        doTransient(propertyMeta, field);
        doVersion(propertyMeta, field);
        doCustomize(propertyMeta, field);
        return propertyMeta;
    }

    protected void doName(PropertyMeta propertyMeta, Field field) {
        propertyMeta.setName(fromFieldNameToPropertyName(field.getName()));
    }

    protected String fromFieldNameToPropertyName(String fieldName) {
        return fieldName;
    }

    protected void doColumnMeta(PropertyMeta propertyMeta, Field field) {
        propertyMeta.setColumnMeta(columnMetaFactory.createColumnMeta(field));
    }

    protected void doId(PropertyMeta propertyMeta, Field field) {
        propertyMeta.setId(isId(field));
    }

    protected boolean isId(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    protected void doTransient(PropertyMeta propertyMeta, Field field) {
        propertyMeta.setTransient(isTransient(field));
    }

    protected boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null
                || Modifier.isTransient(field.getModifiers());
    }

    protected void doVersion(PropertyMeta propertyMeta, Field field) {
        propertyMeta.setVersion(isVersion(field));
    }

    protected boolean isVersion(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

    @SuppressWarnings("unused")
    protected void doCustomize(PropertyMeta propertyMeta, Field field) {
    }
}

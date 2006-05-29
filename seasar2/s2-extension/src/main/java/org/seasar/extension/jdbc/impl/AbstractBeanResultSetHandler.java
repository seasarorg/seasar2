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
package org.seasar.extension.jdbc.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

public abstract class AbstractBeanResultSetHandler implements ResultSetHandler {

    private Class beanClass_;

    private BeanDesc beanDesc_;

    public AbstractBeanResultSetHandler(Class beanClass) {
        setBeanClass(beanClass);

    }

    public Class getBeanClass() {
        return beanClass_;
    }

    public void setBeanClass(Class beanClass) {
        beanClass_ = beanClass;
        beanDesc_ = BeanDescFactory.getBeanDesc(beanClass);
    }

    protected PropertyType[] createPropertyTypes(ResultSetMetaData rsmd)
            throws SQLException {

        int count = rsmd.getColumnCount();
        PropertyType[] propertyTypes = new PropertyType[count];
        for (int i = 0; i < count; ++i) {
            String columnName = rsmd.getColumnLabel(i + 1);
            String propertyName = StringUtil.replace(columnName, "_", "");
            PropertyDesc propertyDesc = beanDesc_.getPropertyDesc(propertyName);
            ValueType valueType = ValueTypes.getValueType(propertyDesc
                    .getPropertyType());
            propertyTypes[i] = new PropertyTypeImpl(propertyDesc, valueType,
                    columnName);
        }
        return propertyTypes;
    }

    protected Object createRow(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {

        Object row = ClassUtil.newInstance(beanClass_);
        for (int i = 0; i < propertyTypes.length; ++i) {
            PropertyType pt = propertyTypes[i];
            ValueType valueType = pt.getValueType();
            Object value = valueType.getValue(rs, i + 1);
            PropertyDesc pd = pt.getPropertyDesc();
            pd.setValue(row, value);
        }
        return row;
    }
}
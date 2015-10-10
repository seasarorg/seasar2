/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.handler;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.impl.PropertyTypeImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * JavaBeans用の {@link ResultSetHandler}の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractBeanResultSetHandler implements ResultSetHandler {

    /**
     * Beanクラスです。
     */
    protected Class<?> beanClass;

    /**
     * Bean記述です。
     */
    protected BeanDesc beanDesc;

    /**
     * データベースの方言です。
     */
    protected DbmsDialect dialect;

    /**
     * 永続化層の規約です。
     */
    protected PersistenceConvention persistenceConvention;

    /**
     * SQLです。
     */
    protected String sql;

    /**
     * {@link AbstractBeanResultSetHandler}を作成します。
     * 
     * @param beanClass
     *            Beanクラス
     * @param dialect
     *            データベースの方言です。
     * @param persistenceConvention
     *            永続化層の規約
     * @param sql
     *            SQL
     */
    public AbstractBeanResultSetHandler(Class<?> beanClass,
            DbmsDialect dialect, PersistenceConvention persistenceConvention,
            String sql) {
        this.beanClass = beanClass;
        beanDesc = BeanDescFactory.getBeanDesc(beanClass);
        this.dialect = dialect;
        this.persistenceConvention = persistenceConvention;
        this.sql = sql;
    }

    /**
     * プロパティの型の配列を作成します。
     * 
     * @param rsmd
     *            結果セットメタデータ
     * @return プロパティタイプの配列
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected PropertyType[] createPropertyTypes(ResultSetMetaData rsmd)
            throws SQLException {

        int count = rsmd.getColumnCount();
        PropertyType[] propertyTypes = new PropertyType[count];
        CaseInsensitiveMap pdWithColumn = createPropertyDescMapWithColumn();
        for (int i = 0; i < count; ++i) {
            String columnName = rsmd.getColumnLabel(i + 1);
            PropertyDesc propertyDesc = (PropertyDesc) pdWithColumn
                    .get(columnName);
            if (propertyDesc == null) {
                String propertyName = persistenceConvention
                        .fromColumnNameToPropertyName(columnName);
                if (!beanDesc.hasPropertyDesc(propertyName)) {
                    continue;
                }
                propertyDesc = beanDesc.getPropertyDesc(propertyName);
            }
            ValueType valueType = getValueType(propertyDesc);
            propertyTypes[i] = new PropertyTypeImpl(propertyDesc, valueType,
                    columnName);
        }
        return propertyTypes;
    }

    /**
     * {@link Column}アノテーションのname属性をもつ {@link PropertyDesc}の {@link Map}を作成します。
     * 
     * @return {@link Column}アノテーションのname属性をもつ {@link PropertyDesc}の {@link Map}
     */
    protected CaseInsensitiveMap createPropertyDescMapWithColumn() {
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            Field field = pd.getField();
            if (field == null) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                String name = column.name();
                if (StringUtil.isEmpty(name)) {
                    continue;
                }
                map.put(name, pd);
            }
        }
        return map;
    }

    /**
     * 行を作成します。
     * 
     * @param rs
     *            結果セット
     * @param propertyTypes
     *            プロパティの型の配列
     * @return 行
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Object createRow(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {

        Object row = ClassUtil.newInstance(beanClass);
        for (int i = 0; i < propertyTypes.length; ++i) {
            PropertyType pt = propertyTypes[i];
            if (pt == null) {
                continue;
            }
            ValueType valueType = pt.getValueType();
            Object value = valueType.getValue(rs, i + 1);
            PropertyDesc pd = pt.getPropertyDesc();
            pd.setValue(row, value);
        }
        return row;
    }

    /**
     * 値タイプを返します。
     * 
     * @param propertyDesc
     *            プロパティ記述
     * @return 値タイプ
     */
    protected ValueType getValueType(PropertyDesc propertyDesc) {
        Field field = propertyDesc.getField();
        if (field == null) {
            return dialect.getValueType(propertyDesc.getPropertyType(), false,
                    null);
        }
        boolean lob = field.isAnnotationPresent(Lob.class);
        Temporal temporal = field.getAnnotation(Temporal.class);
        TemporalType temporalType = temporal != null ? temporal.value() : null;
        return dialect.getValueType(propertyDesc.getPropertyType(), lob,
                temporalType);
    }
}
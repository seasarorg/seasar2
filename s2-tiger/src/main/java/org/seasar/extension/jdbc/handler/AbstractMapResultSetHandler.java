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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.impl.PropertyTypeImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ModifierUtil;

/**
 * マップ用の {@link ResultSetHandler}の抽象クラスです。
 * 
 * @author higa
 * 
 */

public abstract class AbstractMapResultSetHandler implements ResultSetHandler {

    /**
     * デフォルトのマップの実装クラスです。
     */
    @SuppressWarnings("unchecked")
    protected static final Class<? extends Map> DEFAULT_MAP_CLASS = BeanMap.class;

    /**
     * マップクラスです。
     */
    @SuppressWarnings("unchecked")
    protected Class<? extends Map> mapClass = DEFAULT_MAP_CLASS;

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
     * {@link AbstractMapResultSetHandler}を作成します。
     * 
     * @param mapClass
     *            マップクラス
     * @param dialect
     *            データベースの方言
     * @param persistenceConvention
     *            永続化層の規約
     * @param sql
     *            SQL
     */
    @SuppressWarnings("unchecked")
    public AbstractMapResultSetHandler(Class<? extends Map> mapClass,
            DbmsDialect dialect, PersistenceConvention persistenceConvention,
            String sql) {
        if (!ModifierUtil.isAbstract(mapClass)) {
            this.mapClass = mapClass;
        }
        this.dialect = dialect;
        this.persistenceConvention = persistenceConvention;
        this.sql = sql;
    }

    /**
     * {@link PropertyType}の配列を作成します。
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
        for (int i = 0; i < count; ++i) {
            String columnName = rsmd.getColumnLabel(i + 1);
            String propertyName = persistenceConvention
                    .fromColumnNameToPropertyName(columnName);
            Class<?> clazz = ValueTypes.getType(rsmd.getColumnType(i + 1));
            ValueType valueType = dialect.getValueType(clazz, false, null);
            propertyTypes[i] = new PropertyTypeImpl(propertyName, valueType,
                    columnName);
        }
        return propertyTypes;
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
    @SuppressWarnings("unchecked")
    protected Object createRow(ResultSet rs, PropertyType[] propertyTypes)
            throws SQLException {

        Map row = (Map) ClassUtil.newInstance(mapClass);
        for (int i = 0; i < propertyTypes.length; ++i) {
            PropertyType pt = propertyTypes[i];
            if (pt.getColumnName().equalsIgnoreCase("rownumber_")) {
                continue;
            }
            Object value = pt.getValueType().getValue(rs, i + 1);
            row.put(pt.getPropertyName(), value);
        }
        return row;
    }

}
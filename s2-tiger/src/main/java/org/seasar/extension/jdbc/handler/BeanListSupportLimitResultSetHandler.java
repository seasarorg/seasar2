/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;

/**
 * <p>
 * Beanのリストを返す {@link ResultSetHandler}です。
 * </p>
 * <p>
 * フェッチする件数を制限することができます。
 * </p>
 * 
 * @author higa
 * 
 */
public class BeanListSupportLimitResultSetHandler extends
        AbstractBeanResultSetHandler {

    /**
     * リミットです。
     */
    protected int limit;

    /**
     * {@link BeanListSupportLimitResultSetHandler}を作成します。
     * 
     * @param beanClass
     *            Beanクラス
     * @param dialect
     *            データベースの方言
     * @param sql
     *            SQL
     * @param limit
     *            リミット
     */
    public BeanListSupportLimitResultSetHandler(Class<?> beanClass,
            DbmsDialect dialect, String sql, int limit) {
        super(beanClass, dialect, sql);
        setLimit(limit);
    }

    /**
     * リミットを返します。
     * 
     * @return リミット
     */
    public int getLimit() {
        return limit;
    }

    /**
     * リミットを設定します。
     * 
     * @param limit
     *            リミット
     * @throws IllegalArgumentException
     *             リミットがゼロ以下の場合。
     */
    public void setLimit(int limit) throws IllegalArgumentException {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be greater than 0.");
        }
        this.limit = limit;
    }

    public Object handle(ResultSet rs) throws SQLException {
        PropertyType[] propertyTypes = createPropertyTypes(rs.getMetaData());
        List<Object> list = new ArrayList<Object>(100);
        for (int i = 0; i < limit && rs.next(); i++) {
            Object row = createRow(rs, propertyTypes);
            list.add(row);
        }
        return list;
    }
}
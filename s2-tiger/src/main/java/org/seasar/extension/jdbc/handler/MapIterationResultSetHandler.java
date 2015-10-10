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
import java.sql.SQLException;
import java.util.Map;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * 問い合わせ結果を<code>Map</code>にして反復する{@link ResultSetHandler}です。
 * 
 * @author koichik
 */
@SuppressWarnings("unchecked")
public class MapIterationResultSetHandler extends AbstractMapResultSetHandler {

    /** リミット */
    protected int limit;

    /** 反復コールバック */
    protected IterationCallback callback;

    /**
     * {@link MapListSupportLimitResultSetHandler}を作成します。
     * 
     * @param mapClass
     *            マップクラス
     * @param dialect
     *            データベースの方言
     * @param peristenceConvention
     *            永続化層の規約
     * @param sql
     *            SQL
     * @param limit
     *            リミット
     * @param callback
     *            反復コールバック
     */
    public MapIterationResultSetHandler(Class<? extends Map> mapClass,
            DbmsDialect dialect, PersistenceConvention peristenceConvention,
            String sql, int limit, final IterationCallback callback) {
        super(mapClass, dialect, peristenceConvention, sql);
        this.limit = limit;
        this.callback = callback;
    }

    public Object handle(ResultSet rs) throws SQLException {
        final PropertyType[] propertyTypes = createPropertyTypes(rs
                .getMetaData());
        final IterationContext iterationContext = new IterationContext();
        Object result = null;
        for (int i = 0; (limit <= 0 || i < limit) && rs.next(); i++) {
            final Object entity = createRow(rs, propertyTypes);
            result = callback.iterate(entity, iterationContext);
            if (iterationContext.isExit()) {
                return result;
            }
        }
        return result;
    }

}

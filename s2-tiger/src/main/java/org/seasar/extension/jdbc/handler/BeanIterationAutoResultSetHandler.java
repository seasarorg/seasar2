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

import org.seasar.extension.jdbc.EntityMapper;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;

/**
 * 問い合わせ結果を反復するSQL自動生成用の{@link ResultSetHandler}です。
 * 
 * @author koichik
 */
@SuppressWarnings("unchecked")
public class BeanIterationAutoResultSetHandler extends
        AbstractBeanAutoResultSetHandler {

    /** リミット */
    protected int limit;

    /** 反復コールバック */
    protected IterationCallback callback;

    /**
     * {@link BeanIterationAutoResultSetHandler}を作成します。
     * 
     * @param valueTypes
     *            値タイプの配列
     * @param entityMapper
     *            エンティティマッパー
     * @param sql
     *            SQL
     * @param limit
     *            リミット
     * @param callback
     *            反復コールバック
     */
    public BeanIterationAutoResultSetHandler(final ValueType[] valueTypes,
            final EntityMapper entityMapper, final String sql, final int limit,
            final IterationCallback callback) {
        super(valueTypes, entityMapper, sql);
        this.limit = limit;
        this.callback = callback;
    }

    public Object handle(final ResultSet rs) throws SQLException {
        final MappingContext mappingContext = new MappingContext();
        final IterationContext iterationContext = new IterationContext();
        Object entity = null;
        Object previousKey = null;
        Object result = null;
        for (int i = 0; (limit <= 0 || i < limit) && rs.next(); i++) {
            final Object[] values = getValues(rs);
            final Object key = entityMapper.getKey(values);
            if (key != null && key.equals(previousKey)) {
                entityMapper.map(values, mappingContext);
            } else {
                if (entity != null) {
                    result = callback.iterate(entity, iterationContext);
                    if (iterationContext.isExit()) {
                        return result;
                    }
                    mappingContext.clear();
                }
                entity = entityMapper.map(values, mappingContext);
                previousKey = key;
            }
        }
        if (entity != null) {
            result = callback.iterate(entity, iterationContext);
        }
        return result;
    }

}

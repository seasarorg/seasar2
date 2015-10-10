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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.EntityMapper;
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;

/**
 * Beanのリストを返すSQL自動生成用の {@link ResultSetHandler}です。
 * 
 * @author higa
 */
public class BeanListAutoResultSetHandler extends
        AbstractBeanAutoResultSetHandler {

    /**
     * リミットです。
     */
    protected int limit;

    /**
     * {@link BeanListAutoResultSetHandler}を作成します。
     * 
     * @param valueTypes
     *            値タイプの配列
     * @param entityMapper
     *            エンティティマッパー
     * @param sql
     *            SQL
     */
    public BeanListAutoResultSetHandler(ValueType[] valueTypes,
            EntityMapper entityMapper, String sql) {
        this(valueTypes, entityMapper, sql, 0);
    }

    /**
     * {@link BeanListAutoResultSetHandler}を作成します。
     * 
     * @param valueTypes
     *            値タイプの配列
     * @param entityMapper
     *            エンティティマッパー
     * @param sql
     *            SQL
     * @param limit
     *            リミット
     */
    public BeanListAutoResultSetHandler(ValueType[] valueTypes,
            EntityMapper entityMapper, String sql, int limit) {
        super(valueTypes, entityMapper, sql);
        this.limit = limit;
    }

    public Object handle(ResultSet rs) throws SQLException {
        List<Object> list = new ArrayList<Object>(100);
        MappingContext mappingContext = new MappingContext();
        for (int i = 0; (limit <= 0 || i < limit) && rs.next(); i++) {
            Object entity = createEntity(rs, mappingContext);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

}

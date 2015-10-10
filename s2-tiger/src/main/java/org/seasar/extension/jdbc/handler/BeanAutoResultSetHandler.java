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
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

/**
 * Beanを返すSQL自動生成用の {@link ResultSetHandler}です。
 * 
 * @author higa
 * 
 */
public class BeanAutoResultSetHandler extends AbstractBeanAutoResultSetHandler {

    /**
     * {@link BeanAutoResultSetHandler}を作成します。
     * 
     * @param valueTypes
     *            値タイプの配列
     * @param entityMapper
     *            エンティティマッパー
     * @param sql
     *            SQL
     */
    public BeanAutoResultSetHandler(ValueType[] valueTypes,
            EntityMapper entityMapper, String sql) {
        super(valueTypes, entityMapper, sql);
    }

    public Object handle(ResultSet rs) throws SQLException {
        MappingContext mappingContext = new MappingContext();
        Object ret = null;
        if (rs.next()) {
            ret = createEntity(rs, mappingContext);
            while (rs.next()) {
                if (createEntity(rs, mappingContext) != null) {
                    throw new SNonUniqueResultException(sql);
                }
            }
        }
        return ret;
    }

}

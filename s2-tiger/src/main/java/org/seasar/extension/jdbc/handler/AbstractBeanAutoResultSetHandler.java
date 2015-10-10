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

/**
 * SQL自動生成で使うJavaBeans用の {@link ResultSetHandler}の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractBeanAutoResultSetHandler implements
        ResultSetHandler {

    /**
     * 値タイプの配列です。
     */
    protected ValueType[] valueTypes;

    /**
     * エンティティマッパーです。
     */
    protected EntityMapper entityMapper;

    /**
     * SQLです。
     */
    protected String sql;

    /**
     * {@link AbstractBeanAutoResultSetHandler}を作成します。
     * 
     * @param valueTypes
     *            値タイプの配列
     * @param entityMapper
     *            エンティティマッパー
     * @param sql
     *            SQL
     */
    public AbstractBeanAutoResultSetHandler(ValueType[] valueTypes,
            EntityMapper entityMapper, String sql) {
        this.valueTypes = valueTypes;
        this.entityMapper = entityMapper;
        this.sql = sql;
    }

    /**
     * エンティティを作成します。
     * 
     * @param rs
     *            結果セット
     * @param mappingContext
     *            マッピングコンテキスト
     * @return エンティティ
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Object createEntity(ResultSet rs, MappingContext mappingContext)
            throws SQLException {
        Object[] values = getValues(rs);
        return entityMapper.map(values, mappingContext);
    }

    /**
     * 結果セットの現在の行から値の配列を作成します。
     * 
     * @param rs
     *            結果セット
     * @return 値の配列
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Object[] getValues(ResultSet rs) throws SQLException {
        Object[] values = new Object[valueTypes.length];
        for (int i = 0; i < valueTypes.length; ++i) {
            values[i] = valueTypes[i].getValue(rs, i + 1);
        }
        return values;
    }

}

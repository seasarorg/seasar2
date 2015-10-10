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
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * マップを返す {@link ResultSetHandler} の実装クラスです。
 * 
 * @author higa
 * 
 */
public class MapResultSetHandler extends AbstractMapResultSetHandler {

    /**
     * {@link MapResultSetHandler}を作成します。
     * 
     * @param mapClass
     *            マップクラス
     * @param dialect
     *            データベースの方言
     * @param peristenceConvention
     *            永続化層の規約
     * @param sql
     *            SQL
     */
    @SuppressWarnings("unchecked")
    public MapResultSetHandler(Class<? extends Map> mapClass,
            DbmsDialect dialect, PersistenceConvention peristenceConvention,
            String sql) {
        super(mapClass, dialect, peristenceConvention, sql);
    }

    public Object handle(ResultSet rs) throws SQLException,
            SNonUniqueResultException {
        Object ret = null;
        if (rs.next()) {
            ret = createRow(rs, createPropertyTypes(rs.getMetaData()));
            if (rs.next()) {
                throw new SNonUniqueResultException(sql);
            }
        }
        return ret;
    }
}
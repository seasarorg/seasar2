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

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;

/**
 * オブジェクトのリストを返す {@link ResultSetHandler}です。
 * 
 * @author higa
 */
public class ObjectListResultSetHandler implements ResultSetHandler {

    /**
     * 値タイプです。
     */
    protected ValueType valueType;

    /**
     * リミットです。
     */
    protected int limit;

    /**
     * {@link ObjectListResultSetHandler}を作成します。
     * 
     * @param valueType
     *            値タイプ
     */
    public ObjectListResultSetHandler(ValueType valueType) {
        this(valueType, 0);
    }

    /**
     * {@link ObjectListResultSetHandler}を作成します。
     * 
     * @param valueType
     *            値タイプ
     * @param limit
     *            リミット
     */
    public ObjectListResultSetHandler(ValueType valueType, int limit) {
        this.valueType = valueType;
        this.limit = limit;
    }

    public Object handle(ResultSet rs) throws SQLException {
        List<Object> ret = new ArrayList<Object>(100);
        for (int i = 0; (limit <= 0 || i < limit) && rs.next(); i++) {
            ret.add(valueType.getValue(rs, 1));
        }
        return ret;
    }

}

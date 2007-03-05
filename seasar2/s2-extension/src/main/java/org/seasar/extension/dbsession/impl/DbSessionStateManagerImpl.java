/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbsession.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.seasar.extension.dbsession.DbSessionState;
import org.seasar.extension.dbsession.DbSessionStateManager;
import org.seasar.extension.jdbc.impl.BasicBatchHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.impl.MapListResultSetHandler;
import org.seasar.extension.serializer.Serializer;

/**
 * @author higa
 * 
 */
public class DbSessionStateManagerImpl implements DbSessionStateManager {

    private static final String SELECT_SQL = "select name, value from s2session where session_id = ?";

    private static final String INSERT_SQL = "insert into s2session values(?, ?, ?, ?)";

    private static final String UPDATE_SQL = "update s2session set value = ?, last_access = ? where session_id = ? and name = ?";

    private static final String DELETE_SQL = "delete from s2session where session_id = ? and name = ?";

    private static final String DELETE_ALL_SQL = "delete from s2session where session_id = ?";

    private DataSource dataSource;

    /**
     * 
     */
    public DbSessionStateManagerImpl() {
    }

    /**
     * @param dataSource
     */
    public DbSessionStateManagerImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    /**
     * @param dataSource
     *            The dataSource to set.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DbSessionState loadState(String sessionId) {
        BasicSelectHandler handler = new BasicSelectHandler(dataSource,
                SELECT_SQL, new MapListResultSetHandler());
        List result = (List) handler.execute(new String[] { sessionId });
        Map binaryData = new HashMap(result.size());
        for (int i = 0; i < result.size(); i++) {
            Map m = (Map) result.get(i);
            binaryData.put(m.get("name"), m.get("value"));
        }
        return new DbSessionState(binaryData);
    }

    public void removeState(String sessionId) {
        BasicUpdateHandler handler = new BasicUpdateHandler(dataSource,
                DELETE_ALL_SQL);
        handler.execute(new Object[] { sessionId });

    }

    public void updateState(String sessionId, DbSessionState sessionState) {
        List insertedData = new ArrayList();
        List updatedData = new ArrayList();
        List deletedData = new ArrayList();
        Timestamp lastAccess = new Timestamp(new Date().getTime());
        Enumeration e = sessionState.getAccessedAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (sessionState.needInsert(name)) {
                byte[] value = Serializer.fromObjectToBinary(sessionState
                        .getAttribute(name));
                insertedData.add(new Object[] { sessionId, name, value,
                        lastAccess });
            } else if (sessionState.needUpdate(name)) {
                byte[] value = Serializer.fromObjectToBinary(sessionState
                        .getAttribute(name));
                updatedData.add(new Object[] { value, lastAccess, sessionId,
                        name });
            } else if (sessionState.needDelete(name)) {
                deletedData.add(new Object[] { sessionId, name });
            }
        }
        executeBatch(INSERT_SQL, insertedData);
        executeBatch(UPDATE_SQL, updatedData);
        executeBatch(DELETE_SQL, deletedData);
    }

    protected void executeBatch(String sql, List data) {
        if (data.size() == 0) {
            return;
        }
        BasicBatchHandler handler = new BasicBatchHandler(dataSource, sql);
        handler.execute(data);
    }
}

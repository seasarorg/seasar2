/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.extension.httpsession.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.seasar.extension.httpsession.SessionState;
import org.seasar.extension.httpsession.SessionStateManager;
import org.seasar.extension.jdbc.impl.BasicBatchHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.impl.MapListResultSetHandler;
import org.seasar.framework.util.SerializeUtil;

/**
 * データベース用の {@link SessionStateManager}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class DbSessionStateManagerImpl implements SessionStateManager {

    private static final String SELECT_SQL = "select name, value, last_access from s2session where session_id = ?";

    private static final String INSERT_SQL = "insert into s2session values(?, ?, ?, ?)";

    private static final String UPDATE_SQL = "update s2session set value = ?, last_access = ? where session_id = ? and name = ?";

    private static final String DELETE_SQL = "delete from s2session where session_id = ? and name = ?";

    private static final String DELETE_ALL_SQL = "delete from s2session where session_id = ?";

    private DataSource dataSource;

    private boolean batchUpdateDisabled;

    /**
     * {@link DbSessionStateManagerImpl}を作成します。
     */
    public DbSessionStateManagerImpl() {
    }

    /**
     * {@link DbSessionStateManagerImpl}を作成します。
     * 
     * @param dataSource
     *            データソース
     */
    public DbSessionStateManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * データソースを設定します。
     * 
     * @param dataSource
     *            データソース
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * バッチ更新を無効にする場合<code>true</code>を設定します。
     * 
     * @param batchUpdateDisabled
     *            バッチ更新を無効にする場合<code>true</code>
     */
    public void setBatchUpdateDisabled(boolean batchUpdateDisabled) {
        this.batchUpdateDisabled = batchUpdateDisabled;
    }

    public SessionState loadState(String sessionId) {
        BasicSelectHandler handler = new BasicSelectHandler(dataSource,
                SELECT_SQL, new MapListResultSetHandler());
        List result = (List) handler.execute(new String[] { sessionId });
        Map binaryData = new HashMap(result.size());
        long lastAccessedTime = System.currentTimeMillis();
        for (int i = 0; i < result.size(); i++) {
            Map m = (Map) result.get(i);
            binaryData.put(m.get("name"), m.get("value"));
            final Timestamp lastAccess = (Timestamp) m.get("lastAccess");
            if (lastAccess != null) {
                lastAccessedTime = lastAccess.getTime();
            }
        }
        return new SessionState(binaryData, lastAccessedTime);
    }

    public void removeState(String sessionId) {
        BasicUpdateHandler handler = new BasicUpdateHandler(dataSource,
                DELETE_ALL_SQL);
        handler.execute(new Object[] { sessionId });

    }

    public void updateState(String sessionId, SessionState sessionState) {
        List insertedData = new ArrayList();
        List updatedData = new ArrayList();
        List deletedData = new ArrayList();
        Timestamp lastAccess = new Timestamp(new Date().getTime());
        Enumeration e = sessionState.getAccessedAttributeNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (sessionState.needInsert(name)) {
                byte[] value = SerializeUtil.fromObjectToBinary(sessionState
                        .getAttribute(name));
                insertedData.add(new Object[] { sessionId, name, value,
                        lastAccess });
            } else if (sessionState.needUpdate(name)) {
                byte[] value = SerializeUtil.fromObjectToBinary(sessionState
                        .getAttribute(name));
                updatedData.add(new Object[] { value, lastAccess, sessionId,
                        name });
            } else if (sessionState.needDelete(name)) {
                deletedData.add(new Object[] { sessionId, name });
            }
        }
        execute(INSERT_SQL, insertedData);
        execute(UPDATE_SQL, updatedData);
        execute(DELETE_SQL, deletedData);
        sessionState.persisted();
    }

    /**
     * データを更新します。
     * 
     * @param sql
     *            SQL
     * @param data
     *            データ
     */
    protected void execute(String sql, List data) {
        if (data.size() == 0) {
            return;
        }
        if (batchUpdateDisabled) {
            executeUpdate(sql, data);
        } else {
            executeBatch(sql, data);
        }
    }

    /**
     * バッチ更新を行ないます。
     * 
     * @param sql
     *            SQL
     * @param data
     *            データ
     */
    protected void executeBatch(String sql, List data) {
        BasicBatchHandler handler = new BasicBatchHandler(dataSource, sql);
        handler.execute(data);
    }

    /**
     * 1行ずつ更新処理を行ないます。
     * 
     * @param sql
     *            SQL
     * @param data
     *            データ
     */
    protected void executeUpdate(String sql, List data) {
        BasicUpdateHandler handler = new BasicUpdateHandler(dataSource, sql);
        for (Iterator i = data.iterator(); i.hasNext();) {
            handler.execute((Object[]) i.next());
        }
    }
}

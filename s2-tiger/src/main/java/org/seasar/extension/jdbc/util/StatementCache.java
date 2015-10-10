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
package org.seasar.extension.jdbc.util;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.seasar.framework.util.LruHashMap;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link Statement}をキャッシュするクラスです。
 * 
 * @author higa
 * 
 */
public class StatementCache extends LruHashMap {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * {@link StatementCache}を作成します。
     * 
     * @param limitSize
     *            制限数
     */
    public StatementCache(int limitSize) {
        super(limitSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry entry) {
        if (super.removeEldestEntry(entry)) {
            Statement stmt = (Statement) entry.getValue();
            StatementUtil.close(stmt);
            return true;
        }
        return false;
    }

    /**
     * キャッシュしている {@link Statement}を破棄します。
     * 
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    @SuppressWarnings("unchecked")
    public void destroy() throws SQLException {
        SQLException e = null;
        for (Iterator i = values().iterator(); i.hasNext();) {
            Statement stmt = (Statement) i.next();
            try {
                stmt.close();
            } catch (SQLException ex) {
                e = ex;
            }
        }
        clear();
        if (e != null) {
            throw e;
        }
    }
}

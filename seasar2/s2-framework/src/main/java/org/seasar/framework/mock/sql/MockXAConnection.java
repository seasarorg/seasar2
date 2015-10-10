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
package org.seasar.framework.mock.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * {@link XAConnection}用のモッククラスです。
 * 
 * @author koichik
 */
public class MockXAConnection implements XAConnection {

    private boolean closed = false;

    public XAResource getXAResource() throws SQLException {
        return null;
    }

    public void addConnectionEventListener(
            final ConnectionEventListener listener) {
    }

    public void close() throws SQLException {
        closed = true;
    }

    /**
     * この<code>XAConnection</code>が閉じている場合は<code>true</code>を返します。
     * 
     * @return この<code>XAConnection</code>が閉じている場合は<code>true</code>
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * この<code>XAConnection</code>が閉じているかどうかを設定します。
     * 
     * @param closed
     *            この<code>XAConnection</code>が閉じているかどうか
     */
    public void setClosed(final boolean closed) {
        this.closed = closed;
    }

    public Connection getConnection() throws SQLException {
        return new MockConnection();
    }

    public void removeConnectionEventListener(
            final ConnectionEventListener listener) {
    }

}

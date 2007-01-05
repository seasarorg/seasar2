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
package org.seasar.extension.dbcp.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.ConnectionEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

public class XAConnectionImpl implements XAConnection {

    private Connection connection_;

    private XAResource xaResource_;

    private List _listeners = new ArrayList();

    public XAConnectionImpl(Connection connection) {
        connection_ = connection;
        xaResource_ = new DBXAResourceImpl(connection);
    }

    public XAResource getXAResource() {
        return xaResource_;
    }

    public Connection getConnection() throws SQLException {
        return connection_;
    }

    public void close() throws SQLException {
        if (connection_ == null) {
            return;
        }
        if (!connection_.isClosed()) {
            connection_.close();
        }
        connection_ = null;
    }

    public synchronized void addConnectionEventListener(
            final ConnectionEventListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeConnectionEventListener(
            final ConnectionEventListener listener) {
        _listeners.remove(listener);
    }
}
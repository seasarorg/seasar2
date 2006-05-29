/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.seasar.extension.dbcp.DBXAResource;
import org.seasar.extension.jta.xa.DefaultXAResource;
import org.seasar.framework.exception.SXAException;

public class DBXAResourceImpl extends DefaultXAResource implements DBXAResource {

    private Connection connection_;

    public DBXAResourceImpl(Connection connection) {
        connection_ = connection;
    }

    public Connection getConnection() {
        return connection_;
    }

    protected void doBegin(Xid xid) throws XAException {
        try {
            if (connection_.getAutoCommit()) {
                connection_.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            throw new SXAException(ex);
        }
    }

    protected void doCommit(Xid xid, boolean onePhase) throws XAException {
        try {
            connection_.commit();
            connection_.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new SXAException(ex);
        }
    }

    protected int doPrepare(Xid xid) throws XAException {
        try {
            if (connection_.isClosed()) {
                return XA_RDONLY;
            }
            return XA_OK;
        } catch (SQLException ex) {
            throw new SXAException(ex);
        }
    }

    protected void doRollback(Xid xid) throws XAException {
        try {
            connection_.rollback();
            connection_.setAutoCommit(true);
        } catch (SQLException ex) {
            throw new SXAException(ex);
        }
    }
}
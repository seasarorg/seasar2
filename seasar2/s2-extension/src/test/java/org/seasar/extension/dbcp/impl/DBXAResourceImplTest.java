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
package org.seasar.extension.dbcp.impl;

import java.sql.Connection;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.seasar.extension.jta.xa.XidImpl;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class DBXAResourceImplTest extends S2TestCase {

    private static final String PATH = "connection2.dicon";

    private XADataSource xads2_;

    private XAConnection xacon_;

    private Connection con_;

    private XAResource xares_;

    /**
     * @throws Exception
     */
    public void testDoBegen() throws Exception {
        xares_.start(new XidImpl(), XAResource.TMNOFLAGS);
        assertEquals("1", false, con_.getAutoCommit());
    }

    /**
     * @throws Exception
     */
    public void testDoCommit() throws Exception {
        Xid xid = new XidImpl();
        xares_.start(xid, XAResource.TMNOFLAGS);
        xares_.end(xid, XAResource.TMSUCCESS);
        xares_.commit(xid, true);
        assertEquals("1", true, con_.getAutoCommit());
    }

    /**
     * @throws Exception
     */
    public void testDoRollback() throws Exception {
        Xid xid = new XidImpl();
        xares_.start(xid, XAResource.TMNOFLAGS);
        xares_.end(xid, XAResource.TMFAIL);
        xares_.rollback(xid);
        assertEquals("1", true, con_.getAutoCommit());
    }

    protected void setUp() throws Exception {
        include(PATH);
        xads2_ = (XADataSource) getComponent("xads");
        xacon_ = xads2_.getXAConnection();
        con_ = xacon_.getConnection();
        xares_ = new DBXAResourceImpl(con_);
    }

    protected void tearDown() throws Exception {
        xacon_.close();
    }
}
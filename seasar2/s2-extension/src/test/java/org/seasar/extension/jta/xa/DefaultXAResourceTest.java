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
package org.seasar.extension.jta.xa;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.jta.xa.DefaultXAResource;
import org.seasar.extension.jta.xa.XidImpl;

public class DefaultXAResourceTest extends TestCase {

    public DefaultXAResourceTest(String name) {
        super(name);
    }

    public void testStart() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        assertEquals("1", xid, xaRes.getCurrentXid());
        assertEquals("2", DefaultXAResource.RS_ACTIVE, xaRes.getStatus());
        try {
            xaRes.start(xid, XAResource.TMNOFLAGS);
            fail("3");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        DefaultXAResource xaRes2 = new DefaultXAResource();
        Xid xid2 = new XidImpl();
        xaRes2.start(xid2, XAResource.TMNOFLAGS);
        xaRes2.end(xid2, XAResource.TMSUSPEND);
        xaRes2.start(xid2, XAResource.TMRESUME);
        assertEquals("4", xid2, xaRes2.getCurrentXid());
        assertEquals("5", DefaultXAResource.RS_ACTIVE, xaRes2.getStatus());
        try {
            xaRes2.start(xid, XAResource.TMRESUME);
            fail("6");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        DefaultXAResource xaRes3 = new DefaultXAResource();
        try {
            xaRes3.start(xid, XAResource.TMJOIN);
            fail("7");
        } catch (XAException ex) {
          System.out.println(ex);
        }

        DefaultXAResource xaRes4 = new DefaultXAResource();
        try {
            xaRes4.start(xid, -1);
            fail("8");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    public void testEnd() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUSPEND);
        assertEquals("1", DefaultXAResource.RS_SUSPENDED, xaRes.getStatus());

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMFAIL);
        assertEquals("2", DefaultXAResource.RS_FAIL, xaRes.getStatus());

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        assertEquals("3", DefaultXAResource.RS_SUCCESS, xaRes.getStatus());

        xaRes.forget(xid);
        try {
            xaRes.end(xid, XAResource.TMSUCCESS);
            fail("4");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        xaRes.start(xid, XAResource.TMNOFLAGS);
        try {
            xaRes.end(xid, -1);
            fail("5");
        } catch (XAException ex) {
            System.out.println(ex);
        }
        Xid xid2 = new XidImpl();
        try {
            xaRes.end(xid2, XAResource.TMSUCCESS);
            fail("6");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    public void testPrepare() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        assertEquals("1", XAResource.XA_OK, xaRes.prepare(xid));
        assertEquals("2", DefaultXAResource.RS_PREPARED, xaRes.getStatus());

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMFAIL);
        try {
            xaRes.prepare(xid);
            fail("4");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        try {
            xaRes.prepare(new XidImpl());
            fail("5");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    public void testCommit() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        xaRes.commit(xid, true);
        assertEquals("1", DefaultXAResource.RS_NONE, xaRes.getStatus());
        assertNull("2", xaRes.getCurrentXid());

        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        xaRes.prepare(xid);
        xaRes.commit(xid, false);
        assertEquals("3", DefaultXAResource.RS_NONE, xaRes.getStatus());
        assertNull("4", xaRes.getCurrentXid());

        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMFAIL);
        try {
            xaRes.commit(xid, true);
            fail("5");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        try {
            xaRes.commit(xid, false);
            fail("6");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        try {
            xaRes.commit(new XidImpl(), true);
            fail("7");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    public void testRollback() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMFAIL);
        xaRes.rollback(xid);
        assertEquals("1", DefaultXAResource.RS_NONE, xaRes.getStatus());
        assertNull("2", xaRes.getCurrentXid());

        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMSUCCESS);
        xaRes.prepare(xid);
        xaRes.rollback(xid);
        assertEquals("3", DefaultXAResource.RS_NONE, xaRes.getStatus());
        assertNull("4", xaRes.getCurrentXid());

        xaRes.start(xid, XAResource.TMNOFLAGS);
        try {
            xaRes.rollback(xid);
            fail("5");
        } catch (XAException ex) {
            System.out.println(ex);
        }

        xaRes.forget(xid);
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.end(xid, XAResource.TMFAIL);
        try {
            xaRes.rollback(new XidImpl());
            fail("6");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    public void testForget() throws Exception {
        DefaultXAResource xaRes = new DefaultXAResource();
        Xid xid = new XidImpl();
        xaRes.start(xid, XAResource.TMNOFLAGS);
        xaRes.forget(xid);
        assertNull("1", xaRes.getCurrentXid());
        assertEquals("2", DefaultXAResource.RS_NONE, xaRes.getStatus());

        xaRes.start(xid, XAResource.TMNOFLAGS);
        try {
            xaRes.forget(new XidImpl());
            fail("3");
        } catch (XAException ex) {
            System.out.println(ex);
        }
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite ( ) {
        return new TestSuite(DefaultXAResourceTest.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.main(new String[]{DefaultXAResourceTest.class.getName()});
    }
}
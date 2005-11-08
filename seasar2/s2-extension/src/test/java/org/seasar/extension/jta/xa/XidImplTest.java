package org.seasar.extension.jta.xa;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.jta.xa.XidImpl;

public class XidImplTest extends TestCase {

    public XidImplTest(String name) {
        super(name);
    }

    public void testEquals() throws Exception {
        XidImpl xid = new XidImpl();
        assertEquals("1", xid, xid);
        assertTrue("2", !xid.equals(null));
        assertTrue("3", !xid.equals(new XidImpl()));
        assertTrue("4", !xid.equals("test"));

        XidImpl xid2 = new XidImpl(xid, 1);
        XidImpl xid3 = new XidImpl(xid, 1);
        assertEquals("5", xid2, xid3);
        System.out.println(xid2);
        System.out.println(xid3);
    }

    public void testToString() throws Exception {
        XidImpl xid = new XidImpl();
        System.out.println(xid);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite ( ) {
        return new TestSuite(XidImplTest.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.main(new String[]{XidImplTest.class.getName()});
    }
}
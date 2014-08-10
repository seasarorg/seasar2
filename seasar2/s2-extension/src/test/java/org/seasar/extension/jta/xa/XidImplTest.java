/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class XidImplTest extends TestCase {

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        XidImpl xid = new XidImpl();
        System.out.println(xid);
    }
}
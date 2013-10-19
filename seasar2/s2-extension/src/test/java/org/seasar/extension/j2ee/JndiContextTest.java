/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.j2ee;

import java.util.Hashtable;

import javax.naming.Context;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 * 
 */
public class JndiContextTest extends S2TestCase {

    private Context ctx_;

    /**
     * @throws Exception
     */
    public void testLookup() throws Exception {
        assertNotNull(ctx_.lookup("jdbc.dataSource"));
        assertNotNull(ctx_.lookup("jta.TransactionManager"));
        assertNotNull("3", ctx_.lookup("jta.UserTransaction"));
    }

    /**
     * @throws Exception
     */
    public void testLookupENC() throws Exception {
        assertNotNull("1", ctx_.lookup("java:comp/env/jdbc/dataSource"));
        assertNotNull("2", ctx_.lookup("java:comp/env/j2ee/jdbc/dataSource"));
        assertNotNull("3", ctx_.lookup("java:comp/UserTransaction"));
    }

    /**
     * @throws Exception
     */
    public void testBind() throws Exception {
        include("bind.dicon");
        Object obj = new Object();
        ctx_.bind("bind.Hoge", obj);
        assertNotNull(ctx_.lookup("bind/Hoge"));
    }

    protected void setUp() throws Exception {
        include("j2ee.dicon");
        Hashtable env = new Hashtable();
        ctx_ = new JndiContext(env);
    }

    protected void tearDown() throws Exception {
        ctx_.close();
    }
}
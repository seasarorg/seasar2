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
import javax.naming.InitialContext;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 *
 */
public class JndiContextFactoryTest extends S2TestCase {

    private Context ctx_;

    /**
     * @throws Exception
     */
    public void testLookup() throws Exception {
        assertNotNull("1", ctx_.lookup("jndi.transactionManager"));
        assertNotNull("2", ctx_.lookup("jndi.dataSource"));
    }

    protected void setUp() throws Exception {
        include("jndi.dicon");
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JndiContextFactory.class
                .getName());
        ctx_ = new InitialContext(env);
    }

    protected void tearDown() throws Exception {
        ctx_.close();
    }

}
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
package org.seasar.extension.sql.context;

import junit.framework.TestCase;

/**
 * @author li0934
 * 
 */
public class SqlContextImplTest extends TestCase {

    /**
     * 
     */
    public void testGetArg() {
        SqlContextImpl ctx = new SqlContextImpl();
        ctx.addArg("?1", "111", String.class);
        assertEquals("111", ctx.getArg("hoge"));
    }

    /**
     * 
     */
    public void testGetArg_warning() {
        SqlContextImpl ctx = new SqlContextImpl();
        ctx.addArg("?1", "111", String.class);
        ctx.addArg("?2", "111", String.class);
        ctx.getArg("hoge");
    }

    /**
     * 
     */
    public void testGetArgType() {
        SqlContextImpl ctx = new SqlContextImpl();
        ctx.addArg("$?", "111", String.class);
        assertEquals(String.class, ctx.getArgType("hoge"));
    }

    /**
     * 
     */
    public void testGetArgType_warning() {
        SqlContextImpl ctx = new SqlContextImpl();
        ctx.addArg("?1", "111", String.class);
        ctx.addArg("?2", "111", String.class);
        ctx.getArgType("hoge");
    }
}

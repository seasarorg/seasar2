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
package org.seasar.extension.jdbc.where;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class OrTest extends TestCase {

    /**
     * 
     */
    public void testOr() {
        Or or = new Or();
        assertEquals("", or.getCriteria());
        assertEquals(0, or.getParams().length);

        or = new Or(new SimpleWhere().eq("id", 1).eq("name", "hoge"));
        assertEquals("(id = ? and name = ?)", or.getCriteria());
        Object[] params = or.getParams();
        assertEquals(2, params.length);
        assertEquals(1, params[0]);
        assertEquals("hoge", params[1]);

        or = new Or(new SimpleWhere().eq("id", 1).eq("name", "foo"))
                .or(new SimpleWhere().eq("id", 2).eq("name", "bar"));
        assertEquals("(id = ? and name = ?) or (id = ? and name = ?)", or
                .getCriteria());
        params = or.getParams();
        assertEquals(4, params.length);
        assertEquals(1, params[0]);
        assertEquals("foo", params[1]);
        assertEquals(2, params[2]);
        assertEquals("bar", params[3]);
    }
}

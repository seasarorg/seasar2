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
package org.seasar.extension.jdbc;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Bbb;

/**
 * @author higa
 * 
 */
public class MappingContextTest extends TestCase {

    /**
     * 
     */
    public void testSetAndGetCache() {
        MappingContext ctx = new MappingContext(10);
        assertNull(ctx.getCache(Aaa.class, 1));
        Aaa aaa = new Aaa();
        ctx.setCache(Aaa.class, 1, aaa);
        assertSame(aaa, ctx.getCache(Aaa.class, 1));
    }

    /**
     * 
     */
    public void testCheckDone() {
        MappingContext ctx = new MappingContext(10);
        EntityMapper mapper = new MyEntityMapper();
        Aaa aaa = new Aaa();
        Bbb bbb = new Bbb();
        assertFalse(ctx.checkDone(mapper, aaa, bbb));
        assertTrue(ctx.checkDone(mapper, aaa, bbb));
        assertFalse(ctx.checkDone(mapper, new Aaa(), bbb));
        assertFalse(ctx.checkDone(mapper, aaa, new Bbb()));
        assertFalse(ctx.checkDone(mapper, new Aaa(), null));
    }

    /**
     * 
     */
    public void testCheckDone_entityMapper() {
        MappingContext ctx = new MappingContext(10);
        EntityMapper mapper = new MyEntityMapper();
        Aaa aaa = new Aaa();
        Aaa aaa2 = new Aaa();
        assertFalse(ctx.checkDone(mapper, null, aaa));
        assertTrue(ctx.checkDone(mapper, null, aaa));
        assertFalse(ctx.checkDone(mapper, null, aaa2));
        assertTrue(ctx.checkDone(mapper, null, aaa2));
        assertTrue(ctx.checkDone(mapper, null, aaa));
    }

    /**
     * 
     */
    public void testClear() {
        MappingContext ctx = new MappingContext(10);
        EntityMapper mapper = new MyEntityMapper();
        Aaa aaa = new Aaa();
        Bbb bbb = new Bbb();
        ctx.checkDone(mapper, aaa, bbb);
        ctx.setCache(Aaa.class, 1, aaa);
        ctx.clear();
        assertTrue(ctx.cache.isEmpty());
        assertTrue(ctx.doneEntityMap.isEmpty());
    }

    private static class MyEntityMapper implements EntityMapper {

        public Object getKey(Object[] values) {
            return null;
        }

        public Object map(Object[] values, MappingContext mappingContext) {
            return null;
        }

    }
}

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
package org.seasar.extension.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.entity.Ddd;

/**
 * @author higa
 * 
 */
public class OneToManyEntityMapperImplTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testMap() throws Exception {
        Field field = Ddd.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field, 0);
        Field field2 = Ddd.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        Field field3 = Bbb.class.getDeclaredField("ddds");
        Field field4 = Ddd.class.getDeclaredField("bbb");

        OneToManyEntityMapperImpl entityMapper = new OneToManyEntityMapperImpl(
                Ddd.class, new PropertyMapper[] { propertyMapper,
                        propertyMapper2 }, new int[] { 0 }, field3, field4);
        MappingContext mappingContext = new MappingContext(10);
        Object[] values = new Object[] { 1, "DDD" };
        Bbb bbb = new Bbb();
        entityMapper.map(bbb, values, mappingContext);
        List<Ddd> ddds = bbb.ddds;
        assertNotNull(ddds);
        assertEquals(1, ddds.size());
        Ddd ddd = ddds.get(0);
        assertEquals(new Integer(1), ddd.id);
        assertEquals("DDD", ddd.name);
        assertSame(bbb, ddd.bbb);

        Object[] values2 = new Object[] { 2, "DDD2" };
        entityMapper.map(bbb, values2, mappingContext);
        List<Ddd> ddds2 = bbb.ddds;
        assertNotNull(ddds2);
        assertEquals(2, ddds2.size());
        assertSame(ddd, ddds2.get(0));
        Ddd ddd2 = ddds2.get(1);
        assertEquals(new Integer(2), ddd2.id);
        assertEquals("DDD2", ddd2.name);
        assertSame(bbb, ddd2.bbb);

        Bbb bbb2 = new Bbb();
        Object[] values3 = new Object[] { null, null };
        entityMapper.map(bbb2, values3, mappingContext);
        List<Ddd> ddds3 = bbb2.ddds;
        assertNotNull(ddds3);
        assertEquals(0, ddds3.size());

        // CONTAINER-381
        Bbb bbb3 = new Bbb();
        bbb3.ddds = new ArrayList<Ddd>();
        Ddd ddd3 = ddds2.get(0);
        bbb3.ddds.add(ddd3);
        entityMapper.map(bbb3, values, mappingContext);
        assertNotNull(bbb3.ddds);
        assertEquals(1, bbb3.ddds.size());
        assertSame(ddd3, bbb3.ddds.get(0));
    }
}
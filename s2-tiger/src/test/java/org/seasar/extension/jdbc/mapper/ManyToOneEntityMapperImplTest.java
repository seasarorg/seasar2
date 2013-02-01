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
public class ManyToOneEntityMapperImplTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testMap() throws Exception {
        Field field = Bbb.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field, 0);
        Field field2 = Bbb.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        Field field3 = Ddd.class.getDeclaredField("bbb");
        Field field4 = Bbb.class.getDeclaredField("ddds");

        ManyToOneEntityMapperImpl entityMapper = new ManyToOneEntityMapperImpl(
                Bbb.class, new PropertyMapper[] { propertyMapper,
                        propertyMapper2 }, new int[] { 0 }, field3, field4);
        MappingContext mappingContext = new MappingContext(10);
        Object[] values = new Object[] { 1, "BBB" };
        Ddd ddd = new Ddd();
        entityMapper.map(ddd, values, mappingContext);
        Bbb bbb = ddd.bbb;
        assertNotNull(bbb);
        List<Ddd> ddds = bbb.ddds;
        assertNotNull(ddds);
        assertEquals(1, ddds.size());
        assertSame(ddd, ddds.get(0));

        Ddd ddd2 = new Ddd();
        Object[] values2 = new Object[] { null, null };
        entityMapper.map(ddd2, values2, mappingContext);
        assertNull(ddd2.bbb);

        // CONTAINER-381
        Ddd ddd3 = new Ddd();
        bbb.ddds.clear();
        bbb.ddds.add(ddd3);
        Object[] values3 = new Object[] { 1, "BBB" };
        entityMapper.map(ddd3, values3, mappingContext);
        assertSame(bbb, ddd3.bbb);
        assertNotNull(bbb.ddds);
        assertEquals(1, bbb.ddds.size());
        assertSame(ddd3, bbb.ddds.get(0));
    }

}

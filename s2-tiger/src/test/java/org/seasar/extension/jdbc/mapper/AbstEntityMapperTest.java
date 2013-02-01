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

import junit.framework.TestCase;

import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.RelationshipEntityMapper;
import org.seasar.extension.jdbc.entity.Aaa;

/**
 * @author higa
 * 
 */
public class AbstEntityMapperTest extends TestCase {

    private boolean called = false;

    @Override
    protected void setUp() throws Exception {
        called = false;
    }

    /**
     * 
     */
    public void testGetKey() {
        MyMapper mapper = new MyMapper(null, null, new int[] { 0 });
        assertEquals(1, mapper.getKey(new Object[] { 1, "abc" }));
    }

    /**
     * 
     */
    public void testGetKey_multikey() {
        MyMapper mapper = new MyMapper(null, null, new int[] { 0, 1 });
        Object key = mapper.getKey(new Object[] { 1, 2 });
        assertNotNull(key);
        assertEquals(KeyItems.class, key.getClass());
        KeyItems items = (KeyItems) key;
        Object[] values = items.getValues();
        assertEquals(2, values.length);
        assertEquals(1, values[0]);
        assertEquals(2, values[1]);
    }

    /**
     * 
     */
    public void testGetKey_nokey() {
        MyMapper mapper = new MyMapper(null, null, new int[0]);
        assertNull(mapper.getKey(new Object[] { 1, "abc" }));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetEntity() throws Exception {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        MyMapper mapper = new MyMapper(Aaa.class, new PropertyMapper[] {
                propertyMapper, propertyMapper2 }, new int[] { 0 });
        Object[] values = new Object[] { 11, "SCOTT" };
        MappingContext ctx = new MappingContext();
        Object key = mapper.getKey(values);
        Aaa aaa = (Aaa) mapper.getEntity(values, ctx, key);
        assertNotNull(aaa);
        assertEquals(new Integer(11), aaa.id);
        assertEquals("SCOTT", aaa.name);
        assertSame(aaa, mapper.getEntity(values, ctx, key));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetEntity_idIndicesGtZero_nullkey() throws Exception {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        MyMapper mapper = new MyMapper(Aaa.class, new PropertyMapper[] {
                propertyMapper, propertyMapper2 }, new int[] { 0 });
        Object[] values = new Object[] { null, null };
        MappingContext ctx = new MappingContext();
        Object key = mapper.getKey(values);
        Aaa aaa = (Aaa) mapper.getEntity(values, ctx, key);
        assertNull(aaa);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetEntity_idIndicesZero() throws Exception {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        MyMapper mapper = new MyMapper(Aaa.class, new PropertyMapper[] {
                propertyMapper, propertyMapper2 }, new int[0]);
        Object[] values = new Object[] { 11, "SCOTT" };
        MappingContext ctx = new MappingContext();
        Object key = mapper.getKey(values);
        Aaa aaa = (Aaa) mapper.getEntity(values, ctx, key);
        assertNotNull(aaa);
        assertEquals(new Integer(11), aaa.id);
        assertEquals("SCOTT", aaa.name);
        assertNotSame(aaa, mapper.getEntity(values, ctx, key));
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateEntity() throws Exception {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        MyMapper mapper = new MyMapper(Aaa.class, new PropertyMapper[] {
                propertyMapper, propertyMapper2 }, new int[] { 0 });
        Object[] values = new Object[] { 11, "SCOTT" };
        Aaa aaa = (Aaa) mapper.createEntity(values, new MappingContext(), 11);
        assertNotNull(aaa);
        assertEquals(new Integer(11), aaa.id);
        assertEquals("SCOTT", aaa.name);
    }

    /**
     * 
     */
    public void testMapRelationships() {
        MyMapper mapper = new MyMapper(null, null, new int[] { 0 });
        mapper.addRelationshipEntityMapper(new MyRelationshipMapper());
        mapper.mapRelationships(new Aaa(), null, null);
        assertTrue(called);
    }

    /**
     * 
     */
    public void testMapRelationships_entityNull() {
        MyMapper mapper = new MyMapper(null, null, new int[] { 0 });
        mapper.addRelationshipEntityMapper(new MyRelationshipMapper());
        mapper.mapRelationships(null, null, null);
        assertFalse(called);
    }

    private static class MyMapper extends AbstractEntityMapper {

        /**
         * @param entityClass
         * @param propertyMappers
         * @param idIndices
         */
        public MyMapper(Class<?> entityClass, PropertyMapper[] propertyMappers,
                int[] idIndices) {
            super(entityClass, propertyMappers, idIndices);
        }
    }

    private class MyRelationshipMapper implements RelationshipEntityMapper {

        public void map(Object target, Object[] values,
                MappingContext mappingContext) {
            called = true;
        }
    }
}

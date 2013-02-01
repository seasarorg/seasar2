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
package org.seasar.extension.jdbc.meta;

import java.lang.reflect.Field;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

/**
 * @author higa
 * 
 */
public class ColumnMetaFactoryImplTest extends TestCase {

    private ColumnMetaFactoryImpl factory;

    @Override
    protected void setUp() {
        factory = new ColumnMetaFactoryImpl();
        factory.setPersistenceConvention(new PersistenceConventionImpl());
    }

    /**
     * @throws Exception
     */
    public void testCreateColumnMeta_annotation() throws Exception {
        Field field = MyEntity.class.getDeclaredField("aaa");
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setName("aaa");
        ColumnMeta columnMeta = factory.createColumnMeta(field, null,
                propertyMeta);
        assertEquals("aaa2", columnMeta.getName());
        assertFalse(columnMeta.isInsertable());
        assertFalse(columnMeta.isUpdatable());
    }

    /**
     * @throws Exception
     */
    public void testCreateColumnMeta_noannotation() throws Exception {
        Field field = MyEntity.class.getDeclaredField("bbb");
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setName("bbb");
        ColumnMeta columnMeta = factory.createColumnMeta(field, null,
                propertyMeta);
        assertEquals("BBB", columnMeta.getName());
        assertTrue(columnMeta.isInsertable());
        assertTrue(columnMeta.isUpdatable());
    }

    private static class MyEntity {

        /**
         * 
         */
        @Column(name = "aaa2", insertable = false, updatable = false)
        public String aaa;

        /**
         * 
         */
        public String bbb;
    }
}
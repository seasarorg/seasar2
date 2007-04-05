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
package org.seasar.extension.persistence.factory;

import java.lang.reflect.Field;

import javax.persistence.Transient;

import junit.framework.TestCase;

import org.seasar.extension.persistence.PropertyMeta;
import org.seasar.extension.persistence.entity.Employee;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

/**
 * @author higa
 * 
 */
public class PropertyMetaFactoryImplTest extends TestCase {

    @Transient
    private PropertyMetaFactoryImpl factory;

    @SuppressWarnings("unused")
    private transient String aaa;

    @SuppressWarnings("unused")
    private String bbb;

    @Override
    protected void setUp() {
        factory = new PropertyMetaFactoryImpl();
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(new PersistenceConventionImpl());
        factory.setColumnMetaFactory(cmFactory);
    }

    /**
     * @throws Exception
     */
    public void testId() throws Exception {
        Field field = Employee.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertTrue(propertyMeta.isId());
    }

    /**
     * @throws Exception
     */
    public void testId_notid() throws Exception {
        Field field = Employee.class.getDeclaredField("version");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertFalse(propertyMeta.isId());
    }

    /**
     * @throws Exception
     */
    public void testTransient() throws Exception {
        Field field = getClass().getDeclaredField("factory");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertTrue(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testTransient2() throws Exception {
        Field field = getClass().getDeclaredField("aaa");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertTrue(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testTransient_notransient() throws Exception {
        Field field = getClass().getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertFalse(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testColumnMeta() throws Exception {
        Field field = Employee.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertNotNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testName() throws Exception {
        Field field = Employee.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertEquals("id", propertyMeta.getName());
    }

    /**
     * @throws Exception
     */
    public void testVersion() throws Exception {
        Field field = Employee.class.getDeclaredField("version");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertTrue(propertyMeta.isVersion());
    }

    /**
     * @throws Exception
     */
    public void testVersion_notversion() throws Exception {
        Field field = Employee.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field);
        assertFalse(propertyMeta.isVersion());
    }
}
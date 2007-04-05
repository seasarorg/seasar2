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

import junit.framework.TestCase;

import org.seasar.extension.persistence.ColumnMeta;
import org.seasar.extension.persistence.entity.Employee;
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
    public void testCreateTableMeta_annotation() throws Exception {
        Field field = Employee.class.getDeclaredField("id");
        ColumnMeta columnMeta = factory.createColumnMeta(field);
        assertEquals("EMP_ID", columnMeta.getName());
    }

    /**
     * @throws Exception
     */
    public void testCreateTableMeta_noannotation() throws Exception {
        Field field = Employee.class.getDeclaredField("version");
        ColumnMeta columnMeta = factory.createColumnMeta(field);
        assertEquals("VERSION", columnMeta.getName());
    }
}
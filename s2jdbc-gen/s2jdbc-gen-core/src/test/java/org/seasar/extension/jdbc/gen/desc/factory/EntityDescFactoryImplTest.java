/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.desc.factory;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.factory.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.factory.EntityDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityDescFactoryImplTest {

    private EntityDescFactoryImpl factory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention convention = new PersistenceConventionImpl();
        GenDialect dialect = new StandardGenDialect();
        AttributeDescFactoryImpl attributeDescFactory = new AttributeDescFactoryImpl(
                convention, dialect, "VERSION");
        factory = new EntityDescFactoryImpl(convention, attributeDescFactory);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        DbTableMeta dbTableMeta = new DbTableMeta();
        dbTableMeta.setName("HOGE");
        EntityDesc entityDesc = factory.getEntityDesc(dbTableMeta);
        assertEquals("Hoge", entityDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTableName() throws Exception {
        DbTableMeta dbTableMeta = new DbTableMeta();
        dbTableMeta.setName("HOGE");
        EntityDesc entityDesc = factory.getEntityDesc(dbTableMeta);
        assertEquals("HOGE", entityDesc.getTableName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetAttributeDescList() throws Exception {
        DbColumnMeta columnMeta1 = new DbColumnMeta();
        columnMeta1.setName("BAR");
        DbColumnMeta columnMeta2 = new DbColumnMeta();
        columnMeta2.setName("FOO");
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("HOGE");
        tableMeta.addColumnMeta(columnMeta1);
        tableMeta.addColumnMeta(columnMeta2);
        EntityDesc entityModel = factory.getEntityDesc(tableMeta);
        assertEquals(2, entityModel.getAttributeDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testHasCompositeId() throws Exception {
        DbColumnMeta columnMeta1 = new DbColumnMeta();
        columnMeta1.setName("BAR");
        columnMeta1.setPrimaryKey(true);
        DbColumnMeta columnMeta2 = new DbColumnMeta();
        columnMeta2.setName("FOO");
        columnMeta2.setPrimaryKey(true);
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("HOGE");
        tableMeta.addColumnMeta(columnMeta1);
        tableMeta.addColumnMeta(columnMeta2);
        EntityDesc entityModel = factory.getEntityDesc(tableMeta);
        assertEquals(2, entityModel.getIdAttributeDescList().size());
        assertTrue(entityModel.hasCompositeId());
    }
}

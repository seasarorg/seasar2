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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbUniqueKeyMeta;
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
                convention, dialect, "VERSION", null, null, null);
        CompositeUniqueConstraintDescFactoryImpl uniqueConstraintDescFactory = new CompositeUniqueConstraintDescFactoryImpl();
        factory = new EntityDescFactoryImpl(convention, attributeDescFactory,
                uniqueConstraintDescFactory);
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
    public void testTable() throws Exception {
        DbTableMeta dbTableMeta = new DbTableMeta();
        dbTableMeta.setCatalogName("AAA");
        dbTableMeta.setSchemaName("BBB");
        dbTableMeta.setName("HOGE");
        EntityDesc entityDesc = factory.getEntityDesc(dbTableMeta);
        assertEquals("AAA", entityDesc.getCatalogName());
        assertEquals("BBB", entityDesc.getSchemaName());
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
        columnMeta1.setTypeName("varchar");
        DbColumnMeta columnMeta2 = new DbColumnMeta();
        columnMeta2.setName("FOO");
        columnMeta2.setTypeName("varchar");
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("HOGE");
        tableMeta.addColumnMeta(columnMeta1);
        tableMeta.addColumnMeta(columnMeta2);
        EntityDesc entityDesc = factory.getEntityDesc(tableMeta);
        assertEquals(2, entityDesc.getAttributeDescList().size());
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
        columnMeta1.setTypeName("varchar");
        DbColumnMeta columnMeta2 = new DbColumnMeta();
        columnMeta2.setName("FOO");
        columnMeta2.setPrimaryKey(true);
        columnMeta2.setTypeName("varchar");
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("HOGE");
        tableMeta.addColumnMeta(columnMeta1);
        tableMeta.addColumnMeta(columnMeta2);
        EntityDesc entityDesc = factory.getEntityDesc(tableMeta);
        assertEquals(2, entityDesc.getIdAttributeDescList().size());
        assertTrue(entityDesc.hasCompositeId());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetCompositeUniqueConstraintDescList() throws Exception {
        DbUniqueKeyMeta ukMeta1 = new DbUniqueKeyMeta();
        ukMeta1.addColumnName("AAA");
        ukMeta1.addColumnName("BBB");
        DbUniqueKeyMeta ukMeta2 = new DbUniqueKeyMeta();
        ukMeta2.addColumnName("CCC");
        ukMeta2.addColumnName("DDD");
        DbUniqueKeyMeta ukMeta3 = new DbUniqueKeyMeta();
        ukMeta3.addColumnName("EEE");
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("HOGE");
        tableMeta.addUniqueKeyMeta(ukMeta1);
        tableMeta.addUniqueKeyMeta(ukMeta2);
        tableMeta.addUniqueKeyMeta(ukMeta3);
        EntityDesc entityDesc = factory.getEntityDesc(tableMeta);
        assertEquals(2, entityDesc.getCompositeUniqueConstraintDescList()
                .size());
        CompositeUniqueConstraintDesc compositeUniqueConstraintDesc = entityDesc
                .getCompositeUniqueConstraintDescList().get(0);
        assertEquals(Arrays.asList("AAA", "BBB"), compositeUniqueConstraintDesc
                .getColumnNameList());
        compositeUniqueConstraintDesc = entityDesc
                .getCompositeUniqueConstraintDescList().get(1);
        assertEquals(Arrays.asList("CCC", "DDD"), compositeUniqueConstraintDesc
                .getColumnNameList());
    }
}

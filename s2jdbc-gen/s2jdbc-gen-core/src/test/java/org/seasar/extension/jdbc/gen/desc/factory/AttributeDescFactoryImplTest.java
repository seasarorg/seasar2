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

import java.sql.Types;

import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.factory.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AttributeDescFactoryImplTest {

    private AttributeDescFactoryImpl factory;

    /**
     * 
     */
    @Before
    public void setUp() {
        PersistenceConvention convention = new PersistenceConventionImpl();
        GenDialect dialect = new StandardGenDialect();
        factory = new AttributeDescFactoryImpl(convention, dialect, "VERSION");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("HOGE");
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertEquals("hoge", attributeDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIsId() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setPrimaryKey(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertTrue(attributeDesc.isId());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetAttributeClass() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setSqlType(Types.VARCHAR);
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertEquals(String.class, attributeDesc.getAttributeClass());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTemporalType() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setSqlType(Types.DATE);
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertEquals(TemporalType.DATE, attributeDesc.getTemporalType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIsLob() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setSqlType(Types.BLOB);
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertTrue(attributeDesc.isLob());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIsVersion() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("version");
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertTrue(attributeDesc.isVersion());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testColumnName() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("HOGE");
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertEquals("HOGE", attributeDesc.getColumnName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setNullable(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(columnMeta);
        assertTrue(attributeDesc.isNullable());
    }

}

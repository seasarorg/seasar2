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
package org.seasar.extension.jdbc.gen.desc;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ColumnDescFactoryImplTest {

    private PropertyMetaFactoryImpl propertyMetaFactory;

    private ColumnDescFactoryImpl columnDescFactory;

    private String string;

    @Transient
    private String trnsient;

    @OneToOne
    private Aaa relationship;

    @Column(length = 10)
    private String customLengthString;

    @Column(columnDefinition = "VARCHAR2(10)")
    private String customDefinitionString;

    private Integer nullableReference;

    @Column(nullable = false)
    private Integer nonNullableReference;

    private int nonNullablePrimitive;

    @Column(nullable = true)
    private int nullablePrimitive;

    @Column(unique = true)
    private String unique;

    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        columnDescFactory = new ColumnDescFactoryImpl(new StandardGenDialect());
    }

    @Test
    public void testTransient() throws Exception {
        Field field = getClass().getDeclaredField("trnsient");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNull(columnDesc);
    }

    @Test
    public void testRelationship() throws Exception {
        Field field = getClass().getDeclaredField("relationship");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNull(columnDesc);
    }

    @Test
    public void testName() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("STRING", columnDesc.getName());
    }

    @Test
    public void testDefinition() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("varchar(255)", columnDesc.getDefinition());
    }

    @Test
    public void testDefinition_length() throws Exception {
        Field field = getClass().getDeclaredField("customLengthString");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("varchar(10)", columnDesc.getDefinition());
    }

    @Test
    public void testDefinition_columnDefinition() throws Exception {
        Field field = getClass().getDeclaredField("customDefinitionString");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("VARCHAR2(10)", columnDesc.getDefinition());
    }

    @Test
    public void testNullable_nullableReference() throws Exception {
        Field field = getClass().getDeclaredField("nullableReference");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isNullable());
    }

    @Test
    public void testNullable_nonNullableReference() throws Exception {
        Field field = getClass().getDeclaredField("nonNullableReference");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isNullable());
    }

    @Test
    public void testNullable_nullablePrimitive() throws Exception {
        Field field = getClass().getDeclaredField("nullablePrimitive");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isNullable());
    }

    @Test
    public void testNullable_nonNullablePrimitive() throws Exception {
        Field field = getClass().getDeclaredField("nonNullablePrimitive");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());

        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isNullable());
    }

    @Test
    public void testUnique() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isUnique());
    }

    @Test
    public void testUnique_unique() throws Exception {
        Field field = getClass().getDeclaredField("unique");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isUnique());
    }

    @Entity
    public static class Aaa {
    }

}

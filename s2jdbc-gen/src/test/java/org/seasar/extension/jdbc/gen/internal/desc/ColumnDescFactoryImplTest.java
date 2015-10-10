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

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.dialect.H2Dialect;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.internal.dialect.H2GenDialect;
import org.seasar.extension.jdbc.gen.internal.provider.ValueTypeProviderImpl;
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

    @SuppressWarnings("unused")
    private String string;

    @SuppressWarnings("unused")
    @Transient
    private String trnsient;

    @SuppressWarnings("unused")
    @OneToOne
    private Aaa relationship;

    @SuppressWarnings("unused")
    @Column(length = 10)
    private String customLengthString;

    @SuppressWarnings("unused")
    @Column(columnDefinition = "VARCHAR2(10)")
    private String customDefinitionString;

    @SuppressWarnings("unused")
    @Column(columnDefinition = "default 'hoge'")
    private String customDefinitionDefaultString;

    @SuppressWarnings("unused")
    private Integer nullableReference;

    @SuppressWarnings("unused")
    @Column(nullable = false)
    private Integer nonNullableReference;

    @SuppressWarnings("unused")
    private int nonNullablePrimitive;

    @SuppressWarnings("unused")
    @Column(nullable = true)
    private int nullablePrimitive;

    @SuppressWarnings("unused")
    @Column(unique = true)
    private String unique;

    @SuppressWarnings("unused")
    @Id
    private Integer id;

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identityId;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        columnDescFactory = new ColumnDescFactoryImpl(new H2GenDialect(),
                new ValueTypeProviderImpl(new H2Dialect()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTransient() throws Exception {
        Field field = getClass().getDeclaredField("trnsient");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNull(columnDesc);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testRelationship() throws Exception {
        Field field = getClass().getDeclaredField("relationship");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNull(columnDesc);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("STRING", columnDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDefinition() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("varchar(255)", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDefinition_length() throws Exception {
        Field field = getClass().getDeclaredField("customLengthString");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("varchar(10)", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDefinition_columnDefinition() throws Exception {
        Field field = getClass().getDeclaredField("customDefinitionString");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("VARCHAR2(10)", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDefinition_columnDefault() throws Exception {
        Field field = getClass().getDeclaredField(
                "customDefinitionDefaultString");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertEquals("varchar(255) default 'hoge'", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable_nullableReference() throws Exception {
        Field field = getClass().getDeclaredField("nullableReference");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable_nonNullableReference() throws Exception {
        Field field = getClass().getDeclaredField("nonNullableReference");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable_nullablePrimitive() throws Exception {
        Field field = getClass().getDeclaredField("nullablePrimitive");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable_nonNullablePrimitive() throws Exception {
        Field field = getClass().getDeclaredField("nonNullablePrimitive");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());

        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNullable_id() throws Exception {
        Field field = getClass().getDeclaredField("id");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());

        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUnique() throws Exception {
        Field field = getClass().getDeclaredField("string");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertFalse(columnDesc.isUnique());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUnique_unique() throws Exception {
        Field field = getClass().getDeclaredField("unique");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isUnique());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIdentity() throws Exception {
        Field field = getClass().getDeclaredField("identityId");
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, new EntityMeta());
        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(
                new EntityMeta(), propertyMeta);
        assertNotNull(columnDesc);
        assertTrue(columnDesc.isIdentity());
    }

    /**
     * 
     * @author taedium
     * 
     */
    @Entity
    public static class Aaa {
    }

}

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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.RelationshipType;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.dialect.Db2Dialect;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.exception.BothMappedByAndJoinColumnRuntimeException;
import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnNameAndReferencedColumnNameMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.LazyFetchSpecifiedRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotGenericsRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotListRuntimeException;
import org.seasar.extension.jdbc.exception.RelationshipNotEntityRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.UnsupportedPropertyTypeRuntimeException;
import org.seasar.extension.jdbc.exception.VersionPropertyNotNumberRuntimeException;
import org.seasar.extension.jdbc.id.IdentityIdGenerator;
import org.seasar.extension.jdbc.id.SequenceIdGenerator;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.extension.jdbc.types.Authority;
import org.seasar.extension.jdbc.types.EnumOrdinalType;
import org.seasar.extension.jdbc.types.EnumType;
import org.seasar.extension.jdbc.types.UserDefineType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

/**
 * @author higa
 * 
 */
public class PropertyMetaFactoryImplTest extends TestCase {

    @Transient
    private PropertyMetaFactoryImpl factory;

    @SuppressWarnings("unused")
    private transient String hoge;

    @SuppressWarnings("unused")
    @Basic
    private String eager;

    @SuppressWarnings("unused")
    @Basic(fetch = FetchType.LAZY)
    private String lazy;

    @SuppressWarnings("unused")
    @Id
    @Basic(fetch = FetchType.LAZY)
    private String idLazy;

    @SuppressWarnings("unused")
    @Temporal(TemporalType.DATE)
    private Date date;

    @SuppressWarnings("unused")
    private Date illegalDate;

    @SuppressWarnings("unused")
    @Temporal(TemporalType.TIME)
    private Date calendar;

    @SuppressWarnings("unused")
    private Date illegalCalendar;

    @SuppressWarnings("unused")
    @Version
    private Integer version;

    @SuppressWarnings("unused")
    @Version
    private Timestamp illegalVersion;

    @SuppressWarnings("unused")
    @OneToOne
    @JoinColumn(name = "bbb_id", referencedColumnName = "id")
    private Bbb bbb;

    @SuppressWarnings("unused")
    @OneToOne
    @JoinColumns( { @JoinColumn, @JoinColumn })
    private Bbb bbb2;

    private EntityMeta entityMeta;

    @Override
    protected void setUp() {
        entityMeta = new EntityMeta("Aaa");
        entityMeta.setEntityClass(Aaa.class);
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("AAA");
        entityMeta.setTableMeta(tableMeta);
        factory = new PropertyMetaFactoryImpl();
        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        factory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        factory.setColumnMetaFactory(cmFactory);
    }

    /**
     * @throws Exception
     */
    public void testId() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
    }

    /**
     * @throws Exception
     */
    public void testIdAutoGeneratedValue() throws Exception {
        Field field = AutoGeneratedId.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.AUTO, propertyMeta.getGenerationType());
        assertTrue(propertyMeta.getIdGenerator(entityMeta, new Db2Dialect()) instanceof IdentityIdGenerator);
        assertTrue(propertyMeta.getIdGenerator(entityMeta, new OracleDialect()) instanceof SequenceIdGenerator);
        assertTrue(propertyMeta.getIdGenerator(entityMeta,
                new StandardDialect()) instanceof TableIdGenerator);
    }

    /**
     * @throws Exception
     */
    public void testIdIdentityGeneratedValue() throws Exception {
        Field field = IdentityGeneratedId.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.IDENTITY, propertyMeta.getGenerationType());
        try {
            propertyMeta.getIdGenerator(entityMeta, new StandardDialect());
            fail();
        } catch (IdentityGeneratorNotSupportedRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSequenceIdentityGeneratedValue() throws Exception {
        Field field = SequenceGeneratedId.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.SEQUENCE, propertyMeta.getGenerationType());
        try {
            propertyMeta.getIdGenerator(entityMeta, new StandardDialect());
            fail();
        } catch (SequenceGeneratorNotSupportedRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    public void testSequenceIdentityGeneratedValue_classAnnotated()
            throws Exception {
        EntityMeta entityMeta = new EntityMeta("SequenceGeneratedId");
        entityMeta.setEntityClass(SequenceGeneratedId.class);
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("SEQUENCE_GENERATED_ID");
        entityMeta.setTableMeta(tableMeta);
        Field field = SequenceGeneratedId.class.getDeclaredField("id2");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.SEQUENCE, propertyMeta.getGenerationType());
    }

    /**
     * @throws Exception
     */
    public void testTableIdentityGeneratedValue() throws Exception {
        Field field = TableGeneratedId.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.TABLE, propertyMeta.getGenerationType());
        assertTrue(propertyMeta.getIdGenerator(entityMeta,
                new StandardDialect()) instanceof TableIdGenerator);
    }

    /**
     * @throws Exception
     */
    public void testTableIdentityGeneratedValue_classAnnotated()
            throws Exception {
        EntityMeta entityMeta = new EntityMeta("TableGeneratedId");
        entityMeta.setEntityClass(TableGeneratedId.class);
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("TABLE_GENERATED_ID");
        entityMeta.setTableMeta(tableMeta);
        Field field = TableGeneratedId.class.getDeclaredField("id2");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isId());
        assertTrue(propertyMeta.hasIdGenerator());
        assertEquals(GenerationType.TABLE, propertyMeta.getGenerationType());
        assertTrue(propertyMeta.getIdGenerator(entityMeta,
                new StandardDialect()) instanceof TableIdGenerator);
    }

    /**
     * @throws Exception
     */
    public void testId_noid() throws Exception {
        Field field = Aaa.class.getDeclaredField("name");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertFalse(propertyMeta.isId());
    }

    /**
     * @throws Exception
     */
    public void testTransient() throws Exception {
        Field field = getClass().getDeclaredField("factory");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testTransient_modifier() throws Exception {
        Field field = getClass().getDeclaredField("hoge");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testTransient_notransient() throws Exception {
        Field field = Aaa.class.getDeclaredField("name");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertFalse(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testTransient_dto() throws Exception {
        Field field = MyAaa.class.getDeclaredField("myDto");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isTransient());
    }

    /**
     * @throws Exception
     */
    public void testColumnMeta() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertNotNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testColumnMeta_transient() throws Exception {
        Field field = MyAaa.class.getDeclaredField("ignore");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertNull(propertyMeta.getColumnMeta());
        assertNull(propertyMeta.getValueType());
    }

    /**
     * @throws Exception
     */
    public void testField() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertSame(field, propertyMeta.getField());
        assertSame(ValueTypes.INTEGER, propertyMeta.getValueType());
    }

    /**
     * @throws Exception
     */
    public void testName() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals("id", propertyMeta.getName());
        assertSame(ValueTypes.INTEGER, propertyMeta.getValueType());
    }

    /**
     * @throws Exception
     */
    public void testPropertyClass() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(Integer.class, propertyMeta.getPropertyClass());
        assertSame(ValueTypes.INTEGER, propertyMeta.getValueType());
    }

    /**
     * 
     * @throws Exception
     */
    public void testFetchType() throws Exception {
        Field field = getClass().getDeclaredField("eager");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(FetchType.EAGER, propertyMeta.getFetchType());
        assertTrue(propertyMeta.isEager());
        assertFalse(propertyMeta.isLazy());

        field = getClass().getDeclaredField("lazy");
        propertyMeta = factory.createPropertyMeta(field, entityMeta);
        assertEquals(FetchType.LAZY, propertyMeta.getFetchType());
        assertFalse(propertyMeta.isEager());
        assertTrue(propertyMeta.isLazy());

        try {
            field = getClass().getDeclaredField("idLazy");
            propertyMeta = factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (LazyFetchSpecifiedRuntimeException expected) {
            assertEquals("Aaa", expected.getEntityName());
            assertEquals("idLazy", expected.getPropertyName());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalDate() throws Exception {
        Field field = getClass().getDeclaredField("date");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(TemporalType.DATE, propertyMeta.getTemporalType());
        assertSame(ValueTypes.DATE_SQLDATE, propertyMeta.getValueType());
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalDate_noannotation() throws Exception {
        Field field = getClass().getDeclaredField("illegalDate");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (Exception e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalCalendar() throws Exception {
        Field field = getClass().getDeclaredField("calendar");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(TemporalType.TIME, propertyMeta.getTemporalType());
        assertSame(ValueTypes.DATE_TIME, propertyMeta.getValueType());
    }

    /**
     * 
     * @throws Exception
     */
    public void testTemporalCalendar_noannotation() throws Exception {
        Field field = getClass().getDeclaredField("illegalCalendar");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (Exception e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testVersion() throws Exception {
        Field field = getClass().getDeclaredField("version");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isVersion());
        assertSame(ValueTypes.INTEGER, propertyMeta.getValueType());
    }

    /**
     * @throws Exception
     */
    public void testVersion_noannotation() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertFalse(propertyMeta.isVersion());
    }

    /**
     * @throws Exception
     */
    public void testVersion_notNumber() throws Exception {
        Field field = getClass().getDeclaredField("illegalVersion");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (VersionPropertyNotNumberRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testLob() throws Exception {
        Field field = MyAaa.class.getDeclaredField("largeName");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertTrue(propertyMeta.isLob());
        assertSame(ValueTypes.CLOB, propertyMeta.getValueType());
    }

    /**
     * @throws Exception
     */
    public void testUserDefinedType() throws Exception {
        Field field = MyAaa.class.getDeclaredField("authority");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(UserDefineType.class, propertyMeta.getValueType()
                .getClass());

        field = MyAaa.class.getDeclaredField("myInt");
        propertyMeta = factory.createPropertyMeta(field, entityMeta);
        assertEquals(UserDefineType.class, propertyMeta.getValueType()
                .getClass());
    }

    /**
     * @throws Exception
     */
    public void testJoinColumnMeta() throws Exception {
        Field field = getClass().getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(1, propertyMeta.getJoinColumnMetaList().size());
        JoinColumnMeta joinColumnMeta = propertyMeta.getJoinColumnMetaList()
                .get(0);
        assertEquals("bbb_id", joinColumnMeta.getName());
        assertEquals("id", joinColumnMeta.getReferencedColumnName());
    }

    /**
     * @throws Exception
     */
    public void testJoinColumnsMeta() throws Exception {
        Field field = getClass().getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(1, propertyMeta.getJoinColumnMetaList().size());
        JoinColumnMeta joinColumnMeta = propertyMeta.getJoinColumnMetaList()
                .get(0);
        assertEquals("bbb_id", joinColumnMeta.getName());
        assertEquals("id", joinColumnMeta.getReferencedColumnName());
    }

    /**
     * @throws Exception
     */
    public void testJoinColumnsMeta_empty() throws Exception {
        entityMeta.setEntityClass(getClass());
        entityMeta.setName("MyTest");
        Field field = getClass().getDeclaredField("bbb2");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (JoinColumnNameAndReferencedColumnNameMandatoryRuntimeException e) {
            System.out.println(e);
            assertEquals("MyTest", e.getEntityName());
            assertEquals("bbb2", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testJoinColumnMeta_noentity() throws Exception {
        Field field = Aaa.class.getDeclaredField("id");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(0, propertyMeta.getJoinColumnMetaList().size());
    }

    /**
     * @throws Exception
     */
    public void testSerializable() throws Exception {
        entityMeta.setName("MyAaa");
        entityMeta.setEntityClass(MyAaa.class);
        Field field = MyAaa.class.getDeclaredField("mySerializable");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertSame(ValueTypes.SERIALIZABLE_BYTE_ARRAY, propertyMeta
                .getValueType());
    }

    /**
     * @throws Exception
     */
    public void testEnum() throws Exception {
        entityMeta.setName("MyEnum");
        entityMeta.setEntityClass(MyEnum.class);
        Field field = MyEnum.class.getDeclaredField("type");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(EnumOrdinalType.class, propertyMeta.getValueType()
                .getClass());

        field = MyEnum.class.getDeclaredField("ordinalType");
        propertyMeta = factory.createPropertyMeta(field, entityMeta);
        assertEquals(EnumOrdinalType.class, propertyMeta.getValueType()
                .getClass());

        field = MyEnum.class.getDeclaredField("stringType");
        propertyMeta = factory.createPropertyMeta(field, entityMeta);
        assertEquals(EnumType.class, propertyMeta.getValueType().getClass());
    }

    /**
     * @throws Exception
     */
    public void testOneToOne() throws Exception {
        Field field = MyAaa.class.getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(RelationshipType.ONE_TO_ONE, propertyMeta
                .getRelationshipType());
        assertEquals(MyBbb.class, propertyMeta.getRelationshipClass());
        assertEquals(1, propertyMeta.getJoinColumnMetaList().size());
        assertNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testOneToOneInverse() throws Exception {
        entityMeta.setName("MyBbb");
        entityMeta.setEntityClass(MyBbb.class);
        Field field = MyBbb.class.getDeclaredField("aaa");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(RelationshipType.ONE_TO_ONE, propertyMeta
                .getRelationshipType());
        assertEquals(MyAaa.class, propertyMeta.getRelationshipClass());
        assertEquals("bbb", propertyMeta.getMappedBy());
        assertNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testOneToOne_notEntity() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("myDto");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (RelationshipNotEntityRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("myDto", e.getPropertyName());
            assertEquals(MyDto.class, e.getRelationshipClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToOne_bothMappedByAndJoinColumn() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("aaa6");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (BothMappedByAndJoinColumnRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("aaa6", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany() throws Exception {
        entityMeta.setName("MyBbb");
        entityMeta.setEntityClass(MyBbb.class);
        Field field = MyBbb.class.getDeclaredField("ddds");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(RelationshipType.ONE_TO_MANY, propertyMeta
                .getRelationshipType());
        assertEquals(MyDdd.class, propertyMeta.getRelationshipClass());
        assertEquals("bbb", propertyMeta.getMappedBy());
        assertNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testOneToMany_notList() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("aaa2");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (OneToManyNotListRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("aaa2", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany_notGenerics() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("aaa3");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (OneToManyNotGenericsRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("aaa3", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany_notEntity() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("myDto3");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (RelationshipNotEntityRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("myDto3", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany_bothMappedByAndJoinColumn() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("aaa7");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (BothMappedByAndJoinColumnRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("aaa7", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany_mappedByMandatory() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("aaa8");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (MappedByMandatoryRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("aaa8", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testManyToOne() throws Exception {
        entityMeta.setName("MyDdd");
        entityMeta.setEntityClass(MyDdd.class);
        Field field = MyDdd.class.getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                entityMeta);
        assertEquals(RelationshipType.MANY_TO_ONE, propertyMeta
                .getRelationshipType());
        assertEquals(MyBbb.class, propertyMeta.getRelationshipClass());
        assertEquals(1, propertyMeta.getJoinColumnMetaList().size());
        assertNull(propertyMeta.getColumnMeta());
    }

    /**
     * @throws Exception
     */
    public void testManyToOne_notEntity() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("myDto4");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (RelationshipNotEntityRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("myDto4", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testInvalidProperty() throws Exception {
        entityMeta.setEntityClass(BadAaa2.class);
        entityMeta.setName("BadAaa2");
        Field field = BadAaa2.class.getDeclaredField("myDto5");
        try {
            factory.createPropertyMeta(field, entityMeta);
            fail();
        } catch (UnsupportedPropertyTypeRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa2", e.getEntityName());
            assertEquals("myDto5", e.getPropertyName());
        }
    }

    @Entity
    private static class MyAaa {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        public Integer bbbId;

        /**
         * 
         */
        @OneToOne
        @JoinColumn(name = "BBB_ID", referencedColumnName = "ID")
        public MyBbb bbb;

        /**
         * 
         */
        public MyInt myInt;

        /**
         * 
         */
        public MySerializable mySerializable;

        /**
         * 
         */
        @Transient
        public String ignore;

        /**
         * 
         */
        @Transient
        public MyDto myDto;

        /**
         * 
         */
        @Lob
        public String largeName;

        /**
         * 
         */
        public Authority authority;
    }

    @Entity
    private static class MyBbb {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToOne(mappedBy = "bbb")
        public MyAaa aaa;

        /**
         * 
         */
        @OneToMany(mappedBy = "bbb")
        public List<MyDdd> ddds;
    }

    @Entity
    private static class MyDdd {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        public Integer bbbId;

        /**
         * 
         */
        @ManyToOne
        @JoinColumn(name = "BBB_ID", referencedColumnName = "ID")
        public MyBbb bbb;
    }

    @Entity
    private static class BadAaa2 {

        /**
         * 
         */
        @Id
        @JoinColumn
        @Basic(fetch = FetchType.LAZY)
        public Integer id;

        /**
         * 
         */
        public Integer cccId;

        /**
         * 
         */
        @OneToOne
        public MyDto myDto;

        /**
         * 
         */
        @OneToMany
        public List<MyDto> myDto3;

        /**
         * 
         */
        @ManyToOne
        public MyDto myDto4;

        /**
         * 
         */
        public MyDto myDto5;

        /**
         * 
         */
        @OneToOne(mappedBy = "xxx")
        public MyAaa aaa;

        /**
         * 
         */
        @OneToMany
        public MyAaa aaa2;

        /**
         * 
         */
        @SuppressWarnings("unchecked")
        @OneToMany
        public List aaa3;

        /**
         * 
         */
        @OneToMany(mappedBy = "xxx")
        public List<MyAaa> aaa4;

        /**
         * 
         */
        @OneToMany(mappedBy = "bbb")
        public List<MyAaa> aaa5;

        /**
         * 
         */
        @OneToOne(mappedBy = "bbb")
        @JoinColumn(name = "BBB_ID", referencedColumnName = "ID")
        public MyAaa aaa6;

        /**
         * 
         */
        @OneToMany(mappedBy = "bbb")
        @JoinColumn(name = "BBB_ID", referencedColumnName = "ID")
        public List<MyAaa> aaa7;

        /**
         * 
         */
        @OneToMany
        public List<MyAaa> aaa8;
    }

    private static class MyDto {

    }

    private static class MyInt implements Serializable {

        private static final long serialVersionUID = 1L;

        private int value;

        /**
         * @param value
         */
        public MyInt(int value) {
            this.value = value;
        }

        /**
         * @param i
         * @return
         */
        public static MyInt valueOf(int i) {
            return new MyInt(i);
        }

        /**
         * @return
         */
        public int value() {
            return value;
        }
    }

    private static class MyEnum {

        /** */
        public TemporalType type;

        /** */
        @Enumerated
        public TemporalType ordinalType;

        /** */
        @Enumerated(javax.persistence.EnumType.STRING)
        public TemporalType stringType;

    }

    private static class MySerializable implements Serializable {

        private static final long serialVersionUID = 1L;

    }

    private static class AutoGeneratedId {

        /**
         * 
         */
        @Id
        @GeneratedValue
        int id;
    }

    private static class IdentityGeneratedId {

        /**
         * 
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        int id;
    }

    @SequenceGenerator(name = "aaa")
    private static class SequenceGeneratedId {

        /**
         * 
         */
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        int id;

        /**
         * 
         */
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aaa")
        int id2;
    }

    @TableGenerator(name = "aaa")
    private static class TableGeneratedId {

        /**
         * 
         */
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE)
        int id;

        /**
         * 
         */
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "aaa")
        int id2;
    }

}
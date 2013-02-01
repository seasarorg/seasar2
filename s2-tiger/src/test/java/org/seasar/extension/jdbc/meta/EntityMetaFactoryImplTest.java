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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Ggg;
import org.seasar.extension.jdbc.entity.Hhh;
import org.seasar.extension.jdbc.entity.Jjj;
import org.seasar.extension.jdbc.entity.Kkk;
import org.seasar.extension.jdbc.exception.FieldDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnAutoConfigurationRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.ManyToOneFKNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByNotIdenticalRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByPropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.NonEntityRuntimeException;
import org.seasar.extension.jdbc.exception.OneToOneFKNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.ReferencedColumnNameNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.UnsupportedInheritanceRuntimeException;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.util.DisposableUtil;

/**
 * @author higa
 * 
 */
public class EntityMetaFactoryImplTest extends TestCase {

    private EntityMetaFactoryImpl factory;

    @Override
    protected void setUp() {
        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        factory = new EntityMetaFactoryImpl();
        factory.setPersistenceConvention(convention);
        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(convention);
        factory.setTableMetaFactory(tableMetaFactory);

        PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
        pFactory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        pFactory.setColumnMetaFactory(cmFactory);
        factory.setPropertyMetaFactory(pFactory);
        factory.initialize();
    }

    @Override
    protected void tearDown() throws Exception {
        DisposableUtil.dispose();
    }

    /**
     * @throws Exception
     */
    public void testGetEntityMeta() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        assertSame(entityMeta, factory.getEntityMeta(Aaa.class));
    }

    /**
     * @throws Exception
     */
    public void testGetEntityMeta_nonEntity() throws Exception {
        try {
            factory.getEntityMeta(getClass());
            fail();
        } catch (NonEntityRuntimeException e) {
            System.out.println(e);
            assertEquals(getClass(), e.getTargetClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_entityClass() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Aaa.class);
        assertEquals(Aaa.class, entityMeta.getEntityClass());
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_name() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Aaa.class);
        assertEquals("Aaa", entityMeta.getName());
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_customizeName() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Hoge.class);
        assertEquals("Hoge2", entityMeta.getName());
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_tableMeta() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Aaa.class);
        assertNotNull(entityMeta.getTableMeta());
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_propertyMeta() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Aaa.class);
        assertTrue(entityMeta.getPropertyMetaSize() > 0);
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_noEntity() throws Exception {
        try {
            factory.createEntityMeta(getClass());
            fail();
        } catch (NonEntityRuntimeException e) {
            System.out.println(e);
            assertEquals(getClass(), e.getTargetClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_inheritMappedSuperclass() throws Exception {
        EntityMeta entityMeta = factory.createEntityMeta(Hhh.class);
        assertEquals(3, entityMeta.getPropertyMetaSize());
        assertEquals("name", entityMeta.getPropertyMeta(0).getName());
        assertEquals("id", entityMeta.getPropertyMeta(1).getName());
        assertEquals("iiis", entityMeta.getPropertyMeta(2).getName());
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_inheritEntity() throws Exception {
        try {
            factory.createEntityMeta(Jjj.class);
            fail();
        } catch (UnsupportedInheritanceRuntimeException e) {
            System.out.println(e);
            assertEquals(Jjj.class, e.getEntityClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCreateEntityMeta_fieldDuplicated() throws Exception {
        try {
            factory.createEntityMeta(Kkk.class);
            fail();
        } catch (FieldDuplicatedRuntimeException e) {
            System.out.println(e);
            assertEquals(Ggg.class.getField("id"), e.getField());
        }
    }

    /**
     * @throws Exception
     */
    public void testCheckMappedBy_oneToOne_mappedByPropertyNotFound()
            throws Exception {
        try {
            factory.getEntityMeta(OneToOnePropertyNotFound.class);
            fail();
        } catch (MappedByPropertyNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("EntityMetaFactoryImplTest$OneToOnePropertyNotFound",
                    e.getEntityName());
            assertEquals("bar", e.getPropertyName());
            assertEquals("xxx", e.getMappedBy());
            assertEquals(BadCcc.class, e.getInverseRelationshipClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCheckMappedBy_oneToOne_mappedByNotIdentical()
            throws Exception {
        try {
            factory.getEntityMeta(OneToOneNotIdentical.class);
            fail();
        } catch (MappedByNotIdenticalRuntimeException e) {
            System.out.println(e);
            assertEquals("EntityMetaFactoryImplTest$OneToOneNotIdentical", e
                    .getEntityName());
            assertEquals("badAaa", e.getPropertyName());
            assertEquals("badCcc", e.getMappedBy());
            assertEquals(BadAaa.class, e.getInverseRelationshipClass());
            assertEquals("badCcc", e.getInversePropertyName());
            assertEquals(BadCcc.class, e.getInversePropertyClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCheckMappedBy_oneToMany_mappedByPropertyNotFound()
            throws Exception {
        try {
            factory.getEntityMeta(OneToManyPropertyNotFound.class);
            fail();
        } catch (MappedByPropertyNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("EntityMetaFactoryImplTest$OneToManyPropertyNotFound",
                    e.getEntityName());
            assertEquals("bar", e.getPropertyName());
            assertEquals("xxx", e.getMappedBy());
            assertEquals(BadCcc.class, e.getInverseRelationshipClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testCheckMappedBy_oneToMany_mappedByNotIdentical()
            throws Exception {
        try {
            factory.getEntityMeta(OneToManyNotIdentical.class);
            fail();
        } catch (MappedByNotIdenticalRuntimeException e) {
            System.out.println(e);
            assertEquals("EntityMetaFactoryImplTest$OneToManyNotIdentical", e
                    .getEntityName());
            assertEquals("badBbb", e.getPropertyName());
            assertEquals("badCcc", e.getMappedBy());
            assertEquals(BadBbb.class, e.getInverseRelationshipClass());
            assertEquals("badCcc", e.getInversePropertyName());
            assertEquals(BadCcc.class, e.getInversePropertyClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        PropertyMeta pm = entityMeta.getPropertyMeta("bbb");
        assertEquals(1, pm.getJoinColumnMetaList().size());
        JoinColumnMeta jcm = pm.getJoinColumnMetaList().get(0);
        assertEquals("BBB_ID", jcm.getName());
        assertEquals("ID", jcm.getReferencedColumnName());
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn_joinColumnNotFound() throws Exception {
        try {
            factory.getEntityMeta(Hoge.class);
            fail();
        } catch (JoinColumnNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge2", e.getEntityName());
            assertEquals("foo", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn_joinColumnAutoConfiguration()
            throws Exception {
        try {
            factory.getEntityMeta(Hoge3.class);
            fail();
        } catch (JoinColumnAutoConfigurationRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge3", e.getEntityName());
            assertEquals("foo", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn_manyToOneFKNotFound() throws Exception {
        try {
            factory.getEntityMeta(Hoge4.class);
            fail();
        } catch (ManyToOneFKNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge4", e.getEntityName());
            assertEquals("aaa", e.getPropertyName());
            assertEquals("AAA_ID", e.getForeignKey());
        }
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn_oneToOneFKNotFound() throws Exception {
        try {
            factory.getEntityMeta(Hoge5.class);
            fail();
        } catch (OneToOneFKNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge5", e.getEntityName());
            assertEquals("aaa", e.getPropertyName());
            assertEquals("AAA_ID", e.getForeignKey());
        }
    }

    /**
     * @throws Exception
     */
    public void testResolveJoinColumn_referencedColumnNotFound()
            throws Exception {
        try {
            factory.getEntityMeta(Hoge6.class);
            fail();
        } catch (ReferencedColumnNameNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge6", e.getEntityName());
            assertEquals("aaa", e.getPropertyName());
            assertEquals("Aaa", e.getInverseEntityName());
            assertEquals("xxx", e.getPrimaryKey());
        }
    }

    /**
     * @throws Exception
     */
    public void testColumnMeta_relationship() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        assertFalse(entityMeta.hasColumnPropertyMeta("bbb"));
    }

    @Entity(name = "Hoge2")
    private static class Hoge {

        /**
         * 
         */
        @OneToOne
        public Foo foo;

        /**
         * 
         */
        @OneToOne
        @JoinColumn
        public Foo foo2;
    }

    @Entity(name = "Hoge3")
    private static class Hoge3 {

        /**
         * 
         */
        @OneToOne
        @JoinColumn
        public Foo foo;
    }

    @Entity(name = "Hoge4")
    private static class Hoge4 {

        /**
         * 
         */
        @ManyToOne
        public Aaa aaa;
    }

    @Entity(name = "Hoge5")
    private static class Hoge5 {

        /**
         * 
         */
        @OneToOne
        public Aaa aaa;
    }

    @Entity(name = "Hoge6")
    private static class Hoge6 {

        /**
         * 
         */
        public Integer aaaId;

        /**
         * 
         */
        @OneToOne
        @JoinColumn(referencedColumnName = "xxx")
        public Aaa aaa;
    }

    @Entity
    private static class Foo {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @Id
        public Integer id2;
    }

    @Entity
    private static class OneToOnePropertyNotFound {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        public Integer aaaId;

        /**
         * 
         */
        @OneToOne(mappedBy = "xxx")
        public BadCcc bar;
    }

    @Entity
    private static class OneToOneNotIdentical {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToOne(mappedBy = "badCcc")
        public BadAaa badAaa;
    }

    @Entity
    private static class OneToManyPropertyNotFound {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToMany(mappedBy = "xxx")
        public List<BadCcc> bar;
    }

    @Entity
    private static class OneToManyNotIdentical {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToMany(mappedBy = "badCcc")
        public List<BadBbb> badBbb;
    }

    @Entity
    private static class BadAaa {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToOne
        public BadCcc badCcc;
    }

    @Entity
    private static class BadBbb {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @ManyToOne
        public BadCcc badCcc;
    }

    @Entity
    private static class BadCcc {

        /**
         * 
         */
        @Id
        public Integer id;
    }
}
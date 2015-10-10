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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.dialect.H2Dialect;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.TableDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.H2GenDialect;
import org.seasar.extension.jdbc.gen.internal.provider.ValueTypeProviderImpl;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class TableDescFactoryImplTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private TableDescFactory tableDescFactory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        PropertyMetaFactoryImpl pmf = new PropertyMetaFactoryImpl();
        pmf.setPersistenceConvention(pc);
        pmf.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(pmf);
        entityMetaFactory.setTableMetaFactory(tmf);

        GenDialect dialect = new H2GenDialect();
        ValueTypeProvider valueTypeProvider = new ValueTypeProviderImpl(
                new H2Dialect());
        ColumnDescFactoryImpl colFactory = new ColumnDescFactoryImpl(dialect,
                valueTypeProvider);
        PrimaryKeyDescFactoryImpl pkFactory = new PrimaryKeyDescFactoryImpl(
                dialect);
        UniqueKeyDescFactoryImpl ukFactory = new UniqueKeyDescFactoryImpl(
                dialect);
        ForeignKeyDescFactoryImpl fkFactory = new ForeignKeyDescFactoryImpl(
                dialect, entityMetaFactory, true);
        SequenceDescFactoryImpl seqFactory = new SequenceDescFactoryImpl(
                dialect, valueTypeProvider);
        IdTableDescFactoryImpl idTabFactory = new IdTableDescFactoryImpl(
                dialect, ukFactory);

        tableDescFactory = new TableDescFactoryImpl(dialect, colFactory,
                pkFactory, ukFactory, fkFactory, seqFactory, idTabFactory);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTableDesc() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertNotNull(tableDesc);
        assertSame(tableDesc, tableDescFactory.getTableDesc(entityMeta));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals("hoge", tableDesc.getCatalogName());
        assertEquals("foo", tableDesc.getSchemaName());
        assertEquals("AAA", tableDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testColumnDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(2, tableDesc.getColumnDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testColumnDescList_multiClass() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ggg.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        List<ColumnDesc> list = tableDesc.getColumnDescList();
        assertEquals(6, list.size());
        assertEquals("ID1", list.get(0).getName());
        assertEquals("ID2", list.get(1).getName());
        assertEquals("ID3", list.get(2).getName());
        assertEquals("NAME3", list.get(3).getName());
        assertEquals("NAME2", list.get(4).getName());
        assertEquals("NAME1", list.get(5).getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testPrimaryKeyDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertNotNull(tableDesc.getPrimaryKeyDesc());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testForeignKeyDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(1, tableDesc.getForeignKeyDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUniqueKeyDescList_uniqueConstraints() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(1, tableDesc.getUniqueKeyDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUniqueKeyDescList_unique() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ddd.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(1, tableDesc.getUniqueKeyDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSequenceDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(1, tableDesc.getSequenceDescList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIdTableDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
        assertEquals(1, tableDesc.getIdTableDescList().size());
    }

    /** */
    @Entity
    @Table(catalog = "hoge", schema = "foo", name = "AAA", uniqueConstraints = { @UniqueConstraint(columnNames = { "BBB_ID" }) })
    public static class Aaa {

        /** */
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        public Integer id;

        /** */
        public Integer bbbId;

        /** */
        @ManyToOne
        public Bbb bbb;
    }

    /** */
    @Entity
    @Table(catalog = "hoge", schema = "foo", name = "BBB")
    public static class Bbb {

        /** */
        @Id
        public Integer id;

    }

    /** */
    @Entity
    public static class Ccc {

        /** */
        @Id
        @GeneratedValue(strategy = GenerationType.TABLE)
        public Integer id;

    }

    /** */
    @Entity
    public static class Ddd {

        /** */
        @Id
        @Column(unique = true)
        public Integer id;

        /** */
        @Column(unique = true)
        public Integer hoge;
    }

    /** */
    @MappedSuperclass
    public static class Eee {

        /** */
        @Id
        public int id1;

        /** */
        public String name1;
    }

    /** */
    @MappedSuperclass
    public static class Fff extends Eee {

        /** */
        @Id
        public int id2;

        /** */
        public String name2;
    }

    /** */
    @Entity
    public static class Ggg extends Fff {

        /** */
        @Id
        public int id3;

        /** */
        public String name3;
    }

}

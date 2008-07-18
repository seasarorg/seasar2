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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.TableDescFactory;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
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

        GenDialect dialect = new StandardGenDialect();
        ColumnDescFactoryImpl colFactory = new ColumnDescFactoryImpl(dialect);
        PrimaryKeyDescFactoryImpl pkFactory = new PrimaryKeyDescFactoryImpl(
                dialect);
        UniqueKeyDescFactoryImpl ukFactory = new UniqueKeyDescFactoryImpl();
        ForeignKeyDescFactoryImpl fkFactory = new ForeignKeyDescFactoryImpl(
                entityMetaFactory);
        SequenceDescFactoryImpl seqFactory = new SequenceDescFactoryImpl(
                dialect);
        IdTableDescFactoryImpl idTabFactory = new IdTableDescFactoryImpl(
                dialect, colFactory, pkFactory, ukFactory);

        tableDescFactory = new TableDescFactoryImpl(colFactory, pkFactory,
                ukFactory, fkFactory, seqFactory, idTabFactory);
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
    public void testUniqueKeyDescList() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
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

}

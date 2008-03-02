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
package org.seasar.extension.jdbc.gen.converter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.TableModelFactory;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect.StandardSqlType;
import org.seasar.extension.jdbc.gen.entity.Eee;
import org.seasar.extension.jdbc.gen.entity.Fff;
import org.seasar.extension.jdbc.gen.entity.Ggg;
import org.seasar.extension.jdbc.gen.entity.Hhh;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
import org.seasar.extension.jdbc.gen.model.RowModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.TableModelFactoryImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class IdGenerationTableModelConverterImplTest {

    private EntityMetaFactoryImpl factory;

    private IdGenerationTableModelConverterImpl converter;

    @Before
    public void setUp() {
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

        TableModelFactory tFactory = new TableModelFactoryImpl();
        GenDialect dialect = new StandardGenDialect(new StandardDialect());
        converter = new IdGenerationTableModelConverterImpl(tFactory, dialect);
    }

    @Test
    public void testConvert_defaultTableGenerator() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Eee.class);
        List<TableModel> list = converter.convert(entityMeta);

        assertEquals(1, list.size());

        TableModel tableModel = list.get(0);
        assertNotNull(tableModel);
        assertNull(tableModel.getSchema());
        assertEquals("ID_GENERATOR", tableModel.getName());

        assertNotNull(tableModel.getPrimaryKeyModel());
        PrimaryKeyModel primaryKeyModel = tableModel.getPrimaryKeyModel();
        assertEquals(1, primaryKeyModel.getColumnNameList().size());
        assertEquals("PK", primaryKeyModel.getColumnNameList().get(0));

        assertEquals(2, tableModel.getColumnModelList().size());
        ColumnModel columnModel = tableModel.getColumnModelList().get(0);
        assertEquals("PK", columnModel.getName());
        assertEquals(StandardSqlType.VARCHAR, columnModel.getSqlType());
        assertEquals("varchar(255)", columnModel.getDefinition());
        assertFalse(columnModel.isNullable());
        assertTrue(columnModel.isUnique());
        columnModel = tableModel.getColumnModelList().get(1);
        assertEquals("VALUE", columnModel.getName());
        assertEquals(StandardSqlType.BIGINT, columnModel.getSqlType());
        assertEquals("bigint", columnModel.getDefinition());
        assertFalse(columnModel.isNullable());
        assertFalse(columnModel.isUnique());

        assertEquals(1, tableModel.getRowModelList().size());
        RowModel rowModel = tableModel.getRowModelList().get(0);
        assertNotNull(rowModel);
        assertEquals(2, rowModel.getValueList().size());
        assertEquals("EEE_ID", rowModel.getValueList().get(0));
        assertEquals(new Integer(0), rowModel.getValueList().get(1));
    }

    @Test
    public void testConvert_customTableGenerator() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Fff.class);
        List<TableModel> list = converter.convert(entityMeta);

        assertEquals(1, list.size());

        TableModel tableModel = list.get(0);
        assertNotNull(tableModel);
        assertEquals("SCHEMA", tableModel.getSchema());
        assertEquals("GENERATOR_TABLE", tableModel.getName());

        assertNotNull(tableModel.getPrimaryKeyModel());
        PrimaryKeyModel primaryKeyModel = tableModel.getPrimaryKeyModel();
        assertEquals(1, primaryKeyModel.getColumnNameList().size());
        assertEquals("PK_COLUMN", primaryKeyModel.getColumnNameList().get(0));

        assertEquals(2, tableModel.getColumnModelList().size());
        ColumnModel columnModel = tableModel.getColumnModelList().get(0);
        assertEquals("PK_COLUMN", columnModel.getName());
        assertEquals(StandardSqlType.VARCHAR, columnModel.getSqlType());
        assertEquals("varchar(255)", columnModel.getDefinition());
        assertFalse(columnModel.isNullable());
        assertTrue(columnModel.isUnique());
        columnModel = tableModel.getColumnModelList().get(1);
        assertEquals("VALUE_COLUMN", columnModel.getName());
        assertEquals(StandardSqlType.BIGINT, columnModel.getSqlType());
        assertEquals("bigint", columnModel.getDefinition());
        assertFalse(columnModel.isNullable());
        assertFalse(columnModel.isUnique());

        assertEquals(1, tableModel.getRowModelList().size());
        RowModel rowModel = tableModel.getRowModelList().get(0);
        assertNotNull(rowModel);
        assertEquals("HOGE", rowModel.getValueList().get(0));
        assertEquals(new Integer(100), rowModel.getValueList().get(1));
    }

    @Test
    public void testConvert_multiUse() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Ggg.class);
        List<TableModel> list = converter.convert(entityMeta);

        assertEquals(1, list.size());
        TableModel tableModel = list.get(0);

        assertEquals(2, tableModel.getRowModelList().size());
        RowModel rowModel = tableModel.getRowModelList().get(0);
        assertNotNull(rowModel);
        assertEquals("GGG_ID", rowModel.getValueList().get(0));
        assertEquals(new Integer(0), rowModel.getValueList().get(1));
        rowModel = tableModel.getRowModelList().get(1);
        assertNotNull(rowModel);
        assertEquals("GGG_ID2", rowModel.getValueList().get(0));
        assertEquals(new Integer(0), rowModel.getValueList().get(1));
    }

    @Test
    public void testConvert_multiTableGenerators() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Hhh.class);
        List<TableModel> list = converter.convert(entityMeta);
        assertEquals(2, list.size());
    }
}

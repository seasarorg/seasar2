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

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.entity.Aaa;
import org.seasar.extension.jdbc.gen.model.ForeignKeyModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
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
public class ForeignKeyModelConverterImplTest {

    private EntityMetaFactoryImpl factory;

    private ForeignKeyModelConverterImpl converter;

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

        converter = new ForeignKeyModelConverterImpl(factory);
    }

    @Test
    public void testConvert() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        PropertyMeta propertyMeta = entityMeta.getPropertyMeta("bbb");
        ForeignKeyModel model = converter.convert(propertyMeta,
                new TableModel());
        assertNotNull(model);
        assertEquals(1, model.getColumnNameList().size());
        assertEquals("BBB_ID", model.getColumnNameList().get(0));
        assertEquals(1, model.getReferencedColumnNameList().size());
        assertEquals("ID", model.getReferencedColumnNameList().get(0));
        assertEquals(null, model.getReferencedSchemaName());
        assertEquals("BBB", model.getReferencedTableName());
    }

    @Test
    public void testConvert_joinColumn() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        PropertyMeta propertyMeta = entityMeta.getPropertyMeta("bbb2");
        ForeignKeyModel model = converter.convert(propertyMeta,
                new TableModel());
        assertNotNull(model);
        assertEquals(1, model.getColumnNameList().size());
        assertEquals("bar", model.getColumnNameList().get(0));
        assertEquals(1, model.getReferencedColumnNameList().size());
        assertEquals("ID", model.getReferencedColumnNameList().get(0));
        assertEquals(null, model.getReferencedSchemaName());
        assertEquals("BBB", model.getReferencedTableName());
    }

    @Test
    public void testConvert_joinColumns() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        PropertyMeta propertyMeta = entityMeta.getPropertyMeta("ccc");
        ForeignKeyModel model = converter.convert(propertyMeta,
                new TableModel());
        assertNotNull(model);
        assertEquals(2, model.getColumnNameList().size());
        assertEquals("hoge", model.getColumnNameList().get(0));
        assertEquals("foo", model.getColumnNameList().get(1));
        assertEquals(2, model.getReferencedColumnNameList().size());
        assertEquals("hogehoge", model.getReferencedColumnNameList().get(0));
        assertEquals("foofoo", model.getReferencedColumnNameList().get(1));
        assertEquals(null, model.getReferencedSchemaName());
        assertEquals("CCC", model.getReferencedTableName());
    }

    @Test
    public void testConvert_nonRelationship() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        ForeignKeyModel model = converter.convert(entityMeta
                .getPropertyMeta("id"), new TableModel());
        assertNull(model);
    }

}

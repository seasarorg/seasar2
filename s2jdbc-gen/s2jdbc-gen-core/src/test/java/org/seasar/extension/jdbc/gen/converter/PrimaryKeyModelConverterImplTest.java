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
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.entity.Aaa;
import org.seasar.extension.jdbc.gen.entity.Ccc;
import org.seasar.extension.jdbc.gen.entity.Ddd;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
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
public class PrimaryKeyModelConverterImplTest {

    private EntityMetaFactoryImpl factory;

    private PrimaryKeyModelConverterImpl converter;

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

        GenDialect dialect = new StandardGenDialect(new StandardDialect());
        converter = new PrimaryKeyModelConverterImpl(dialect);
    }

    @Test
    public void testConvert() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        PrimaryKeyModel model = converter.convert(entityMeta, new TableModel());
        assertNotNull(model);
        assertEquals(1, model.getColumnNameList().size());
        assertEquals("ID", model.getColumnNameList().get(0));
    }

    @Test
    public void testConvert_compositeId() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Ccc.class);
        PrimaryKeyModel model = converter.convert(entityMeta, new TableModel());
        assertNotNull(model);
        assertEquals(2, model.getColumnNameList().size());
        assertEquals("hogehoge", model.getColumnNameList().get(0));
        assertEquals("foofoo", model.getColumnNameList().get(1));
    }

    @Test
    public void testConvert_noId() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Ddd.class);
        PrimaryKeyModel model = converter.convert(entityMeta, new TableModel());
        assertNull(model);
    }
}

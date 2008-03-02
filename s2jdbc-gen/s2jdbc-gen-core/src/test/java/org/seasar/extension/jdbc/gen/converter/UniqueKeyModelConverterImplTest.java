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
import org.seasar.extension.jdbc.gen.entity.Aaa;
import org.seasar.extension.jdbc.gen.entity.Bbb;
import org.seasar.extension.jdbc.gen.entity.Ccc;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.UniqueKeyModel;
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
public class UniqueKeyModelConverterImplTest {

    private EntityMetaFactoryImpl factory;

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
    }

    @Test
    public void testConvert_uniqueConstraint() throws Exception {
        UniqueKeyModelConverterImpl converter = new UniqueKeyModelConverterImpl();
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        List<UniqueKeyModel> list = converter.convert(entityMeta,
                new TableModel());

        assertEquals(1, list.size());

        UniqueKeyModel model = list.get(0);
        assertNotNull(model);
        assertEquals(3, model.getColumnNameList().size());
        assertEquals("aaa", model.getColumnNameList().get(0));
        assertEquals("bbb", model.getColumnNameList().get(1));
        assertEquals("ccc", model.getColumnNameList().get(2));
    }

    @Test
    public void testConvert_uniqueConstraints() throws Exception {
        UniqueKeyModelConverterImpl converter = new UniqueKeyModelConverterImpl();
        EntityMeta entityMeta = factory.getEntityMeta(Bbb.class);
        List<UniqueKeyModel> list = converter.convert(entityMeta,
                new TableModel());

        assertEquals(2, list.size());

        UniqueKeyModel model = list.get(0);
        assertNotNull(model);
        assertEquals(3, model.getColumnNameList().size());
        assertEquals("aaa", model.getColumnNameList().get(0));
        assertEquals("bbb", model.getColumnNameList().get(1));
        assertEquals("ccc", model.getColumnNameList().get(2));

        model = list.get(1);
        assertNotNull(model);
        assertEquals(3, model.getColumnNameList().size());
        assertEquals("ddd", model.getColumnNameList().get(0));
        assertEquals("eee", model.getColumnNameList().get(1));
        assertEquals("fff", model.getColumnNameList().get(2));
    }

    @Test
    public void testConvert_noUniqueConstraint() throws Exception {
        UniqueKeyModelConverterImpl converter = new UniqueKeyModelConverterImpl();
        EntityMeta entityMeta = factory.getEntityMeta(Ccc.class);
        List<UniqueKeyModel> list = converter.convert(entityMeta,
                new TableModel());
        assertTrue(list.isEmpty());
    }
}

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
import org.seasar.extension.jdbc.gen.TableModelFactory;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.entity.Aaa;
import org.seasar.extension.jdbc.gen.model.Key;
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
public class TableModelConverterImplTest {

    private EntityMetaFactoryImpl factory;

    private TableModelConverterImpl converter;

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
        TableModelFactory tFactory = new TableModelFactoryImpl();
        ColumnModelConverterImpl cConverter = new ColumnModelConverterImpl(
                dialect);
        PrimaryKeyModelConverterImpl pkConverter = new PrimaryKeyModelConverterImpl(
                dialect);
        ForeignKeyModelConverterImpl fkConverter = new ForeignKeyModelConverterImpl(
                factory);
        UniqueKeyModelConverterImpl uqConverter = new UniqueKeyModelConverterImpl();
        converter = new TableModelConverterImpl(tFactory, cConverter,
                pkConverter, fkConverter, uqConverter);
    }

    @Test
    public void testConvert() throws Exception {
        EntityMeta entityMeta = factory.getEntityMeta(Aaa.class);
        TableModel model = converter.convert(entityMeta);
        assertEquals("AAA", model.getName());
        assertEquals(null, model.getSchema());
        assertTrue(model.hasPrimaryKeyModel());
        assertNotNull(model.getPrimaryKeyModel());
        assertEquals(3, model.getForeignKeyModelList().size());
        Key key = new Key(new Object[] { "bbb_id" });
        assertTrue(model.hasForeignKeyModel(key));
        key = new Key(new Object[] { "bar" });
        assertTrue(model.hasForeignKeyModel(key));
        key = new Key(new Object[] { "hoge", "foo" });
        assertTrue(model.hasForeignKeyModel(key));
        assertEquals(1, model.getUniqueKeyModelList().size());
        key = new Key(new Object[] { "aaa", "bbb", "ccc" });
        assertTrue(model.hasUniqueKeyModel(key));
    }
}

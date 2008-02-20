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
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityModelConverterTest {

    private EntityModelConverter converter;

    @Before
    public void before() throws Exception {
        PropertyModelConverter propertyModelConverter = new PropertyModelConverter();
        propertyModelConverter
                .setPersistenceConvention(new PersistenceConventionImpl());
        propertyModelConverter.setGenerationDialect(new StandardGenDialect());
        propertyModelConverter.setVersionColumn("VERSION");

        converter = new EntityModelConverter();
        converter.setPropertyModelConverter(propertyModelConverter);
        converter.setPersistenceConvention(new PersistenceConventionImpl());
    }

    @Test
    public void testName() throws Exception {
        TableModel tableModel = new TableModel();
        tableModel.setName("HOGE");
        EntityModel entityModel = converter.convert(tableModel);
        assertEquals("Hoge", entityModel.getName());
    }

    @Test
    public void testPropertyModelSize() throws Exception {
        ColumnModel cm1 = new ColumnModel();
        cm1.setName("BAR");
        ColumnModel cm2 = new ColumnModel();
        cm2.setName("FOO");
        TableModel tableModel = new TableModel();
        tableModel.setName("HOGE");
        tableModel.addColumnModel(cm1);
        tableModel.addColumnModel(cm2);
        EntityModel entityModel = converter.convert(tableModel);
        assertEquals(2, entityModel.getPropertyModelList().size());
    }
}

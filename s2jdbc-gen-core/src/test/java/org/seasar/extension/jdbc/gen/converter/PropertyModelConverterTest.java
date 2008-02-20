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

import java.sql.Types;

import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class PropertyModelConverterTest {

    private PropertyModelConverter converter;

    @Before
    public void before() {
        converter = new PropertyModelConverter();
        converter.setPersistenceConvention(new PersistenceConventionImpl());
        converter.setGenerationDialect(new StandardGenDialect());
        converter.setVersionColumn("VERSION");
    }

    @Test
    public void testName() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("HOGE");
        PropertyModel propertyModel = converter.convert(columnModel);
        assertEquals("hoge", propertyModel.getName());
    }

    @Test
    public void testId() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("hoge");
        columnModel.setPrimaryKey(true);
        PropertyModel propertyModel = converter.convert(columnModel);
        assertTrue(propertyModel.isId());
    }

    @Test
    public void testPropertyClass() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("hoge");
        columnModel.setSqlType(Types.VARCHAR);
        PropertyModel propertyModel = converter.convert(columnModel);
        assertEquals(String.class, propertyModel.getPropertyClass());
    }

    @Test
    public void testTemporalType() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("hoge");
        columnModel.setSqlType(Types.DATE);
        PropertyModel propertyModel = converter.convert(columnModel);
        assertEquals(TemporalType.DATE, propertyModel.getTemporalType());
    }

    @Test
    public void testLob() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("hoge");
        columnModel.setSqlType(Types.BLOB);
        PropertyModel propertyModel = converter.convert(columnModel);
        assertTrue(propertyModel.isLob());
    }

    @Test
    public void testVersion() throws Exception {
        ColumnModel columnModel = new ColumnModel();
        columnModel.setName("version");
        PropertyModel propertyModel = converter.convert(columnModel);
        assertTrue(propertyModel.isVersion());
    }

}

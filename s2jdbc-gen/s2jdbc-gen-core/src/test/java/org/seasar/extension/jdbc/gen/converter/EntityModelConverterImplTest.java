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
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.DbColumnDesc;
import org.seasar.extension.jdbc.gen.model.DbTableDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityModelConverterImplTest {

    private EntityModelConverterImpl converter;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention convention = new PersistenceConventionImpl();
        GenDialect dialect = new StandardGenDialect(new StandardDialect());
        PropertyModelConverterImpl propertyModelConverter = new PropertyModelConverterImpl(
                convention, dialect, "VERSION");
        converter = new EntityModelConverterImpl(convention,
                propertyModelConverter);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        DbTableDesc tableDesc = new DbTableDesc();
        tableDesc.setName("HOGE");
        EntityModel entityModel = converter.convert(tableDesc);
        assertEquals("Hoge", entityModel.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testPropertyModelSize() throws Exception {
        DbColumnDesc columnDesc1 = new DbColumnDesc();
        columnDesc1.setName("BAR");
        DbColumnDesc columnDesc2 = new DbColumnDesc();
        columnDesc2.setName("FOO");
        DbTableDesc tableDesc = new DbTableDesc();
        tableDesc.setName("HOGE");
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);
        EntityModel entityModel = converter.convert(tableDesc);
        assertEquals(2, entityModel.getPropertyModelList().size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetCompositeId() throws Exception {
        DbColumnDesc columnDesc1 = new DbColumnDesc();
        columnDesc1.setName("BAR");
        columnDesc1.setPrimaryKey(true);
        DbColumnDesc columnDesc2 = new DbColumnDesc();
        columnDesc2.setName("FOO");
        columnDesc2.setPrimaryKey(true);
        DbTableDesc tableDesc = new DbTableDesc();
        tableDesc.setName("HOGE");
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);
        EntityModel entityModel = converter.convert(tableDesc);
        assertEquals(2, entityModel.getIdPropertyModelList().size());
        assertTrue(entityModel.hasCompositeId());
    }
}

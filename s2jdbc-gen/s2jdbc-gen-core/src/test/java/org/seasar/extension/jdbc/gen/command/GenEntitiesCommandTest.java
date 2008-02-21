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
package org.seasar.extension.jdbc.gen.command;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenEntitiesCommandTest {

    private GenEntitiesCommand command;

    @Before
    public void setUp() throws Exception {
        command = new GenEntitiesCommand();
        command.setRootPackageName("aaa");
        command.setEntityPackageName("entity");
        command.setEntityBasePackageName("entity.base");
        command.setupDefaults();
        command.dataSource = new MockDataSource();
        command.persistenceConvention = new PersistenceConventionImpl();
        command.dialect = new StandardGenDialect();
    }

    @Test
    public void testCreateEntityCode() throws Exception {
        assertNotNull(command.createEntityCode(new EntityModel()));
    }

    @Test
    public void testCreateEntityBaseCode() throws Exception {
        assertNotNull(command.createEntityBaseCode(new EntityModel()));
    }

    @Test
    public void testCreateSchemaReader() throws Exception {
        assertNotNull(command.createSchemaReader());
    }

    @Test
    public void testCreateEntityModelConverter() throws Exception {
        assertNotNull(command.createEntityModelConverter());
    }

    @Test
    public void testCreatePropertyModelConverter() throws Exception {
        assertNotNull(command.createPropertyModelConverter());
    }

    @Test
    public void testCreateJavaCodeGenerator() throws Exception {
        assertNotNull(command.createJavaCodeGenerator());
    }

    @Test
    public void testGetClassName() throws Exception {
        String className = command.getClassName("aaa", "bbb", "Ccc");
        assertEquals("aaa.bbb.Ccc", className);
    }

    @Test
    public void testGetEntityClassName() throws Exception {
        String className = command.getEntityClassName("Hoge");
        assertEquals("aaa.entity.Hoge", className);
    }

    @Test
    public void testGetEntityBaseClassName() throws Exception {
        String className = command.getEntityBaseClassName("Hoge");
        assertEquals("aaa.entity.base.AbstractHoge", className);
    }
}

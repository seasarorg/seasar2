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
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.DbColumnDesc;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class PropertyModelConverterImplTest {

    private PropertyModelConverterImpl converter;

    /**
     * 
     */
    @Before
    public void setUp() {
        PersistenceConvention convention = new PersistenceConventionImpl();
        GenDialect dialect = new StandardGenDialect(new StandardDialect());
        converter = new PropertyModelConverterImpl(convention, dialect,
                "VERSION");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("HOGE");
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertEquals("hoge", propertyModel.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testId() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("hoge");
        columnDesc.setPrimaryKey(true);
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertTrue(propertyModel.isId());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testPropertyClass() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("hoge");
        columnDesc.setSqlType(Types.VARCHAR);
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertEquals(String.class, propertyModel.getPropertyClass());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTemporalType() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("hoge");
        columnDesc.setSqlType(Types.DATE);
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertEquals(TemporalType.DATE, propertyModel.getTemporalType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testLob() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("hoge");
        columnDesc.setSqlType(Types.BLOB);
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertTrue(propertyModel.isLob());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testVersion() throws Exception {
        DbColumnDesc columnDesc = new DbColumnDesc();
        columnDesc.setName("version");
        PropertyModel propertyModel = converter.convert(columnDesc);
        assertTrue(propertyModel.isVersion());
    }

}

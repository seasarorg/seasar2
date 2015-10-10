/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;

import javax.persistence.Lob;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialect.SqlBlockContext;
import org.seasar.extension.jdbc.gen.internal.provider.ValueTypeProviderImpl;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockConnection;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class PostgreGenDialectTest {

    private PostgreGenDialect dialect = new PostgreGenDialect();

    private PropertyMetaFactoryImpl propertyMetaFactory;

    @Lob
    @SuppressWarnings("unused")
    private String lob;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_integer() throws Exception {
        SqlType type = dialect.getSqlType(Types.INTEGER);
        assertEquals("serial", type.getDataType(10, 0, 0, true));
        assertEquals("integer", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_bigint() throws Exception {
        SqlType type = dialect.getSqlType(Types.BIGINT);
        assertEquals("bigserial", type.getDataType(10, 0, 0, true));
        assertEquals("bigint", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_clob() throws Exception {
        SqlType type = dialect.getSqlType(Types.CLOB);
        assertEquals("text", type.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlType_blob() throws Exception {
        SqlType type = dialect.getSqlType(Types.BLOB);
        assertEquals("oid", type.getDataType(10, 0, 0, false));
        assertEquals(PostgreGenDialect.PostgreBlobType.class, type.getClass());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSqlTypeWithPropertyMeta() throws Exception {
        ValueTypeProviderImpl valueTypeProvider = new ValueTypeProviderImpl(
                new PostgreDialect());
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                getClass().getDeclaredField("lob"), new EntityMeta());
        SqlType sqlType = dialect.getSqlType(valueTypeProvider, propertyMeta);
        assertEquals("text", sqlType.getDataType(10, 0, 0, false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateSqlBlockContext() throws Exception {
        SqlBlockContext context = dialect.createSqlBlockContext();
        assertFalse(context.isInSqlBlock());
        context.addKeyword("$$");
        assertTrue(context.isInSqlBlock());
        context.addKeyword("$$");
        assertFalse(context.isInSqlBlock());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testPostgreBlobType_bindEmptyValue() throws Exception {
        Connection conn = new MockConnection();
        PreparedStatement ps = conn
                .prepareStatement("select * from a where b = ?");
        PostgreGenDialect.PostgreBlobType blobType = new PostgreGenDialect.PostgreBlobType(
                "oid");
        blobType.bindValue(ps, 1, "");
    }
}

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

import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect.StandardSqlType;
import org.seasar.extension.jdbc.gen.entity.Bbb;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ColumnModelConverterImplTest {

    private PropertyMetaFactoryImpl factory;

    private ColumnModelConverterImpl converter;

    private String hoge;

    @Column(name = "foofoo", nullable = false, unique = true, columnDefinition = "VARCHAR2(10)", length = 100, precision = 10, scale = 3)
    private String foo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Transient
    private String bar;

    @ManyToOne
    private Bbb bbb;

    @Before
    public void setUp() {
        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        factory = new PropertyMetaFactoryImpl();
        factory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        factory.setColumnMetaFactory(cmFactory);

        GenDialect dialect = new StandardGenDialect(new StandardDialect());
        converter = new ColumnModelConverterImpl(dialect);
    }

    @Test
    public void testConvert() throws Exception {
        Field field = getClass().getDeclaredField("hoge");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                new EntityMeta());
        ColumnModel model = converter.convert(propertyMeta);
        assertNotNull(model);
        assertEquals("HOGE", model.getName());
        assertEquals(StandardSqlType.VARCHAR, model.getSqlType());
        assertEquals("varchar(255)", model.getDefinition());
        assertTrue(model.isNullable());
        assertFalse(model.isUnique());
    }

    @Test
    public void testConvert_column() throws Exception {
        Field field = getClass().getDeclaredField("foo");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                new EntityMeta());
        ColumnModel model = converter.convert(propertyMeta);
        assertNotNull(model);
        assertEquals("foofoo", model.getName());
        assertEquals(StandardSqlType.VARCHAR, model.getSqlType());
        assertEquals("VARCHAR2(10)", model.getDefinition());
        assertFalse(model.isNullable());
        assertTrue(model.isUnique());
    }

    @Test
    public void testConvert_temporal() throws Exception {
        Field field = getClass().getDeclaredField("timestamp");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                new EntityMeta());
        ColumnModel model = converter.convert(propertyMeta);
        assertNotNull(model);
        assertEquals("TIMESTAMP", model.getName());
        assertEquals(StandardSqlType.TIMESTAMP, model.getSqlType());
        assertEquals("timestamp", model.getDefinition());
        assertTrue(model.isNullable());
        assertFalse(model.isUnique());
    }

    @Test
    public void testConvert_transient() throws Exception {
        Field field = getClass().getDeclaredField("bar");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                new EntityMeta());
        ColumnModel model = converter.convert(propertyMeta);
        assertNull(model);
    }

    @Test
    public void testConvert_relationship() throws Exception {
        Field field = getClass().getDeclaredField("bbb");
        PropertyMeta propertyMeta = factory.createPropertyMeta(field,
                new EntityMeta());
        ColumnModel model = converter.convert(propertyMeta);
        assertNull(model);
    }
}

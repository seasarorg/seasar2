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
package org.seasar.extension.jdbc.gen.internal.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.EntityTestModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityTestModelFactoryImplTest {

    private EntityTestModelFactoryImpl factory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new EntityTestModelFactoryImpl("s2jdbc.dicon", "jdbcManager",
                "Test", new NamesModelFactoryImpl("hoge.entity", "Names"),
                true, false);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetEntityTestModel() throws Exception {
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setName("Foo");
        entityMeta.setEntityClass(getClass());
        EntityTestModel entityTestModel = factory
                .getEntityTestModel(entityMeta);
        assertEquals("s2jdbc.dicon", entityTestModel.getConfigPath());
        assertEquals("jdbcManager", entityTestModel.getJdbcManagerName());
        assertEquals("org.seasar.extension.jdbc.gen.internal.model",
                entityTestModel.getPackageName());
        assertEquals("FooTest", entityTestModel.getShortClassName());
        assertEquals("Foo", entityTestModel.getShortEntityClassName());
        assertEquals(3, entityTestModel.getImportNameSet().size());
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Boolean() {
        assertEquals("true", factory.getExpression(Boolean.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Character() {
        assertEquals("'a'", factory.getExpression(Character.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Byte() {
        assertEquals("(byte) 1", factory.getExpression(Byte.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Short() {
        assertEquals("(short) 1", factory.getExpression(Short.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Integer() {
        assertEquals("1", factory.getExpression(Integer.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Long() {
        assertEquals("1L", factory.getExpression(Long.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Float() {
        assertEquals("1f", factory.getExpression(Float.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Double() {
        assertEquals("1d", factory.getExpression(Double.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_BigDecimal() {
        assertEquals("BigDecimal.ONE", factory.getExpression(BigDecimal.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_BigInteger() {
        assertEquals("BigInteger.ONE", factory.getExpression(BigInteger.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_String() {
        assertEquals("\"aaa\"", factory.getExpression(String.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Date() {
        assertEquals("new Date()", factory.getExpression(Date.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Calender() {
        assertEquals("Calendar.getInstance()",
                factory.getExpression(Calendar.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_sqlDate() {
        assertEquals("Date.valueOf(\"2008-01-01\")",
                factory.getExpression(java.sql.Date.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Time() {
        assertEquals("Time.valueOf(\"12:00:00\")",
                factory.getExpression(Time.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_Timestamp() {
        assertEquals("Timestamp.valueOf(\"2008-01-01 12:00:00\")",
                factory.getExpression(Timestamp.class));
    }

    /**
     * 
     */
    @Test
    public void testGetIdExpression_bytes() {
        assertEquals("new byte[0]", factory.getExpression(byte[].class));
    }
}

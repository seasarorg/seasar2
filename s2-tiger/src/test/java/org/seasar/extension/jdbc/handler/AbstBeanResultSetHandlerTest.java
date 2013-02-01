/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * @author higa
 * 
 */
public class AbstBeanResultSetHandlerTest extends TestCase {

    /**
     * 
     */
    public void testCreatePropertyDescMapWithColumn_publicfield() {
        MyHandler handler = new MyHandler(AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        CaseInsensitiveMap map = handler.createPropertyDescMapWithColumn();
        assertNotNull(map.get("foo2"));
    }

    /**
     * 
     */
    public void testCreatePropertyDescMapWithColumn_property() {
        MyHandler handler = new MyHandler(Aaa2Dto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        CaseInsensitiveMap map = handler.createPropertyDescMapWithColumn();
        assertNotNull(map.get("foo2"));
    }

    /**
     * 
     */
    public void testCreatePropertyDescMapWithColumn_nothing() {
        MyHandler handler = new MyHandler(Aaa3Dto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        CaseInsensitiveMap map = handler.createPropertyDescMapWithColumn();
        assertEquals(0, map.size());
    }

    /**
     * 
     */
    public void testCreatePropertyDescMapWithColumn_emptyName() {
        MyHandler handler = new MyHandler(Aaa4Dto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        CaseInsensitiveMap map = handler.createPropertyDescMapWithColumn();
        assertEquals(0, map.size());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreatePropertyTypes() throws Exception {
        MyHandler handler = new MyHandler(AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO2");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        assertEquals(2, ptypes.length);
        assertEquals("FOO2", ptypes[0].getColumnName());
        assertEquals("foo", ptypes[0].getPropertyName());
        assertEquals("AAA_BBB", ptypes[1].getColumnName());
        assertEquals("aaaBbb", ptypes[1].getPropertyName());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreatePropertyTypes_noNameConversion() throws Exception {
        PersistenceConventionImpl persistenceConvention = new PersistenceConventionImpl();
        persistenceConvention.setNoNameConversion(true);
        MyHandler handler = new MyHandler(Aaa7Dto.class, new StandardDialect(),
                persistenceConvention);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("CCC_DDD");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("EEE_FFF");
        rsMeta.addColumnMetaData(columnMeta);
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        assertEquals(3, ptypes.length);
        assertEquals("AAA_BBB", ptypes[0].getColumnName());
        assertEquals("AAA_BBB", ptypes[0].getPropertyName());
        assertEquals("CCC_DDD", ptypes[1].getColumnName());
        assertEquals("cccDdd", ptypes[1].getPropertyName());
        assertNull(ptypes[2]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreatePropertyTypes_propertyNotExists() throws Exception {
        MyHandler handler = new MyHandler(Aaa5Dto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO2");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        assertEquals(2, ptypes.length);
        assertEquals("FOO2", ptypes[0].getColumnName());
        assertEquals("foo2", ptypes[0].getPropertyName());
        assertNull(ptypes[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRow() throws Exception {
        MyHandler handler = new MyHandler(AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO2");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("FOO2", "111");
        data.put("AAA_BBB", "222");
        rs.addRowData(data);
        rs.next();
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        AaaDto dto = (AaaDto) handler.createRow(rs, ptypes);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRow_propertyNotExists() throws Exception {
        MyHandler handler = new MyHandler(AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO2");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("_rownum");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("FOO2", "111");
        data.put("AAA_BBB", "222");
        rs.addRowData(data);
        rs.next();
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        AaaDto dto = (AaaDto) handler.createRow(rs, ptypes);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        MyHandler handler = new MyHandler(Aaa6Dto.class, new StandardDialect(),
                new PersistenceConventionImpl());
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Aaa6Dto.class);
        PropertyDesc propertyDesc = beanDesc.getPropertyDesc("aaa");
        assertEquals(ValueTypes.STRING, handler.getValueType(propertyDesc));
        propertyDesc = beanDesc.getPropertyDesc("bbb");
        assertEquals(ValueTypes.CLOB, handler.getValueType(propertyDesc));
        propertyDesc = beanDesc.getPropertyDesc("ccc");
        assertEquals(ValueTypes.DATE_TIME, handler.getValueType(propertyDesc));
    }

    private static class MyHandler extends AbstractBeanResultSetHandler {

        /**
         * @param beanClass
         * @param dialect
         * @param persistenceConvention
         */
        public MyHandler(Class<?> beanClass, DbmsDialect dialect,
                PersistenceConvention persistenceConvention) {
            super(beanClass, dialect, persistenceConvention,
                    "select * from aaa");
        }

        public Object handle(ResultSet resultSet) throws SQLException {
            return null;
        }

    }

    private static class Aaa2Dto {

        /**
         * 
         */
        @Column(name = "foo2")
        private String foo;

        /**
         * @return the foo
         */
        public String getFoo() {
            return foo;
        }

        /**
         * @param foo
         *            the foo to set
         */
        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    private static class Aaa3Dto {

        /**
         * 
         */
        public String foo;
    }

    private static class Aaa4Dto {

        /**
         * 
         */
        @Column(name = "")
        public String foo;
    }

    private static class Aaa5Dto {

        /**
         * 
         */
        public String foo2;
    }

    private static class Aaa6Dto {

        /**
         * 
         */
        public String aaa;

        /**
         * 
         */
        @Lob
        public String bbb;

        /**
         * 
         */
        @Temporal(TemporalType.TIME)
        public Date ccc;
    }

    private static class Aaa7Dto {

        /**
         * 
         */
        public String AAA_BBB;

        /**
         * 
         */
        @Column(name = "CCC_DDD")
        public String cccDdd;

        /**
         * 
         */
        public String eeeFff;
    }

}

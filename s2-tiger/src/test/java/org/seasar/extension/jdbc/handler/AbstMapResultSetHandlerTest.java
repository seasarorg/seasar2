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
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class AbstMapResultSetHandlerTest extends TestCase {

    /**
     * 
     */
    public void testMapClass() {
        MyHandler handler = new MyHandler(Map.class);
        assertEquals(BeanMap.class, handler.mapClass);
    }

    /**
     * 
     */
    public void testMapClass_notAbstract() {
        MyHandler handler = new MyHandler(HashMap.class);
        assertEquals(HashMap.class, handler.mapClass);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreatePropertyTypes() throws Exception {
        MyHandler handler = new MyHandler(Map.class);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO");
        columnMeta.setColumnType(Types.INTEGER);
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        assertEquals(2, ptypes.length);
        assertEquals("FOO", ptypes[0].getColumnName());
        assertEquals("foo", ptypes[0].getPropertyName());
        assertEquals(ValueTypes.INTEGER, ptypes[0].getValueType());
        assertEquals("AAA_BBB", ptypes[1].getColumnName());
        assertEquals("aaaBbb", ptypes[1].getPropertyName());
        assertEquals(ValueTypes.STRING, ptypes[1].getValueType());
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testCreateRow() throws Exception {
        MyHandler handler = new MyHandler(Map.class);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("FOO");
        columnMeta.setColumnType(Types.INTEGER);
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA_BBB");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ROWNUMBER_");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("FOO", "111");
        data.put("AAA_BBB", "222");
        data.put("ROWNUMBER_", 1);
        rs.addRowData(data);
        rs.next();
        PropertyType[] ptypes = handler.createPropertyTypes(rsMeta);
        Map<String, Object> map = (Map<String, Object>) handler.createRow(rs,
                ptypes);
        assertEquals(2, map.size());
        assertEquals(111, map.get("foo"));
        assertEquals("222", map.get("aaaBbb"));
        assertFalse(map.containsKey("rownumber_"));
    }

    private static class MyHandler extends AbstractMapResultSetHandler {

        /**
         * @param mapClass
         * @param dialect
         * @param persistenceConvention
         * @param sql
         */
        @SuppressWarnings("unchecked")
        public MyHandler(Class<? extends Map> mapClass) {
            super(mapClass, new StandardDialect(),
                    new PersistenceConventionImpl(), null);
        }

        public Object handle(ResultSet resultSet) throws SQLException {
            return null;
        }
    }
}

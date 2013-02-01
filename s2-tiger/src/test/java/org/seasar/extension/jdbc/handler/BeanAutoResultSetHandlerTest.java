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

import java.lang.reflect.Field;
import java.sql.ResultSet;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.mapper.EntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class BeanAutoResultSetHandlerTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testHandle() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2 },
                new int[] { 0 });

        BeanAutoResultSetHandler handler = new BeanAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa");
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("ID", new Integer(1));
        data.put("NAME", "SCOTT");
        rs.addRowData(data);
        Aaa aaa = (Aaa) handler.handle(rs);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("SCOTT", aaa.name);
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandle_uniqueResult() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2 },
                new int[] { 0 });

        BeanAutoResultSetHandler handler = new BeanAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa");
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap data1 = new ArrayMap();
        data1.put("ID", new Integer(1));
        data1.put("NAME", "SCOTT");
        rs.addRowData(data1);
        ArrayMap data2 = new ArrayMap();
        data2.put("ID", new Integer(1));
        data2.put("NAME", "SCOTT");
        rs.addRowData(data2);
        Aaa aaa = (Aaa) handler.handle(rs);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("SCOTT", aaa.name);
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandle_nonUniqueResult() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2 },
                new int[] { 0 });

        BeanAutoResultSetHandler handler = new BeanAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa");
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap data1 = new ArrayMap();
        data1.put("ID", new Integer(1));
        data1.put("NAME", "SCOTT");
        rs.addRowData(data1);
        ArrayMap data2 = new ArrayMap();
        data2.put("ID", new Integer(2));
        data2.put("NAME", "TIGER");
        rs.addRowData(data2);
        try {
            handler.handle(rs);
            fail();
        } catch (SNonUniqueResultException e) {
            System.out.println(e);
            assertEquals("select * from aaa", e.getSql());
        }
    }

}

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
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.entity.Aaa;
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
public class BeanListAutoResultSetHandlerTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
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

        BeanListAutoResultSetHandler handler = new BeanListAutoResultSetHandler(
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
        rs.addRowData(data);
        List list = (List) handler.handle(rs);
        assertEquals(1, list.size());
        Aaa aaa = (Aaa) list.get(0);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("SCOTT", aaa.name);
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandleWithLimit() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2 },
                new int[] { 0 });

        BeanListAutoResultSetHandler handler = new BeanListAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa", 1);
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
        rs.addRowData(data);
        data = new ArrayMap();
        data.put("ID", new Integer(2));
        data.put("NAME", "SCOTT2");
        rs.addRowData(data);
        List list = (List) handler.handle(rs);
        assertEquals(1, list.size());
        Aaa aaa = (Aaa) list.get(0);
        assertEquals(new Integer(1), aaa.id);
        assertEquals("SCOTT", aaa.name);
    }

}

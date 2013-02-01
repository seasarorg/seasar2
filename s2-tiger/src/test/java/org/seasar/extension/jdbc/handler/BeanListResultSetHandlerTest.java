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

import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class BeanListResultSetHandlerTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandle() throws Exception {
        BeanListResultSetHandler handler = new BeanListResultSetHandler(
                AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl(), "select * from aaa");
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
        List list = (List) handler.handle(rs);
        assertEquals(1, list.size());
        AaaDto dto = (AaaDto) list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandleWithLimit() throws Exception {
        BeanListResultSetHandler handler = new BeanListResultSetHandler(
                AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl(), "select * from aaa", 1);
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
        rs.addRowData(data);
        List list = (List) handler.handle(rs);
        assertEquals(1, list.size());
        AaaDto dto = (AaaDto) list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
    }

}

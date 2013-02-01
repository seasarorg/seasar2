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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
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
public class BeanIterationResultSetHandlerTest extends TestCase {

    int count;

    List<AaaDto> list = new ArrayList<AaaDto>();

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandle() throws Exception {
        BeanIterationResultSetHandler handler = new BeanIterationResultSetHandler(
                AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl(), "select * from aaa", 0,
                new IterationCallback<AaaDto, Integer>() {

                    public Integer iterate(AaaDto entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(new Integer(4), count);
        assertEquals(4, list.size());

        AaaDto dto = list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("AAA", dto.aaaBbb);

        dto = list.get(1);
        assertEquals("222", dto.foo);
        assertEquals("BBB", dto.aaaBbb);

        dto = list.get(2);
        assertEquals("333", dto.foo);
        assertEquals("CCC", dto.aaaBbb);

        dto = list.get(3);
        assertEquals("444", dto.foo);
        assertEquals("DDD", dto.aaaBbb);
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandle_WithLimit() throws Exception {
        BeanIterationResultSetHandler handler = new BeanIterationResultSetHandler(
                AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl(), "select * from aaa", 2,
                new IterationCallback<AaaDto, Integer>() {

                    public Integer iterate(AaaDto entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(new Integer(2), count);
        assertEquals(2, list.size());

        AaaDto dto = list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("AAA", dto.aaaBbb);

        dto = list.get(1);
        assertEquals("222", dto.foo);
        assertEquals("BBB", dto.aaaBbb);
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    public void testHandle_WithExit() throws Exception {
        BeanIterationResultSetHandler handler = new BeanIterationResultSetHandler(
                AaaDto.class, new StandardDialect(),
                new PersistenceConventionImpl(), "select * from aaa", 0,
                new IterationCallback<AaaDto, Integer>() {

                    public Integer iterate(AaaDto entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        if (list.size() == 2) {
                            context.setExit(true);
                        }
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(new Integer(2), count);
        assertEquals(2, list.size());

        AaaDto dto = list.get(0);
        assertEquals("111", dto.foo);
        assertEquals("AAA", dto.aaaBbb);

        dto = list.get(1);
        assertEquals("222", dto.foo);
        assertEquals("BBB", dto.aaaBbb);
    }

    /**
     * @return
     */
    private MockResultSet createResultSet() {
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
        data.put("AAA_BBB", "AAA");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("FOO2", "222");
        data.put("AAA_BBB", "BBB");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("FOO2", "333");
        data.put("AAA_BBB", "CCC");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("FOO2", "444");
        data.put("AAA_BBB", "DDD");
        rs.addRowData(data);
        return rs;
    }

}

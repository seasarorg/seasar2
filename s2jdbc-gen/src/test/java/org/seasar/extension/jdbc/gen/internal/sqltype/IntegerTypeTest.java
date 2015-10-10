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
package org.seasar.extension.jdbc.gen.internal.sqltype;

import org.junit.Test;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class IntegerTypeTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetValue() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        rowData.put("hoge", 1);
        rs.addRowData(rowData);
        rs.next();
        IntegerType type = new IntegerType();
        assertEquals("1", type.getValue(rs, 1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetValue_null() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        rowData.put("hoge", null);
        rs.addRowData(rowData);
        rs.next();
        IntegerType type = new IntegerType();
        assertNull(type.getValue(rs, 1));
    }
}

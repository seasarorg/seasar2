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
package org.seasar.extension.jdbc.gen.model;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.DumpModel;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumpModelFactoryImplTest {

    private GenDialect dialect;

    private DumpModelFactoryImpl factory;

    /**
     * 
     */
    @Before
    public void setUp() {
        dialect = new StandardGenDialect();
        factory = new DumpModelFactoryImpl(dialect, ',');
    }

    /**
     * 
     */
    @Test
    public void testBuildSql() {
        ColumnDesc columnDesc1 = new ColumnDesc();
        columnDesc1.setName("column1");
        columnDesc1.setSqlType(dialect.getSqlType(Types.VARCHAR));

        ColumnDesc columnDesc2 = new ColumnDesc();
        columnDesc2.setName("column2");
        columnDesc2.setSqlType(dialect.getSqlType(Types.INTEGER));

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);

        assertEquals(
                "select \"column1\", \"column2\" from \"AAA\".\"BBB\".\"HOGE\"",
                factory.buildSql(tableDesc));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAddRows() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap row = new ArrayMap();
        row.put("column1", "hoge");
        row.put("column2", 100);
        rs.addRowData(row);
        row = new ArrayMap();
        row.put("column1", "f\"oo");
        row.put("column2", 200);
        rs.addRowData(row);

        DumpModel dumpModel = new DumpModel();
        List<SqlType> sqlTypeList = Arrays.asList(dialect
                .getSqlType(Types.VARCHAR), dialect.getSqlType(Types.INTEGER));
        factory.addRows(dumpModel, sqlTypeList, rs);

        assertEquals(2, dumpModel.getRowList().size());
        assertEquals(Arrays.asList("hoge", "100"), dumpModel.getRowList()
                .get(0));
        assertEquals(Arrays.asList("\"f\"\"oo\"", "200"), dumpModel
                .getRowList().get(1));
    }
}

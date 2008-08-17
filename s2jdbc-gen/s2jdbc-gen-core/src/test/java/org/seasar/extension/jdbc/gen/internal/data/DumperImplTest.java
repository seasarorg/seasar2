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
package org.seasar.extension.jdbc.gen.internal.data;

import java.sql.Types;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DumperImplTest {

    private GenDialect dialect;

    private DumperImpl dumper;

    /**
     * 
     */
    @Before
    public void setUp() {
        dialect = new StandardGenDialect();
        dumper = new DumperImpl(dialect, "UTF-8");
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
        tableDesc.setFullName("AAA.BBB.HOGE");
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);

        assertEquals("select column1, column2 from AAA.BBB.HOGE", dumper
                .buildSql(tableDesc));
    }
}

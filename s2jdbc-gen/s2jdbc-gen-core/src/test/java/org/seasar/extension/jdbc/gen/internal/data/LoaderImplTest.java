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

import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.internal.data.LoaderImpl;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class LoaderImplTest {

    /**
     * 
     */
    @Test
    public void testBuildSql() {
        ColumnDesc columnDesc1 = new ColumnDesc();
        columnDesc1.setName("foo");

        ColumnDesc columnDesc2 = new ColumnDesc();
        columnDesc2.setName("BAR");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.addColumnDesc(columnDesc1);
        tableDesc.addColumnDesc(columnDesc2);

        LoaderImpl loader = new LoaderImpl(new StandardGenDialect(), "UTF-8",
                10);
        String sql = loader.buildSql(tableDesc, Arrays.asList("FOO", "BAR"));
        assertEquals(
                "insert into \"AAA\".\"BBB\".\"HOGE\" (\"FOO\", \"BAR\") values (?, ?)",
                sql);
    }
}

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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class LoaderImplTest {

    private LoaderImpl loader = new LoaderImpl(new StandardGenDialect(),
            "UTF-8", 10, false);

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

        String sql = loader.buildSql(tableDesc, Arrays.asList("FOO", "BAR"));
        assertEquals("insert into AAA.BBB.HOGE (FOO, BAR) values (?, ?)", sql);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIsTarget() throws Exception {
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCanonicalName("aaa.bbb.ccc");
        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);
        File match = new File("aaa.bbb.ccc.csv");
        File unmatch = new File("xxx.csv");

        assertTrue(loader.isTarget(databaseDesc, match));
        assertFalse(loader.isTarget(databaseDesc, unmatch));
    }
}

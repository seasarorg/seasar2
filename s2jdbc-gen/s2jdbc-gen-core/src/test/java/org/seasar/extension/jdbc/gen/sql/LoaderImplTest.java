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
package org.seasar.extension.jdbc.gen.sql;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class LoaderImplTest {

    private LoaderImpl loader;

    @Before
    public void setUp() {
    }

    /**
     * Test method for
     * {@link org.seasar.extension.jdbc.gen.sql.dump.LoaderImpl#createDumpFileMap()}
     * .
     */
    @Test
    public void testCreateDumpFileMap() {
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setCatalogName("AAA");
        tableDesc2.setSchemaName("BBB");
        tableDesc2.setName("FOO");

        String path = getClass().getPackage().getName().replace('.', '/')
                + "/dump";
        File dumpDir = ResourceUtil.getResourceAsFile(path);

        LoaderImpl loader = new LoaderImpl(dumpDir, "UTF-8", Arrays.asList(
                tableDesc, tableDesc2));
        Map<String, File> map = loader.createDumpFileMap();
        assertEquals(2, map.size());
        assertEquals("aaa.bbb.hoge.csv", map.get("AAA.BBB.HOGE").getName());
        assertEquals("AAA.BBB.FOO.csv", map.get("AAA.BBB.FOO").getName());
    }

    /**
     * Test method for
     * {@link org.seasar.extension.jdbc.gen.sql.dump.LoaderImpl#buildSql(org.seasar.extension.jdbc.gen.TableDesc, java.util.List)}
     * .
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

        LoaderImpl loader = new LoaderImpl(new File("dumpDir"), "UTF-8", Arrays
                .asList(tableDesc));
        String sql = loader.buildSql(tableDesc, Arrays.asList("FOO", "BAR"));
        assertEquals("insert into AAA.BBB.HOGE (FOO, BAR) values (?, ?)", sql);
    }

}

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
package org.seasar.extension.jdbc.gen.internal.util;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class TableUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testBuildCanonicalTableName() throws Exception {
        GenDialect dialect = new StandardGenDialect();
        assertEquals("aaa.bbb.ccc", TableUtil.buildCanonicalTableName(dialect,
                "AAA", "BBB", "CCC"));
        assertEquals("bbb.ccc", TableUtil.buildCanonicalTableName(dialect,
                null, "BBB", "CCC"));
        assertEquals("aaa..ccc", TableUtil.buildCanonicalTableName(dialect,
                "AAA", null, "CCC"));
        assertEquals("ccc", TableUtil.buildCanonicalTableName(dialect, null,
                null, "CCC"));
        assertEquals("aaa.bbb.ccc", TableUtil.buildCanonicalTableName(dialect,
                "\"AAA\"", "\"BBB\"", "\"CCC\""));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testFullTableName() throws Exception {
        assertEquals("AAA.BBB.CCC", TableUtil.buildFullTableName("AAA", "BBB",
                "CCC"));
        assertEquals("BBB.CCC", TableUtil
                .buildFullTableName(null, "BBB", "CCC"));
        assertEquals("AAA.CCC", TableUtil
                .buildFullTableName("AAA", null, "CCC"));
        assertEquals("CCC", TableUtil.buildFullTableName(null, null, "CCC"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSplitCanonicalTableName() throws Exception {
        String[] elements = TableUtil.splitCanonicalTableName("aaa.bbb.ccc");
        assertEquals(3, elements.length);
        assertEquals("aaa", elements[0]);
        assertEquals("bbb", elements[1]);
        assertEquals("ccc", elements[2]);

        elements = TableUtil.splitCanonicalTableName("bbb.ccc");
        assertEquals(3, elements.length);
        assertEquals(null, elements[0]);
        assertEquals("bbb", elements[1]);
        assertEquals("ccc", elements[2]);

        elements = TableUtil.splitCanonicalTableName("aaa..ccc");
        assertEquals(3, elements.length);
        assertEquals("aaa", elements[0]);
        assertEquals(null, elements[1]);
        assertEquals("ccc", elements[2]);

        elements = TableUtil.splitCanonicalTableName("ccc");
        assertEquals(3, elements.length);
        assertEquals(null, elements[0]);
        assertEquals(null, elements[1]);
        assertEquals("ccc", elements[2]);
    }
}

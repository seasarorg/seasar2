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
package org.seasar.extension.jdbc;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class TableMetaTest extends TestCase {

    /**
     * 
     */
    public void testGetFullName() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("HOGE");
        assertEquals("HOGE", tableMeta.getFullName());
    }

    /**
     * 
     */
    public void testGetFullName_schema() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("HOGE");
        tableMeta.setSchema("SCOTT");
        assertEquals("SCOTT.HOGE", tableMeta.getFullName());
    }

    /**
     * 
     */
    public void testGetFullName_catalog() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("HOGE");
        tableMeta.setCatalog("CATALOG");
        assertEquals("CATALOG.HOGE", tableMeta.getFullName());
    }

    /**
     * 
     */
    public void testGetFullName_catalog_schema() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("HOGE");
        tableMeta.setCatalog("CATALOG");
        tableMeta.setSchema("SCOTT");
        assertEquals("CATALOG.SCOTT.HOGE", tableMeta.getFullName());
    }
}

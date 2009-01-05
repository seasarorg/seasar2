/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.datasource.impl;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 * 
 */
public class DataSourceFactoryImplTest extends S2FrameworkTestCase {

    private DataSourceFactoryImpl factory;

    protected void setUp() {
        include("jdbc.dicon");
        register(DataSourceFactoryImpl.class);
    }

    /**
     * @throws Exception
     */
    public void testGetDataSourceName() throws Exception {
        assertNull(factory.getDataSourceName(null));
        assertEquals("oracle", factory.getDataSourceName("oracle"));
        factory.setSelectableDataSourceName("hoge");
        assertEquals("hoge", factory.getDataSourceName(null));
    }

    /**
     * @throws Exception
     */
    public void testGetDataSourceComponentName() throws Exception {
        assertEquals("dataSource", factory.getDataSourceComponentName(null));
        assertEquals("oracleDataSource", factory
                .getDataSourceComponentName("oracle"));
    }

    /**
     * @throws Exception
     */
    public void testGetDataSource() throws Exception {
        assertNotNull(factory.getDataSource(null));
    }
}
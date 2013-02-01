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
package org.seasar.extension.jdbc.service;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class S2AbstractServiceTest extends S2TestCase {

    private EmpService empDao;

    @Override
    protected void setUp() {
        include("s2jdbc.dicon");
        register(EmpService.class);
    }

    /**
     * @throws Exception
     */
    public void testSelect() throws Exception {
        assertNotNull(empDao.select());
    }

    /**
     * @throws Exception
     */
    public void testSqlFilePathPrefix() throws Exception {
        assertEquals("META-INF/sql/org/seasar/extension/jdbc/entity/Emp/",
                empDao.sqlFilePathPrefix);
    }
}
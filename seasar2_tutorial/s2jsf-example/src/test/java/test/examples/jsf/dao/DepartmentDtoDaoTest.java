/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package test.examples.jsf.dao;

import org.seasar.dao.unit.S2DaoTestCase;

import examples.jsf.dao.DepartmentDtoDao;

/**
 * @author higa
 *
 */
public class DepartmentDtoDaoTest extends S2DaoTestCase {

    private DepartmentDtoDao departmentDtoDao_;

    public void setUpContainer() throws Throwable {
        setServletContext(new MyMockServletContextImpl());
        super.setUpContainer();
    }

    protected void setUp() throws Exception {
        super.setUp();
        include("app.dicon");
    }

    public void testGetDname() throws Exception {
        assertNotNull("1", departmentDtoDao_.getDname(new Integer(10)));
    }

}

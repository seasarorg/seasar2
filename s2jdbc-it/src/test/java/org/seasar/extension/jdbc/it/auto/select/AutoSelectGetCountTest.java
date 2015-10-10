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
package org.seasar.extension.jdbc.it.auto.select;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectGetCountTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testSingleKey() throws Exception {
        long count = jdbcManager.from(Department.class).getCount();
        assertEquals(4L, count);
    }

    /**
     * 
     * @throws Exception
     */
    public void testCompKey() throws Exception {
        long count = jdbcManager.from(CompKeyEmployee.class).getCount();
        assertEquals(14L, count);
    }

    /**
     * 
     * @throws Exception
     */
    public void testNoId() throws Exception {
        long count = jdbcManager.from(NoId.class).getCount();
        assertEquals(2L, count);
    }

    /**
     * 
     * @throws Exception
     */
    public void testOneToMany() throws Exception {
        long count =
            jdbcManager
                .from(Department.class)
                .innerJoin("employees")
                .getCount();
        assertEquals(14L, count);
    }

}

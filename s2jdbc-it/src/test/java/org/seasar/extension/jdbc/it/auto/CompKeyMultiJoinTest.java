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
package org.seasar.extension.jdbc.it.auto;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.CompKeyEmployee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class CompKeyMultiJoinTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testJoin_nest() throws Exception {
        List<CompKeyDepartment> list =
            jdbcManager.from(CompKeyDepartment.class).leftOuterJoin("employees").leftOuterJoin(
                "employees.address").getResultList();
        assertEquals(4, list.size());
        assertNotNull(list.get(0).employees);
        assertNotNull(list.get(0).employees.get(0).address);
    }

    /**
     * 
     * @throws Exception
     */
    public void testJoin_star() throws Exception {
        List<CompKeyEmployee> list =
            jdbcManager
                .from(CompKeyEmployee.class)
                .join("manager", JoinType.INNER)
                .leftOuterJoin("department")
                .leftOuterJoin("address")
                .getResultList();
        assertEquals(13, list.size());
        assertNotNull(list.get(0).department);
        assertNotNull(list.get(0).manager);
        assertNotNull(list.get(0).address);
    }
}

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
package org.seasar.extension.jdbc.it.auto.select.join;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.it.entity.ConcreateAddress;
import org.seasar.extension.jdbc.it.entity.ConcreateEmployee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class MappedSuperclassOneToOneTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_fromOwnerToInverse() throws Exception {
        List<ConcreateEmployee> list =
            jdbcManager
                .from(ConcreateEmployee.class)
                .leftOuterJoin("address")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreateEmployee e : list) {
            assertNotNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_fromOwnerToInverse_noFetch() throws Exception {
        List<ConcreateEmployee> list =
            jdbcManager.from(ConcreateEmployee.class).leftOuterJoin(
                "address",
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreateEmployee e : list) {
            assertNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fromOwnerToInverse() throws Exception {
        List<ConcreateEmployee> list =
            jdbcManager
                .from(ConcreateEmployee.class)
                .innerJoin("address")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreateEmployee e : list) {
            assertNotNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fromOwnerToInverse_noFetch() throws Exception {
        List<ConcreateEmployee> list =
            jdbcManager.from(ConcreateEmployee.class).join(
                "address",
                JoinType.INNER,
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreateEmployee e : list) {
            assertNull(e.address);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_fromInverseToOwner() throws Exception {
        List<ConcreateAddress> list =
            jdbcManager
                .from(ConcreateAddress.class)
                .leftOuterJoin("employee")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreateAddress e : list) {
            assertNotNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_fromInverseToOwner_noFetch() throws Exception {
        List<ConcreateAddress> list =
            jdbcManager.from(ConcreateAddress.class).leftOuterJoin(
                "employee",
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreateAddress e : list) {
            assertNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fromInverseToOwner() throws Exception {
        List<ConcreateAddress> list =
            jdbcManager
                .from(ConcreateAddress.class)
                .innerJoin("employee")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreateAddress e : list) {
            assertNotNull(e.employee);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_fromInverseToOwner_noFetch() throws Exception {
        List<ConcreateAddress> list =
            jdbcManager.from(ConcreateAddress.class).innerJoin(
                "employee",
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreateAddress e : list) {
            assertNull(e.employee);
        }
    }
}

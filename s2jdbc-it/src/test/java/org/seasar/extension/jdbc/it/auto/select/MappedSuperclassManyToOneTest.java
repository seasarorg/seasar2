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

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.ConcreteEmployee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class MappedSuperclassManyToOneTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager
                .from(ConcreteEmployee.class)
                .leftOuterJoin("department")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreteEmployee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_noFetch() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager.from(ConcreteEmployee.class).leftOuterJoin(
                "department",
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreteEmployee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager
                .from(ConcreteEmployee.class)
                .innerJoin("department")
                .getResultList();
        assertEquals(14, list.size());
        for (ConcreteEmployee e : list) {
            assertNotNull(e);
            assertNotNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_noFetch() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager.from(ConcreteEmployee.class).innerJoin(
                "department",
                false).getResultList();
        assertEquals(14, list.size());
        for (ConcreteEmployee e : list) {
            assertNotNull(e);
            assertNull(e.department);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testInnerJoin_self() throws Exception {
        List<ConcreteEmployee> list =
            jdbcManager
                .from(ConcreteEmployee.class)
                .innerJoin("manager")
                .getResultList();
        assertEquals(13, list.size());
        for (ConcreteEmployee e : list) {
            assertNotNull(e);
            assertNotNull(e.manager);
        }
    }

}

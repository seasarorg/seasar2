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
package org.seasar.extension.jdbc.it.sqlfile.select;

import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlFileSelectGetResultTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_getResultList_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getResultList_NoResultException.sql";
        try {
            jdbcManager
                .selectBySqlFile(Employee.class, path)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_getSingleResult.sql";
        Employee employee =
            jdbcManager.selectBySqlFile(Employee.class, path).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_NonUniqueResultException()
            throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NonUniqueResultException.sql";
        try {
            jdbcManager.selectBySqlFile(Employee.class, path).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NoResultException.sql";
        try {
            jdbcManager
                .selectBySqlFile(Employee.class, path)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_null() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_null.sql";
        Employee employee =
            jdbcManager.selectBySqlFile(Employee.class, path).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getResultList_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getResultList_NoResultException.sql";
        try {
            jdbcManager
                .selectBySqlFile(Map.class, path)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_getSingleResult.sql";
        Map<?, ?> employee =
            jdbcManager.selectBySqlFile(Map.class, path).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_NonUniqueResultException()
            throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NonUniqueResultException.sql";
        try {
            jdbcManager.selectBySqlFile(Map.class, path).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NoResultException.sql";
        try {
            jdbcManager
                .selectBySqlFile(Map.class, path)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_null() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_null.sql";
        Map<?, ?> employee =
            jdbcManager.selectBySqlFile(Map.class, path).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getResultList_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getResultList_NoResultException2.sql";
        try {
            jdbcManager
                .selectBySqlFile(Integer.class, path)
                .disallowNoResult()
                .getResultList();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult() throws Exception {
        String path =
            getClass().getName().replace(".", "/") + "_getSingleResult2.sql";
        Integer employeeId =
            jdbcManager.selectBySqlFile(Integer.class, path).getSingleResult();
        assertNotNull(employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_NonUniqueResultException()
            throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NonUniqueResultException2.sql";
        try {
            jdbcManager.selectBySqlFile(Integer.class, path).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_NoResultException() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_NoResultException2.sql";
        try {
            jdbcManager
                .selectBySqlFile(Integer.class, path)
                .disallowNoResult()
                .getSingleResult();
            fail();
        } catch (NoResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_null() throws Exception {
        String path =
            getClass().getName().replace(".", "/")
                + "_getSingleResult_null2.sql";
        Integer employeeId =
            jdbcManager.selectBySqlFile(Integer.class, path).getSingleResult();
        assertNull(employeeId);
    }
}

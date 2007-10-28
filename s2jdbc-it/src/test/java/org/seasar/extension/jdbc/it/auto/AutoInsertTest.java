/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.JdbcManagerImplementor;
import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.it.entity.AutoStrategy;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department2;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.IdentityStrategy;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy2;
import org.seasar.extension.jdbc.it.entity.TableStrategy;
import org.seasar.extension.jdbc.it.entity.TableStrategy2;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author taedium
 * 
 */
public class AutoInsertTest extends S2TestCase {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor jdbcManagerImplementor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jdbc.dicon");
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecuteTx() throws Exception {
        Department department = new Department();
        department.departmentId = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).execute();
        assertEquals(1, result);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludesNullTx() throws Exception {
        Department department = new Department();
        department.departmentId = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).excludesNull().execute();
        assertEquals(1, result);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertEquals("TOKYO", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includesTx() throws Exception {
        Department department = new Department();
        department.departmentId = 99;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result =
            jdbcManager.insert(department).includes(
                "departmentId",
                "departmentNo",
                "location",
                "version").execute();
        assertEquals(1, result);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertNull(department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludesTx() throws Exception {
        Department department = new Department();
        department.departmentId = 99;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        int result =
            jdbcManager.insert(department).excludes(
                "departmentName",
                "location").execute();
        assertEquals(1, result);
        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertNull(department.departmentName);
        assertEquals("TOKYO", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testCompKeyTx() throws Exception {
        CompKeyDepartment department = new CompKeyDepartment();
        department.departmentId1 = 99;
        department.departmentId2 = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).execute();
        assertEquals(1, result);
        department =
            jdbcManager.from(CompKeyDepartment.class).where(
                new SimpleWhere().eq("departmentId1", 99).eq(
                    "departmentId2",
                    99)).getSingleResult();
        assertEquals(99, department.departmentId1);
        assertEquals(99, department.departmentId2);
        assertEquals(0, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_autoTx() throws Exception {
        for (int i = 0; i < 110; i++) {
            AutoStrategy entity = new AutoStrategy();
            jdbcManager.insert(entity).execute();
            assertNotNull(entity.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_identityTx() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                IdentityStrategy entity = new IdentityStrategy();
                jdbcManager.insert(entity).execute();
                if (!jdbcManagerImplementor.getDialect().supportsIdentity()) {
                    fail();
                }
                assertNotNull(entity.id);
            }
        } catch (IdentityGeneratorNotSupportedRuntimeException e) {
            if (jdbcManagerImplementor.getDialect().supportsIdentity()) {
                fail();
            }
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_sequenceTx() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                SequenceStrategy entity = new SequenceStrategy();
                jdbcManager.insert(entity).execute();
                if (!jdbcManagerImplementor.getDialect().supportsSequence()) {
                    fail();
                }
                assertNotNull(entity.id);
            }
        } catch (SequenceGeneratorNotSupportedRuntimeException e) {
            if (jdbcManagerImplementor.getDialect().supportsSequence()) {
                fail();
            }
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_sequence_explicitGeneratorTx() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                SequenceStrategy2 entity = new SequenceStrategy2();
                jdbcManager.insert(entity).execute();
                if (!jdbcManagerImplementor.getDialect().supportsSequence()) {
                    fail();
                }
                assertNotNull(entity.id);
            }
        } catch (SequenceGeneratorNotSupportedRuntimeException e) {
            if (jdbcManagerImplementor.getDialect().supportsSequence()) {
                fail();
            }
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_tableTx() throws Exception {
        for (int i = 0; i < 110; i++) {
            TableStrategy entity = new TableStrategy();
            jdbcManager.insert(entity).execute();
            assertNotNull(entity.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_table_explicitGeneratorTx() throws Exception {
        for (int i = 0; i < 110; i++) {
            TableStrategy2 entity = new TableStrategy2();
            jdbcManager.insert(entity).execute();
            assertNotNull(entity.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testColumnAnnotationTx() throws Exception {
        Department2 department = new Department2();
        department.departmentId = 99;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select department_name from Department where department_id = ?",
                    99)
                .getSingleResult();
        assertNull(departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotationTx() throws Exception {
        Department3 department = new Department3();
        department.departmentId = 99;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select department_name from Department where department_id = ?",
                    99)
                .getSingleResult();
        assertNull(departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifierTx() throws Exception {
        Department4 department = new Department4();
        department.departmentId = 99;
        department.departmentNo = 99;
        department.departmentName = "hoge";
        int result = jdbcManager.insert(department).execute();
        assertEquals(1, result);
        String departmentName =
            jdbcManager
                .selectBySql(
                    String.class,
                    "select department_name from Department where department_id = ?",
                    99)
                .getSingleResult();
        assertNull(departmentName);
    }
}

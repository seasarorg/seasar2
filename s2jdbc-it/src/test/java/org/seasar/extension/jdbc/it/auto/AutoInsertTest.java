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

import javax.persistence.EntityExistsException;

import junitx.framework.ArrayAssert;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.IdGenerationFailedRuntimeException;
import org.seasar.extension.jdbc.exception.IdGeneratorNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.IdPropertyNotAssignedRuntimeException;
import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.it.entity.Authority;
import org.seasar.extension.jdbc.it.entity.AuthorityType;
import org.seasar.extension.jdbc.it.entity.AutoStrategy;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department2;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.Department5;
import org.seasar.extension.jdbc.it.entity.IdentityStrategy;
import org.seasar.extension.jdbc.it.entity.Job;
import org.seasar.extension.jdbc.it.entity.JobType;
import org.seasar.extension.jdbc.it.entity.LargeObject;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy2;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy3;
import org.seasar.extension.jdbc.it.entity.TableStrategy;
import org.seasar.extension.jdbc.it.entity.TableStrategy2;
import org.seasar.extension.jdbc.it.entity.TableStrategy3;
import org.seasar.extension.jdbc.it.entity.TableStrategy4;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoInsertTest {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor jdbcManagerImplementor;

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
        assertEquals(1, department.version);
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
        assertEquals(1, department.version);
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
        assertEquals(1, department.version);
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
        assertEquals(1, department.version);
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
        assertEquals(1, department.version);
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
    public void testId_IdPropertyNotAssignedRuntimeException() throws Exception {
        try {
            jdbcManager.insert(new Department5()).execute();
            fail();
        } catch (IdPropertyNotAssignedRuntimeException e) {
        }
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
    public void testId_sequence_IdGeneratorNotFoundRuntimeExceptionTx()
            throws Exception {
        try {
            jdbcManager.insert(new SequenceStrategy3()).execute();
            fail();
        } catch (IdGeneratorNotFoundRuntimeException e) {
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
    public void testId_table_IdGenerationFailedRuntimeExceptionTx()
            throws Exception {
        try {
            jdbcManager.insert(new TableStrategy3()).execute();
            fail();
        } catch (IdGenerationFailedRuntimeException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_table_IdGeneratorNotFoundRuntimeExceptionTx()
            throws Exception {
        try {
            jdbcManager.insert(new TableStrategy4()).execute();
            fail();
        } catch (IdGeneratorNotFoundRuntimeException e) {
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

    /**
     * 
     * @throws Exception
     */
    public void testLargeObjectTx() throws Exception {
        byte[] bytes = new byte[10000];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) 'b';
        }
        char[] chars = new char[10000];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = 'c';
        }
        LargeObject.MyDto dto = new LargeObject.MyDto("bar");
        LargeObject lob = new LargeObject();
        lob.id = 1;
        lob.name = "hoge";
        lob.largeName = new String(chars);
        lob.bytes = new byte[] { 'f', 'o', 'o' };
        lob.largeBytes = bytes;
        lob.dto = dto;
        lob.largeDto = dto;
        jdbcManager.insert(lob).execute();
        lob = jdbcManager.from(LargeObject.class).id(1).getSingleResult();
        assertEquals("hoge", lob.name);
        assertEquals(new String(chars), lob.largeName);
        ArrayAssert.assertEquals(new byte[] { 'f', 'o', 'o' }, lob.bytes);
        ArrayAssert.assertEquals(bytes, lob.largeBytes);
        assertEquals(dto, lob.dto);
        assertEquals(dto, lob.largeDto);
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntityExistsExceptionTx() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 50;
        department.departmentName = "hoge";
        try {
            jdbcManager.insert(department).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testEnumTypeTx() throws Exception {
        Job job = new Job();
        job.id = 10;
        job.jobType = JobType.CLERK;
        jdbcManager.insert(job).execute();
        job = jdbcManager.from(Job.class).id(10).getSingleResult();
        assertEquals(JobType.CLERK, job.jobType);
    }

    /**
     * 
     * @throws Exception
     */
    public void testUserDefineValueType() throws Exception {
        Authority authority = new Authority();
        authority.id = 10;
        authority.authorityType = AuthorityType.valueOf(100);
        jdbcManager.insert(authority).execute();
        jdbcManager.from(Authority.class).id(10).getSingleResult();
        assertEquals(100, authority.authorityType.value());
    }
}

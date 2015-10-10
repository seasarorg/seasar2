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
package org.seasar.extension.jdbc.it.auto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.it.entity.AutoStrategy;
import org.seasar.extension.jdbc.it.entity.CompKeyDepartment;
import org.seasar.extension.jdbc.it.entity.ConcreteDepartment;
import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.entity.Department2;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.it.entity.IdentityStrategy;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy;
import org.seasar.extension.jdbc.it.entity.SequenceStrategy2;
import org.seasar.extension.jdbc.it.entity.TableStrategy;
import org.seasar.extension.jdbc.it.entity.TableStrategy2;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoBatchInsertTest {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor jdbcManagerImplementor;

    /**
     * 
     * @throws Exception
     */
    public void testExecute() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.insertBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(1, department.version);
        assertEquals(1, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 98)).getSingleResult();
        assertEquals(98, department.departmentId);
        assertEquals(98, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_includes() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "bar";
        department2.location = "baz";
        department2.version = 1;
        list.add(department2);

        int[] result =
            jdbcManager.insertBatch(list).includes(
                "departmentId",
                "departmentNo",
                "location",
                "version").execute();
        assertEquals(2, result.length);
        assertEquals(1, department.version);
        assertEquals(1, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 98)).getSingleResult();
        assertEquals(98, department.departmentId);
        assertEquals(98, department.departmentNo);
        assertNull(department.departmentName);
        assertEquals("foo", department.location);
        assertEquals(1, department.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertNull(department.departmentName);
        assertEquals("baz", department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_excludes() throws Exception {
        List<Department> list = new ArrayList<Department>();
        Department department = new Department();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        department.location = "foo";
        department.version = 1;
        list.add(department);
        Department department2 = new Department();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "bar";
        department2.location = "baz";
        department2.version = 1;
        list.add(department2);

        int[] result =
            jdbcManager
                .insertBatch(list)
                .excludes("departmentName", "location")
                .execute();
        assertEquals(2, result.length);
        assertEquals(1, department.version);
        assertEquals(1, department2.version);

        department =
            jdbcManager.from(Department.class).where(
                new SimpleWhere().eq("departmentId", 98)).getSingleResult();
        assertEquals(98, department.departmentId);
        assertEquals(98, department.departmentNo);
        assertNull(department.departmentName);
        assertEquals("TOKYO", department.location);
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
    public void testExecute_compKey() throws Exception {
        List<CompKeyDepartment> list = new ArrayList<CompKeyDepartment>();
        CompKeyDepartment department = new CompKeyDepartment();
        department.departmentId1 = 98;
        department.departmentId2 = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        list.add(department);
        CompKeyDepartment department2 = new CompKeyDepartment();
        department2.departmentId1 = 99;
        department2.departmentId2 = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.insertBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(1, department.version);
        assertEquals(1, department2.version);

        department =
            jdbcManager.from(CompKeyDepartment.class).where(
                new SimpleWhere().eq("departmentId1", 98).eq(
                    "departmentId2",
                    98)).getSingleResult();
        assertEquals(98, department.departmentId1);
        assertEquals(98, department.departmentId2);
        assertEquals(98, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);

        department =
            jdbcManager.from(CompKeyDepartment.class).where(
                new SimpleWhere().eq("departmentId1", 99).eq(
                    "departmentId2",
                    99)).getSingleResult();
        assertEquals(99, department.departmentId1);
        assertEquals(99, department.departmentId2);
        assertEquals(99, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testExecute_mappedSuperclass() throws Exception {
        List<ConcreteDepartment> list = new ArrayList<ConcreteDepartment>();
        ConcreteDepartment department = new ConcreteDepartment();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        list.add(department);
        ConcreteDepartment department2 = new ConcreteDepartment();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";
        list.add(department2);

        int[] result = jdbcManager.insertBatch(list).execute();
        assertEquals(2, result.length);
        assertEquals(1, department.version);
        assertEquals(1, department2.version);

        department =
            jdbcManager.from(ConcreteDepartment.class).where(
                new SimpleWhere().eq("departmentId", 98)).getSingleResult();
        assertEquals(98, department.departmentId);
        assertEquals(98, department.departmentNo);
        assertEquals("hoge", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);

        department =
            jdbcManager.from(ConcreteDepartment.class).where(
                new SimpleWhere().eq("departmentId", 99)).getSingleResult();
        assertEquals(99, department.departmentId);
        assertEquals(99, department.departmentNo);
        assertEquals("foo", department.departmentName);
        assertNull(department.location);
        assertEquals(1, department.version);
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_auto() throws Exception {
        for (int i = 0; i < 110; i++) {
            AutoStrategy entity1 = new AutoStrategy();
            AutoStrategy entity2 = new AutoStrategy();
            jdbcManager.insertBatch(entity1, entity2).execute();
            assertNotNull(entity1.id);
            assertNotNull(entity2.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_identity() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                IdentityStrategy entity1 = new IdentityStrategy();
                IdentityStrategy entity2 = new IdentityStrategy();
                jdbcManager.insertBatch(entity1, entity2).execute();
                if (!jdbcManagerImplementor.getDialect().supportsIdentity()) {
                    fail();
                }
                assertNotNull(entity1.id);
                assertNotNull(entity2.id);
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
    public void testId_sequence() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                SequenceStrategy entity1 = new SequenceStrategy();
                SequenceStrategy entity2 = new SequenceStrategy();
                jdbcManager.insertBatch(entity1, entity2).execute();
                if (!jdbcManagerImplementor.getDialect().supportsSequence()) {
                    fail();
                }
                assertNotNull(entity1.id);
                assertNotNull(entity2.id);
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
    public void testId_sequence_explicitGenerator() throws Exception {
        try {
            for (int i = 0; i < 110; i++) {
                SequenceStrategy2 entity1 = new SequenceStrategy2();
                SequenceStrategy2 entity2 = new SequenceStrategy2();
                jdbcManager.insertBatch(entity1, entity2).execute();
                if (!jdbcManagerImplementor.getDialect().supportsSequence()) {
                    fail();
                }
                assertNotNull(entity1.id);
                assertNotNull(entity2.id);
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
    public void testId_table() throws Exception {
        for (int i = 0; i < 110; i++) {
            TableStrategy entity1 = new TableStrategy();
            TableStrategy entity2 = new TableStrategy();
            jdbcManager.insertBatch(entity1, entity2).execute();
            assertNotNull(entity1.id);
            assertNotNull(entity2.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testId_table_explicitGenerator() throws Exception {
        for (int i = 0; i < 110; i++) {
            TableStrategy2 entity1 = new TableStrategy2();
            TableStrategy2 entity2 = new TableStrategy2();
            jdbcManager.insertBatch(entity1, entity2).execute();
            assertNotNull(entity1.id);
            assertNotNull(entity2.id);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testColumnAnnotation() throws Exception {
        Department2 department = new Department2();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        Department2 department2 = new Department2();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";

        int[] result =
            jdbcManager.insertBatch(department, department2).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 98).getSingleResult();
        assertNull(departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 99).getSingleResult();
        assertNull(departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotation() throws Exception {
        Department3 department = new Department3();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        Department3 department2 = new Department3();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";

        int[] result =
            jdbcManager.insertBatch(department, department2).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 98).getSingleResult();
        assertNull(departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 99).getSingleResult();
        assertNull(departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifier() throws Exception {
        Department4 department = new Department4();
        department.departmentId = 98;
        department.departmentNo = 98;
        department.departmentName = "hoge";
        Department4 department2 = new Department4();
        department2.departmentId = 99;
        department2.departmentNo = 99;
        department2.departmentName = "foo";

        int[] result =
            jdbcManager.insertBatch(department, department2).execute();
        assertEquals(2, result.length);
        String sql =
            "select DEPARTMENT_NAME from DEPARTMENT where DEPARTMENT_ID = ?";
        String departmentName =
            jdbcManager.selectBySql(String.class, sql, 98).getSingleResult();
        assertNull(departmentName);
        departmentName =
            jdbcManager.selectBySql(String.class, sql, 99).getSingleResult();
        assertNull(departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'hsqldb'")
    public void testEntityExistsException() throws Exception {
        Department department = new Department();
        department.departmentId = 1;
        department.departmentNo = 50;
        department.departmentName = "hoge";
        try {
            jdbcManager.insertBatch(department).execute();
            fail();
        } catch (EntityExistsException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testNoId() throws Exception {
        NoId noId1 = new NoId();
        noId1.value1 = 1;
        noId1.value2 = 1;
        NoId noId2 = new NoId();
        noId2.value1 = 1;
        noId2.value2 = 1;
        jdbcManager.insertBatch(noId1, noId2).execute();
        assertEquals(4L, jdbcManager.from(NoId.class).getCount());
    }

}

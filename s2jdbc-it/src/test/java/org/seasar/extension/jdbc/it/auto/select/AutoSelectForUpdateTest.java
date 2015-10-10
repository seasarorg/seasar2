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
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.annotation.Prerequisite;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.SelectForUpdateType.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectForUpdateTest {

    private JdbcManager jdbcManager;

    private JdbcManagerImplementor implementor;

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdate().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'standard'")
    public void testForUpdate_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)
            || !implementor.getDialect().supportsInnerJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .innerJoin("department")
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    @Prerequisite("#ENV != 'standard'")
    public void testForUpdate_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdate()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate()
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdate_UnsupportedOperationException() throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, false)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdate();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateNowait().getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .innerJoin("department")
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateNowait()
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait()
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, false)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .innerJoin("department")
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .innerJoin("department")
            .forUpdateNowait("department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateNowait("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateNowait("department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait("employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait("employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateNowait(
            "employeeName",
            "salary").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .innerJoin("department")
            .forUpdateNowait("employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateNowait("employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_pagingOffsetLimit()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateNowait("employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateNowaitWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NOWAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateNowait(
                "employeeName",
                "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateWait(1).getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdateWait(
            1).getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateWait(1)
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1)
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWait_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, false)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(1);
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdateWait(1, "employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdateWait(
            1,
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdateWait(
            1,
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateWait(1, "employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateWait(1, "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1, "employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(1, "employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).forUpdateWait(
            1,
            "employeeName",
            "salary").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdateWait(
            1,
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .leftOuterJoin("department")
            .forUpdateWait(1, "employeeName", "department.location")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdateWait(1, "employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWaitWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(WAIT, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdateWait(
                1,
                "employeeName",
                "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdate("employeeName")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdate(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_innerJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdate(
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "employeeName").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_leftOuterJoin2() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate("employeeName")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTarget_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).forUpdate("employeeName");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager
            .from(Employee.class)
            .forUpdate("employeeName", "salary")
            .getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .forUpdate("illegal")
                .getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName2()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).innerJoin("department").forUpdate(
                "illegal.location").getResultList();
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_illegalPropertyName3()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager.from(Employee.class).innerJoin("department").forUpdate(
                "department").getResultList();
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_innerJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        jdbcManager.from(Employee.class).innerJoin("department").forUpdate(
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_leftOuterJoin() throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)
            || !implementor.getDialect().supportsOuterJoinForUpdate()) {
            return;
        }
        jdbcManager.from(Employee.class).leftOuterJoin("department").forUpdate(
            "employeeName",
            "department.location").getResultList();
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_paging_UnsupportedOperationException()
            throws Exception {
        if (!implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .orderBy("employeeName")
                .offset(5)
                .limit(3)
                .forUpdate("employeeName", "salary")
                .getResultList();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testForUpdateWithTargets_UnsupportedOperationException()
            throws Exception {
        if (implementor.getDialect().supportsForUpdate(NORMAL, true)) {
            return;
        }
        try {
            jdbcManager
                .from(Employee.class)
                .forUpdate("employeeName", "salary");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

}

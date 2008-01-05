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
package org.seasar.extension.jdbc.it.sql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TemporalType;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.entity.Tense;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;
import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class SqlSelectTest {

    private static String sql = "select * from Employee order by employee_no";

    private static String sql2 =
        "select employee_id, employee_no from Employee order by employee_no";

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging() throws Exception {
        List<Employee> list =
            jdbcManager.selectBySql(Employee.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_limitOnly() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetZero_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offset_limitZero() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(14, list.get(10).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_paging_offset_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).employeeId);
        assertEquals(8, list.get(4).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter_none() throws Exception {
        String sql = "select * from Employee";
        List<Employee> list =
            jdbcManager.selectBySql(Employee.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_parameter() throws Exception {
        String sql =
            "select * from Employee where department_Id = ? and salary = ?";
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql, 2, 3000)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult() throws Exception {
        String sql = "select * from Employee where employee_Id = 1";
        Employee employee =
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_null() throws Exception {
        String sql = "select * from Employee where employee_Id = 100";
        Employee employee =
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getSingleResult_NoResultException() throws Exception {
        String sql = "select * from Employee where employee_Id = 100";
        try {
            jdbcManager
                .selectBySql(Employee.class, sql)
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
    public void testBean_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "select * from Employee where department_Id = 1";
        try {
            jdbcManager.selectBySql(Employee.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_getResultList_NoResultException() throws Exception {
        String sql = "select * from Employee where employee_id = 100";
        try {
            jdbcManager
                .selectBySql(Employee.class, sql)
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
    public void testBean_temporalType() throws Exception {
        String sql = "select * from Tense where id = 1";
        Tense tense =
            jdbcManager.selectBySql(Tense.class, sql).getSingleResult();
        long date =
            new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14").getTime();
        long time =
            new SimpleDateFormat("HH:mm:ss").parse("12:11:10").getTime();
        long timestamp =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        assertNotNull(tense);
        assertEquals(date, tense.calDate.getTimeInMillis());
        assertEquals(date, tense.dateDate.getTime());
        assertEquals(date, tense.sqlDate.getTime());
        assertEquals(time, tense.calTime.getTimeInMillis());
        assertEquals(time, tense.dateTime.getTime());
        assertEquals(time, tense.sqlTime.getTime());
        assertEquals(timestamp, tense.calTimestamp.getTimeInMillis());
        assertEquals(timestamp, tense.dateTimestamp.getTime());
        assertEquals(timestamp, tense.sqlTimestamp.getTime());
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType_Calendar() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar
            .setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14"));
        Tense tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where cal_date = ?",
                date(calendar)).getSingleResult();
        assertNotNull(tense);

        calendar.setTime(new SimpleDateFormat("HH:mm:ss").parse("12:11:10"));
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where cal_time = ?",
                time(calendar)).getSingleResult();
        assertNotNull(tense);

        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .parse("2005-02-14 12:11:10"));
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where cal_timestamp = ?",
                timestamp(calendar)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testBean_temporalType_Date() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2005-02-14");
        Tense tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where date_date = ?",
                date(date)).getSingleResult();
        assertNotNull(tense);

        date = new SimpleDateFormat("HH:mm:ss").parse("12:11:10");
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where date_time = ?",
                time(date)).getSingleResult();
        assertNotNull(tense);

        date =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse("2005-02-14 12:11:10");
        tense =
            jdbcManager.selectBySql(
                Tense.class,
                "select * from Tense where date_timestamp = ?",
                timestamp(date)).getSingleResult();
        assertNotNull(tense);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).getResultList();
        assertEquals(14, list.size());
        assertEquals(9, list.get(0).size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetOnly() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).offset(3).getResultList();
        assertEquals(11, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(14, ((Number) list.get(10).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_limitOnly() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).limit(3).getResultList();
        assertEquals(3, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(1, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(3, ((Number) list.get(2).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetZero_limitZero() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offset_limitZero() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(14, ((Number) list.get(10).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offsetZero_limit() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(1, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(3, ((Number) list.get(2).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_paging_offset_limit() throws Exception {
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager
                .selectBySql(Map.class, sql)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(9, list.get(0).size());
        assertEquals(4, ((Number) list.get(0).get("employeeId")).intValue());
        assertEquals(8, ((Number) list.get(4).get("employeeId")).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter_none() throws Exception {
        String sql = "select * from Employee";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_parameter() throws Exception {
        String sql =
            "select * from Employee where department_Id = ? and salary = ?";
        @SuppressWarnings("unchecked")
        List<Map> list =
            jdbcManager.selectBySql(Map.class, sql, 2, 3000).getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult() throws Exception {
        String sql = "select * from Employee where employee_Id = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNotNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_null() throws Exception {
        String sql = "select * from Employee where employee_Id = 100";
        Map<?, ?> employee =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNull(employee);
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getSingleResult_NoResultException() throws Exception {
        String sql = "select * from Employee where employee_Id = 100";
        try {
            jdbcManager
                .selectBySql(Map.class, sql)
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
    public void testMap_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "select * from Employee where department_Id = 1";
        try {
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_getResultList_NoResultException() throws Exception {
        String sql = "select * from Employee where employee_Id = 100";
        try {
            jdbcManager
                .selectBySql(Map.class, sql)
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
    public void testMap_linkedHashMap() throws Exception {
        String sql =
            "select version, employee_name, employee_Id from Employee where employee_Id = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(LinkedHashMap.class, sql).getSingleResult();
        Iterator<?> it = employee.keySet().iterator();
        assertEquals("version", it.next());
        assertNotNull(employee.get("version"));
        assertEquals("employeeName", it.next());
        assertNotNull(employee.get("employeeName"));
        assertNotNull(employee.get("employeeId"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_treeMap() throws Exception {
        String sql =
            "select version, employee_name, employee_Id from Employee where employee_Id = 1";
        Map<?, ?> employee =
            jdbcManager.selectBySql(TreeMap.class, sql).getSingleResult();
        Iterator<?> it = employee.keySet().iterator();
        assertEquals("employeeId", it.next());
        assertNotNull(employee.get("employeeId"));
        assertEquals("employeeName", it.next());
        assertNotNull(employee.get("employeeName"));
        assertEquals("version", it.next());
        assertNotNull(employee.get("version"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testMap_temporalType() throws Exception {
        String sql = "select * from Tense where id = 1";
        Map<?, ?> tense =
            jdbcManager.selectBySql(Map.class, sql).getSingleResult();
        assertNotNull(tense);
        assertNotNull(tense.get("calDate"));
        assertNotNull(tense.get("dateDate"));
        assertNotNull(tense.get("sqlDate"));
        assertNotNull(tense.get("calTime"));
        assertNotNull(tense.get("dateTime"));
        assertNotNull(tense.get("sqlTime"));
        assertNotNull(tense.get("calTimestamp"));
        assertNotNull(tense.get("dateTimestamp"));
        assertNotNull(tense.get("sqlTimestamp"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging() throws Exception {
        List<Integer> list =
            jdbcManager.selectBySql(Integer.class, sql2).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetOnly() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(14, list.get(10).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_limitOnly() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetZero_limitZero() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(0)
                .limit(0)
                .getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offset_limitZero() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .limit(0)
                .getResultList();
        assertEquals(11, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(14, list.get(10).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offsetZero_limit() throws Exception {
        List<Employee> list =
            jdbcManager
                .selectBySql(Employee.class, sql2)
                .offset(0)
                .limit(3)
                .getResultList();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).employeeId);
        assertEquals(3, list.get(2).employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_paging_offset_limit() throws Exception {
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql2)
                .offset(3)
                .limit(5)
                .getResultList();
        assertEquals(5, list.size());
        assertEquals(4, list.get(0).intValue());
        assertEquals(8, list.get(4).intValue());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter_none() throws Exception {
        String sql = "select employee_id from Employee";
        List<Integer> list =
            jdbcManager.selectBySql(Integer.class, sql).getResultList();
        assertEquals(14, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_parameter() throws Exception {
        String sql =
            "select employee_id from Employee where department_Id = ? and salary = ?";
        List<Integer> list =
            jdbcManager
                .selectBySql(Integer.class, sql, 2, 3000)
                .getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult() throws Exception {
        String sql = "select employee_id from Employee where employee_Id = 1";
        Integer employeeId =
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
        assertNotNull(employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_null() throws Exception {
        String sql = "select employee_id from Employee where employee_Id = 100";
        Integer employeeId =
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
        assertNull(employeeId);
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getSingleResult_NoResultException() throws Exception {
        String sql = "select employee_id from Employee where employee_Id = 100";
        try {
            jdbcManager
                .selectBySql(Integer.class, sql)
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
    public void testObject_getSingleResult_NonUniqueResultException()
            throws Exception {
        String sql = "select employee_id from Employee where department_Id = 1";
        try {
            jdbcManager.selectBySql(Integer.class, sql).getSingleResult();
            fail();
        } catch (NonUniqueResultException e) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testObject_getResultList_NoResultException() throws Exception {
        String sql = "select employee_id from Employee where employee_Id = 100";
        try {
            jdbcManager
                .selectBySql(Integer.class, sql)
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
    public void testObject_temporalType() throws Exception {
        String sql = "select cal_timestamp from Tense where id = 1";
        Calendar calTimestamp =
            jdbcManager.selectBySql(Calendar.class, sql).temporal(
                TemporalType.TIMESTAMP).getSingleResult();
        assertNotNull(calTimestamp);
        long time =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                "2005-02-14 12:11:10").getTime();
        assertEquals(time, calTimestamp.getTimeInMillis());
    }
}

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
package examples.entity;

import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.util.TigerBeanUtil;

import examples.dto.EmployeeDto;

/**
 * @author higa
 * 
 */
public class S2JDBCTest extends S2TestCase {

    private static final String SELECT_EMPLOYEE_DTO =
        "select e.*, d.name as department_name"
            + " from employee e left outer join department d on e.department_id = d.id"
            + " where d.id = ?";

    private static final String SELECT_COUNT = "select count(*) from employee";

    private static final String SQL_FILE =
        "examples/sql/employee/selectWithDept.sql";

    private static final String SQL_FILE2 =
        "examples/sql/employee/selectWithDept2.sql";

    private JdbcManager jdbcManager;

    protected void setUp() throws Exception {
        include("app.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetResultList() throws Exception {
        List<Employee> results =
            jdbcManager.from(Employee.class).getResultList();
        for (Employee e : results) {
            System.out.println(e.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testTotalBonus() throws Exception {
        List<Employee> results =
            jdbcManager.from(Employee.class).getResultList();
        long totalBonus = 0;
        for (Employee e : results) {
            totalBonus += e.jobType.createStrategy().calculateBonus(e.salary);
        }
        System.out.println("Total Bonus:" + totalBonus);
    }

    /**
     * @throws Exception
     */
    public void testGetSingleResult() throws Exception {
        Employee result =
            jdbcManager
                .from(Employee.class)
                .where("id = ?", 1)
                .getSingleResult();
        System.out.println(result.name);
    }

    /**
     * @throws Exception
     */
    public void testManyToOne() throws Exception {
        List<Employee> results =
            jdbcManager.from(Employee.class).join("department").getResultList();
        for (Employee e : results) {
            System.out.println(e.name + ", " + e.department.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToMany() throws Exception {
        List<Department> results =
            jdbcManager
                .from(Department.class)
                .join("employeeList")
                .getResultList();
        for (Department d : results) {
            System.out.println(d.name);
            for (Employee e : d.employeeList) {
                System.out.println(" " + e.name);
            }
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToOneOwner() throws Exception {
        List<Employee> results =
            jdbcManager.from(Employee.class).join("address").getResultList();
        for (Employee e : results) {
            System.out.println(e.name + ", " + e.address.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToOneNonowner() throws Exception {
        List<Address> results =
            jdbcManager.from(Address.class).join("employee").getResultList();
        for (Address a : results) {
            System.out.println(a.name + ", " + a.employee.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testManyToOneWhere() throws Exception {
        List<Employee> results =
            jdbcManager.from(Employee.class).join("department").where(
                "department.id in (? , ?)",
                1,
                2).getResultList();
        for (Employee e : results) {
            System.out.println(e.name + ", " + e.department.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToManyWhere() throws Exception {
        List<Department> results =
            jdbcManager.from(Department.class).join("employeeList").where(
                "employeeList.id = ?",
                1).getResultList();
        for (Department d : results) {
            System.out.println(d.name);
            for (Employee e : d.employeeList) {
                System.out.println(" " + e.name);
            }
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToOneOwnerWhere() throws Exception {
        List<Employee> results =
            jdbcManager
                .from(Employee.class)
                .join("address")
                .where(
                    new SimpleWhere().starts("name", "A").ends(
                        "address.name",
                        "1"))
                .getResultList();
        for (Employee e : results) {
            System.out.println(e.name + ", " + e.address.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testOneToOneNonownerWhere() throws Exception {
        MyAction action = new MyAction();
        action.search_name_ENDS = "1";
        action.search_employee$name_STARTS = "A";
        List<Address> results =
            jdbcManager
                .from(Address.class)
                .join("employee")
                .where(TigerBeanUtil.createProperties(action, "search_"))
                .getResultList();
        for (Address a : results) {
            System.out.println(a.name + ", " + a.employee.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testOrderBy() throws Exception {
        List<Employee> results =
            jdbcManager
                .from(Employee.class)
                .orderBy("name desc")
                .getResultList();
        for (Employee e : results) {
            System.out.println(e.name);
        }
    }

    /**
     * @throws Exception
     */
    public void testLimitOffset() throws Exception {
        List<Employee> results =
            jdbcManager
                .from(Employee.class)
                .orderBy("id")
                .limit(5)
                .offset(4)
                .getResultList();
        for (Employee e : results) {
            System.out.println(e.id);
        }
    }

    /**
     * @throws Exception
     */
    public void testInsertTx() throws Exception {
        Employee emp = new Employee();
        emp.name = "test";
        emp.jobType = JobType.ANALYST;
        emp.salary = 300;
        jdbcManager.insert(emp).execute();
        System.out.println(emp.id);
    }

    /**
     * @throws Exception
     */
    public void testUpdateTx() throws Exception {
        Employee emp =
            jdbcManager
                .from(Employee.class)
                .where("id = ?", 1)
                .getSingleResult();
        emp.name = "hoge";
        System.out.println(emp.version);
        jdbcManager.update(emp).execute();
        System.out.println(emp.version);
    }

    /**
     * @throws Exception
     */
    public void testDeleteTx() throws Exception {
        Employee emp =
            jdbcManager
                .from(Employee.class)
                .where("id = ?", 1)
                .getSingleResult();
        jdbcManager.delete(emp).execute();
        emp =
            jdbcManager
                .from(Employee.class)
                .where("id = ?", 1)
                .getSingleResult();
        System.out.println(emp);
    }

    /**
     * @throws Exception
     */
    public void testSqlGetResultList() throws Exception {
        List<EmployeeDto> results =
            jdbcManager
                .selectBySql(EmployeeDto.class, SELECT_EMPLOYEE_DTO, 1)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out.println(e.name + " " + e.departmentName);
        }
    }

    /**
     * @throws Exception
     */
    public void testSqlGetSingleResult() throws Exception {
        Integer result =
            jdbcManager
                .selectBySql(Integer.class, SELECT_COUNT)
                .getSingleResult();
        System.out.println(result);
    }

    /**
     * @throws Exception
     */
    public void testSqlFile() throws Exception {
        MyParam param = new MyParam();
        param.salaryMin = 1200;
        param.salaryMax = 1800;
        List<EmployeeDto> results =
            jdbcManager
                .selectBySqlFile(EmployeeDto.class, SQL_FILE, param)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out
                .println(e.name + " " + e.salary + " " + e.departmentName);
        }
    }

    /**
     * @throws Exception
     */
    public void testSqlFile_salaryMaxNull() throws Exception {
        MyParam param = new MyParam();
        param.salaryMin = 1200;
        List<EmployeeDto> results =
            jdbcManager
                .selectBySqlFile(EmployeeDto.class, SQL_FILE2, param)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out
                .println(e.name + " " + e.salary + " " + e.departmentName);
        }
    }

    /**
     * @throws Exception
     */
    public void testSqlFile_salaryMinNull() throws Exception {
        MyParam param = new MyParam();
        param.salaryMax = 1600;
        List<EmployeeDto> results =
            jdbcManager
                .selectBySqlFile(EmployeeDto.class, SQL_FILE2, param)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out
                .println(e.name + " " + e.salary + " " + e.departmentName);
        }
    }

    /**
     * @throws Exception
     */
    public void testSqlFile_bothNull() throws Exception {
        MyParam param = new MyParam();
        List<EmployeeDto> results =
            jdbcManager
                .selectBySqlFile(EmployeeDto.class, SQL_FILE2, param)
                .getResultList();
        for (EmployeeDto e : results) {
            System.out
                .println(e.name + " " + e.salary + " " + e.departmentName);
        }
    }

    private static class MyAction {

        /**
         * 
         */
        public String search_name_ENDS;

        /**
         * 
         */
        public String search_employee$name_STARTS;
    }

    private static final class MyParam {

        /**
         * 
         */
        public Integer salaryMin;

        /**
         * 
         */
        public Integer salaryMax;
    }
}
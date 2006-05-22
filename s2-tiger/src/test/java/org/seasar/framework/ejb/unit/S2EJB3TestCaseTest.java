/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.unit;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.TransactionManager;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.unit.annotation.Rollback;

/**
 * @author taedium
 * 
 */
public class S2EJB3TestCaseTest extends S2EJB3TestCase {

    @EJB
    private IHoge hoge;

    @EJB(beanName="xxx")
    private IHoge yyy;
    
    @EJB
    private IBar zzz;
    
    public void setUpRegisterEJB() throws Exception {
        register(Hoge.class);
        register(Foo.class);
    }

    public void testRegisterEJB() throws Exception {
        assertNotNull("1", hoge);
        assertNotNull("2", hoge.aaa());
    }

    public void setUpInclude() throws Exception {
        include("S2EJB3TestCaseTest.dicon");
    }

    public void testInclude() throws Exception {
        assertNotNull("1", hoge);
        assertNotNull("2", hoge.aaa());
    }

    public void setUpBindFieldByFieldName() {
        register(Hoge.class);   
    }

    public void testBindFieldByFieldName() {
        assertNotNull("1", hoge);
    }

    public void setUpBindFieldByBeanName() {
        register(Hoge2.class);   
    }

    public void testBindFieldByBeanName() {
        assertNotNull("1", yyy);
    }

    public void setUpBindFieldByType() {
        register(Bar.class);   
    }

    public void testBindFieldByType() {
        assertNotNull("1", zzz);
    }
    
    public void testAssertEntityEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("EMPLOYEE");
        table.addColumn("ID");
        table.addColumn("EMPNO");
        table.addColumn("NAME");
        DataRow row = table.addRow();
        row.setValue("ID", new Long(1));
        row.setValue("EMPNO", 7782);
        row.setValue("NAME", "CLARK");

        Employee clark = new Employee(new Long(1), 7782, "CLARK");

        assertEntityEquals("1", expected, clark);
    }

    public void testAssertEntityListEquals() {
        DataSet expected = new DataSetImpl();
        DataTable table = expected.addTable("EMPLOYEE");
        table.addColumn("ID");
        table.addColumn("EMPNO");
        table.addColumn("NAME");
        DataRow row1 = table.addRow();
        row1.setValue("ID", new Long(1));
        row1.setValue("EMPNO", 7782);
        row1.setValue("NAME", "CLARK");
        DataRow row2 = table.addRow();
        row2.setValue("ID", new Long(2));
        row2.setValue("EMPNO", 7839);
        row2.setValue("NAME", "KING");

        Employee clark = new Employee(new Long(1), 7782, "CLARK");
        Employee king = new Employee(new Long(2), 7839, "KING");
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(clark);
        employees.add(king);
        
        assertEntityListEquals("1", expected, employees);
    }

    public void setUpGetEntityManager() {
        MockInterceptor mi = new MockInterceptor();
        register(mi.createProxy(EntityManager.class));
    }

    public void testGetEntityManager() {
        assertNotNull("1", getEntityManager());
    }

    public void testIsTransactionActiveReturnTrue() throws Exception {
        include("ejb3tx.dicon");
        TransactionManager tm = null;
        tm = (TransactionManager) getComponent(TransactionManager.class);
        try {
            tm.begin();
            assertEquals(true, isTransactionActive());
        } catch (Throwable t) {
            System.out.println(t);
            fail();
        } finally {
            tm.rollback();
        }
    }

    public void testIsTransactionActiveReturnFalse() throws Exception {
        assertEquals(false, isTransactionActive());
    }

    @Rollback
    public void testNeedTransactionReturnTrue() throws Exception {
        assertEquals(true, needTransaction());
    }

    public void testNeedTransactionReturnFalse() throws Exception {
        assertEquals(false, needTransaction());
    }
    
    public void testReloadAsDataSet() throws Exception {
        Dept dept = new Dept();
        dept.deptno = new Long(1);
        dept.dname = "CLARK";
        DataSet dataSet = reloadAsDataSet(dept);
        DataTable table = dataSet.getTable("DEPT");
        assertEquals("1", true, table.hasColumn("DEPTNO"));
        assertEquals("2", false, table.hasColumn("NONE"));
    }

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        private long empno;

        private String name;

        public Employee() {
        }

        public Employee(Long id, long empno, String name) {
            this.id = id;
            this.empno = empno;
            this.name = name;
        }
    }
    
    @Entity
    @Table(name = "Dept")
    public static class Dept {

        @Id
        private Long deptno;
        
        private String dname;
        
        private String none;

    }
}

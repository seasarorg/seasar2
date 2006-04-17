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
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.transaction.TransactionManager;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.framework.aop.interceptors.MockInterceptor;

/**
 * @author taedium
 * 
 */
public class S2EJB3TestCaseTest extends S2EJB3TestCase {

    @EJB
    private IHoge hoge;

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

    public void testIsTransactionActinveReturnTrue() throws Exception {
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

    public void testIsTransactionActinveReturnFalse() throws Exception {
        assertEquals(false, isTransactionActive());
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
    
    @Local
    public static interface IHoge {
        String aaa();
    }

    @Local
    public static interface IFoo {
        String aaa();
    }
    
    @Stateless
    public static class Hoge implements IHoge {

        @EJB
        private IFoo foo;
        
        public String aaa() {
            return foo.aaa();
        }
    }

    @Stateless
    public static class Foo implements IFoo {
        public String aaa() {
            return "aaa";
        }
    }
}

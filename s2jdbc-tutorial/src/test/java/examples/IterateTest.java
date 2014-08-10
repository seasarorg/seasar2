/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package examples;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.unit.S2TestCase;

import examples.entity.Employee;

/**
 * @author koichik
 */
public class IterateTest extends S2TestCase {

    private JdbcManager jdbcManager;

    @Override
    protected void setUp() throws Exception {
        include("app.dicon");
    }

    /**
     * @throws Exception
     */
    public void testIterate() throws Exception {
        long sum =
            jdbcManager.from(Employee.class).iterate(
                new IterationCallback<Employee, Long>() {
                    private long sum;

                    public Long iterate(Employee emp, IterationContext context) {
                        sum += emp.salary;
                        return sum;
                    }
                });
        System.out.println(sum);
    }

    /**
     * @throws Exception
     */
    public void testIterateExit() throws Exception {
        Employee emp =
            jdbcManager.from(Employee.class).iterate(
                new IterationCallback<Employee, Employee>() {
                    private long sum;

                    public Employee iterate(Employee emp,
                            IterationContext context) {
                        sum += emp.salary;
                        if (sum > 10000) {
                            context.setExit(true);
                        }
                        return emp;
                    }
                });
        System.out.println(emp.name);
    }

}
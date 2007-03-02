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
package examples.dao.tiger;

import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class EmployeeAutoDaoClient {

    private static final String PATH = "examples/dao/tiger/EmployeeAutoDao.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        try {
            EmployeeAutoDao dao = (EmployeeAutoDao) container
                    .getComponent(EmployeeAutoDao.class);

            dao.getEmployeeByJobDeptno(null, null);
            dao.getEmployeeByJobDeptno("CLERK", null);
            dao.getEmployeeByJobDeptno(null, new Integer(20));
            dao.getEmployeeByJobDeptno("CLERK", new Integer(20));

            List employees = dao.getEmployeesBySal(0, 1000);
            for (int i = 0; i < employees.size(); ++i) {
                System.out.println(employees.get(i));
            }

            employees = dao.getEmployeeByDname("SALES");
            for (int i = 0; i < employees.size(); ++i) {
                System.out.println(employees.get(i));
            }

            EmployeeSearchCondition dto = new EmployeeSearchCondition();
            dto.setDname("RESEARCH");
            employees = dao.getEmployeesBySearchCondition(dto);
            for (int i = 0; i < employees.size(); ++i) {
                System.out.println(employees.get(i));
            }

            Employee employee = dao.getEmployeeByEmpno(7499);
            System.out.println("before timestamp:" + employee.getTimestamp());
            dao.update(employee);
            System.out.println("after timestamp:" + employee.getTimestamp());
        } finally {
            container.destroy();
        }

    }
}
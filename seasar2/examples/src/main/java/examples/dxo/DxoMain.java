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
package examples.dxo;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author koichik
 * 
 */
public class DxoMain {

    public static void main(String[] args) {
        SingletonS2ContainerFactory.setConfigPath("examples/dxo/app.dicon");
        SingletonS2ContainerFactory.init();
        S2Container container = SingletonS2ContainerFactory.getContainer();

        Employee employee = new Employee();
        employee.setEname("Mike");
        Department department = new Department();
        department.setDname("Sales");
        employee.setDepartment(department);

        EmployeeDxo dxo = (EmployeeDxo) container
                .getComponent(EmployeeDxo.class);
        EmployeePage page = dxo.convert(employee);
        System.out.println(page);
    }
}
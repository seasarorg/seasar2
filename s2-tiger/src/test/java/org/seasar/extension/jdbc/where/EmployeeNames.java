/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.where;

import java.math.BigDecimal;
import java.util.Date;

import org.seasar.extension.jdbc.impl.Employee;
import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.extension.jdbc.where.DepartmentNames._DepartmentNames;

/**
 * @author higa
 * 
 */
public class EmployeeNames extends Operations {

    /**
     * @return
     */
    public static PropertyName<BigDecimal> id() {
        return new PropertyName<BigDecimal>("id");
    }

    /**
     * @return
     */
    public static PropertyName<String> name() {
        return new PropertyName<String>("name");
    }

    /**
     * @return
     */
    public static PropertyName<Date> hireDate() {
        return new PropertyName<Date>("hireDate");
    }

    /**
     * @return
     */
    public static _DepartmentNames department() {
        return new _DepartmentNames("department");
    }

    /**
     * @author koichik
     */
    public static class _EmployeeNames extends PropertyName<Employee> {

        /**
         * 
         */
        public _EmployeeNames() {
        }

        /**
         * @param name
         */
        public _EmployeeNames(String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public _EmployeeNames(PropertyName<?> parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<BigDecimal> id() {
            return new PropertyName<BigDecimal>(this, "id");
        }

        /**
         * @return
         */
        public PropertyName<String> name() {
            return new PropertyName<String>(this, "name");
        }

        /**
         * @return
         */
        public PropertyName<Date> hireDate() {
            return new PropertyName<Date>(this, "hireDate");
        }

        /**
         * @return
         */
        public _DepartmentNames department() {
            return new _DepartmentNames(this, "department");
        }
    }
}

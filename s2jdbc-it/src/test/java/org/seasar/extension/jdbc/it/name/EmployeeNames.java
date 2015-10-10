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
package org.seasar.extension.jdbc.it.name;

import java.math.BigDecimal;
import java.util.Date;

import org.seasar.extension.jdbc.it.entity.Employee;
import org.seasar.extension.jdbc.it.name.AddressNames._AddressNames;
import org.seasar.extension.jdbc.it.name.DepartmentNames._DepartmentNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * @author koichik
 */
public class EmployeeNames {

    /**
     * @return
     */
    public static PropertyName<Integer> employeeId() {
        return new PropertyName<Integer>("employeeId");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> employeeNo() {
        return new PropertyName<Integer>("employeeNo");
    }

    /**
     * @return
     */
    public static PropertyName<String> employeeName() {
        return new PropertyName<String>("employeeName");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> managerId() {
        return new PropertyName<Integer>("managerId");
    }

    /**
     * @return
     */
    public static _EmployeeNames manager() {
        return new _EmployeeNames("manager");
    }

    /**
     * @return
     */
    public static PropertyName<Date> hiredate() {
        return new PropertyName<Date>("hiredate");
    }

    /**
     * @return
     */
    public static PropertyName<BigDecimal> salary() {
        return new PropertyName<BigDecimal>("salary");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> departmentId() {
        return new PropertyName<Integer>("departmentId");
    }

    /**
     * @return
     */
    public static _DepartmentNames department() {
        return new _DepartmentNames("department");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> addressId() {
        return new PropertyName<Integer>("addressId");
    }

    /**
     * @return
     */
    public static _AddressNames address() {
        return new _AddressNames("address");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> version() {
        return new PropertyName<Integer>("version");
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
        public _EmployeeNames(final String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public _EmployeeNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<Integer> employeeId() {
            return new PropertyName<Integer>(this, "employeeId");
        }

        /**
         * @return
         */
        public PropertyName<Integer> employeeNo() {
            return new PropertyName<Integer>(this, "employeeNo");
        }

        /**
         * @return
         */
        public PropertyName<String> employeeName() {
            return new PropertyName<String>(this, "employeeName");
        }

        /**
         * @return
         */
        public PropertyName<Integer> managerId() {
            return new PropertyName<Integer>(this, "managerId");
        }

        /**
         * @return
         */
        public _EmployeeNames manager() {
            return new _EmployeeNames(this, "manager");
        }

        /**
         * @return
         */
        public PropertyName<Date> hiredate() {
            return new PropertyName<Date>(this, "hiredate");
        }

        /**
         * @return
         */
        public PropertyName<BigDecimal> salary() {
            return new PropertyName<BigDecimal>(this, "salary");
        }

        /**
         * @return
         */
        public PropertyName<Integer> departmentId() {
            return new PropertyName<Integer>(this, "departmentId");
        }

        /**
         * @return
         */
        public _DepartmentNames department() {
            return new _DepartmentNames(this, "department");
        }

        /**
         * @return
         */
        public PropertyName<Integer> addressId() {
            return new PropertyName<Integer>(this, "addressId");
        }

        /**
         * @return
         */
        public _AddressNames address() {
            return new _AddressNames(this, "address");
        }

        /**
         * @return
         */
        public PropertyName<Integer> version() {
            return new PropertyName<Integer>(this, "version");
        }

    }

}

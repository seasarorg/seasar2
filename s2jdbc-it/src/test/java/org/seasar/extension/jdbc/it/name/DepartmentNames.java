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

import org.seasar.extension.jdbc.it.entity.Department;
import org.seasar.extension.jdbc.it.name.EmployeeNames._EmployeeNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * @author koichik
 */
public class DepartmentNames {

    /**
     * @return
     */
    public static PropertyName<Integer> departmentId() {
        return new PropertyName<Integer>("departmentId");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> departmentNo() {
        return new PropertyName<Integer>("departmentNo");
    }

    /**
     * @return
     */
    public static PropertyName<String> departmentName() {
        return new PropertyName<String>("departmentName");
    }

    /**
     * @return
     */
    public static PropertyName<String> location() {
        return new PropertyName<String>("location");
    }

    /**
     * @return
     */
    public static PropertyName<Integer> version() {
        return new PropertyName<Integer>("version");
    }

    /**
     * @return
     */
    public static _EmployeeNames employees() {
        return new _EmployeeNames("employees");
    }

    /**
     * @author koichik
     */
    public static class _DepartmentNames extends PropertyName<Department> {

        /**
         * 
         */
        public _DepartmentNames() {
        }

        /**
         * @param name
         */
        public _DepartmentNames(final String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public _DepartmentNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
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
        public PropertyName<Integer> departmentNo() {
            return new PropertyName<Integer>(this, "departmentNo");
        }

        /**
         * @return
         */
        public PropertyName<String> departmentName() {
            return new PropertyName<String>(this, "departmentName");
        }

        /**
         * @return
         */
        public PropertyName<String> location() {
            return new PropertyName<String>(this, "location");
        }

        /**
         * @return
         */
        public PropertyName<Integer> version() {
            return new PropertyName<Integer>(this, "version");
        }

        /**
         * @return
         */
        public _EmployeeNames employees() {
            return new _EmployeeNames(this, "employees");
        }

    }

}

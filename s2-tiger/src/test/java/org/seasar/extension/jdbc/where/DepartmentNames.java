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

import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.extension.jdbc.where.EmployeeNames._EmployeeNames;

/**
 * @author higa
 */
public class DepartmentNames extends Operations {

    /**
     * @return
     */
    public static PropertyName<Integer> id() {
        return new PropertyName<Integer>("id");
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
        public _DepartmentNames(String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public _DepartmentNames(PropertyName<?> parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<Integer> id() {
            return new PropertyName<Integer>(this, "id");
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
        public _EmployeeNames employees() {
            return new _EmployeeNames(this, "employees");
        }

    }

}

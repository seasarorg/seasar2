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

import org.seasar.extension.jdbc.it.entity.Address;
import org.seasar.extension.jdbc.it.name.EmployeeNames._EmployeeNames;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * @author koichik
 */
public class AddressNames {

    /**
     * @return
     */
    public static PropertyName<Integer> addressId() {
        return new PropertyName<Integer>("addressId");
    }

    /**
     * @return
     */
    public static PropertyName<String> street() {
        return new PropertyName<String>("street");
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
    public static _EmployeeNames employee() {
        return new _EmployeeNames("employee");
    }

    /**
     * @author koichik
     */
    public static class _AddressNames extends PropertyName<Address> {

        /**
         * 
         */
        public _AddressNames() {
        }

        /**
         * @param name
         */
        public _AddressNames(final String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public _AddressNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
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
        public PropertyName<String> street() {
            return new PropertyName<String>(this, "street");
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
        public _EmployeeNames employee() {
            return new _EmployeeNames(this, "employee");
        }

    }

}

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
package org.seasar.extension.jdbc.it.condition;

import java.math.BigDecimal;
import java.util.Date;

import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.condition.AbstractEntityCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;

/**
 * @author koichik
 */
public class EmployeeCondition extends
        AbstractEntityCondition<EmployeeCondition> {

    /**
     * 
     */
    public EmployeeCondition() {
    }

    /**
     * @param prefix
     * @param where
     */
    public EmployeeCondition(String prefix, ComplexWhere where) {
        super(prefix, where);
    }

    /** */
    public NotNullableCondition<EmployeeCondition, Integer> employeeId =
        new NotNullableCondition<EmployeeCondition, Integer>("employeeId", this);

    /** */
    public NullableCondition<EmployeeCondition, Integer> employeeNo =
        new NullableCondition<EmployeeCondition, Integer>("employeeNo", this);

    /** */
    public NullableStringCondition<EmployeeCondition> employeeName =
        new NullableStringCondition<EmployeeCondition>("employeeName", this);

    /** */
    public NullableCondition<EmployeeCondition, Integer> managerId =
        new NullableCondition<EmployeeCondition, Integer>("managerId", this);

    /**
     * @return
     */
    public EmployeeCondition manager() {
        return new EmployeeCondition(prefix + "manager.", where);
    }

    /** */
    public NullableCondition<EmployeeCondition, Date> hiredate =
        new NullableCondition<EmployeeCondition, Date>("hiredate", this);

    /** */
    public NullableCondition<EmployeeCondition, BigDecimal> salary =
        new NullableCondition<EmployeeCondition, BigDecimal>("salary", this);

    /** */
    public NullableCondition<EmployeeCondition, Integer> departmentId =
        new NullableCondition<EmployeeCondition, Integer>("departmentId", this);

    /**
     * @return
     */
    public DepartmentCondition department() {
        return new DepartmentCondition(prefix + "department.", where);
    }

    /** */
    public NullableCondition<EmployeeCondition, Integer> addressId =
        new NullableCondition<EmployeeCondition, Integer>("addressId", this);

    /**
     * @return
     */
    public AddressCondition address() {
        return new AddressCondition(prefix + "address.", where);
    }

    /** */
    public NullableCondition<EmployeeCondition, Integer> version =
        new NullableCondition<EmployeeCondition, Integer>("version", this);

}

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

import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.condition.AbstractEntityCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;

/**
 * @author koichik
 */
public class DepartmentCondition extends
        AbstractEntityCondition<DepartmentCondition> {

    /**
     * 
     */
    public DepartmentCondition() {
    }

    /**
     * @param prefix
     * @param where
     */
    public DepartmentCondition(String prefix, ComplexWhere where) {
        super(prefix, where);
    }

    /** */
    public NotNullableCondition<DepartmentCondition, Integer> departmentId =
        new NotNullableCondition<DepartmentCondition, Integer>(
            "departmentId",
            this);

    /** */
    public NullableCondition<DepartmentCondition, Integer> departmentNo =
        new NullableCondition<DepartmentCondition, Integer>(
            "departmentNo",
            this);

    /** */
    public NullableStringCondition<DepartmentCondition> departmentName =
        new NullableStringCondition<DepartmentCondition>("departmentName", this);

    /** */
    public NullableStringCondition<DepartmentCondition> location =
        new NullableStringCondition<DepartmentCondition>("location", this);

    /** */
    public NullableCondition<DepartmentCondition, Integer> version =
        new NullableCondition<DepartmentCondition, Integer>("version", this);

    /**
     * @return
     */
    public EmployeeCondition employees() {
        return new EmployeeCondition(prefix + "employees.", where);
    }

}

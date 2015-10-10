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
package org.seasar.extension.jdbc.it.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * @author taedium
 * 
 */
@Entity
public class Employee {

    /** */
    @Id
    public int employeeId;

    /** */
    public int employeeNo;

    /** */
    public String employeeName;

    /** */
    public int managerId;

    /** */
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "EMPLOYEE_ID")
    public Employee manager;

    /** */
    @Temporal(TemporalType.DATE)
    public Date hiredate;

    /** */
    public BigDecimal salary;

    /** */
    public int departmentId;

    /** */
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "DEPARTMENT_ID")
    public Department department;

    /** */
    public int addressId;

    /** */
    @OneToOne()
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    public Address address;

    /** */
    @Version
    public int version;
}

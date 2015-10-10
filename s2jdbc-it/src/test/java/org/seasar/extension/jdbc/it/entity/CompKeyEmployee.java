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
import javax.persistence.JoinColumns;
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
public class CompKeyEmployee {

    /** */
    @Id
    public int employeeId1;

    /** */
    @Id
    public int employeeId2;

    /** */
    public int employeeNo;

    /** */
    public String employeeName;

    /** */
    public int managerId1;

    /** */
    public int managerId2;

    /** */
    @ManyToOne
    @JoinColumns( {
        @JoinColumn(name = "MANAGER_ID1", referencedColumnName = "EMPLOYEE_ID1"),
        @JoinColumn(name = "MANAGER_ID2", referencedColumnName = "EMPLOYEE_ID2") })
    public CompKeyEmployee manager;

    /** */
    @Temporal(TemporalType.DATE)
    public Date hiredate;

    /** */
    public BigDecimal salary;

    /** */
    public int departmentId1;

    /** */
    public int departmentId2;

    /** */
    @ManyToOne
    @JoinColumns( {
        @JoinColumn(name = "department_id1", referencedColumnName = "DEPARTMENT_ID1"),
        @JoinColumn(name = "department_id2", referencedColumnName = "DEPARTMENT_ID2") })
    public CompKeyDepartment department;

    /** */
    public int addressId1;

    /** */
    public int addressId2;

    /** */
    @OneToOne()
    @JoinColumns( {
        @JoinColumn(name = "ADDRESS_ID1", referencedColumnName = "ADDRESS_ID1"),
        @JoinColumn(name = "ADDRESS_ID2", referencedColumnName = "ADDRESS_ID2") })
    public CompKeyAddress address;

    /** */
    @Version
    public int version;
}

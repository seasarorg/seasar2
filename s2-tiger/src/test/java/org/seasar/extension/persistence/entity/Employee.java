/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * @author higa
 * 
 */
@Entity
public class Employee {

    @Column(name = "EMP_ID")
    @Id
    private Long id;

    private String employeeName;

    private BigDecimal salary;

    @Version
    private Integer version;

    private Employee manager;

    private List<Employee> assistants;

    private Department department;

    /**
     * @return Returns the assistants.
     */
    public List<Employee> getAssistants() {
        return assistants;
    }

    /**
     * @param assistants
     *            The assistants to set.
     */
    public void setAssistants(List<Employee> assistants) {
        this.assistants = assistants;
    }

    /**
     * @return Returns the department.
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param department
     *            The department to set.
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * @return Returns the employeeName.
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName
     *            The employeeName to set.
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the manager.
     */
    public Employee getManager() {
        return manager;
    }

    /**
     * @param manager
     *            The manager to set.
     */
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    /**
     * @return Returns the salary.
     */
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * @param salary
     *            The salary to set.
     */
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    /**
     * @return Returns the version.
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     *            The version to set.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

}
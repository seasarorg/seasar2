/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.unit;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Employee6 {

    @Id
    private Long id;

    private long empno;

    private String name;

    @ManyToOne
    @JoinColumn(name="FOO", referencedColumnName = "DEPT_ID")
    private Department2 department;

    public Employee6() {
    }

    public Employee6(Long id, long empno, String name, Department2 department) {
        this.id = id;
        this.empno = empno;
        this.name = name;
        this.department = department;
    }

    public Department2 getDepartment() {
        return department;
    }

    public void setDepartment(Department2 department) {
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEmpno() {
        return empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Employee6))
            return false;
        Employee6 castOther = (Employee6) other;
        return this.getEmpno() == castOther.getEmpno();
    }

    @Override
    public int hashCode() {
        return (int) this.getEmpno();
    }
}

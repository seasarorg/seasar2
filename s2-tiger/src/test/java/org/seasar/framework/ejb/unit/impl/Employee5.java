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
package org.seasar.framework.ejb.unit.impl;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="EMP")
@Inheritance(strategy=JOINED)
public abstract class Employee5 {
    @Id
    protected Long id;
    
    protected long empno;
    
    @Version
    protected Integer version;
    
    @ManyToOne
    protected Address address;

    public Employee5() {
    }

    public Employee5(Long id, long empno, Address address) {
        this.id = id;
        this.empno = empno;
        this.address = address;
    }
    
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getEmpno() {
        return empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object other) {
        if ( !(other instanceof Employee) ) return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

    @Override
    public int hashCode() {
        return (int) this.getEmpno();
    }
}

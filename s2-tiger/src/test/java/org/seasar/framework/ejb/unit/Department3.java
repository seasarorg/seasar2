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

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

@Entity
@IdClass(Department3PK.class)
public class Department3 {

	@Id
	private Long id1;

    @Id
    private Long id2;
    
	private long deptno;

	private String name; 
	
	@OneToMany(mappedBy = "department")
	private Collection<Employee> employees = new HashSet<Employee>();

	public Department3() {
	}

	public Department3(Long id1, Long id2, long deptno, String name) {
		this.id1 = id1;
		this.id2 = id2;
		this.deptno = deptno;
		this.name = name;
	}
	
	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}

	public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public long getDeptno() {
		return deptno;
	}

	public void setDeptno(long deptno) {
		this.deptno = deptno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public boolean equals(Object other) {
        if ( !(other instanceof Department3) ) return false;
        Department3 castOther = (Department3) other;
        return this.getDeptno() == castOther.getDeptno();
    }

	@Override
    public int hashCode() {
        return (int) this.getDeptno();
    }

}

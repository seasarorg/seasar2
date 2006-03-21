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

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee2 {

	@Id
	private Long id;
	
	private long empno;
	
	private String name;
	
	@Embedded
	private EmployeePeriod period;

	public Employee2() {
	}

	public Employee2(Long id, long empno, String name, EmployeePeriod period) {
		this.id = id;
		this.empno = empno;
		this.name = name;
		this.period = period;
	}

	public EmployeePeriod getPeriod() {
		return period;
	}

	public void setPeriod(EmployeePeriod period) {
		this.period = period;
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
        if ( !(other instanceof Employee) ) return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

	@Override
    public int hashCode() {
        return (int) this.getEmpno();
    }
}

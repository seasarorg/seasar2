package org.seasar.framework.ejb.unit.impl;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee3 {
	@Id
	private Long id;
	
	private long empno;
	
	private String name;
	
	@ManyToOne
	private Employee3 boss;

	public Employee3() {
	}

	public Employee3(Long id, long empno, String name, Employee3 boss) {
		this.id = id;
		this.empno = empno;
		this.name = name;
		this.boss = boss;
	}

	public Employee3 getBoss() {
		return boss;
	}

	public void setBoss(Employee3 boss) {
		this.boss = boss;
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

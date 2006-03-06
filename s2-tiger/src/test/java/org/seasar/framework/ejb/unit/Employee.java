package org.seasar.framework.ejb.unit;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	private Long id;
	
	private long empno;
	
	private String name;

    @ManyToOne
	private Department department;

	public Employee() {
	}

	public Employee(Long id, long empno, String name, Department department) {
		this.id = id;
		this.empno = empno;
		this.name = name;
		this.department = department;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
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
        if ( !(other instanceof Employee) ) return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

	@Override
    public int hashCode() {
        return (int) this.getEmpno();
    }
}

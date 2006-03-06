package org.seasar.framework.ejb.unit.impl;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee4 {
	@Id
	private Long id;

	private long empno;

	private String name;

	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = "startDate", column = @Column(name = "beginDate", table="Hoge")),
			@AttributeOverride(name = "endDate", column = @Column(name = "finishDate")) })
	private EmployeePeriod period;

	public Employee4() {
	}

	public Employee4(Long id, long empno, String name, EmployeePeriod period) {
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
		if (!(other instanceof Employee))
			return false;
		Employee castOther = (Employee) other;
		return this.getEmpno() == castOther.getEmpno();
	}

	@Override
	public int hashCode() {
		return (int) this.getEmpno();
	}
}

package org.seasar.framework.ejb.unit;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Project {

	@Id
	private Long id;

	@ManyToMany
	private Collection<Employee4> employees = new HashSet<Employee4>();

	public Project() {
	}

	public Project(Long id) {
		this.id = id;
	}

	public Collection<Employee4> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee4> employees) {
		this.employees = employees;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

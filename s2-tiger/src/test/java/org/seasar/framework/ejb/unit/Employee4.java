package org.seasar.framework.ejb.unit;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Employee4 {

	@Id
	private Long id;

	@ManyToMany(mappedBy="employees")
	private Collection<Project> projects = new HashSet<Project>();

	public Employee4() {
	}

	public Employee4(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<Project> getProjects() {
		return projects;
	}

	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}
}

package org.seasar.framework.ejb.unit.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

@Entity
@Table(name = "Foo1")
@SecondaryTable(name = "Foo2")
@SecondaryTables( { @SecondaryTable(name = "Foo3") })
public class Hoge3 {

	@Id
	@Column(name="Foo1aaa", table="Foo1")
	private Long aaa;

	@Column(name="Foo2bbb", table="Foo2")
	private Integer bbb;

	@Column(name="Foo3ccc", table="Foo3")
	private java.util.Date ccc;

	public Long getAaa() {
		return aaa;
	}

	public void setAaa(Long aaa) {
		this.aaa = aaa;
	}

	public Integer getBbb() {
		return bbb;
	}

	public void setBbb(Integer bbb) {
		this.bbb = bbb;
	}

	public java.util.Date getCcc() {
		return ccc;
	}

	public void setCcc(java.util.Date ccc) {
		this.ccc = ccc;
	}
}

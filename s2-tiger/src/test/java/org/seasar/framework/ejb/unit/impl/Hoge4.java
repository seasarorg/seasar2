package org.seasar.framework.ejb.unit.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Hoge4 {
	private Long aaa;

	private Integer bbb;

	private java.util.Date ccc;

	@Id
	public Long getAaa() {
		return aaa;
	}

	public void setAaa(Long aaa) {
		this.aaa = aaa;
	}

	public void setBbb(Integer bbb) {
		this.bbb = bbb;
	}

	public java.util.Date getCcc() {
		return ccc;
	}
}

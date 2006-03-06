package org.seasar.framework.ejb.unit.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Hoge {

	@Id
	private Long aaa;

	private Integer bbb;

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

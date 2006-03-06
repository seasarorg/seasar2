package org.seasar.framework.ejb.unit.impl;

import javax.persistence.Embeddable;

@Embeddable
public class EmployeePeriod {

	private java.util.Date startDate;

	private java.util.Date endDate;
	
	public EmployeePeriod(long startDate, long endDate) {
		this.startDate = new java.util.Date(startDate);
		this.endDate = new java.util.Date(endDate);
	}

	public java.util.Date getEndDate() {
		return new java.util.Date(endDate.getTime());
	}

	public java.util.Date getStartDate() {
		return new java.util.Date(startDate.getTime());
	}
}

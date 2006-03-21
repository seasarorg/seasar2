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

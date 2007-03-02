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
package examples.jsf.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Employee implements Serializable {

	public static final String TABLE = "EMP";

	private Integer empno;

	private String ename;

	private String job;

	private Integer mgr;

	private java.util.Date hiredate;

	private BigDecimal sal;

	private BigDecimal comm;

	private Integer deptno;

	private int versionNo;

	public Employee() {
	}

	public Integer getEmpno() {
		return this.empno;
	}

	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public java.lang.String getEname() {
		return this.ename;
	}

	public void setEname(java.lang.String ename) {
		this.ename = ename;
	}

	public java.lang.String getJob() {
		return this.job;
	}

	public void setJob(java.lang.String job) {
		this.job = job;
	}

	public Integer getMgr() {
		return this.mgr;
	}

	public void setMgr(Integer mgr) {
		this.mgr = mgr;
	}

	public java.util.Date getHiredate() {
		return this.hiredate;
	}

	public void setHiredate(java.util.Date hiredate) {
		this.hiredate = hiredate;
	}

	public BigDecimal getSal() {
		return this.sal;
	}

	public void setSal(BigDecimal sal) {
		this.sal = sal;
	}

	public BigDecimal getComm() {
		return this.comm;
	}

	public void setComm(BigDecimal comm) {
		this.comm = comm;
	}

	public Integer getDeptno() {
		return this.deptno;
	}

	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}

	public int getVersionNo() {
		return versionNo;
	}
	
	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

	public boolean equals(Object other) {
		if (!(other instanceof Employee)) {
			return false;
		}
		Employee castOther = (Employee) other;
		return getEmpno() == castOther.getEmpno();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("[");
		setupToString(buf);
		buf.append("]");
		return buf.toString();
	}
	
	protected void setupToString(StringBuffer buf) {
		buf.append(empno).append(", ");
		buf.append(ename).append(", ");
		buf.append(job).append(", ");
		buf.append(mgr).append(", ");
		buf.append(hiredate).append(", ");
		buf.append(sal).append(", ");
		buf.append(comm).append(", ");
		buf.append(deptno).append(", ");
		buf.append(versionNo);
	}

	public int hashCode() {
		if (empno != null) {
			return empno.intValue();
		}
		return 0;
	}
}
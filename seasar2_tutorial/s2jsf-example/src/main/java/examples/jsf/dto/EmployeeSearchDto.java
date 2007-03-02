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
package examples.jsf.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class EmployeeSearchDto implements Serializable {

    public static final String COMPONENT = "instance = session";

    private static final long serialVersionUID = 1L;

    private Integer empno;

    private String ename;

    private String job;

    private Short mgr;

    private java.util.Date fromHiredate;

    private java.util.Date toHiredate;

    private BigDecimal fromSal;

    private BigDecimal toSal;

    private Integer deptno;

    public EmployeeSearchDto() {
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

    public Short getMgr() {
        return this.mgr;
    }

    public void setMgr(Short mgr) {
        this.mgr = mgr;
    }

    public java.util.Date getFromHiredate() {
        return this.fromHiredate;
    }

    public void setFromHiredate(java.util.Date fromHiredate) {
        this.fromHiredate = fromHiredate;
    }

    public java.util.Date getToHiredate() {
        return this.toHiredate;
    }

    public void setToHiredate(java.util.Date toHiredate) {
        this.toHiredate = toHiredate;
    }

    public BigDecimal getFromSal() {
        return this.fromSal;
    }

    public void setFromSal(BigDecimal fromSal) {
        this.fromSal = fromSal;
    }

    public BigDecimal getToSal() {
        return this.toSal;
    }

    public void setToSal(BigDecimal toSal) {
        this.toSal = toSal;
    }

    public Integer getDeptno() {
        return this.deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        buf.append(empno).append(", ");
        buf.append(ename).append(", ");
        buf.append(job).append(", ");
        buf.append(mgr).append(", ");
        buf.append(fromHiredate).append(", ");
        buf.append(toHiredate).append(", ");
        buf.append(fromSal).append(", ");
        buf.append(toSal).append(", ");
        buf.append(deptno).append("]");
        return buf.toString();
    }

}

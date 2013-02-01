/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo;

/**
 *
 */
public class Employee {

    private static final long serialVersionUID = -8378271087258215629L;

    private long empno;

    private String ename;

    private String job;

    private Employee mgr;

    private java.util.Date hiredate;

    private Float sal;

    private Float comm;

    private Integer deptno;

    private byte[] password;

    private String dummy;

    private Department department;

    /**
     * 
     */
    public Employee() {
    }

    /**
     * @param empno
     */
    public Employee(long empno) {
        this.empno = empno;
    }

    /**
     * @return
     */
    public long getEmpno() {
        return this.empno;
    }

    /**
     * @param empno
     */
    public void setEmpno(long empno) {
        this.empno = empno;
    }

    /**
     * @return
     */
    public java.lang.String getEname() {
        return this.ename;
    }

    /**
     * @param ename
     */
    public void setEname(java.lang.String ename) {
        this.ename = ename;
    }

    /**
     * @return
     */
    public java.lang.String getJob() {
        return this.job;
    }

    /**
     * @param job
     */
    public void setJob(java.lang.String job) {
        this.job = job;
    }

    /**
     * @return
     */
    public Employee getMgr() {
        return this.mgr;
    }

    /**
     * @param mgr
     */
    public void setMgr(Employee mgr) {
        this.mgr = mgr;
    }

    /**
     * @return
     */
    public java.util.Date getHiredate() {
        return this.hiredate;
    }

    /**
     * @param hiredate
     */
    public void setHiredate(java.util.Date hiredate) {
        this.hiredate = hiredate;
    }

    /**
     * @return
     */
    public Float getSal() {
        return this.sal;
    }

    /**
     * @param sal
     */
    public void setSal(Float sal) {
        this.sal = sal;
    }

    /**
     * @return
     */
    public Float getComm() {
        return this.comm;
    }

    /**
     * @param comm
     */
    public void setComm(Float comm) {
        this.comm = comm;
    }

    /**
     * @return
     */
    public Integer getDeptno() {
        return this.deptno;
    }

    /**
     * @param deptno
     */
    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    /**
     * @return
     */
    public byte[] getPassword() {
        return this.password;
    }

    /**
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }

    /**
     * @return
     */
    public String getDummy() {
        return this.dummy;
    }

    /**
     * @param dummy
     */
    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    /**
     * @return
     */
    public Department getDepartment() {
        return this.department;
    }

    /**
     * @param department
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Employee))
            return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(empno).append(", ");
        buf.append(ename).append(", ");
        buf.append(job).append(", ");
        buf.append(mgr).append(", ");
        buf.append(hiredate).append(", ");
        buf.append(sal).append(", ");
        buf.append(comm).append(", ");
        buf.append(deptno).append(" {");
        buf.append(department).append("}");
        return buf.toString();
    }

    @Override
    public int hashCode() {
        return (int) this.getEmpno();
    }
}

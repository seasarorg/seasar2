/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author higa
 *
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = -5411969211921106291L;

    private long empno;

    private String ename;

    private String job;

    private Short mgr;

    private java.util.Date hiredate;

    private Float sal;

    private Float comm;

    private short deptno;

    private Date tstamp;

    private Department department;

    /**
     * @param empno
     * @param ename
     * @param job
     * @param mgr
     * @param hiredate
     * @param sal
     * @param comm
     * @param deptno
     * @param tstamp
     */
    public Employee(long empno, java.lang.String ename, java.lang.String job,
            Short mgr, java.util.Date hiredate, Float sal, Float comm,
            short deptno, Date tstamp) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
        this.tstamp = tstamp;
    }

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
    public Short getMgr() {
        return this.mgr;
    }

    /**
     * @param mgr
     */
    public void setMgr(Short mgr) {
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
    public short getDeptno() {
        return this.deptno;
    }

    /**
     * @param deptno
     */
    public void setDeptno(short deptno) {
        this.deptno = deptno;
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

    /**
     * @return
     */
    public Date getTstamp() {
        return this.tstamp;
    }

    /**
     * @param tstamp
     */
    public void setTstamp(Date tstamp) {
        this.tstamp = tstamp;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Employee))
            return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

    public int hashCode() {
        return (int) this.getEmpno();
    }
}

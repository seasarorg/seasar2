package org.seasar.extension.jdbc.impl;

import java.io.Serializable;

public class Department implements Serializable {
	
    private static final long serialVersionUID = -1031433105487668130L;

	public static final String TABLE = "DEPT";

    private int deptno;

    private String dname;

    private String loc;
    
    private int versionNo;
    
    private boolean active;

    public Department() {
    }

    public int getDeptno() {
        return this.deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

    public java.lang.String getDname() {
        return this.dname;
    }

    public void setDname(java.lang.String dname) {
        this.dname = dname;
    }

    public java.lang.String getLoc() {
        return this.loc;
    }

    public void setLoc(java.lang.String loc) {
        this.loc = loc;
    }
    
    public int getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

    public boolean equals(Object other) {
        if ( !(other instanceof Department) ) return false;
        Department castOther = (Department) other;
        return this.getDeptno() == castOther.getDeptno();
    }

    public int hashCode() {
        return this.getDeptno();
    }
}

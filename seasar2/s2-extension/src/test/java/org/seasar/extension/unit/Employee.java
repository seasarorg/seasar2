package org.seasar.extension.unit;

import java.io.Serializable;

public class Employee implements Serializable {

    private static final long serialVersionUID = -8710890359270847237L;

    private long empno;

    private String ename;

    private int deptno;
    
    private String dname;

    public Employee() {
    }

    public Employee(long empno) {
        this.empno = empno;
    }

    public long getEmpno() {
        return this.empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    public String getEname() {
        return this.ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getDeptno() {
        return this.deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }
    
	public String getDname() {
		return this.dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

    public boolean equals(Object other) {
        if ( !(other instanceof Employee) ) return false;
        Employee castOther = (Employee) other;
        return this.getEmpno() == castOther.getEmpno();
    }

    public int hashCode() {
        return (int) this.getEmpno();
    }
    
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append(empno).append(", ");
		buf.append(ename).append(", ");
		buf.append(deptno).append(", ");
		buf.append(dname);
    	return buf.toString();
    }
}

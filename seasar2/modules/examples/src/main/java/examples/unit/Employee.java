package examples.unit;

import java.io.Serializable;

public class Employee implements Serializable {

    private static final long serialVersionUID = -7931222958183639575L;

    private long empno;

    private String ename;

    private short deptno;
    
    private String dname;

    public Employee(long empno, String ename, short deptno, String dname) {
        this.empno = empno;
        this.ename = ename;
        this.deptno = deptno;
        this.dname = dname;
    }

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

    public short getDeptno() {
        return this.deptno;
    }

    public void setDeptno(short deptno) {
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

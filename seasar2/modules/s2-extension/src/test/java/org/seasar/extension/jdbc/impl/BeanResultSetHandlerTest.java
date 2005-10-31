package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.BeanResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class BeanResultSetHandlerTest extends S2TestCase {

	public BeanResultSetHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandle() throws Exception {
		ResultSetHandler handler = new BeanResultSetHandler(Employee.class);
		String sql = "select * from emp where empno = 7788";
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		Employee ret = null;
		try {
			ResultSet rs = ps.executeQuery();
			try {
				ret = (Employee) handler.handle(rs);
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}
		assertNotNull("1", ret);
		System.out.println(ret.getEmpno() + "," + ret.getEname());
	}
	
	public void testHandle2() throws Exception {
		ResultSetHandler handler = new BeanResultSetHandler(Employee.class);
		String sql = "select ename, job from emp where empno = 7788";
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		Employee ret = null;
		try {
			ResultSet rs = ps.executeQuery();
			try {
				ret = (Employee) handler.handle(rs);
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}
		assertNotNull("1", ret);
		System.out.println(ret.getEname() + "," + ret.getJob());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BeanResultSetHandlerTest.class);
	}

}
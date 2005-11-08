package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.BeanListResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class BeanListResultSetHandlerTest extends S2TestCase {

	public BeanListResultSetHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandle() throws Exception {
		ResultSetHandler handler = new BeanListResultSetHandler(Employee.class);
		String sql = "select * from emp";
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		List ret = null;
		try {
			ResultSet rs = ps.executeQuery();
			try {
				ret = (List) handler.handle(rs);
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}
		assertNotNull("1", ret);
		for (int i = 0; i < ret.size(); ++i) {
			Employee emp = (Employee) ret.get(i);
			System.out.println(emp.getEmpno() + "," + emp.getEname());
		}
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BeanListResultSetHandlerTest.class);
	}

}
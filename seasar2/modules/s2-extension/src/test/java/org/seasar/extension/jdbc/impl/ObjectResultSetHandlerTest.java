package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.ObjectResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class ObjectResultSetHandlerTest extends S2TestCase {

	public ObjectResultSetHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandle() throws Exception {
		ResultSetHandler handler = new ObjectResultSetHandler();
		String sql = "select ename from emp where empno = 7788";
		Connection con = getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		String ret = null;
		try {
			ResultSet rs = ps.executeQuery();
			try {
				ret = (String) handler.handle(rs);
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}
		assertEquals("1", "SCOTT", ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ObjectResultSetHandlerTest.class);
	}

}

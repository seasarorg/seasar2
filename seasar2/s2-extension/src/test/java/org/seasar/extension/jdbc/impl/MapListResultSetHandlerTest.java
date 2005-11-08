package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.impl.MapListResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class MapListResultSetHandlerTest extends S2TestCase {

	public MapListResultSetHandlerTest(String arg0) {
		super(arg0);
	}

	public void testHandle() throws Exception {
		ResultSetHandler handler = new MapListResultSetHandler();
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
		for (int i = 0; i < ret.size(); ++i) {
			System.out.println(ret.get(i));
		}
		assertNotNull("1", ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MapListResultSetHandlerTest.class);
	}

}

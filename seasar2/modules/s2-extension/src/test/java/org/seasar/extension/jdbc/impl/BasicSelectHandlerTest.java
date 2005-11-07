package org.seasar.extension.jdbc.impl;

import java.util.Map;

import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.MapResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class BasicSelectHandlerTest extends S2TestCase {

	public BasicSelectHandlerTest(String arg0) {
		super(arg0);
	}

	public void testExecute() throws Exception {
		String sql = "select * from emp where empno = ?";
		BasicSelectHandler handler = new BasicSelectHandler(getDataSource(),
				sql, new MapResultSetHandler());
		Map ret = (Map) handler.execute(new Object[] { new Integer(7788) });
		System.out.println(ret);
		assertNotNull("1", ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BasicSelectHandlerTest.class);
	}

}
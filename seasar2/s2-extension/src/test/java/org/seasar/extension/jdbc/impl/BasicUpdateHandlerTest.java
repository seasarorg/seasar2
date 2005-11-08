package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.unit.S2TestCase;

public class BasicUpdateHandlerTest extends S2TestCase {

	public BasicUpdateHandlerTest(String arg0) {
		super(arg0);
	}

	public void testExecuteTx() throws Exception {
		String sql = "update emp set ename = ?, comm = ? where empno = ?";
		BasicUpdateHandler handler =
			new BasicUpdateHandler(getDataSource(), sql);
		int ret =
			handler.execute(new Object[] { "SCOTT", null, new Integer(7788)});
		assertEquals("1", 1, ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BasicUpdateHandlerTest.class);
	}

}

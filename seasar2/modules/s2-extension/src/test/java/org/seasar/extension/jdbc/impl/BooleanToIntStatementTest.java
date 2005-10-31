package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.impl.BooleanToIntStatementFactory;
import org.seasar.extension.jdbc.impl.ObjectResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class BooleanToIntStatementTest extends S2TestCase {

	public BooleanToIntStatementTest(String arg0) {
		super(arg0);
	}

	public void testBooleanToIntTx() throws Exception {
		String sql = "update dept set active = ? where deptno = 10";
		BasicUpdateHandler handler =
			new BasicUpdateHandler(getDataSource(), sql, BooleanToIntStatementFactory.INSTANCE);
		int ret =
			handler.execute(new Object[] { Boolean.FALSE });
		assertEquals("1", 1, ret);
		
		String sql2 = "select active from dept where deptno = 10";
		BasicSelectHandler handler2 = new BasicSelectHandler(getDataSource(),
				sql2, new ObjectResultSetHandler());
		Number active = (Number) handler2.execute((Object[]) null);
		assertEquals("1", 0, active.intValue());
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BooleanToIntStatementTest.class);
	}

}

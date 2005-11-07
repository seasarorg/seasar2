package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.impl.BasicStatementFactory;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.impl.ObjectResultSetHandler;
import org.seasar.extension.jdbc.impl.OracleResultSetFactory;
import org.seasar.extension.unit.S2TestCase;

public class BasicSelectHandlerForOracleTest extends S2TestCase {
    public static final String WAVE_DASH = "\u301C";
	public static final String FULL_WIDTH_TILDE = "\uFF5E";

	public BasicSelectHandlerForOracleTest(String arg0) {
		super(arg0);
	}

	public void testExecuteTx() throws Exception {
		String sql = "insert into emp(empno, ename) values(99, ?)";
		BasicUpdateHandler handler = new BasicUpdateHandler(getDataSource(),
				sql);
		handler.execute(new Object[] { WAVE_DASH });

		String sql2 = "select ename from emp where empno = 99";
		BasicSelectHandler handler2 = new BasicSelectHandler(getDataSource(),
				sql2, new ObjectResultSetHandler(),
				BasicStatementFactory.INSTANCE,
				new OracleResultSetFactory());
		String ret = (String) handler2.execute((Object[]) null);
		System.out.println(ret);
		assertEquals("1", FULL_WIDTH_TILDE, ret);
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BasicSelectHandlerForOracleTest.class);
	}

}
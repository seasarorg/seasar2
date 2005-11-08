package org.seasar.extension.jdbc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.impl.BasicBatchHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.MapListResultSetHandler;
import org.seasar.extension.unit.S2TestCase;

public class BasicBatchHandlerTest extends S2TestCase {

	public BasicBatchHandlerTest(String arg0) {
		super(arg0);
	}

	public void testExecuteTx() throws Exception {
		String sql = "update emp set ename = ?, comm = ? where empno = ?";
		BasicBatchHandler handler =
			new BasicBatchHandler(getDataSource(), sql, 2);
		List list = new ArrayList();
		list.add(new Object[] { "aaa", null, new Integer(7369)});
		list.add(new Object[] { "bbb", new Double(100.5), new Integer(7499)});
		list.add(new Object[] { "ccc", null, new Integer(7521)});
		int ret = handler.execute(list);
		assertEquals("1", 3, ret);
		String sql2 =
			"select empno, ename, comm from emp where empno in (7369, 7499, 7521) order by empno";
		BasicSelectHandler handler2 =
			new BasicSelectHandler(
				getDataSource(),
				sql2,
				new MapListResultSetHandler());
		List ret2 = (List) handler2.execute((Object[]) null);
		Map rec = (Map) ret2.get(0);
		assertEquals("2", "aaa", rec.get("ename"));
		rec = (Map) ret2.get(1);
		assertEquals("3", "bbb", rec.get("ename"));
		rec = (Map) ret2.get(2);
		assertEquals("4", "ccc", rec.get("ename"));
	}

	public void setUp() {
		include("j2ee.dicon");
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BasicBatchHandlerTest.class);
	}

}

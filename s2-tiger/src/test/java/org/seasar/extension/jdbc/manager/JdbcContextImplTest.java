package org.seasar.extension.jdbc.manager;

import java.sql.Statement;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.manager.JdbcContextImpl;
import org.seasar.framework.mock.sql.MockCallableStatement;
import org.seasar.framework.mock.sql.MockConnection;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author higa
 * 
 */
public class JdbcContextImplTest extends TestCase {

	private MockConnection con = new MockConnection();

	private JdbcContextImpl ctx = new JdbcContextImpl(con, false);

	@Override
	protected void setUp() throws Exception {
	}

	@Override
	protected void tearDown() throws Exception {
		con = null;
		ctx = null;
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testGetPreparedStatement() throws Exception {
		ctx.setPreparedStatementCacheSize(1);
		MockPreparedStatement ps = (MockPreparedStatement) ctx
				.getPreparedStatement("aaa");
		assertNotNull(ps);
		assertFalse(ctx.isPreparedStatementCacheEmpty());
		assertSame(ps, ctx.getPreparedStatement("aaa"));
		ctx.getPreparedStatement("bbb");
		assertTrue(ps.isClosed());
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testGetCursorPreparedStatement() throws Exception {
		ctx.setCursorPreparedStatementCacheSize(1);
		MockPreparedStatement ps = (MockPreparedStatement) ctx
				.getCursorPreparedStatement("aaa");
		assertNotNull(ps);
		assertFalse(ctx.isCursorPreparedStatementCacheEmpty());
		assertSame(ps, ctx.getCursorPreparedStatement("aaa"));
		ctx.getCursorPreparedStatement("bbb");
		assertTrue(ps.isClosed());
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testGetCallableStatement() throws Exception {
		ctx.setCallableStatementCacheSize(1);
		MockCallableStatement cs = (MockCallableStatement) ctx
				.getCallableStatement("aaa");
		assertNotNull(cs);
		assertFalse(ctx.isCallableStatementCacheEmpty());
		assertSame(cs, ctx.getCallableStatement("aaa"));
		ctx.getCallableStatement("bbb");
		assertTrue(cs.isClosed());
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testGetStatement() throws Exception {
		Statement stmt = ctx.getStatement();
		assertNotNull(stmt);
		assertSame(stmt, ctx.getStatement());
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void testDestroy() throws Exception {
		ctx.getStatement();
		ctx.getPreparedStatement("aaa");
		ctx.getCursorPreparedStatement("bbb");
		ctx.getCallableStatement("ccc");
		ctx.destroy();
		assertTrue(ctx.isStatementNull());
		assertTrue(ctx.isPreparedStatementCacheEmpty());
		assertTrue(ctx.isCursorPreparedStatementCacheEmpty());
		assertTrue(ctx.isCallableStatementCacheEmpty());
		assertTrue(ctx.isConnectionNull());
	}
}

package org.seasar.extension.jdbc.util;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.util.StatementCache;
import org.seasar.framework.mock.sql.MockConnection;
import org.seasar.framework.mock.sql.MockStatement;

/**
 * @author higa
 * 
 */
public class StatementCacheTest extends TestCase {

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testRemoveEldestEntryEntry() throws Exception {
		StatementCache cache = new StatementCache(1);
		MockConnection con = new MockConnection();
		MockStatement stmt1 = con.createMockStatement();
		MockStatement stmt2 = con.createMockStatement();
		cache.put("1", stmt1);
		cache.put("2", stmt2);
		assertTrue(stmt1.isClosed());
	}

	/**
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void testDestroy() throws Exception {
		StatementCache cache = new StatementCache(1);
		MockConnection con = new MockConnection();
		MockStatement stmt1 = con.createMockStatement();
		cache.put("1", stmt1);
		cache.destroy();
		assertTrue(stmt1.isClosed());
		assertTrue(cache.isEmpty());
	}

}

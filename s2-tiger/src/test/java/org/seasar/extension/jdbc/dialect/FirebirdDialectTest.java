package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.dialect.FirebirdDialect;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class FirebirdDialectTest extends TestCase {

	private FirebirdDialect dialect = new FirebirdDialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = "select first 5 * from emp order by id";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id";
		String expected = "select first 15 skip 5 e.* from emp e order by id";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}
}

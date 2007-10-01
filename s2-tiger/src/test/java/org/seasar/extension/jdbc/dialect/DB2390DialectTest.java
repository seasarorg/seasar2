package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.dialect.DB2390Dialect;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class DB2390DialectTest extends TestCase {

	private DB2390Dialect dialect = new DB2390Dialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = sql + " fetch first 5 rows only";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id";
		String expected = sql + " fetch first 15 rows only";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}
}

package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.dialect.MySQLDialect;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MySQLDialectTest extends TestCase {

	private MySQLDialect dialect = new MySQLDialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = sql + " limit 5";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetOnly() throws Exception {
		String sql = "select * from emp order by id";
		try {
			dialect.convertLimitSql(sql, 5, 0);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id";
		String expected = sql + " limit 5, 10";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}
}

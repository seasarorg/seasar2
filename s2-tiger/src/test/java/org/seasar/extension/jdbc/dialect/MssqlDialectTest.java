package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.dialect.MssqlDialect;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MssqlDialectTest extends TestCase {

	private MssqlDialect dialect = new MssqlDialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = "select top 5 * from emp order by id";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly_distinct() throws Exception {
		String sql = "select distinct * from emp order by id";
		String expected = "select distinct top 5 * from emp order by id";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id";
		String expected = "select top 15 e.* from emp e order by id";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}
}

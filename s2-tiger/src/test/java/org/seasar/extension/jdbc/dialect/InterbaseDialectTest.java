package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.dialect.InterbaseDialect;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class InterbaseDialectTest extends TestCase {

	private InterbaseDialect dialect = new InterbaseDialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = sql + " rows 5";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id";
		String expected = sql + " rows 5 to 10";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}
}

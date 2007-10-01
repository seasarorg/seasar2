package org.seasar.extension.jdbc.dialect;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.dialect.DB2Dialect;
import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;

/**
 * @author higa
 * 
 */
public class DB2DialectTest extends TestCase {

	private DB2Dialect dialect = new DB2Dialect();

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
		String sql = "select * from emp order by id";
		String expected = "select * from ( select rownumber() over(order by id)"
				+ " as rownumber_, temp_.* from ( select * from emp ) as temp_ ) as temp2_"
				+ " where rownumber_ >= 6 and rownumber_ <= 15";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));
	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetOnly() throws Exception {
		String sql = "select * from emp order by id";
		String expected = "select * from ( select rownumber() over(order by id)"
				+ " as rownumber_, temp_.* from ( select * from emp ) as temp_ ) as temp2_"
				+ " where rownumber_ >= 6";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));
	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit_notFoundOrderBy()
			throws Exception {
		String sql = "select * from emp";
		try {
			dialect.convertLimitSql(sql, 5, 10);
			fail();
		} catch (OrderByNotFoundRuntimeException e) {
			System.out.println(e.getMessage());
		}

	}
}

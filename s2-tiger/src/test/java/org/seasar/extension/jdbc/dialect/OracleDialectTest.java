package org.seasar.extension.jdbc.dialect;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class OracleDialectTest extends TestCase {

	private OracleDialect dialect = new OracleDialect();

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_limitOnly() throws Exception {
		String sql = "select * from emp order by id for update";
		String expected = "select * from ( select * from emp order by id ) where rownum <= 5 for update";
		assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetLimit() throws Exception {
		String sql = "select e.* from emp e order by id for update";
		String expected = "select * from ( select temp_.*, rownum rownum_ from ( select e.* from emp e order by id ) temp_ where rownum <= 15 ) where rownum_ > 5 for update";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

	}

	/**
	 * @throws Exception
	 */
	public void testConvertLimitSql_offsetOnly() throws Exception {
		String sql = "select e.* from emp e order by id for update";
		String expected = "select * from ( select temp_.*, rownum rownum_ from ( select e.* from emp e order by id ) temp_ ) where rownum_ > 5 for update";
		assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

	}

	/**
	 * @throws Exception
	 */
	public void testGetValueType() throws Exception {
		assertEquals(ValueTypes.WAVE_DASH_STRING, dialect
				.getValueType(String.class));
		assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect
				.getValueType(boolean.class));

	}
}

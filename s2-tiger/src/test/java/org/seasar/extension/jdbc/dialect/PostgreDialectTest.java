package org.seasar.extension.jdbc.dialect;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class PostgreDialectTest extends TestCase {

    private PostgreDialect dialect = new PostgreDialect();

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
        String expected = sql + " offset 5";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetLimit() throws Exception {
        String sql = "select e.* from emp e order by id";
        String expected = sql + " limit 10 offset 5";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

    }

    /**
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect
                .getValueType(List.class));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect
                .getValueType(ArrayList.class));
    }

    /**
     * @throws Exception
     */
    public void testNeedsParameterForResultSet() throws Exception {
        assertTrue(dialect.needsParameterForResultSet());
    }
}

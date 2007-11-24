package org.seasar.extension.jdbc.dialect;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class OracleDialectTest extends TestCase {

    private OracleDialect dialect = new OracleDialect();

    /** */
    public String stringField;

    /** */
    public boolean booleanField;

    /** */
    public List<?> listField;

    /** */
    public ArrayList<?> arrayListField;

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
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ where rownum <= 15 ) where rownumber_ > 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

    }

    /**
     * @throws Exception
     */
    public void testConvertLimitSql_offsetOnly() throws Exception {
        String sql = "select e.* from emp e order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ ) where rownumber_ > 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

    }

    /**
     * @throws Exception
     */
    public void testGetValueType() throws Exception {
        assertEquals(ValueTypes.WAVE_DASH_STRING, dialect.getValueType(
                String.class, false, null));
        assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect.getValueType(
                boolean.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(
                List.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(
                ArrayList.class, false, null));
    }

    /**
     * @throws Exception
     */
    public void testGetValueType_propertyMeta() throws Exception {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("stringField"));
        pm.setValueType(ValueTypes.STRING);
        assertEquals(ValueTypes.WAVE_DASH_STRING, dialect.getValueType(pm));

        pm.setField(getClass().getField("booleanField"));
        pm.setValueType(ValueTypes.BOOLEAN);
        assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect.getValueType(pm));

        pm.setField(getClass().getField("listField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));

        pm.setField(getClass().getField("arrayListField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));
    }

    /**
     * @throws Exception
     */
    public void testNeedsParameterForResultSet() throws Exception {
        assertTrue(dialect.needsParameterForResultSet());
    }
}

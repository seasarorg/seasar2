package org.seasar.extension.jdbc.dialect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.types.ValueTypes;

/**
 * @author higa
 * 
 */
public class PostgreDialectTest extends TestCase {

    private PostgreDialect dialect = new PostgreDialect();

    /** */
    public byte[] bytesField;

    /** */
    public Serializable serializableField;

    /** */
    public List<?> listField;

    /** */
    public ArrayList<?> arrayListField;

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
        assertEquals(PostgreDialect.BLOB_TYPE, dialect.getValueType(
                byte[].class, true, null));
        assertEquals(PostgreDialect.SERIALIZABLE_BLOB_TYPE, dialect
                .getValueType(Serializable.class, true, null));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(
                List.class, false, null));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(
                ArrayList.class, false, null));
    }

    /**
     * @throws Exception
     */
    public void testGetValueType_propertyMeta() throws Exception {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("bytesField"));
        pm.setLob(true);
        assertEquals(PostgreDialect.BLOB_TYPE, dialect.getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("serializableField"));
        pm.setLob(true);
        assertEquals(PostgreDialect.SERIALIZABLE_BLOB_TYPE, dialect
                .getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("listField"));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(pm));

        pm = new PropertyMeta();
        pm.setField(getClass().getField("arrayListField"));
        assertEquals(ValueTypes.POSTGRE_RESULT_SET, dialect.getValueType(pm));
    }

    /**
     * @throws Exception
     */
    public void testNeedsParameterForResultSet() throws Exception {
        assertTrue(dialect.needsParameterForResultSet());
    }
}

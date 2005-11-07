package org.seasar.extension.unit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;

/**
 * @author higa
 * 
 */
public class MapReaderTest extends TestCase {

    public void testRead() throws Exception {
        Map emp = new HashMap();
        emp.put("empno", new Integer(7788));
        emp.put("ename", "SCOTT");
        MapReader reader = new MapReader(emp);
        DataSet ds = reader.read();
        DataTable table = ds.getTable(0);
        DataRow row = table.getRow(0);
        assertEquals("1", new BigDecimal(7788), row.getValue("empno"));
        assertEquals("2", "SCOTT", row.getValue("ename"));
        assertEquals("3", RowStates.UNCHANGED, row.getState());
    }
}
package org.seasar.extension.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.unit.MapListReader;

/**
 * @author higa
 *  
 */
public class MapListReaderTest extends TestCase {

	public MapListReaderTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MapListReaderTest.class);
	}

	protected void tearDown() throws Exception {
	}

	public void testRead() throws Exception {
		Map emp = new HashMap();
		emp.put("empno", new Integer(7788));
		emp.put("ename", "SCOTT");
		List list = new ArrayList();
		list.add(emp);
		MapListReader reader = new MapListReader(list);
		DataSet ds = reader.read();
		DataTable table = ds.getTable(0);
		DataRow row = table.getRow(0);
		assertEquals("1", new BigDecimal(7788), row.getValue("empno"));
		assertEquals("2", "SCOTT", row.getValue("ename"));
	}
}
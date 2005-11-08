package org.seasar.extension.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;

/**
 * @author higa
 * 
 */
public class BeanListReaderTest extends TestCase {

    public void testRead() throws Exception {
        Employee emp = new Employee();
        emp.setEmpno(7788);
        emp.setEname("SCOTT");
        emp.setDeptno(10);
        emp.setDname("HOGE");
        List list = new ArrayList();
        list.add(emp);
        BeanListReader reader = new BeanListReader(list);
        DataSet ds = reader.read();
        DataTable table = ds.getTable(0);
        DataRow row = table.getRow(0);
        assertEquals("1", new BigDecimal(7788), row.getValue("empno"));
        assertEquals("2", "SCOTT", row.getValue("ename"));
        assertEquals("3", new BigDecimal(10), row.getValue("deptno"));
        assertEquals("4", "HOGE", row.getValue("dname"));
    }
}
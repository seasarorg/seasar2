package examples.unit;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;

import examples.unit.Employee;
import examples.unit.EmployeeDao;

public class EmployeeDaoImplTest extends S2TestCase {

	private EmployeeDao dao_;

	public EmployeeDaoImplTest(String arg0) {
		super(arg0);
	}

	public void setUp() {
		include("examples/unit/EmployeeDao.dicon");
	}

	public void testGetEmployeeTx() throws Exception {
		readXlsWriteDb("getEmployeePrepare.xls");
		Employee emp = dao_.getEmployee(9900);
		DataSet expected = readXls("getEmployeeExpected.xls");
		assertEquals("1", expected, emp);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(EmployeeDaoImplTest.class);
	}
}
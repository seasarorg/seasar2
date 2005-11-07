package examples.unit;

import org.seasar.extension.jdbc.SelectHandler;

public class EmployeeDaoImpl implements EmployeeDao {

	private SelectHandler getEmployeeHandler_;

	public EmployeeDaoImpl() {
	}

	public void setGetEmployeeHandler(SelectHandler getEmployeeHandler) {
		getEmployeeHandler_ = getEmployeeHandler;
	}

	public Employee getEmployee(int empno) {
		return (Employee) getEmployeeHandler_.execute(
			new Object[] { new Integer(empno)});
	}
}

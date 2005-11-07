package examples.dbcp;

import java.sql.SQLException;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeDao dao_;
	
	public EmployeeServiceImpl(EmployeeDao dao) {
		dao_ = dao;
	}

	public String getEmployeeName(int empno) throws SQLException {
		return dao_.getEmployeeName(empno);
	}

}

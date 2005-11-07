package examples.dbcp;

import java.sql.SQLException;

public interface EmployeeService {

	public String getEmployeeName(int empno) throws SQLException;
}

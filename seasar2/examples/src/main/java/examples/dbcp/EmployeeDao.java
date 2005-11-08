package examples.dbcp;

import java.sql.SQLException;

public interface EmployeeDao {

	public String getEmployeeName(int empno) throws SQLException;
}

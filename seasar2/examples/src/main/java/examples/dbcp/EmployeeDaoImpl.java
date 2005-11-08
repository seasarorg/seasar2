package examples.dbcp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class EmployeeDaoImpl implements EmployeeDao {

	private DataSource dataSource_;
	
	public EmployeeDaoImpl(DataSource dataSource) {
		dataSource_ = dataSource;
	}

	public String getEmployeeName(int empno) throws SQLException {
		String ename = null;
		Connection con = dataSource_.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
				"SELECT ename FROM emp WHERE empno = ?");
			try {
				ps.setInt(1, empno);
				ResultSet rs = ps.executeQuery();
				try {
					if (rs.next()) {
						ename = rs.getString("ename");
					}
				} finally {
					rs.close();
				}
			} finally {
				ps.close();
			}
		} finally {
			con.close();
		}
		return ename;
	}

}

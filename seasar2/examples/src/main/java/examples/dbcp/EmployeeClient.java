package examples.dbcp;

import java.sql.SQLException;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class EmployeeClient {

	private static final String PATH =
		"examples/dbcp/Employee.dicon";
		
	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			EmployeeService service = (EmployeeService)
			container.getComponent(EmployeeService.class);
			System.out.println(service.getEmployeeName(7788));
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			container.destroy();
		}
		
	}
}

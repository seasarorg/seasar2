package examples.jdbc;

import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class UpdateClient {

	private static final String PATH = "examples/jdbc/Update.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			UpdateHandler handler =
				(UpdateHandler) container.getComponent("updateHandler");
			int result =
				handler.execute(new Object[] { "SCOTT", new Integer(7788)});
			System.out.println(result);
		} finally {
			container.destroy();
		}

	}
}

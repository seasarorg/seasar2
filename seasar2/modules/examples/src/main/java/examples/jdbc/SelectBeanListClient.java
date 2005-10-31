package examples.jdbc;

import java.util.List;

import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class SelectBeanListClient {

	private static final String PATH =
		"examples/jdbc/SelectBeanList.dicon";
		
	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			SelectHandler handler = (SelectHandler)
			container.getComponent("selectBeanListHandler");
			List result = (List) handler.execute(null);
			for (int i = 0; i < result.size(); ++i) {
				System.out.println(result.get(i));
			}
		} finally {
			container.destroy();
		}
		
	}
}

package examples.jdbc;

import java.util.Map;

import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class SelectMapClient {

	private static final String PATH =
		"examples/jdbc/SelectMap.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			SelectHandler handler =
				(SelectHandler) container.getComponent("selectMapHandler");
			Map result = (Map) handler.execute(
				new Object[]{new Integer(7788)});
			System.out.println(result);
		} finally {
			container.destroy();
		}

	}
}

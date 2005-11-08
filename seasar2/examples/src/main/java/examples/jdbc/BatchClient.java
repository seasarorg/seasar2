package examples.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.BatchHandler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class BatchClient {

	private static final String PATH = "examples/jdbc/Batch.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		container.init();
		try {
			BatchHandler handler =
				(BatchHandler) container.getComponent("batchHandler");
			List argsList = new ArrayList();
			argsList.add(new Object[] { "SMITH", new Integer(7369)});
			argsList.add(new Object[] { "SCOTT", new Integer(7788)});
			int result = handler.execute(argsList);
			System.out.println(result);
		} finally {
			container.destroy();
		}

	}
}
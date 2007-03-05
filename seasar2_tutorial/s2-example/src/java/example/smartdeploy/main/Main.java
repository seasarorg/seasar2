package example.smartdeploy.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;

import example.smartdeploy.service.PrintService;

public class Main {

	public static void main(String[] args) throws Exception {
		SingletonS2ContainerFactory.init();
		S2Container container = SingletonS2ContainerFactory.getContainer();

		Class.forName(PrintService.class.getName());
		for (int i = 0; i < 3; ++i) {
			HotdeployUtil.start();
			PrintService printer = (PrintService) container
					.getComponent("printService");
			printer.print();
			HotdeployUtil.stop();
		}
	}

}

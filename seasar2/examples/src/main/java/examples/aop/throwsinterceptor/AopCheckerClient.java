package examples.aop.throwsinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopCheckerClient {
	private static String PATH = "examples/aop/throwsinterceptor/Checker.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		Checker checker = (Checker) container.getComponent(Checker.class);
		checker.check("foo");
		checker.check(null);
		checker.check("hoge");
	}
}
package examples.aop.prototypedelegateinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopPrototypeDelegateClient {
	private static String PATH = "examples/aop/prototypedelegateinterceptor/PrototypeDelegate.dicon";
	public static void main(String[] args) throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		IBase base = (IBase) container.getComponent(Dummy.class);
		for (int i = 0; i < 5; ++i) {
			base.run();
		}
	}
}
package examples.aop.delegateinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopDelegateClient {
	private static String PATH = "examples/aop/delegateinterceptor/Delegate.dicon";
	public static void main(String[] args) throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		IBase base = (IBase) container.getComponent(Dummy.class);
		base.run();
	}
}
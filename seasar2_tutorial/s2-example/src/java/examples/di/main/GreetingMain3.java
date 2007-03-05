package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.GreetingClientSingleton;

public class GreetingMain3 {

	private static final String PATH = "examples/di/dicon/GreetingMain3.dicon";

	public static void main(String[] args) throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);

		GreetingClientSingleton greetingClientSingleton = (GreetingClientSingleton) container
				.getComponent("greetingClientSingleton");
		greetingClientSingleton.execute();

		Thread.sleep(1000);

		greetingClientSingleton = (GreetingClientSingleton) container
				.getComponent("greetingClientSingleton");
		greetingClientSingleton.execute();
	}
}
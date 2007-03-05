package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.GreetingClient3;

public class GreetingMain3 {

	private static final String PATH = "examples/di/dicon/GreetingMain3.dicon";

	public static void main(String[] args) throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);

		System.out.print("singleton1　: ");
		GreetingClient3 singleton = (GreetingClient3) container
				.getComponent("singletonClient");
		singleton.execute();

		Thread.sleep(1000);

		System.out.print("singleton2　: ");
		singleton = (GreetingClient3) container.getComponent("singletonClient");
		singleton.execute();

		System.out.print("prototype1　: ");
		GreetingClient3 prototype = (GreetingClient3) container
				.getComponent("prototypeClient");
		prototype.execute();

		Thread.sleep(1000);

		System.out.print("prototype2　: ");
		prototype = (GreetingClient3) container.getComponent("prototypeClient");
		prototype.execute();
	}
}
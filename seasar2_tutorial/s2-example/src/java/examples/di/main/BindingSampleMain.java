package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.BindingSample;

public class BindingSampleMain {
	private static final String PATH = "examples/di/dicon/BindingSample.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		BindingSample bindingSample = (BindingSample) container
				.getComponent("bindingSample");
		System.out.println(bindingSample.greet());
	}
}

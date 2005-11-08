package examples.aop.originalinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopMeasurementClient {
	private static String PATH = "examples/aop/originalinterceptor/Measurement.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		HeavyProcess heavyProcess = (HeavyProcess) container
				.getComponent(HeavyProcess.class);
		heavyProcess.heavy();
	}
}
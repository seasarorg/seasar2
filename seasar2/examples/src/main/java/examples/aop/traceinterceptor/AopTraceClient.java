package examples.aop.traceinterceptor;

import java.util.Date;
import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopTraceClient {

	private static String PATH = "examples/aop/traceinterceptor/Trace.dicon";

	public static void main(String[] args) {
		S2Container container = S2ContainerFactory.create(PATH);
		List list = (List) container.getComponent(List.class);
		list.size();
		Date date = (Date) container.getComponent(Date.class);
		date.getTime();
		date.hashCode();
	}
}
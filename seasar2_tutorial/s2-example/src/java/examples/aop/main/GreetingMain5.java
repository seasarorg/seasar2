package examples.aop.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.GreetingClient;

public class GreetingMain5 {

    private static final String PATH = "examples/aop/dicon/GreetingMain5.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        GreetingClient greetingClient = (GreetingClient) container
                .getComponent("greetingClient");
        greetingClient.execute();
    }
}
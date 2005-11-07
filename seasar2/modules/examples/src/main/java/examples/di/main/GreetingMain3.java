package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.GreetingClient;

public class GreetingMain3 {

    private static final String PATH =
        "examples/di/dicon/GreetingMain3.dicon";
 
    public static void main(String[] args) {
        S2Container container =
            S2ContainerFactory.create(PATH);
        GreetingClient greetingClient = (GreetingClient)
            container.getComponent("greetingClient");
        greetingClient.execute();
    }
}
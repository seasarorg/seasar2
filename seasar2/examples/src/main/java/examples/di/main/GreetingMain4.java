package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.GreetingClient;

public class GreetingMain4 {

    private static final String PATH =
        "examples/di/dicon/GreetingMain4.dicon";
 
    public static void main(String[] args) {
        S2Container container =
            S2ContainerFactory.create(PATH);
        container.init();
        try {
            GreetingClient greetingClient = (GreetingClient)
                container.getComponent("greetingClient");
            greetingClient.execute();
        } finally {
            container.destroy();
        }
    }
}
package examples.hotswap.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.hotswap.Greeting;

public class GreetingMain {

    private static final String CONFIGURE_PATH = "examples/hotswap/dicon/s2container.dicon";

    private static final String PATH = "examples/hotswap/dicon/hotswap.dicon";

    public static void main(String[] args) throws Exception {
        S2ContainerFactory.configure(CONFIGURE_PATH);
        S2Container container = S2ContainerFactory.create(PATH);
        System.out.println("hotswapMode:" + container.isHotswapMode());
        container.init();
        try {
            Greeting greeting = (Greeting) container
                    .getComponent(Greeting.class);
            System.out.println(greeting.greet());
            System.out.println("Let's modify GreetingImpl, then press ENTER.");
            System.in.read();
            System.out.println("after modify");
            System.out.println(greeting.greet());
        } finally {
            container.destroy();
        }
    }
}
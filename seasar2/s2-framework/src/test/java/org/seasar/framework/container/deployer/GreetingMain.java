package org.seasar.framework.container.deployer;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

public class GreetingMain {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        ComponentDefImpl cd = new ComponentDefImpl(GreetingImpl.class);
        container.register(cd);
        container.init();
        Greeting greeting = (Greeting) container.getComponent(Greeting.class);
        System.out.println(greeting.greet());
        System.out.println("Let's modify GreetingImpl, then press ENTER.");
        System.in.read();
        System.out.println("after modify");
        System.out.println(greeting.greet());
    }

}

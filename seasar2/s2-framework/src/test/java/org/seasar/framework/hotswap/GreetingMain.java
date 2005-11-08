package org.seasar.framework.hotswap;

import org.seasar.framework.hotswap.HotswapProxy;
import org.seasar.framework.hotswap.SimpleHotswapTargetFactory;

public class GreetingMain {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Greeting greeting = (Greeting) HotswapProxy.create(GreetingImpl.class, new SimpleHotswapTargetFactory(GreetingImpl.class));
        System.out.println(greeting.greet());
        System.out.println("Let's modify GreetingImpl, then press ENTER.");
        System.in.read();
        System.out.println("after modify");
        System.out.println(greeting.greet());
    }

}

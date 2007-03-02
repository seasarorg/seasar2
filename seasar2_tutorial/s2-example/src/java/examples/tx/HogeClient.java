package examples.tx;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class HogeClient {

    private static final String PATH = "examples/tx/HogeClient.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        Hoge hoge = (Hoge) container.getComponent(Hoge.class);
        hoge.foo();
    }
}

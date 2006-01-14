package examples.aop.tostringinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopToStringClient {
    private static String PATH = "examples/aop/tostringinterceptor/ToString.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        Entity entity = (Entity) container.getComponent(EntityImplImpl.class);
        System.out.println(entity);
    }
}

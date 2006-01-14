package examples.aop.tostringinterceptor;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private int intValue = 1;

    private static int staticIntValue = 12;

    private double doubleValue = 12.3;

    private String stringValue = "1234";

    public static final int staticFinalIntValue = 12345;

    private Map mapValue = new HashMap();

    private InnerEntity innerEntity;

    public Entity(InnerEntity innerEntity) {
        this.innerEntity = innerEntity;
    }
}

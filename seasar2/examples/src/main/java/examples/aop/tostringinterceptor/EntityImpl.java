package examples.aop.tostringinterceptor;

public class EntityImpl extends Entity {
    private int implValue = 2;

    public EntityImpl(InnerEntity innerEntity) {
        super(innerEntity);
    }
}

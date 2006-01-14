package examples.aop.tostringinterceptor;

public class EntityImplImpl extends EntityImpl {
    private int implImplValue = 3;

    public EntityImplImpl(InnerEntity innerEntity) {
        super(innerEntity);
    }
}

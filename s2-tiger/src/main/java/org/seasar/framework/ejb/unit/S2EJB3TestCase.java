package org.seasar.framework.ejb.unit;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.ejb.unit.annotation.Rollback;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public abstract class S2EJB3TestCase extends S2TestCase {

    private EntityManager entityManager;

    public S2EJB3TestCase() {
    }

    public S2EJB3TestCase(String name) {
        super(name);
    }

    @Override
    protected boolean needTransaction() {
        Method m = ClassUtil.getMethod(getClass(), getName(), new Class[] {});
        return m.isAnnotationPresent(Rollback.class) || super.needTransaction();
    }

    @Override
    public DataSet reload(DataSet dataSet) {
        entityManager.flush();
        return super.reload(dataSet);
    }
    
    @Override
    protected void setUpAfterContainerInit() throws Throwable {
        super.setUpAfterContainerInit();
        setupEntityManager();
    }

    @Override
    protected void tearDownBeforeContainerDestroy() throws Throwable {
        tearDownEntityManager();
        super.tearDownBeforeContainerDestroy();
    }

    protected void setupEntityManager() {
        S2Container container = getContainer();
        try {
            if (container.hasComponentDef(EntityManager.class)) {
                entityManager = (EntityManager) container
                        .getComponent(EntityManager.class);
            }
        } catch (Throwable t) {
            System.err.println(t);
        }
    }

    protected void tearDownEntityManager() {
        entityManager = null;
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new EmptyRuntimeException("entityManager");
        }
        return entityManager;
    }

    protected void assertEntityEquals(String message, DataSet expected,
            Object entity) {

        EntityReader reader = new EntityReader(entity);
        assertEntityEquals(message, expected, reader.read());
    }

    protected void assertEntityListEquals(String message, DataSet expected,
            List<?> list) {

        EntityListReader reader = new EntityListReader(list);
        assertEntityEquals(message, expected, reader.read());
    }

    protected void assertEntityEquals(String message, DataSet expected,
            DataSet actual) {

        message = message == null ? "" : message;
        for (int i = 0; i < expected.getTableSize(); ++i) {
            String tableName = expected.getTable(i).getTableName();
            String notFound = message + ":Table " + tableName
                    + " was not found.";
            assertTrue(notFound, actual.hasTable(tableName));
            assertEquals(message, expected.getTable(i), actual
                    .getTable(tableName));
        }
    }

}

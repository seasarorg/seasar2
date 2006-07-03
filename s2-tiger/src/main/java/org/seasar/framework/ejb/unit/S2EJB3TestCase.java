/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.ejb.unit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.j2ee.JndiContextFactory;
import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.TigerAnnotationHandler;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.unit.annotation.Rollback;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TransactionManagerUtil;

/**
 * @author taedium
 * 
 */
public abstract class S2EJB3TestCase extends S2TestCase {

    private static final String EJB3TX_DICON = "ejb3tx.dicon";

    private static final String S2HIBERNATE_JPA_DICON = "s2hibernate-jpa.dicon";

    private TigerAnnotationHandler handler = new TigerAnnotationHandler();

    private ProxiedObjectResolver resolver;

    private EntityManager entityManager;

    public S2EJB3TestCase() {
    }

    public S2EJB3TestCase(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        includeDicons();
    }

    protected void includeDicons() {
        if (ResourceUtil.isExist(EJB3TX_DICON)) {
            include(EJB3TX_DICON);
        }
        if (ResourceUtil.isExist(S2HIBERNATE_JPA_DICON)) {
            include(S2HIBERNATE_JPA_DICON);
        }
    }

    @Override
    public void register(Class componentClass) {
        ComponentDef cd = handler.createComponentDef(componentClass, null);
        if (cd.getComponentName() == null) {
            cd.setComponentName(getNamingConvention()
                    .fromClassNameToComponentName(componentClass.getName()));
        }
        handler.appendDI(cd);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        handler.appendInitMethod(cd);
        register(cd);
    }

    @Override
    public void deleteDb(DataSet dataSet) {
        flush();
        super.deleteDb(dataSet);
    }

    @Override
    public void deleteTable(String tableName) {
        flush();
        super.deleteTable(tableName);
    }

    @Override
    public DataSet readDb(DataSet dataSet) {
        flush();
        return super.readDb(dataSet);
    }

    @Override
    public DataTable readDbByTable(String table) {
        flush();
        return super.readDbByTable(table);
    }

    @Override
    public DataTable readDbByTable(String table, String condition) {
        flush();
        return super.readDbByTable(table, condition);
    }

    @Override
    public DataTable readDbBySql(String sql, String tableName) {
        flush();
        return super.readDbBySql(sql, tableName);
    }

    @Override
    public void writeDb(DataSet dataSet) {
        flush();
        super.writeDb(dataSet);
    }

    @Override
    public DataSet reload(DataSet dataSet) {
        flush();
        return super.reload(dataSet);
    }

    @Override
    public DataTable reload(DataTable table) {
        flush();
        return super.reload(table);
    }

    @Override
    protected boolean needTransaction() {
        Method method = ClassUtil.getMethod(getClass(), getName(), null);
        return super.needTransaction()
                || method.isAnnotationPresent(Rollback.class);
    }

    @Override
    protected void bindField(Field field) {
        EJB ejb = field.getAnnotation(EJB.class);
        if (ejb == null) {
            super.bindField(field);
            return;
        }
        if (isAutoBindable(field)) {
            field.setAccessible(true);
            Object component = null;
            String name = null;
            if (!StringUtil.isEmpty(ejb.beanName())) {
                name = ejb.beanName();
            } else if (!StringUtil.isEmpty(ejb.name())) {
                name = ejb.name();
            }
            Hashtable env = getEnv();
            if (name != null) {
                try {
                    component = JndiResourceLocator.lookup(name, env);
                } catch (Throwable ignore) {
                }
            } else {
                try {
                    component = JndiResourceLocator
                            .lookup(field.getName(), env);
                } catch (Throwable ignore) {
                }
                if (component == null
                        && getContainer().hasComponentDef(field.getType())) {
                    component = getComponent(field.getType());
                }
            }
            if (component != null) {
                FieldUtil.set(field, this, component);
            }
        }
    }

    protected Hashtable getEnv() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JndiContextFactory.class
                .getName());
        return env;
    }

    protected void flush() {
        if (entityManager != null && isTransactionActive()) {
            entityManager.flush();
        }
    }

    protected boolean isTransactionActive() {
        TransactionManager tm = (TransactionManager) getComponent(TransactionManager.class);
        return tm != null && TransactionManagerUtil.isActive(tm);
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
        assertEntityEquals(message, expected, entity, false);
    }

    protected void assertEntityEquals(String message, DataSet expected,
            Object entity, boolean includesRelationships) {

        assertNotNull("entity is null.", entity);
        EntityReader reader = createEntityReader(entity, includesRelationships);
        assertEqualsIgnoreOrder(message, expected, reader.read());
    }

    protected EntityReader createEntityReader(Object entity,
            boolean includesRelationships) {
        if (resolver == null) {
            return new EntityReader(entity, includesRelationships);
        } else {
            return new EntityReader(entity, includesRelationships, resolver);
        }
    }

    protected void assertEntityListEquals(String message, DataSet expected,
            List<?> list) {
        assertEntityListEquals(message, expected, list, false);
    }

    protected void assertEntityListEquals(String message, DataSet expected,
            List<?> list, boolean includesRelationships) {

        assertNotNull("entity list is null.", list);
        EntityListReader reader = createEntityListReader(list,
                includesRelationships);
        assertEqualsIgnoreOrder(message, expected, reader.read());
    }

    protected EntityListReader createEntityListReader(List<?> list,
            boolean includesRelationships) {
        if (resolver == null) {
            return new EntityListReader(list, includesRelationships);
        } else {
            return new EntityListReader(list, includesRelationships, resolver);
        }
    }

    protected void assertEqualsIgnoreOrder(String message, DataSet expected,
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

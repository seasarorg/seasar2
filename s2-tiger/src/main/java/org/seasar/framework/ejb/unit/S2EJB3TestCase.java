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
import java.lang.reflect.Modifier;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.TigerAnnotationHandler;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.ejb.unit.annotation.Rollback;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
public abstract class S2EJB3TestCase extends S2TestCase {

    private static final String EJB3TX_DICON = "ejb3tx.dicon";

    private static final String S2HIBERNATE_JPA_DICON = "s2hibernate-jpa.dicon";

    private TigerAnnotationHandler handler = new TigerAnnotationHandler();

    private EntityManager entityManager;

    public S2EJB3TestCase() {
    }

    public S2EJB3TestCase(String name) {
        super(name);
    }

//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        if (ResourceUtil.isExist(EJB3TX_DICON)) {
//            include(EJB3TX_DICON);
//        }
//        if (ResourceUtil.isExist(S2HIBERNATE_JPA_DICON)) {
//            include(S2HIBERNATE_JPA_DICON);
//        }
//    }

    @Override
    public void register(Class componentClass) {
        ComponentDef cd = handler.createComponentDef(componentClass, null);
        handler.appendDI(cd);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        handler.appendInitMethod(cd);
        injectEntityManagerTemp(cd);
        register(cd);
    }

    protected void injectEntityManagerTemp(ComponentDef cd) {
        // move to TigerAnnotationHandler
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(cd.getComponentClass());
        for (int i = 0; i < beanDesc.getFieldSize(); ++i) {
            Field field = beanDesc.getField(i);
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (field.isAnnotationPresent(PersistenceContext.class)) {
                cd.addPropertyDef(new PropertyDefImpl(field.getName()));
            }
        }
    }

    @Override
    protected boolean needTransaction() {
        Method m = ClassUtil.getMethod(getClass(), getName(), new Class[] {});
        return m.isAnnotationPresent(Rollback.class) || super.needTransaction();
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

    private void flush() {
        if (entityManager != null) {
            entityManager.flush();
        }
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

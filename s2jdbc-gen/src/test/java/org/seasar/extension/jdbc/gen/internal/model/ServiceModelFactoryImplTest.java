/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ServiceModelFactoryImplTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private NamesModelFactoryImpl namesModelFactory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        PropertyMetaFactoryImpl propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tmf);
        namesModelFactory = new NamesModelFactoryImpl("aaa.ccc", "Names");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSingleId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceModelFactoryImpl serviceModelFactory = new ServiceModelFactoryImpl(
                "aaa.bbb", "Service", namesModelFactory, true, "jdbcManager");
        ServiceModel serviceModel = serviceModelFactory
                .getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryImplTest$AaaService", serviceModel
                .getShortClassName());
        assertEquals("Aaa", serviceModel.getShortEntityClassName());
        assertEquals(1, serviceModel.getIdPropertyMetaList().size());
        assertEquals("jdbcManager", serviceModel.getJdbcManagerName());
        assertFalse(serviceModel.isJdbcManagerSetterNecessary());

        assertEquals(3, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Aaa.class.getCanonicalName(), iterator.next());

        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryImplTest$AaaNames.*", iterator
                .next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator
                .next());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCompositeId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bbb.class);
        ServiceModelFactoryImpl serviceModelFactory = new ServiceModelFactoryImpl(
                "aaa.bbb", "Service", namesModelFactory, true, "jdbcManager");
        ServiceModel serviceModel = serviceModelFactory
                .getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryImplTest$BbbService", serviceModel
                .getShortClassName());
        assertEquals(2, serviceModel.getIdPropertyMetaList().size());
        assertEquals("jdbcManager", serviceModel.getJdbcManagerName());
        assertFalse(serviceModel.isJdbcManagerSetterNecessary());

        assertEquals(4, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Bbb.class.getCanonicalName(), iterator.next());

        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryImplTest$BbbNames.*", iterator
                .next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator
                .next());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testJdbcManagerName() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bbb.class);
        ServiceModelFactoryImpl serviceModelFactory = new ServiceModelFactoryImpl(
                "aaa.bbb", "Service", namesModelFactory, true, "myJdbcManager");
        ServiceModel serviceModel = serviceModelFactory
                .getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryImplTest$BbbService", serviceModel
                .getShortClassName());
        assertEquals(2, serviceModel.getIdPropertyMetaList().size());
        assertEquals("myJdbcManager", serviceModel.getJdbcManagerName());
        assertTrue(serviceModel.isJdbcManagerSetterNecessary());

        assertEquals(8, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Resource.class.getCanonicalName(), iterator.next());
        assertEquals(TransactionAttribute.class.getCanonicalName(), iterator
                .next());
        assertEquals(TransactionAttributeType.class.getCanonicalName(),
                iterator.next());
        assertEquals(JdbcManager.class.getCanonicalName(), iterator.next());
        assertEquals(Bbb.class.getCanonicalName(), iterator.next());

        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryImplTest$BbbNames.*", iterator
                .next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator
                .next());
    }

    /** */
    @Entity
    public static class Aaa {

        /** */
        @Id
        protected Integer id;

        /** */
        protected String name;
    }

    /** */
    @Entity
    public static class Bbb {

        /** */
        @Id
        @Column(nullable = false)
        protected Integer id1;

        /** */
        @Id
        @Temporal(TemporalType.DATE)
        @Column(nullable = false)
        protected Date id2;

        /** */
        protected String name;
    }
}

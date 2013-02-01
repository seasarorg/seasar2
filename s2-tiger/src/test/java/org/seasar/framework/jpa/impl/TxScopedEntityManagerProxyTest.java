/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.unit.annotation.EasyMock;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class TxScopedEntityManagerProxyTest extends S2TigerTestCase {

    UserTransaction ut;

    TransactionManager tm;

    TransactionSynchronizationRegistry tsr;

    @EasyMock
    EntityManagerFactory emf1;

    @EasyMock
    EntityManagerFactory emf2;

    @EasyMock
    EntityManager em1;

    @EasyMock
    EntityManager em2;

    @EasyMock
    EntityManager em3;

    TxScopedEntityManagerProxy target1 = new TxScopedEntityManagerProxy();

    TxScopedEntityManagerProxy target2 = new TxScopedEntityManagerProxy();

    TxScopedEntityManagerProxy target3 = new TxScopedEntityManagerProxy();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jta.dicon");
    }

    /**
     */
    protected void setUpTarget() {
        target1.tsr = tsr;
        target1.emf = emf1;

        target2.tsr = tsr;
        target2.emf = emf1;

        target3.tsr = tsr;
        target3.emf = emf2;
    }

    /**
     */
    public void testGetEntityManager() {
        setUpTarget();

        EntityManager em = target1.getEntityManager();
        assertSame(em1, em);

        em = target2.getEntityManager();
        assertSame(em2, em);

        em = target3.getEntityManager();
        assertSame(em3, em);
    }

    /**
     */
    public void recordGetEntityManager() {
        expect(emf1.createEntityManager()).andReturn(em1);
        expect(emf1.createEntityManager()).andReturn(em2);
        expect(emf2.createEntityManager()).andReturn(em3);
    }

    /**
     */
    public void testGetEntityManagerTx() {
        setUpTarget();

        EntityManager em = target1.getEntityManager();
        assertSame(em1, em);

        em = target2.getEntityManager();
        assertSame(em1, em);

        em = target3.getEntityManager();
        assertSame(em3, em);
    }

    /**
     */
    public void recordGetEntityManagerTx() {
        expect(emf1.createEntityManager()).andReturn(em1);
        expect(emf2.createEntityManager()).andReturn(em3);
        em1.close();
        em3.close();
    }

}

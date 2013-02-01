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

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.extension.datasource.impl.DataSourceFactoryImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.jpa.EntityManagerProvider;
import org.seasar.framework.unit.S2TigerTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 * 
 */
public class SelectableEntityManagerProxyTest extends S2TigerTestCase {

    DataSourceFactory dataSourceFactory;

    EntityManagerProvider entityManagerProvider;

    SelectableEntityManagerProxy proxy;

    EntityManager fooEntityManager;

    EntityManager barEntityManager;

    Query query;

    EntityTransaction entityTransaction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        register(DataSourceFactoryImpl.class);
        register(EntityManagerProviderImpl.class, "entityManagerProvider");
        register(SelectableEntityManagerProxy.class, "entityManager");
        register(createStrictMock(EntityManager.class), "fooEntityManager");
        register(createStrictMock(EntityManager.class), "barEntityManager");
        query = createStrictMock(Query.class);
        entityTransaction = createStrictMock(EntityTransaction.class);
    }

    /**
     * @throws Exception
     */
    public void testAll() throws Exception {
        dataSourceFactory.setSelectableDataSourceName("foo");
        new Subsequence() {

            @Override
            public void replay() throws Exception {
                proxy.clear();
                proxy.close();
                assertTrue(proxy.contains(new Foo(1)));
                assertSame(query, proxy.createNamedQuery("findFoo"));
                assertSame(query, proxy
                        .createNativeQuery("select foo.id from FOO foo"));
                assertSame(query, proxy.createNativeQuery(
                        "select foo.id from FOO foo", Foo.class));
                assertSame(query, proxy.createNativeQuery(
                        "select foo.id from FOO foo", "mapping"));
                assertSame(query, proxy.createQuery("select foo from Foo foo"));
                assertEquals(new Foo(2), proxy.find(Foo.class, 2));
                proxy.flush();
                assertSame(fooEntityManager, proxy.getDelegate());
                assertEquals(FlushModeType.AUTO, proxy.getFlushMode());
                assertEquals(new Foo(3), proxy.getReference(Foo.class, 3));
                assertSame(entityTransaction, proxy.getTransaction());
                assertTrue(proxy.isOpen());
                proxy.joinTransaction();
                proxy.lock(new Foo(4), LockModeType.READ);
                assertEquals(new Foo(5), proxy.merge(new Foo(5)));
                proxy.persist(new Foo(6));
                proxy.refresh(new Foo(7));
                proxy.remove(new Foo(7));
                proxy.setFlushMode(FlushModeType.COMMIT);
            }

            @Override
            public void record() throws Exception {
                fooEntityManager.clear();
                fooEntityManager.close();
                expect(fooEntityManager.contains(new Foo(1))).andReturn(true);
                expect(fooEntityManager.createNamedQuery("findFoo")).andReturn(
                        query);
                expect(
                        fooEntityManager
                                .createNativeQuery("select foo.id from FOO foo"))
                        .andReturn(query);
                expect(
                        fooEntityManager.createNativeQuery(
                                "select foo.id from FOO foo", Foo.class))
                        .andReturn(query);
                expect(
                        fooEntityManager.createNativeQuery(
                                "select foo.id from FOO foo", "mapping"))
                        .andReturn(query);
                expect(fooEntityManager.createQuery("select foo from Foo foo"))
                        .andReturn(query);
                expect(fooEntityManager.find(Foo.class, 2)).andReturn(
                        new Foo(2));
                fooEntityManager.flush();
                expect(fooEntityManager.getDelegate()).andReturn(
                        fooEntityManager);
                expect(fooEntityManager.getFlushMode()).andReturn(
                        FlushModeType.AUTO);
                expect(fooEntityManager.getReference(Foo.class, 3)).andReturn(
                        new Foo(3));
                expect(fooEntityManager.getTransaction()).andReturn(
                        entityTransaction);
                expect(fooEntityManager.isOpen()).andReturn(true);
                fooEntityManager.joinTransaction();
                fooEntityManager.lock(new Foo(4), LockModeType.READ);
                expect(fooEntityManager.merge(new Foo(5)))
                        .andReturn(new Foo(5));
                fooEntityManager.persist(new Foo(6));
                fooEntityManager.refresh(new Foo(7));
                fooEntityManager.remove(new Foo(7));
                fooEntityManager.setFlushMode(FlushModeType.COMMIT);
            }
        }.doTest();

        dataSourceFactory.setSelectableDataSourceName("bar");
        new Subsequence() {

            @Override
            public void replay() throws Exception {
                proxy.clear();
                proxy.close();
                assertFalse(proxy.contains(new Foo(11)));
                assertSame(query, proxy.createNamedQuery("findFoo"));
                assertSame(query, proxy
                        .createNativeQuery("select foo.id from FOO foo"));
                assertSame(query, proxy.createNativeQuery(
                        "select foo.id from FOO foo", Foo.class));
                assertSame(query, proxy.createNativeQuery(
                        "select foo.id from FOO foo", "mapping"));
                assertSame(query, proxy.createQuery("select foo from Foo foo"));
                assertEquals(new Foo(12), proxy.find(Foo.class, 12));
                proxy.flush();
                assertSame(barEntityManager, proxy.getDelegate());
                assertEquals(FlushModeType.COMMIT, proxy.getFlushMode());
                assertEquals(new Foo(13), proxy.getReference(Foo.class, 13));
                assertSame(entityTransaction, proxy.getTransaction());
                assertFalse(proxy.isOpen());
                proxy.joinTransaction();
                proxy.lock(new Foo(14), LockModeType.WRITE);
                assertEquals(new Foo(15), proxy.merge(new Foo(15)));
                proxy.persist(new Foo(16));
                proxy.refresh(new Foo(17));
                proxy.remove(new Foo(17));
                proxy.setFlushMode(FlushModeType.AUTO);
            }

            @Override
            public void record() throws Exception {
                barEntityManager.clear();
                barEntityManager.close();
                expect(barEntityManager.contains(new Foo(11))).andReturn(false);
                expect(barEntityManager.createNamedQuery("findFoo")).andReturn(
                        query);
                expect(
                        barEntityManager
                                .createNativeQuery("select foo.id from FOO foo"))
                        .andReturn(query);
                expect(
                        barEntityManager.createNativeQuery(
                                "select foo.id from FOO foo", Foo.class))
                        .andReturn(query);
                expect(
                        barEntityManager.createNativeQuery(
                                "select foo.id from FOO foo", "mapping"))
                        .andReturn(query);
                expect(barEntityManager.createQuery("select foo from Foo foo"))
                        .andReturn(query);
                expect(barEntityManager.find(Foo.class, 12)).andReturn(
                        new Foo(12));
                barEntityManager.flush();
                expect(barEntityManager.getDelegate()).andReturn(
                        barEntityManager);
                expect(barEntityManager.getFlushMode()).andReturn(
                        FlushModeType.COMMIT);
                expect(barEntityManager.getReference(Foo.class, 13)).andReturn(
                        new Foo(13));
                expect(barEntityManager.getTransaction()).andReturn(
                        entityTransaction);
                expect(barEntityManager.isOpen()).andReturn(false);
                barEntityManager.joinTransaction();
                barEntityManager.lock(new Foo(14), LockModeType.WRITE);
                expect(barEntityManager.merge(new Foo(15))).andReturn(
                        new Foo(15));
                barEntityManager.persist(new Foo(16));
                barEntityManager.refresh(new Foo(17));
                barEntityManager.remove(new Foo(17));
                barEntityManager.setFlushMode(FlushModeType.AUTO);
            }
        }.doTest();
    }

    /**
     * @throws Exception
     */
    public void testNotSelected() throws Exception {
        try {
            proxy.close();
            fail();
        } catch (EmptyRuntimeException expected) {
        }
    }

    /**
     * @author taedium
     */
    @Entity
    public static class Foo {

        @Id
        private int id;

        /**
         * 
         */
        public Foo() {
        }

        /**
         * 
         * @param id
         */
        public Foo(int id) {
            this.id = id;
        }

        /**
         * 
         * @return
         */
        public int getId() {
            return id;
        }

        /**
         * 
         * @param id
         */
        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (!Foo.class.isInstance(obj)) {
                return false;
            }
            Foo rhs = Foo.class.cast(obj);
            return id == rhs.id;
        }
    }
}

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
package org.seasar.framework.jpa;

import static org.easymock.EasyMock.expect;

import javax.persistence.EntityManager;

import org.seasar.framework.unit.EasyMockTestCase;

/**
 * @author koichik
 * 
 */
public class EntityDescFactoryTest extends EasyMockTestCase {

    private EntityManager em1;

    private EntityManager em2;

    private EntityDesc entityDesc1;

    private EntityDesc entityDesc2;

    private EntityDescProvider provider1;

    private EntityDescProvider provider2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        em1 = createStrictMock(EntityManager.class);
        em2 = createStrictMock(EntityManager.class);
        entityDesc1 = createStrictMock(EntityDesc.class);
        entityDesc2 = createStrictMock(EntityDesc.class);
        provider1 = createStrictMock(EntityDescProvider.class);
        provider2 = createStrictMock(EntityDescProvider.class);

        EntityDescFactory.addProvider(Foo.class, provider1);
        EntityDescFactory.addProvider(Bar.class, provider2);
    }

    public void testSingleProvider() throws Exception {
        new EasyMockTestCase.Subsequence() {
            @Override
            protected void replay() throws Exception {
                EntityDesc fooDesc = EntityDescFactory.getEntityDesc(em1,
                        Foo.class);
                assertSame(entityDesc1, fooDesc);

                EntityDesc fooDesc2 = EntityDescFactory.getEntityDesc(em1,
                        Foo.class);
                assertSame(entityDesc1, fooDesc2);

                EntityDesc barDesc = EntityDescFactory.getEntityDesc(em1,
                        Bar.class);
                assertSame(entityDesc2, barDesc);
            }

            @Override
            protected void record() throws Exception {
                Object key1 = new Object();
                expect(em1.getDelegate()).andReturn(new Foo());
                expect(provider1.getContextKey(em1)).andReturn(key1);
                expect(provider1.createEntityDesc(Foo.class, key1)).andReturn(
                        entityDesc1);

                expect(em1.getDelegate()).andReturn(new Foo());
                expect(provider1.getContextKey(em1)).andReturn(key1);

                expect(em1.getDelegate()).andReturn(new Foo());
                expect(provider1.getContextKey(em1)).andReturn(key1);
                expect(provider1.createEntityDesc(Bar.class, key1)).andReturn(
                        entityDesc2);
            }
        }.doTest();
    }

    public void testMultipleProvider() throws Exception {
        new EasyMockTestCase.Subsequence() {
            @Override
            protected void replay() throws Exception {
                EntityDesc fooDesc = EntityDescFactory.getEntityDesc(em1,
                        Foo.class);
                assertSame(entityDesc1, fooDesc);
                EntityDesc fooDesc2 = EntityDescFactory.getEntityDesc(em2,
                        Foo.class);
                assertSame(entityDesc2, fooDesc2);
            }

            @Override
            protected void record() throws Exception {
                Object key1 = new Object();
                Object key2 = new Object();
                expect(em1.getDelegate()).andReturn(new Foo());
                expect(provider1.getContextKey(em1)).andReturn(key1);
                expect(provider1.createEntityDesc(Foo.class, key1)).andReturn(
                        entityDesc1);

                expect(em2.getDelegate()).andReturn(new Bar());
                expect(provider2.getContextKey(em2)).andReturn(key2);
                expect(provider2.createEntityDesc(Foo.class, key2)).andReturn(
                        entityDesc2);
            }
        }.doTest();
    }

    public static class Foo {
    }

    public static class Bar {
    }
}

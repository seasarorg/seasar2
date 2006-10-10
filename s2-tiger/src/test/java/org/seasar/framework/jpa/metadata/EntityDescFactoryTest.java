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
package org.seasar.framework.jpa.metadata;

import static org.easymock.EasyMock.expect;

import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.unit.EasyMockTestCase;

/**
 * @author koichik
 * 
 */
public class EntityDescFactoryTest extends EasyMockTestCase {

    private EntityDesc entityDesc1;

    private EntityDesc entityDesc2;

    private EntityDescProvider provider1;

    private EntityDescProvider provider2;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        entityDesc1 = createStrictMock(EntityDesc.class);
        entityDesc2 = createStrictMock(EntityDesc.class);
        provider1 = createStrictMock(EntityDescProvider.class);
        provider2 = createStrictMock(EntityDescProvider.class);

        EntityDescFactory.addProvider(provider1);
        EntityDescFactory.addProvider(provider2);
    }

    public void testGetProvider() throws Exception {
        new EasyMockTestCase.Subsequence() {
            @Override
            protected void replay() throws Exception {
                EntityDesc fooDesc = EntityDescFactory.getEntityDesc(Foo.class);
                assertSame(entityDesc1, fooDesc);

                EntityDesc fooDesc2 = EntityDescFactory
                        .getEntityDesc(Foo.class);
                assertSame(entityDesc1, fooDesc2);

                EntityDesc barDesc = EntityDescFactory.getEntityDesc(Bar.class);
                assertSame(entityDesc2, barDesc);
            }

            @Override
            protected void record() throws Exception {
                expect(provider1.createEntityDesc(Foo.class)).andReturn(
                        entityDesc1);

                expect(provider1.createEntityDesc(Bar.class)).andReturn(null);

                expect(provider2.createEntityDesc(Bar.class)).andReturn(
                        entityDesc2);
            }
        }.doTest();
    }

    public static class Foo {
    }

    public static class Bar {
    }
}

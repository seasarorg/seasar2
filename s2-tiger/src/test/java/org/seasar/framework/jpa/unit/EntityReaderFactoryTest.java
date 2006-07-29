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
package org.seasar.framework.jpa.unit;

import static org.easymock.EasyMock.expect;

import org.seasar.framework.unit.EasyMockTestCase;

/**
 * @author taedium
 * 
 */
public class EntityReaderFactoryTest extends EasyMockTestCase {

    private EntityReader reader1;

    private EntityReader reader2;

    private EntityReaderProvider provider1;

    private EntityReaderProvider provider2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        reader1 = createStrictMock(EntityReader.class);
        reader2 = createStrictMock(EntityReader.class);
        provider1 = createStrictMock(EntityReaderProvider.class);
        provider2 = createStrictMock(EntityReaderProvider.class);

        EntityReaderFactory.addProvider(provider1);
        EntityReaderFactory.addProvider(provider2);
    }

    public void testGetProvider() throws Exception {
        final Foo foo = new Foo();
        final Bar bar = new Bar();

        new EasyMockTestCase.Subsequence() {
            @Override
            protected void replay() throws Exception {
                EntityReader fooReader = EntityReaderFactory
                        .getEntityReader(foo);
                assertSame(reader1, fooReader);

                EntityReader barReader = EntityReaderFactory
                        .getEntityReader(bar);
                assertSame(reader2, barReader);
            }

            @Override
            protected void record() throws Exception {
                expect(provider1.createEntityReader(foo)).andReturn(reader1);

                expect(provider1.createEntityReader(bar)).andReturn(null);

                expect(provider2.createEntityReader(bar)).andReturn(reader2);
            }
        }.doTest();
    }

    public static class Foo {
    }

    public static class Bar {
    }
}

/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.tx.adapter;

import java.util.Map;

import javax.transaction.Synchronization;
import javax.transaction.UserTransaction;

import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.ExtendedJTATransaction;
import org.seasar.extension.tx.adapter.WAS5TransactionManagerAdapter.SynchronizationCallback;
import org.seasar.framework.unit.EasyMockTestCase;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 */
public class WAS5TransactionSynchronizationRegistryTest extends
        EasyMockTestCase {

    static final byte[] GLOBAL_ID = new byte[0];

    WAS5TransactionSynchronizationRegistry target;

    UserTransaction ut;

    ExtendedJTATransaction extendedTransaction;

    Synchronization sync;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ut = createStrictMock(UserTransaction.class);
        extendedTransaction = createStrictMock(ExtendedJTATransaction.class);
        sync = createStrictMock(Synchronization.class);
        target = new WAS5TransactionSynchronizationRegistry(ut,
                extendedTransaction);
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testResource() throws Exception {
        Map resourcesMap = Map.class
                .cast(WAS5TransactionSynchronizationRegistry.resourcesMaps
                        .get());
        assertFalse(resourcesMap.containsKey(GLOBAL_ID));

        target.putResource("foo", "FOO");
        assertTrue(resourcesMap.containsKey(GLOBAL_ID));
        Map resources = Map.class.cast(resourcesMap.get(GLOBAL_ID));
        assertEquals(1, resources.size());

        target.putResource("bar", "BAR");
        assertEquals(2, resources.size());

        assertEquals("FOO", target.getResource("foo"));
        assertEquals("BAR", target.getResource("bar"));

        target.afterCompletion(0, GLOBAL_ID, true);
        assertFalse(resourcesMap.containsKey(GLOBAL_ID));
    }

    /**
     * @throws Exception
     */
    public void recordResource() throws Exception {
        expect(extendedTransaction.getGlobalId()).andReturn(GLOBAL_ID);
        extendedTransaction
                .registerSynchronizationCallbackForCurrentTran(isA(SynchronizationCallback.class));
        expect(extendedTransaction.getGlobalId()).andReturn(GLOBAL_ID);
        expect(extendedTransaction.getGlobalId()).andReturn(GLOBAL_ID);
        expect(extendedTransaction.getGlobalId()).andReturn(GLOBAL_ID);
    }

}

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
package org.seasar.extension.timer;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class TimeoutManagerTest extends TestCase {

    private int expiredCount;

    protected void setUp() throws Exception {
        expiredCount = 0;
        TimeoutManager.getInstance().clear();
    }

    protected void tearDown() throws Exception {
        TimeoutManager.getInstance().clear();
    }

    /**
     * @throws Exception
     */
    public void testExpired() throws Exception {
        TimeoutTask task = TimeoutManager.getInstance().addTimeoutTarget(
                new TimeoutTarget() {
                    public void expired() {
                        System.out.println("expired");
                        expiredCount++;
                    }
                }, 1, true);

        assertNotNull(TimeoutManager.getInstance().thread);
        Thread.sleep(2000);
        assertTrue(expiredCount > 0);
        assertEquals(1, TimeoutManager.getInstance().getTimeoutTaskCount());
        TimeoutManager.getInstance().stop();
        assertNull(TimeoutManager.getInstance().thread);
        Thread.sleep(10);
        int count = expiredCount;
        task.stop();
        TimeoutManager.getInstance().start();
        assertNotNull(TimeoutManager.getInstance().thread);
        Thread.sleep(2000);
        assertEquals(count, expiredCount);
        assertEquals(1, TimeoutManager.getInstance().getTimeoutTaskCount());
        task.cancel();
        Thread.sleep(2000);
        assertEquals(0, TimeoutManager.getInstance().getTimeoutTaskCount());
        assertNull(TimeoutManager.getInstance().thread);
    }

}

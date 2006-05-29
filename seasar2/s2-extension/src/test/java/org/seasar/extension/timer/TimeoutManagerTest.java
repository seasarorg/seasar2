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
package org.seasar.extension.timer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimeoutManagerTest extends TestCase {

    private int _expiredCount;

    public TimeoutManagerTest(String name) {
        super(name);
    }

    public void testExpired() throws Exception {
        TimeoutTask task = TimeoutManager.getInstance().addTimeoutTarget(
                new TimeoutTarget() {
                    public void expired() {
                        System.out.println("expired");
                        _expiredCount++;
                    }
                }, 1, true);

        Thread.sleep(7000);
        assertTrue("1", _expiredCount > 1);
        assertEquals("2", 1, TimeoutManager.getInstance().getTimeoutTaskCount());
        TimeoutManager.getInstance().stop();
        int count = _expiredCount;
        task.stop();
        TimeoutManager.getInstance().start();
        Thread.sleep(3000);
        assertEquals("3", count, _expiredCount);
        assertEquals("4", 1, TimeoutManager.getInstance().getTimeoutTaskCount());
        task.cancel();
        Thread.sleep(3000);
        assertEquals("5", 0, TimeoutManager.getInstance().getTimeoutTaskCount());
    }

    protected void setUp() throws Exception {
        _expiredCount = 0;
        TimeoutManager.getInstance().clear();
    }

    protected void tearDown() throws Exception {
        TimeoutManager.getInstance().clear();
    }

    public static Test suite() {
        return new TestSuite(TimeoutManagerTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { TimeoutManagerTest.class
                .getName() });
    }
}

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
package org.seasar.framework.autodetector.impl;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector.Entry;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author taedium
 * 
 */
public class ResourceAutoDetectorImplTest extends S2FrameworkTestCase {

    private ResourceAutoDetector detector;

    public void setUpDetect() throws Exception {
        include("ResourceAutoDetectorImplTest.dicon");
    }

    public void testDetect() throws Exception {
        Entry[] entries = detector.detect();
        List pathes = new ArrayList();
        for (int i = 0; i < entries.length; i++) {
            pathes.add(entries[i].getPath());
        }

        assertEquals(2, pathes.size());
        assertTrue(pathes.contains("junit/framework/TestSuite.class"));
        assertTrue(pathes
                .contains("org/seasar/framework/autodetector/impl/foo.txt"));
    }
}

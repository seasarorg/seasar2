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
package org.seasar.framework.autodetector.tiger;

import java.util.Arrays;
import java.util.List;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author taedium
 * 
 */
public class AnnotatedClassAutoDetectorTest extends S2FrameworkTestCase {

    private AnnotatedClassAutoDetector detector;

    public void setUpDetect() throws Exception {
        include("AnnotatedClassAutoDetectorTest.dicon");
    }

    public void testDetect() throws Exception {
        List classes = Arrays.asList(detector.detect());
        assertEquals(2, classes.size());
        assertTrue(classes.contains(Foo2.class));
        assertTrue(classes.contains(Foo3.class));
    }
}

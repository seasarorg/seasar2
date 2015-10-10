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
package org.seasar.framework.container.autoregister;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class ClassPatternTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAppliedForShortClassNameNull() throws Exception {
        ClassPattern cp = new ClassPattern();
        assertTrue("1", cp.isAppliedShortClassName("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void testAppliedForNormalPattern() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setShortClassNames(".*Impl");
        assertTrue("1", cp.isAppliedShortClassName("HogeImpl"));
        assertFalse("2", cp.isAppliedShortClassName("Hoge"));
    }

    /**
     * @throws Exception
     */
    public void testAppliedForMulti() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setShortClassNames("Hoge, HogeImpl");
        assertTrue("1", cp.isAppliedShortClassName("HogeImpl"));
        assertTrue("2", cp.isAppliedShortClassName("Hoge"));
        assertFalse("3", cp.isAppliedShortClassName("Hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testAppliedPackageName() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setPackageName("org.seasar");
        assertTrue(cp.isAppliedPackageName("org.seasar"));
        assertTrue(cp.isAppliedPackageName("org.seasar.framework"));
        assertFalse(cp.isAppliedPackageName("org"));
        assertFalse(cp.isAppliedPackageName("org.seasar2"));
        assertFalse(cp.isAppliedPackageName(null));

        cp.setPackageName(null);
        assertTrue(cp.isAppliedPackageName(null));
        assertFalse(cp.isAppliedPackageName("org"));
    }
}
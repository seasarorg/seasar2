/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import org.seasar.framework.container.filter.S2ContainerFilter;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class InvalidateSessionInterceptorTest extends S2FrameworkTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include(InvalidateSessionInterceptorTest.class.getName().replace('.',
                '/')
                + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        Hoge hoge = (Hoge) getComponent("hoge");
        hoge.begin();
        assertNull(getRequest().getAttribute(
                S2ContainerFilter.INVALIDATE_SESSION));

        try {
            hoge.fail();
            fail();
        } catch (RuntimeException expected) {
        }
        assertNull(getRequest().getAttribute(
                S2ContainerFilter.INVALIDATE_SESSION));

        hoge.end();
        assertEquals(Boolean.TRUE, getRequest().getAttribute(
                S2ContainerFilter.INVALIDATE_SESSION));
    }

    /**
     */
    public static class Hoge {
        /** */
        public void begin() {
        }

        /** */
        public void end() {
        }

        /** */
        public void fail() {
            throw new RuntimeException();
        }
    }

}

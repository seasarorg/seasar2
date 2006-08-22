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
package org.seasar.framework.util;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class MethodUtilTigerTest extends TestCase {

    public void testIsBridgeMethod() throws Exception {
        Method[] methods = Bar.class.getDeclaredMethods();
        assertEquals(2, methods.length);
        int bridge = 0;
        for (Method method : methods) {
            if (MethodUtil.isBridgeMethod(method)) {
                ++bridge;
            }
        }
        assertEquals(1, bridge);
    }

    public class Foo {
        public Foo foo() {
            return null;
        }
    }

    public class Bar extends Foo {
        @Override
        public Bar foo() {
            return null;
        }
    }
}

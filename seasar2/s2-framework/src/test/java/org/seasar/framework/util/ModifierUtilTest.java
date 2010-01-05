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
package org.seasar.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author shot
 */
public class ModifierUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsPublic() throws Exception {
        Field f = Hoge.class.getDeclaredField("s");
        assertTrue(ModifierUtil.isPublicStaticFinalField(f));
        Method m = Hoge.class.getDeclaredMethod("hoge", new Class[] {});
        assertTrue(ModifierUtil.isPublic(m));
    }

    /**
     * @throws Exception
     */
    public void testIsInstanceField() throws Exception {
        Field f = Hoge.class.getDeclaredField("aaa");
        assertTrue(ModifierUtil.isInstanceField(f));
        f = Hoge.class.getDeclaredField("s");
        assertFalse(ModifierUtil.isInstanceField(f));
    }

    /**
     * @throws Exception
     */
    public void testIsTransient() throws Exception {
        Field f = Hoge.class.getDeclaredField("bbb");
        assertTrue(ModifierUtil.isTransient(f));
        f = Hoge.class.getDeclaredField("s");
        assertFalse(ModifierUtil.isTransient(f));
    }

    /**
     * @throws Exception
     */
    public void testIsAbstract() throws Exception {
        assertTrue(ModifierUtil.isAbstract(Map.class));
        assertFalse(ModifierUtil.isAbstract(HashMap.class));
    }

    /**
     * 
     */
    public static class Hoge {

        /**
         * 
         */
        public static final String s = null;

        /**
         * @return
         */
        public static String hoge() {
            return "aaa";
        }

        /**
         * 
         */
        public String aaa;

        /**
         * 
         */
        public transient String bbb;
    }
}

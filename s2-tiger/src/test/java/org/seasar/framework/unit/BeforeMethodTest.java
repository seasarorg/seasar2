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
package org.seasar.framework.unit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.annotation.BeforeMethod;

public class BeforeMethodTest extends TestCase {
 
    static boolean run;
    static String log;

    @Override
    public void setUp() {
        run = false;
        log = "";
    }

    public void testBeforeMethod() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge.class);
        assertEquals("1", "one-two-three-four", log);
    }

    public void testBeforeMethodNotExists() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge2.class);
        assertTrue("1", run);
    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge extends S2FrameworkTestCase {

        // dummy
        public void test() {
        }

        @BeforeClass
        public static void beforeClass() {
            log += "one";
        }
        
        @Before
        public void before() {
            log += "-two";
        }
        
        public void beforeMethod() throws Exception {
           log += "-three";
        }

        @Test
        @BeforeMethod("beforeMethod")
        public void aaa() throws Exception {
            log += "-four";
        }

    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge2 extends S2FrameworkTestCase {

        // dummy
        public void test() {
        }

        @Test
        @BeforeMethod("beforeMethod")
        public void aaa() throws Exception {
            run = true;
        }
    }

}

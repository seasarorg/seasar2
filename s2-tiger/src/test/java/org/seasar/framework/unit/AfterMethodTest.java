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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.annotation.AfterMethod;

public class AfterMethodTest extends TestCase {
 
    static boolean run;
    static String log;

    @Override
    public void setUp() {
        run = false;
        log = "";
    }

    public void testAfterMethod() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge.class);
        assertEquals("1", "one-two-three-four", log);
    }

    public void testAfterMethodNotExists() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge2.class);
        assertTrue("1", run);
    }

    public void testAfterMethodAfterFailure() {
        JUnitCore runner = new JUnitCore();
        runner.run(Hoge3.class);
        assertTrue("1", run);
    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge extends S2FrameworkTestCase {

        // dummy
        public void test() {
        }

        @Test
        @AfterMethod("afterMethod")
        public void aaa() throws Exception {
            log += "one";
        }

        public void afterMethod() throws Exception {
            log += "-two";
         }

        @After
        public void after() {
            log += "-three";
        }

        @AfterClass
        public static void afterClass() {
            log += "-four";
        }
    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge2 extends S2FrameworkTestCase {

        // dummy
        public void test() {
        }

        @Test
        @AfterMethod("afterMethod")
        public void aaa() throws Exception {
           run = true;
        }
    }
    
    @RunWith(S2TestClassRunner.class)
    public static class Hoge3 extends S2FrameworkTestCase {

        // dummy
        public void test() {
        }

        @Test
        @AfterMethod("afterMethod")
        public void aaa() throws Exception {
            throw new Exception();
        }
        
        public void afterMethod() throws Exception {
            run = true;
         }
    }
}
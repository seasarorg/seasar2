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
package org.seasar.framework.aop.javassist;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class TryBlockSupportTest extends TestCase {
    public TryBlockSupportTest() {
        super();
    }

    public TryBlockSupportTest(String name) {
        super(name);
    }

    public void testCatch() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        tryBlock.addCatchBlock(Exception.class, "e.printStackTrace();");
        tryBlock.addCatchBlock(Error.class, "");
        assertEquals("1", "try {" + "return null;" + "}"
                + "catch (java.lang.Exception e) {" + "e.printStackTrace();"
                + "}" + "catch (java.lang.Error e) {" + "}", tryBlock
                .getSourceCode());
    }

    public void testFinally() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        tryBlock.setFinallyBlock("System.out.println();");
        assertEquals("1", "try {" + "return null;" + "}" + "finally {"
                + "System.out.println();" + "}", tryBlock.getSourceCode());
    }

    public void testCatchFinally() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        tryBlock.addCatchBlock(Exception.class, "e.printStackTrace();");
        tryBlock.addCatchBlock(Error.class, "");
        tryBlock.setFinallyBlock("System.out.println();");
        assertEquals("1", "try {" + "return null;" + "}"
                + "catch (java.lang.Exception e) {" + "e.printStackTrace();"
                + "}" + "catch (java.lang.Error e) {" + "}" + "finally {"
                + "System.out.println();" + "}", tryBlock.getSourceCode());
    }

    public void testFail() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        try {
            tryBlock.getSourceCode();
            fail("1");
        } catch (IllegalStateException expected) {
        }

        tryBlock.setFinallyBlock("");

        try {
            tryBlock.addCatchBlock(Exception.class, "");
            fail("1");
        } catch (IllegalStateException expected) {
        }

        try {
            tryBlock.setFinallyBlock("");
            fail("1");
        } catch (IllegalStateException expected) {
        }
    }

    public void testNotThrowable() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        try {
            tryBlock.addCatchBlock(Object.class, "");
            fail("1");
        } catch (IllegalArgumentException expected) {
        }
    }
}
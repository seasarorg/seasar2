package org.seasar.framework.aop.javassist;

import junit.framework.TestCase;

import org.seasar.framework.aop.javassist.TryBlockSupport;

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
        assertEquals("1", "try {" + "return null;" + "}" + "catch (java.lang.Exception e) {"
                + "e.printStackTrace();" + "}" + "catch (java.lang.Error e) {" + "}", tryBlock
                .getSourceCode());
    }

    public void testFinally() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        tryBlock.setFinallyBlock("System.out.println();");
        assertEquals("1", "try {" + "return null;" + "}" + "finally {" + "System.out.println();"
                + "}", tryBlock.getSourceCode());
    }

    public void testCatchFinally() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        tryBlock.addCatchBlock(Exception.class, "e.printStackTrace();");
        tryBlock.addCatchBlock(Error.class, "");
        tryBlock.setFinallyBlock("System.out.println();");
        assertEquals("1", "try {" + "return null;" + "}" + "catch (java.lang.Exception e) {"
                + "e.printStackTrace();" + "}" + "catch (java.lang.Error e) {" + "}" + "finally {"
                + "System.out.println();" + "}", tryBlock.getSourceCode());
    }

    public void testFail() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        try {
            tryBlock.getSourceCode();
            fail("1");
        }
        catch (IllegalStateException expected) {
        }

        tryBlock.setFinallyBlock("");

        try {
            tryBlock.addCatchBlock(Exception.class, "");
            fail("1");
        }
        catch (IllegalStateException expected) {
        }

        try {
            tryBlock.setFinallyBlock("");
            fail("1");
        }
        catch (IllegalStateException expected) {
        }
    }
    
    public void testNotThrowable() throws Exception {
        TryBlockSupport tryBlock = new TryBlockSupport("return null;");
        try {
            tryBlock.addCatchBlock(Object.class, "");
            fail("1");
        }
        catch (IllegalArgumentException expected) {
        }
    }
}
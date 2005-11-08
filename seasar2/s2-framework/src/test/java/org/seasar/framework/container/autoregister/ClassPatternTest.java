package org.seasar.framework.container.autoregister;

import junit.framework.TestCase;

import org.seasar.framework.container.autoregister.ClassPattern;

/**
 * @author higa
 */
public class ClassPatternTest extends TestCase {

    public void testAppliedForShortClassNameNull() throws Exception {
        ClassPattern cp = new ClassPattern();
        assertTrue("1", cp.isAppliedShortClassName("Hoge"));
    }
    
    public void testAppliedForNormalPattern() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setShortClassNames(".*Impl");
        assertTrue("1", cp.isAppliedShortClassName("HogeImpl"));
        assertFalse("2", cp.isAppliedShortClassName("Hoge"));
    }
    
    public void testAppliedForMulti() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setShortClassNames("Hoge, HogeImpl");
        assertTrue("1", cp.isAppliedShortClassName("HogeImpl"));
        assertTrue("2", cp.isAppliedShortClassName("Hoge"));
        assertFalse("3", cp.isAppliedShortClassName("Hoge2"));
    }
    
    public void testAppliedPackageName() throws Exception {
        ClassPattern cp = new ClassPattern();
        cp.setPackageName("org.seasar");
        assertTrue("1", cp.isAppliedPackageName("org.seasar"));
        assertTrue("2", cp.isAppliedPackageName("org.seasar.framework"));
        assertFalse("3", cp.isAppliedPackageName("org"));
    }
}
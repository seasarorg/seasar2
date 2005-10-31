package org.seasar.framework.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.util.TextUtil;

public class TextUtilTest extends TestCase {

    public TextUtilTest(String name) {
        super(name);
    }

    public void testReadTextCr() throws Exception {
        assertEquals("1", "aaa\rbbb", TextUtil.readText(getPath("hoge_cr.txt")));
    }

    public void testReadTextLf() throws Exception {
        assertEquals("1", "aaa\nbbb", TextUtil.readText(getPath("hoge_lf.txt")));
    }

    public void testReadTextCrLf() throws Exception {
        assertEquals("1", "aaa\r\nbbb", TextUtil.readText(getPath("hoge_crlf.txt")));
    }

    protected String getPath(String fileName) {
        return getClass().getName().replace('.', '/').replaceFirst("TextUtilTest", fileName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        return new TestSuite(TextUtilTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] { TextUtilTest.class.getName() });
    }
}

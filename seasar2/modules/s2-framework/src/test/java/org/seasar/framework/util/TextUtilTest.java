package org.seasar.framework.util;

import junit.framework.TestCase;

public class TextUtilTest extends TestCase {

    public void testReadTextCr() throws Exception {
        assertEquals("1", "aaa\rbbb", TextUtil.readText(getPath("hoge_cr.txt")));
    }

    public void testReadTextLf() throws Exception {
        assertEquals("1", "aaa\nbbb", TextUtil.readText(getPath("hoge_lf.txt")));
    }

    public void testReadTextCrLf() throws Exception {
        assertEquals("1", "aaa\r\nbbb", TextUtil
                .readText(getPath("hoge_crlf.txt")));
    }

    protected String getPath(String fileName) {
        return getClass().getName().replace('.', '/').replaceFirst(
                "TextUtilTest", fileName);
    }
}
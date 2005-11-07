package org.seasar.framework.message;

import junit.framework.TestCase;

public class MessageFormatterTest extends TestCase {

    public void testGetMessage() throws Exception {
        String s = MessageFormatter.getMessage("ESSR0304", null);
        System.out.println(s);
        assertNotNull("1", s);
    }

    public void testGetMessageIllegalSystem() throws Exception {
        String s = MessageFormatter.getMessage("EXXX0304", null);
        System.out.println(s);
        assertNotNull("1", s);
    }

    public void testGetMessageIllegalMessageCode() throws Exception {
        String s = MessageFormatter.getMessage("ESSRxxxx", null);
        System.out.println(s);
        assertNotNull("1", s);
    }

    public void testGetMessageIllegalMessageCode2() throws Exception {
        String s = MessageFormatter.getMessage(null, null);
        System.out.println(s);
        assertNotNull("1", s);
    }

    public void testGetMessageIllegalArgs() throws Exception {
        String s = MessageFormatter.getMessage("ESSR0007", null);
        System.out.println(s);
        assertNotNull("1", s);
    }
}

package org.seasar.framework.message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.framework.message.MessageFormatter;

public class MessageFormatterTest extends TestCase {

    public MessageFormatterTest(String name) {
        super(name);
    }

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

    protected void setUp() throws Exception {
    }


    protected void tearDown() throws Exception {
    }


    public static Test suite() {
        return new TestSuite(MessageFormatterTest.class);
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[]{MessageFormatterTest.class.getName()});
    }
}

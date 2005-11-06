package org.seasar.framework.exception;

import org.seasar.framework.exception.SRuntimeException;

import junit.framework.*;

public class SRuntimeExceptionTest extends TestCase {

	public void testSeasarRuntimeException() throws Exception {
		SRuntimeException ex =
			new SRuntimeException("ESSR0001", new Object[] { "hoge" });
		assertEquals("1", "ESSR0001", ex.getMessageCode());
		assertEquals("2", 1, ex.getArgs().length);
		assertEquals("3", "hoge", ex.getArgs()[0]);
		System.out.println(ex.getMessage());
	}

	public void testGetCause() throws Exception {
		Throwable t = new NullPointerException("test");
		SRuntimeException ex =
			new SRuntimeException("ESSR0017", new Object[] { t }, t);
		assertEquals("1", t, ex.getCause());
		ex.printStackTrace();
	}
}
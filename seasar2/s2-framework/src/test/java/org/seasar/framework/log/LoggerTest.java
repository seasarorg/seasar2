package org.seasar.framework.log;

import junit.framework.TestCase;

public class LoggerTest extends TestCase {

    private Logger _logger = Logger.getLogger(getClass());

    public void testGetLogger() throws Exception {
        assertEquals("1", _logger, Logger.getLogger(getClass()));
    }

    public void testDebug() throws Exception {
        _logger.debug("debug");
    }

    public void testInfo() throws Exception {
        _logger.info("info");
    }

    public void testWarn() throws Exception {
        _logger.warn("warn");
    }

    public void testError() throws Exception {
        _logger.error("error");
    }

    public void testFatal() throws Exception {
        _logger.fatal("fatal");
    }

    public void testLog() throws Exception {
        _logger.log("ISSR0001", null);
    }

    public void testPerformance() throws Exception {
        final int num = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            System.out.println("test" + i);
        }
        long sysout = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            _logger.fatal("test" + i);
        }
        long logger = System.currentTimeMillis() - start;
        System.out.println("System.out:" + sysout);
        System.out.println("logger:" + logger);
    }
}

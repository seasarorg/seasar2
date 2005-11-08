package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

/**
 * 
 * @author taichi S. 
 */
public class NotSupportedInterceptorTest extends S2TestCase {
    
    private static final String PATH = "NotSupportedInterceptorTest.dicon";

    private TxBean txBean;
	private ExceptionBean exBean;
	private TransactionManager tm;
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(NotSupportedInterceptorTest.class);
    }

    public static Test suite() {
		return new TestSuite(NotSupportedInterceptorTest.class);
	}

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(PATH);
    }

    public NotSupportedInterceptorTest(String arg0) {
        super(arg0);
    }
    
    public void testInvoke() throws Exception {
        assertEquals("トランザクションが有効で無い事",false ,txBean.hasTransaction());
    }
    
    public void testInvokeTx() throws Exception {
        assertEquals("トランザクションが有効で無い事",false ,txBean.hasTransaction());
        assertEquals("既に開始されているトランザクションが有効である事", Status.STATUS_ACTIVE, tm.getStatus());
    }
    
    public void testInvokeExceptionTx() throws Exception {
        try {
            exBean.invoke();
            fail("例外がAspectで握り潰されている可能性がある。");
        } catch(Exception e) {
            System.out.println(e);
        }
        assertEquals("既に開始されているトランザクションが有効である事", Status.STATUS_ACTIVE, tm.getStatus());
    }

}

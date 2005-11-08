package org.seasar.extension.jta;

import junit.framework.*;
import javax.transaction.*;

import org.seasar.extension.jta.TransactionManagerImpl;

public class TransactionManagerImplTest extends TestCase {

	private TransactionManager tm_;
	
    public TransactionManagerImplTest(String name) {
        super(name);
    }

    public void testResume() throws Exception {
        tm_.begin();
        Transaction tx = tm_.suspend();
        tm_.begin();
        tm_.commit();
        tm_.resume(tx);
        tm_.commit();
    }

    public void testCommit() throws Exception {
        tm_.begin();
        tm_.commit();
        assertEquals("1", Status.STATUS_NO_TRANSACTION,
        	tm_.getStatus());
    }

    public void testRollback() throws Exception {
		tm_.begin();
		tm_.rollback();
		assertEquals("1", Status.STATUS_NO_TRANSACTION,
			tm_.getStatus());
    }

    protected void setUp() throws Exception {
		tm_ = new TransactionManagerImpl();
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite ( ) {
        return new TestSuite(TransactionManagerImplTest.class);
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.main(new String[]{TransactionManagerImplTest.class.getName()});
    }
}
package org.seasar.extension.j2ee;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.j2ee.JndiContextFactory;
import org.seasar.extension.unit.S2TestCase;

public class JndiContextFactoryTest extends S2TestCase {

	private Context ctx_;

	public void testLookup() throws Exception {
		assertNotNull("1", ctx_.lookup("jndi.transactionManager"));
		assertNotNull("2", ctx_.lookup("jndi.dataSource"));
	}

	protected void setUp() throws Exception {
        include("jndi.dicon");
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JndiContextFactory.class.getName());
		ctx_ = new InitialContext(env);
	}

	protected void tearDown() throws Exception {
		ctx_.close();
	}

	public static Test suite() {
		return new TestSuite(JndiContextFactoryTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { JndiContextFactoryTest.class.getName()});
	}
}
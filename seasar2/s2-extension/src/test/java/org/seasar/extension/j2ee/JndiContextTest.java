package org.seasar.extension.j2ee;

import java.util.Hashtable;

import javax.naming.Context;

import org.seasar.extension.j2ee.JndiContext;
import org.seasar.extension.unit.S2TestCase;

public class JndiContextTest extends S2TestCase {

	private Context ctx_;

	public void testLookup() throws Exception {
		assertNotNull("1", ctx_.lookup("jndi.transactionManager"));
		assertNotNull("2", ctx_.lookup("jndi.dataSource"));
	}

    public void testLookupENC() throws Exception {
        assertNotNull("1", ctx_.lookup("java:comp/env/transactionManager"));
        assertNotNull("2", ctx_.lookup("java:comp/env/jndi/dataSource"));
    }

	protected void setUp() throws Exception {
        include("jndi.dicon");
		Hashtable env = new Hashtable();
		ctx_ = new JndiContext(env);
	}

	protected void tearDown() throws Exception {
		ctx_.close();
	}
}
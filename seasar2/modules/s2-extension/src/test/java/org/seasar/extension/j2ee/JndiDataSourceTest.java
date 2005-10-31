package org.seasar.extension.j2ee;

import org.seasar.extension.unit.S2TestCase;

public class JndiDataSourceTest extends S2TestCase {

	public void testDataSource() throws Exception {
		assertNotNull("1", getDataSource());
		assertNotNull("2", getDataSource().getConnection());
	}

	public void setUp() {
		include("j2ee.dicon");
	}
}

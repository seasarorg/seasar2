package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class DialectDiconTest extends S2TestCase {

    @Override
    protected void setUp() {
        include("s2jdbc-internal.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetComponent() throws Exception {
        assertNotNull(getComponent("db2390Dialect"));
        assertNotNull(getComponent("db2400Dialect"));
        assertNotNull(getComponent("db2Dialect"));
        assertNotNull(getComponent("derbyDialect"));
        assertNotNull(getComponent("firebirdDialect"));
        assertNotNull(getComponent("h2Dialect"));
        assertNotNull(getComponent("hsqlDialect"));
        assertNotNull(getComponent("interbaseDialect"));
        assertNotNull(getComponent("maxdbDialect"));
        assertNotNull(getComponent("mssql2005Dialect"));
        assertNotNull(getComponent("mssqlDialect"));
        assertNotNull(getComponent("mysqlDialect"));
        assertNotNull(getComponent("oracleDialect"));
        assertNotNull(getComponent("postgreDialect"));
        assertNotNull(getComponent("standardDialect"));
        assertNotNull(getComponent("sybaseDialect"));
    }
}

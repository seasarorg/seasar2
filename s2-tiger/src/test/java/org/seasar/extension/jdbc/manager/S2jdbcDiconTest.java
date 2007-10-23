package org.seasar.extension.jdbc.manager;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.entity.Emp;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class S2jdbcDiconTest extends S2TestCase {

    private JdbcManager jdbcManager;

    @Override
    protected void setUp() {
        include("s2jdbc.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetResultList() throws Exception {
        assertTrue(jdbcManager.from(Emp.class).getResultList().size() > 1);
    }
}

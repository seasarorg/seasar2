package org.seasar.extension.jdbc.meta;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class MetaDiconTest extends S2TestCase {

    private EntityMetaFactory entityMetaFactory;

    @Override
    protected void setUp() {
        include("s2jdbc-internal.dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetComponent() throws Exception {
        assertNotNull(entityMetaFactory.getEntityMeta(Aaa.class));
    }
}

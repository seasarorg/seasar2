package org.seasar.framework.container.deployer;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.IllegalInstanceDefRuntimeException;
import org.seasar.framework.container.deployer.InstanceDefFactory;

/**
 * @author higa
 */
public class InstanceDefFactoryTest extends S2TestCase {
    
    public void testGetInstanceDef() throws Exception {
        assertEquals("1", InstanceDefFactory.SESSION,
                InstanceDefFactory.getInstanceDef("session"));
        try {
            InstanceDefFactory.getInstanceDef("hoge");
            fail("2");
        } catch (IllegalInstanceDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}
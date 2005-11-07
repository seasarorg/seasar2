package org.seasar.framework.container.deployer;

import junit.framework.TestCase;

import org.seasar.framework.container.IllegalInstanceDefRuntimeException;

/**
 * @author higa
 */
public class InstanceDefFactoryTest extends TestCase {
    
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
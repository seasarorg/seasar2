package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.container.IllegalAutoBindingDefRuntimeException;

/**
 * @author higa
 */
public class AutoBindingDefFactoryTest extends TestCase {
    
    public void testGetAutoBindingDef() throws Exception {
        assertEquals("1", AutoBindingDefFactory.AUTO,
                AutoBindingDefFactory.getAutoBindingDef("auto"));
        assertEquals("2", AutoBindingDefFactory.CONSTRUCTOR,
                AutoBindingDefFactory.getAutoBindingDef("constructor"));
        assertEquals("3", AutoBindingDefFactory.PROPERTY,
                AutoBindingDefFactory.getAutoBindingDef("property"));
        assertEquals("4", AutoBindingDefFactory.NONE,
                AutoBindingDefFactory.getAutoBindingDef("none"));
        try {
            AutoBindingDefFactory.getAutoBindingDef("hoge");
            fail("5");
        } catch (IllegalAutoBindingDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}
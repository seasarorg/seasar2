package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class JarComponentAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;
    private JarComponentAutoRegister autoRegister;
    
    protected void setUp() {
        include("JarComponentAutoRegisterTest.dicon");
    }
    public void testGetBaseDir() throws Exception { 
        String file = autoRegister.getBaseDir();
        System.out.println(file);
        assertNotNull("1", file);
    }
    
    public void testRegisterAll() throws Exception {
        assertTrue("1", child.hasComponentDef("testSuite"));
    }
}
package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class InterfaceAspectAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;
  
    public void setUpRegisterAll() throws Exception {
        include("InterfaceAspectAutoRegisterTest.dicon");
    }
    
    public void testRegisterAll() throws Exception {
        ComponentDef cd = child.getComponentDef("foo");
        assertEquals("1", 1, cd.getAspectDefSize());
        Foo foo = (Foo) cd.getComponent();
        assertEquals("2", "Hello", foo.greet());
        ComponentDef cd2 = child.getComponentDef("foo2");
        assertEquals("3", 0, cd2.getAspectDefSize());
    }
}
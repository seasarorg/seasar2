package org.seasar.framework.container.factory;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;

/**
 * @author higa
 */
public class ComponentTagHandler2Test extends S2TestCase {

    private S2Container child;
    
    public void setUpAll() throws Exception {
        include("ComponentTagHandler2Test.dicon");
    }
    
    public void testAll() throws Exception {
        Foo foo = (Foo) child.getComponent("foo");
        assertNotNull("1", foo);
        assertEquals("2", "111", foo.getAaa());
        Foo foo2 = (Foo) child.getComponent("foo2");
        assertNotNull("3", foo);
        assertEquals("4", "222", foo2.getAaa());
    }
}
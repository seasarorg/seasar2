package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class AspectAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;
  
    public void setUpRegisterAll() throws Exception {
        include("autoRegister2.dicon");
    }
    
    public void testRegisterAll() throws Exception {
        Bar bar = (Bar) child.getComponent("bar");
        assertNotNull("1", bar);
        assertEquals("2", "Hello", bar.greet());
        ComponentDef cd = child.getComponentDef("bar2");
        assertEquals("3", 2, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("4", "greetingInterceptor2", aspectDef.getExpression());
        bar = (Bar) child.getComponent("bar2");
        assertEquals("5", "Hello", bar.greet());
        AspectDef aspectDef2 = cd.getAspectDef(1);
        assertNotNull("6", aspectDef2.getValue());
        assertNull("7", aspectDef2.getExpression());
    }
}
package org.seasar.framework.container.autoregister;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * @author higa
 */
public class AspectAutoRegisterTest extends S2TestCase {

    private S2Container child;
  
    public void setUpRegistAll2() throws Exception {
        include("autoRegister2.dicon");
    }
    
    public void testRegistAll2() throws Exception {
        Bar bar = (Bar) child.getComponent("bar");
        assertNotNull("1", bar);
        assertEquals("2", "Hello", bar.greet());
        ComponentDef cd = child.getComponentDef("bar2");
        assertEquals("3", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("4", "greetingInterceptor2", aspectDef.getExpression());
        bar = (Bar) child.getComponent("bar2");
        assertEquals("5", "Hello", bar.greet());
    }
}
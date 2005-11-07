package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class FileSystemComponentAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;
    
    public void setUpRegistAll() throws Exception {
        include("autoRegister.dicon");
    }
    
    public void testRegistAll() throws Exception {
        Foo foo = (Foo) child.getComponent(Foo.class);
        assertNotNull("1", foo);
        Foo2 foo2 = (Foo2) child.getComponent(Foo2.class);
        assertNotNull("2", foo2);
        assertNotNull("3", child.getComponent(Foo3.class));
        assertSame("4", foo2, foo.getFoo2());
        assertNotNull("5", child.getComponent("foo3"));
        assertFalse("6", child.hasComponentDef(Foo4Impl.class));
    }
    
    public void setUpRegistAll2() throws Exception {
        include("autoRegister3.dicon");
    }
    
    public void testRegistAll2() throws Exception {
        ComponentDef cd = child.getComponentDef(Foo.class);
        assertEquals("1", InstanceDef.PROTOTYPE_NAME, cd.getInstanceDef().getName());
        ComponentDef cd2 = child.getComponentDef(Foo2.class);
        assertEquals("2", InstanceDef.REQUEST_NAME, cd2.getInstanceDef().getName());
        assertNotNull("3", child.getComponent(Foo5.class));
        try {
            child.getComponent("foo5");
            fail("4");
        } catch (TooManyRegistrationRuntimeException ex) {
            System.out.println(ex);
        }
    }
}
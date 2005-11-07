package org.seasar.framework.container.deployer;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.hotswap.Hotswap;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 *  
 */
public class SessionComponentDeployerTest extends TestCase {

	public void testDeployAutoAutoConstructor() throws Exception {
		MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		S2Container container = new S2ContainerImpl();
		container.setRequest(request);
		ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
		container.register(cd);
		ComponentDeployer deployer = new SessionComponentDeployer(cd);
		Foo foo = (Foo) deployer.deploy();
		assertSame("1", foo, request.getSession().getAttribute("foo"));
		assertSame("2", foo, deployer.deploy());
	}
    
    public void testDeployForHotswap() throws Exception {
        MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
        HttpServletRequest request = ctx.createRequest("/hello.html");
        S2Container container = new S2ContainerImpl();
        container.setHotswapMode(true);
        container.setRequest(request);
        ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
        container.register(cd);
        container.init();
        ComponentDeployer deployer = new SessionComponentDeployer(cd);
        Foo foo = (Foo) deployer.deploy();
        Hotswap hotswap = cd.getHotswap();
        Thread.sleep(500);
        hotswap.getFile().setLastModified(new Date().getTime());
        assertNotSame("1", foo.getClass(), deployer.deploy().getClass());
    }
    
    public static class Foo {
        
        public void aaa() {
        }
    }
}
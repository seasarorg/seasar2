package org.seasar.framework.container.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 *
 */
public class S2ContainerImplTest extends TestCase {

	public void testRegister() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(A.class);
		container.register(B.class);
		container.register(B2.class);
		try {
			container.getComponent(A.class);
			fail("1");
		} catch (TooManyRegistrationRuntimeException ex) {
			System.out.println(ex);
			assertEquals("2", Hoge.class, ex.getKey());
			assertEquals("3", 2, ex.getComponentClasses().length);
			assertEquals("4", B.class, ex.getComponentClasses()[0]);
			assertEquals("5", B2.class, ex.getComponentClasses()[1]);
		}
	}

	public void testRegisterForAlreadyRegistration() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDef cd = new ComponentDefImpl(B.class, "B");
		ComponentDef cd2 = new ComponentDefImpl(B2.class, "B");
		container.register(cd);
		container.register(cd2);
		try {
			container.getComponent("B");
			fail("1");
		} catch (TooManyRegistrationRuntimeException ex) {
			System.out.println(ex);
			assertEquals("2", "B", ex.getKey());
			assertEquals("3", 2, ex.getComponentClasses().length);
			assertEquals("4", B.class, ex.getComponentClasses()[0]);
			assertEquals("5", B2.class, ex.getComponentClasses()[1]);
		}
	}

	public void testInclude() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(A.class);
		S2Container container2 = new S2ContainerImpl();
		container2.register(B.class);
		container.include(container2);
		A a = (A) container.getComponent(A.class);
		assertEquals("1", "B", a.getHogeName());
	}

	public void testInclude2() throws Exception {
		S2Container root = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		child.register("hoge", "hoge");
		root.include(child);
		S2Container child2 = new S2ContainerImpl();
		child2.setNamespace("bbb");
		child2.register("hoge2", "hoge");
		S2Container grandchild = new S2ContainerImpl();
		grandchild.setNamespace("ccc");
		grandchild.register("hoge3", "hoge");
		child2.include(grandchild);
		root.include(child2);
		assertEquals("1", "hoge", child.getComponent("hoge"));
		assertEquals("2", "hoge3", grandchild.getComponent("hoge"));
		assertEquals("3", child, root.getComponent("aaa"));
		assertEquals("4", child2, root.getComponent("bbb"));
		assertEquals("5", "hoge", root.getComponent("aaa.hoge"));
		assertEquals("6", "hoge2", root.getComponent("bbb.hoge"));
		assertEquals("7", "hoge3", root.getComponent("ccc.hoge"));
		assertEquals("8", "hoge", child.getComponent("aaa.hoge"));
		assertEquals("9", false, child.hasComponentDef("bbb.hoge"));
		assertEquals("10", false, child.hasComponentDef("ccc.hoge"));
		assertEquals("11", "hoge2", child2.getComponent("hoge"));
		assertEquals("12", "hoge3", child2.getComponent("ccc.hoge"));
		assertEquals("13", 0, root.getComponentDefSize());
	}

	public void testInclude3() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setPath("aaa.xml");
		S2Container grandchild = new S2ContainerImpl();
		grandchild.setPath("bbb.xml");
		grandchild.setNamespace("bbb");
		child.include(grandchild);
		container.include(child);
		container.include(grandchild);
		assertNotNull("1", container.getComponentDef("bbb"));
	}

	public void testInclude4() throws Exception {
		S2Container aaa = new S2ContainerImpl();
		aaa.setPath("aaa.xml");
		aaa.setNamespace("aaa");
		S2Container bbb = new S2ContainerImpl();
		bbb.setPath("bbb.xml");
		S2Container aaa2 = new S2ContainerImpl();
		aaa2.setPath("aaa.xml");
		aaa2.setNamespace("aaa");
		bbb.include(aaa2);
		aaa.include(bbb);
		assertNotNull("1", aaa.getComponentDef("aaa"));
	}
	
	public void testInclude5() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		S2Container child2 = new S2ContainerImpl();
		child2.setNamespace("aaa");
		container.include(child);
		container.include(child2);
		try {
			container.getComponent("aaa");
			fail("1");
		} catch (TooManyRegistrationRuntimeException ex) {
			System.out.println(ex);
		}
	}

	public void testInitAndDestroy() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		List initList = new ArrayList();
		List destroyList = new ArrayList();
		ComponentDef componentDef = new ComponentDefImpl(C.class, "c1");
		componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
		componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
		componentDef.addArgDef(new ArgDefImpl("c1"));
		componentDef.addArgDef(new ArgDefImpl(initList));
		componentDef.addArgDef(new ArgDefImpl(destroyList));
		container.register(componentDef);

		componentDef = new ComponentDefImpl(C.class, "c2");
		componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
		componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
		componentDef.addArgDef(new ArgDefImpl("c2"));
		componentDef.addArgDef(new ArgDefImpl(initList));
		componentDef.addArgDef(new ArgDefImpl(destroyList));
		container.register(componentDef);
		
		componentDef = new ComponentDefImpl(C.class, "c3");
		componentDef.addInitMethodDef(new InitMethodDefImpl("init"));
		componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("destroy"));
		componentDef.addArgDef(new ArgDefImpl("c3"));
		componentDef.addArgDef(new ArgDefImpl(initList));
		componentDef.addArgDef(new ArgDefImpl(destroyList));
		child.register(componentDef);
		container.include(child);

		container.init();
		assertEquals("1", 3, initList.size());
		assertEquals("2", "c3", initList.get(0));
		assertEquals("3", "c1", initList.get(1));
		assertEquals("4", "c2", initList.get(2));
		container.destroy();
		assertEquals("5", 3, destroyList.size());
		assertEquals("6", "c2", destroyList.get(0));
		assertEquals("7", "c1", destroyList.get(1));
		assertEquals("8", "c3", destroyList.get(2));
	}

	public void testInjectDependency() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDef componentDef = new ComponentDefImpl(HashMap.class, "hoge");
		componentDef.setInstanceDef(InstanceDefFactory.OUTER);
		InitMethodDef md = new InitMethodDefImpl("put");
		md.addArgDef(new ArgDefImpl("aaa"));
		md.addArgDef(new ArgDefImpl("111"));
		componentDef.addInitMethodDef(md);
		container.register(componentDef);

		HashMap map = new HashMap();
		container.injectDependency(map);
		assertEquals("1", "111", map.get("aaa"));

		HashMap map2 = new HashMap();
		container.injectDependency(map2, Map.class);
		assertEquals("2", "111", map2.get("aaa"));

		HashMap map3 = new HashMap();
		container.injectDependency(map3, "hoge");
		assertEquals("3", "111", map3.get("aaa"));
	}

	public void testSelf() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(D.class);
		D d = (D) container.getComponent(D.class);
		assertSame("1", container, d.getContainer());
	}

	public void testSelf2() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDef cd = new ComponentDefImpl(D.class);
		PropertyDef pd = new PropertyDefImpl("container");
		pd.setExpression(ContainerConstants.CONTAINER_NAME);
		cd.addPropertyDef(pd);
		container.register(cd);
		D d = (D) container.getComponent(D.class);
		assertSame("1", container, d.getContainer());
	}

	public void testConstructor() throws Exception {
		S2Container container = new S2ContainerImpl();
		assertEquals("1", 0, container.getComponentDefSize());
	}

	public void testNamespace() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.setNamespace("aaa");
		container.register(String.class, "bbb");
		assertNotNull("1", container.getComponent("bbb"));
		assertNotNull("2", container.getComponent("aaa.bbb"));
	}

	public void testGetComponentDef() throws Exception {
		S2Container aaa = new S2ContainerImpl();
		aaa.setNamespace("aaa");
		S2Container bbb = new S2ContainerImpl();
		bbb.setNamespace("bbb");
		bbb.register(String.class, "hoge");
		aaa.include(bbb);
		assertNotNull("1", aaa.getComponentDef("bbb.hoge"));
		assertNotNull("2", bbb.getComponentDef("bbb.hoge"));
	}
	
	public void testGetComponentDef2() throws Exception {
		S2Container container = new S2ContainerImpl();
		container.register(FooImpl.class);
		Hoge hoge = (Hoge) container.getComponent(Hoge.class);
		assertEquals("1", "Foo", hoge.getName());
	}
	
	public void testRequest() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		child.register(RequestClient.class);
		container.include(child);
		MockServletContextImpl ctx = new MockServletContextImpl("/s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		container.setRequest(request);
		RequestClient client = (RequestClient) container.getComponent(RequestClient.class);
		assertNotNull("1", client.getRequest());
	}
	
	public void testSession() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		child.register(SessionClient.class);
		container.include(child);
		MockServletContextImpl ctx = new MockServletContextImpl("/s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		container.setRequest(request);
		SessionClient client = (SessionClient) container.getComponent(SessionClient.class);
		assertNotNull("1", client.getSession());
	}
	
	public void testResponse() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		child.register(ResponseClient.class);
		container.include(child);
		MockServletContextImpl ctx = new MockServletContextImpl("/s2jsf-example");
		HttpServletRequest request = ctx.createRequest("/hello.html");
		HttpServletResponse response = new MockHttpServletResponseImpl(request);
		container.setResponse(response);
		ResponseClient client = (ResponseClient) container.getComponent(ResponseClient.class);
		assertNotNull("1", client.getResponse());
	}
	
	public void testServletContext() throws Exception {
		S2Container container = new S2ContainerImpl();
		S2Container child = new S2ContainerImpl();
		child.setNamespace("aaa");
		child.register(ServletContextClient.class);
		container.include(child);
		MockServletContextImpl ctx = new MockServletContextImpl("/s2jsf-example");
		container.setServletContext(ctx);
		ServletContextClient client = (ServletContextClient) container.getComponent(ServletContextClient.class);
		assertNotNull("1", client.getServletContext());
	}

	public static class A {

		private Hoge hoge_;

		public A(Hoge hoge) {
			hoge_ = hoge;
		}

		public String getHogeName() {
			return hoge_.getName();
		}
	}

	public interface Hoge {

		public String getName();
	}
	
	public interface Foo extends Hoge {
	}

	public static class B implements Hoge {

		public String getName() {
			return "B";
		}
	}

	public static class B2 implements Hoge {

		public String getName() {
			return "B2";
		}
	}

	public static class C {

		private String name_;
		private List initList_;
		private List destroyList_;

		public C(String name, List initList, List destoryList) {
			name_ = name;
			initList_ = initList;
			destroyList_ = destoryList;
		}

		public void init() {
			initList_.add(name_);
		}

		public void destroy() {
			destroyList_.add(name_);
		}
	}

	public static class D {

		private S2Container container_;

		public S2Container getContainer() {
			return container_;
		}

		public void setContainer(S2Container container) {
			container_ = container;
		}
	}
	
	public static class FooImpl implements Foo {
		public String getName() {
			return "Foo";
		}
	}
	
	public static class RequestClient {
		
		private HttpServletRequest request_;
		
		public HttpServletRequest getRequest() {
			return request_;
		}
		
		public void setRequest(HttpServletRequest request) {
			this.request_ = request;
		}
	}
	
	public static class SessionClient {
		
		private HttpSession session_;
		
		public HttpSession getSession() {
			return session_;
		}
		
		public void setSession(HttpSession session) {
			this.session_ = session;
		}
	}
	
	public static class ResponseClient {
		
		private HttpServletResponse response_;
		
		public HttpServletResponse getResponse() {
			return response_;
		}
		
		public void setResponse(HttpServletResponse response) {
			this.response_ = response;
		}
	}
	
	public static class ServletContextClient {
		
		private ServletContext servletContext_;
		
		public ServletContext getServletContext() {
			return servletContext_;
		}
		
		public void setServletContext(ServletContext servletContext) {
			servletContext_ = servletContext;
		}
	}
}
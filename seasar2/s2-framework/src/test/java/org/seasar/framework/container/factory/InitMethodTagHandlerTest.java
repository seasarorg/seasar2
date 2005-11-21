package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 *
 */
public class InitMethodTagHandlerTest extends TestCase {

	private static final String PATH =
		"org/seasar/framework/container/factory/InitMethodTagHandlerTest.dicon";

	public void testArg() throws Exception {
		S2Container container = S2ContainerFactory.create(PATH);
		Map aaa = (HashMap) container.getComponent("aaa");
		assertEquals("1", new Integer(111), aaa.get("aaa"));
		Bbb bbb = (Bbb) container.getComponent("bbb");
		assertEquals("2", false, bbb.isEmpty());
	}
    
    public void testInitMethodAnnotation() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        Bbb bbb = (Bbb) container.getComponent("bbb");
        Bbb bbb2 = (Bbb) container.getComponent("bbb2");
        ComponentDef cd = container.getComponentDef("bbb2");
        assertEquals("1", 1, bbb.getInitCount());
        assertEquals("2", 1, cd.getInitMethodDefSize());
        assertEquals("3", 1, bbb2.getInitCount());
    }
	
	public static class Bbb {
		
        public static final String INIT_METHOD = "init";
        
		private List value;
        
        private int initCount = 0;
		
		public void value(List value) {
			this.value = value;
		}
		
		public boolean isEmpty() {
			return value == null;
		}
        
        public void init() {
            ++initCount;
        }
        
        public int getInitCount() {
            return initCount;
        }
	}
}

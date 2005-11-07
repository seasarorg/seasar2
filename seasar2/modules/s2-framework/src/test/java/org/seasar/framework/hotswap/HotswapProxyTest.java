package org.seasar.framework.hotswap;

import junit.framework.TestCase;

import org.seasar.framework.hotswap.HotswapProxy;
import org.seasar.framework.hotswap.SimpleHotswapTargetFactory;
import org.seasar.framework.util.SerializeUtil;

/**
 * @author higa
 * 
 */
public class HotswapProxyTest extends TestCase {

    private Greeting greeting;

    protected void setUp() {
        greeting = (Greeting) HotswapProxy.create(GreetingImpl.class,
                new SimpleHotswapTargetFactory(GreetingImpl.class));
    }

    public void testEquals() {
        assertTrue("1", greeting.equals(greeting));
        assertFalse("2", greeting.equals(this));
        assertFalse("3", greeting.equals(null));
    }

    public void testHashCode() {
        System.out.println(greeting.hashCode());
        assertEquals("1", greeting.hashCode(), greeting.hashCode());
    }

    public void testToString() {
        assertEquals("1", "hoge", greeting.toString());
    }

    public void testGetProxy() {
        assertNotNull("1", HotswapProxy.getProxy(greeting));
    }

    public void testSerialize() throws Exception {
        HotswapProxy proxy = HotswapProxy.getProxy(greeting);
        Greeting other = (Greeting) SerializeUtil.serialize(greeting);
        HotswapProxy otherProxy = HotswapProxy.getProxy(other);
        assertNotNull("1", otherProxy);
        assertEquals("2", proxy.getTargetClass(), otherProxy.getTargetClass());
        assertEquals("3", proxy.getPath(), otherProxy.getPath());
        assertEquals("4", proxy.getLastModified(), otherProxy.getLastModified());
        assertNotNull("5", otherProxy.getFile());
    }
}
/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.hotswap;

import junit.framework.TestCase;

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

    public void testInherit() throws Exception {
        greeting = (Greeting) HotswapProxy.create(GreetingImpl2.class,
                new SimpleHotswapTargetFactory(GreetingImpl2.class));

        greeting = (Greeting) HotswapProxy.create(GreetingImpl3.class,
                new SimpleHotswapTargetFactory(GreetingImpl3.class));
    }
}
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

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.util.SerializeUtil;

/**
 * @author higa
 * 
 */
public class HotswapTest extends TestCase {

    private Hotswap hotswap;

    protected void setUp() {
        hotswap = new Hotswap(GreetingImpl.class);
    }

    public void testPath() {
        assertEquals("1", "org/seasar/framework/hotswap/GreetingImpl.class",
                hotswap.getPath());
    }

    public void testFile() {
        assertTrue("1", hotswap.getFile().exists());
    }

    public void testLastModified() {
        assertTrue("1", hotswap.getLastModified() > 0);
    }

    public void testUpdateTargetClass() throws Exception {
        long lm = hotswap.getLastModified();
        Class clazz = hotswap.getTargetClass();
        Thread.sleep(500);
        hotswap.getFile().setLastModified(new Date().getTime());
        assertTrue("1", hotswap.isModified());
        Class clazz2 = hotswap.updateTargetClass();
        assertTrue("2", hotswap.getLastModified() > lm);
        assertNotSame("3", clazz, clazz2);
        assertFalse("4", hotswap.isModified());
    }

    public void testInJar() throws Exception {
        Hotswap h = new Hotswap(String.class);
        assertEquals("1", 0, h.getLastModified());
        assertEquals("2", String.class, h.getTargetClass());
    }

    public void testSerialize() throws Exception {
        Hotswap other = (Hotswap) SerializeUtil.serialize(hotswap);
        assertEquals("1", hotswap.getTargetClass(), other.getTargetClass());
        assertEquals("2", hotswap.getPath(), other.getPath());
        assertEquals("3", hotswap.getLastModified(), other.getLastModified());
        assertNotNull("4", hotswap.getFile());
    }
}
/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class DefaultAutoNamingTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testDefineName() throws Exception {
        DefaultAutoNaming naming = new DefaultAutoNaming();
        naming.setCustomizedName("aaa.Foo", "aaaFoo");
        assertEquals("1", "foo", naming.defineName(null, "Foo"));
        assertEquals("2", "foo4", naming.defineName(null, "Foo4Impl"));
        assertEquals("3", "aaaFoo", naming.defineName("aaa", "Foo"));
        assertEquals("3", "foo", naming.defineName("bbb", "FooBean"));
    }

    /**
     * @throws Exception
     */
    public void testApplyRule() throws Exception {
        DefaultAutoNaming naming = new DefaultAutoNaming();
        naming.addReplaceRule("aaa", "bbb");
        assertEquals("bbbccc", naming.applyRule("aaaccc"));
    }
}
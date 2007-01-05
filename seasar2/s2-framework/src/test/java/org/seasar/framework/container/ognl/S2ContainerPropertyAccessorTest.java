/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.ognl;

import junit.framework.TestCase;
import ognl.Ognl;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class S2ContainerPropertyAccessorTest extends TestCase {

    private PropertyAccessor propAccessor;

    protected void setUp() throws Exception {
        propAccessor = OgnlRuntime.getPropertyAccessor(S2Container.class);
    }

    protected void tearDown() throws Exception {
        OgnlRuntime.setPropertyAccessor(S2Container.class, propAccessor);
    }

    public void testGetProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register("111", "aaa");
        OgnlRuntime.setPropertyAccessor(S2Container.class,
                new S2ContainerPropertyAccessor());
        assertEquals("1", "111", Ognl.getValue("aaa", container));
    }
}
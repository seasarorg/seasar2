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
package org.seasar.extension.component.impl;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.unit.S2FrameworkTestCase;

public class ComponentInvokerImplTest extends S2FrameworkTestCase {

    private ComponentInvoker invoker;
    
	public void testInvoke() throws Throwable {
		assertEquals("1", new Integer(4), invoker.invoke("aaa", "length", null));
        try {
            invoker.invoke("aaa", "substring", new Object[]{new Integer(-1)});
            fail("1");
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println(ex);
        }
	}
		
	protected void setUp() throws Exception {
        register(ComponentInvokerImpl.class);
        register("Hoge", "aaa");
	}
}
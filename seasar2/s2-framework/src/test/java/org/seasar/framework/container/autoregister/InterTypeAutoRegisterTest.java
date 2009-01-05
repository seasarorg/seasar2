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

import java.lang.reflect.Field;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 *
 */
public class InterTypeAutoRegisterTest extends S2FrameworkTestCase {

    private S2Container child;

    /**
     * @throws Exception
     */
    public void setUpRegisterAll() throws Exception {
        include("autoRegister2.dicon");
    }

    /**
     * @throws Exception
     */
    public void testRegisterAll() throws Exception {
        Bar bar = (Bar) child.getComponent("bar");
        assertNotNull("1", bar);
        Field field = bar.getClass().getDeclaredField("test");
        field.setAccessible(true);
        assertNotNull("2", field);
        assertFalse("3", field.getBoolean(bar));

        Bar bar2 = (Bar) child.getComponent("bar");
        assertNotNull("3", bar2);
        field = bar2.getClass().getDeclaredField("test");
        field.setAccessible(true);
        assertNotNull("4", field);
        assertFalse("5", field.getBoolean(bar2));
    }

}

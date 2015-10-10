/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

/**
 * @author higa
 * 
 */
public class PropertyTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/PropertyTagHandlerTest.dicon";

    /**
     * @throws Exception
     */
    public void testProperty() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        assertEquals("1", new Date(0), container.getComponent("date"));
        ComponentDef cd = container.getComponentDef("date");
        PropertyDef pd = cd.getPropertyDef("time");
        assertEquals("2", BindingTypeDefFactory.NONE.getName(), pd
                .getBindingTypeDef().getName());
    }
}

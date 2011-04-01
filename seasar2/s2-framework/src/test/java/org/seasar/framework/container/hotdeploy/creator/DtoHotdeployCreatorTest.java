/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.hotdeploy.creator;

import java.lang.reflect.Method;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.creator.DtoCreator;
import org.seasar.framework.container.customizer.InterTypeCustomizer;
import org.seasar.framework.convention.NamingConvention;

/**
 * @author higa
 * 
 */
public class DtoHotdeployCreatorTest extends HotdeployCreatorTestCase {

    protected ComponentCreator newOndemandCreator(NamingConvention convention) {
        include("aop.dicon");
        DtoCreator creator = new DtoCreator(convention);
        InterTypeCustomizer customizer = new InterTypeCustomizer();
        customizer.addInterTypeName("aop.propertyInterType");
        creator.setDtoCustomizer(customizer);
        return creator;
    }

    /**
     * @throws Exception
     */
    public void testNormal() throws Exception {
        String name = "bbbDto";
        ComponentDef cd = getComponentDef(name);
        assertNotNull(cd);
        assertSame(cd, getComponentDef(name));
        assertEquals(name, cd.getComponentName());
        Object o = cd.getComponent();
        Method m = o.getClass().getMethod("getName", null);
        assertEquals("Hoge", m.invoke(o, null));
    }

}

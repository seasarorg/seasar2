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
package org.seasar.framework.container.hotdeploy.creator;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.creator.ServiceCreator;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class ServiceOndemandCreatorTest extends OndemandCreatorTestCase {

    protected ComponentCreator newOndemandCreator(NamingConvention convention) {
        return new ServiceCreator(convention);
    }

    public void testIsTargetByName() throws Exception {
        String name = "aaa_hogeService";
        ComponentDef cd = getComponentDef(name);
        assertNotNull("1", cd);
        assertEquals("2", name, cd.getComponentName());
    }

    public void testIsTargetByName2() throws Exception {
        String name = "hogeService";
        ComponentDef cd = getComponentDef(name);
        assertNotNull("1", cd);
        assertEquals("2", name, cd.getComponentName());
    }

    public void testIsTargetByClass() throws Exception {
        Class clazz = ClassUtil.forName(ClassUtil.getPackageName(getClass())
                + ".web.aaa.HogeService");
        assertNotNull("1", getComponent(clazz));
    }
}
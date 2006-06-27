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
package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 * 
 */
public class ComponentTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/ComponentTagHandlerTest.dicon";

    public void testComponent() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        ComponentDeployerFactory
                .setProvider(new ExternalComponentDeployerProvider());
        MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
        HttpServletRequest request = ctx.createRequest("/hello.html");
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setRequest(request);
        extCtx.setApplication(ctx);
        container.setExternalContext(extCtx);
        
        container.init();
        assertNotNull(container.getComponent(List.class));
        assertNotNull(container.getComponent("aaa"));
        assertEquals(new Integer(1), container.getComponent("bbb"));
        assertEquals(true, container.getComponent("ccc") != container
                .getComponent("ccc"));
        ComponentDef cd = container.getComponentDef("ddd");
        assertEquals(AutoBindingDefFactory.NONE, cd.getAutoBindingDef());
        Map map = new HashMap();
        container.injectDependency(map, "eee");
        assertEquals("111", map.get("aaa"));
        assertNotNull(container.getComponent("fff"));
        assertNotNull(container.getComponent("ggg"));
        
        assertNotNull(container.getComponent("hhh"));
        assertNotNull(container.getComponent("iii"));
        assertEquals("jjj", container.getComponent("jjj"));
        request.setAttribute("name", "aaa");
        cd = container.getComponentDef("kkk");
        assertEquals(true, cd.isExternalBinding());
        Kkk kkk = (Kkk) cd.getComponent();
        assertEquals("aaa", kkk.getName());
    }
    
    public static class Kkk {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

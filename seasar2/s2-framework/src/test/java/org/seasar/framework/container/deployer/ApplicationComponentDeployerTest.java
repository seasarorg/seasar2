/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.deployer;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;

/**
 * @author higa
 * 
 */
public class ApplicationComponentDeployerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testDeployAutoAutoConstructor() throws Exception {
        MockServletContextImpl ctx = new MockServletContextImpl("s2jsf-example");
        S2Container container = new S2ContainerImpl();
        ExternalContext extCtx = new HttpServletExternalContext();
        extCtx.setApplication(ctx);
        container.setExternalContext(extCtx);
        container.register("aaa", "hoge");
        ComponentDef cd = new ComponentDefImpl(Foo.class, "foo");
        container.register(cd);
        ComponentDeployer deployer = new ApplicationComponentDeployer(cd);
        Foo foo = (Foo) deployer.deploy();
        assertSame("1", foo, ctx.getAttribute("foo"));
        assertSame("2", foo, deployer.deploy());
        assertEquals("aaa", foo.getHoge());
    }

    /**
     * 
     */
    public static class Foo {

        private String hoge;

        /**
         * 
         */
        public void aaa() {
        }

        /**
         * @return
         */
        public String getHoge() {
            return hoge;
        }

        /**
         * @param hoge
         */
        public void setHoge(String hoge) {
            this.hoge = hoge;
        }
    }

}

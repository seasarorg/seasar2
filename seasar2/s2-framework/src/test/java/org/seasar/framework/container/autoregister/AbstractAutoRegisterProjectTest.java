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
package org.seasar.framework.container.autoregister;

import junit.framework.TestCase;

import org.seasar.framework.container.autoregister.AbstractAutoRegisterProject;
import org.seasar.framework.container.autoregister.AutoRegisterProject;

/**
 * @author taichi
 * 
 */
public class AbstractAutoRegisterProjectTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * Test method for
     * 'org.seasar.framework.container.autoregister.impl.AbstractAutoRegisterProject.matchClassName(String)'
     */
    public void testMatchClassName() {
        AbstractAutoRegisterProject project = new AbstractAutoRegisterProject() {
        };
        assertEquals(AutoRegisterProject.MATCH, project.matchClassName("Hoge"));
        project.setRootPackageName("aaa");
        assertEquals(AutoRegisterProject.MATCH, project
                .matchClassName("aaa.Hoge"));
        assertEquals(AutoRegisterProject.UNMATCH, project
                .matchClassName("bbb.Hoge"));

        project.addIgnorePackageName("entity");
        assertEquals(AutoRegisterProject.MATCH, project
                .matchClassName("aaa.bbb.Hoge"));
        assertEquals(AutoRegisterProject.IGNORE, project
                .matchClassName("aaa.entity.Hoge"));

        project.setRootPackageName(null);
        assertEquals(AutoRegisterProject.MATCH, project
                .matchClassName("bbb.Hoge"));
        assertEquals(AutoRegisterProject.IGNORE, project
                .matchClassName("entity.Hoge"));
    }

}

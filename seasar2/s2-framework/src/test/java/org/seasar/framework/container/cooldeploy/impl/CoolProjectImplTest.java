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
package org.seasar.framework.container.cooldeploy.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.OndemandProject;

/**
 * @author higa
 * 
 */
public class CoolProjectImplTest extends TestCase {

    public void testMatchClassName() throws Exception {
        CoolProjectImpl project = new CoolProjectImpl();
        assertEquals(OndemandProject.MATCH, project.matchClassName("Hoge"));
        project.setRootPackageName("aaa");
        assertEquals(OndemandProject.MATCH, project.matchClassName("aaa.Hoge"));
        assertEquals(OndemandProject.UNMATCH, project
                .matchClassName("bbb.Hoge"));

        project.addIgnorePackageName("entity");
        assertEquals(OndemandProject.MATCH, project
                .matchClassName("aaa.bbb.Hoge"));
        assertEquals(OndemandProject.IGNORE, project
                .matchClassName("aaa.entity.Hoge"));

        project.setRootPackageName(null);
        assertEquals(OndemandProject.MATCH, project.matchClassName("bbb.Hoge"));
        assertEquals(OndemandProject.IGNORE, project
                .matchClassName("entity.Hoge"));
    }
}
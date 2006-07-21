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
package org.seasar.framework.container.hotdeploy.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.OndemandBehavior;
import org.seasar.framework.container.hotdeploy.OndemandCreator;
import org.seasar.framework.container.hotdeploy.OndemandProject;
import org.seasar.framework.container.hotdeploy.OndemandS2Container;
import org.seasar.framework.container.hotdeploy.creator.DtoOndemandCreator;
import org.seasar.framework.container.hotdeploy.creator.PageOndemandCreator;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;

/**
 * @author higa
 * 
 */
public class OndemandProjectImplTest extends TestCase {

    public void testMatchClassName() throws Exception {
        OndemandProjectImpl project = new OndemandProjectImpl();
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

    public void testFromComponentNameToClassName() throws Exception {

        // ## Arrange ##
        OndemandProjectImpl project = new OndemandProjectImpl();
        project.setRootPackageName("com.example");
        NamingConvention namingConvention = new NamingConventionImpl();
        project.setCreators(new OndemandCreator[] {
                new DtoOndemandCreator(namingConvention),
                new PageOndemandCreator(namingConvention) });
        OndemandS2Container container = new OndemandBehavior();

        // ## Act ##
        // ## Assert ##
        assertNull(project.fromComponentNameToClassName(container, null));

        // ## Act ##
        // ## Assert ##
        assertNull(project.fromComponentNameToClassName(container, "hoehoe"));

        // ## Act ##
        // ## Assert ##
        assertEquals("com.example.web.add.AddPage", project
                .fromComponentNameToClassName(container, "add_addPage"));
    }
}
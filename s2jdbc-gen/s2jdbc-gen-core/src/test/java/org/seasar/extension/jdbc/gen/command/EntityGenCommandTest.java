/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.GenerationContext;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityGenCommandTest {

    /**
     * 
     */
    @Test
    public void testGetEntityConditionClassName() {
        EntityGenCommand command = new EntityGenCommand();
        command.setRootPackageName("aaa");
        command.setConditionPackageName("bbb");
        command.setConditionClassNameSuffix("$");
        String name = command.getEntityConditionClassName("Hoge");
        assertEquals("aaa.bbb.Hoge$", name);
    }

    /**
     * 
     */
    @Test
    public void testGetEntityConditionBaseClassName() {
        EntityGenCommand command = new EntityGenCommand();
        command.setRootPackageName("aaa");
        command.setConditionBasePackageName("bbb");
        String name = command.getEntityConditionBaseClassName("Hoge");
        assertEquals("aaa.bbb.AbstractHogeCondition", name);
    }

    /**
     * 
     */
    @Test
    public void testGetEntityClassName() {
        EntityGenCommand command = new EntityGenCommand();
        command.setRootPackageName("aaa");
        command.setEntityPackageName("bbb");
        String name = command.getEntityClassName("Hoge");
        assertEquals("aaa.bbb.Hoge", name);
    }

    /**
     * 
     */
    @Test
    public void testGetEntityBaseClassName() {
        EntityGenCommand command = new EntityGenCommand();
        command.setRootPackageName("aaa");
        command.setEntityBasePackageName("bbb");
        command.setBaseClassNamePrefix("_");
        String name = command.getEntityBaseClassName("Hoge");
        assertEquals("aaa.bbb._Hoge", name);
    }

    /**
     * 
     */
    @Test
    public void testGetGenerationContext() {
        EntityGenCommand command = new EntityGenCommand();
        command.setDestDir(new File("hoge/foo"));
        command.setJavaFileEncoding("Shift_JIS");
        Object model = new Object();
        GenerationContext context = command.getGenerationContext(model,
                "aaa.bbb.Hoge", "ccc.ftl", false);
        assertNotNull(context);
        assertEquals(new File("hoge/foo/aaa/bbb"), context.getDir());
        assertEquals(new File("hoge/foo/aaa/bbb/Hoge.java"), context.getFile());
        assertEquals("Shift_JIS", context.getEncoding());
        assertSame(model, context.getModel());
        assertEquals("ccc.ftl", context.getTemplateName());
    }

}

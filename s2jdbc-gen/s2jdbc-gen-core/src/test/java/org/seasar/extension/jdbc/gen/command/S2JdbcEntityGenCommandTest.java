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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class S2JdbcEntityGenCommandTest {

    /**
     * 
     */
    @Test
    public void testGetEntityConditionClassName() {
        S2JdbcEntityGenCommand command = new S2JdbcEntityGenCommand();
        command.setRootPackageName("aaa");
        command.setEntityConditionPackageName("bbb");
        command.setEntityConditionClassNameSuffix("$");
        String name = command.getEntityConditionClassName("Hoge");
        assertEquals("aaa.bbb.Hoge$", name);
    }

    /**
     * 
     */
    @Test
    public void testGetEntityConditionBaseClassName() {
        S2JdbcEntityGenCommand command = new S2JdbcEntityGenCommand();
        command.setRootPackageName("aaa");
        command.setEntityConditionBasePackageName("bbb");
        command.setEntityConditionBaseClassNamePrefix("_");
        command.setEntityConditionBaseClassNameSuffix("$");
        String name = command.getEntityConditionBaseClassName("Hoge");
        assertEquals("aaa.bbb._Hoge$", name);
    }

}

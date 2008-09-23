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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DdlVersionOpDirectoryImplTest {

    private File parent;

    private DdlVersionOpDirectoryImpl opDirectory;

    @Before
    public void setUp() {
        String packageName = ClassUtil.splitPackageAndShortClassName(getClass()
                .getName())[0];
        String path = packageName.replace(".", "/") + "/migrate/v020";
        parent = ResourceUtil.getResourceAsFile(path);
        opDirectory = new DdlVersionOpDirectoryImpl(parent, "create", "ut");
    }

    @Test
    public void testAsFile() throws Exception {
        assertEquals(new File(parent, "create"), opDirectory.asFile());
    }

    @Test
    public void testGetChildFile() throws Exception {
        opDirectory = new DdlVersionOpDirectoryImpl(new File("aaa"), "create",
                null);
        File child = opDirectory.getChildFile("bbb");
        assertNotNull(child);
        assertEquals("aaa", child.getParentFile().getParentFile().getName());

        opDirectory = new DdlVersionOpDirectoryImpl(new File("aaa"), "create",
                "ut");
        child = opDirectory.getChildFile("bbb");
        assertNotNull(child);
        assertEquals("aaa#ut", child.getParentFile().getParentFile().getName());
    }

    @Test
    public void testList() throws Exception {
        List<File> list = opDirectory.list();
        assertEquals(2, list.size());
    }
}

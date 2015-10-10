/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ManagedFileImplTest {

    private ManagedFileImpl managedFile;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        String packageName = ClassUtil.splitPackageAndShortClassName(getClass()
                .getName())[0];
        String path = packageName.replace(".", "/") + "/migrate/v020";
        File file = ResourceUtil.getResourceAsFile(path);
        managedFile = new ManagedFileImpl(file.getCanonicalPath(), "create",
                "ut");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAsFile() throws Exception {
        assertEquals("v020#ut", managedFile.asFile().getParentFile().getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateChild() throws Exception {
        ManagedFile child = managedFile.createChild("aaa");
        assertNotNull(child);
        assertEquals("v020#ut", child.asFile().getParentFile().getParentFile()
                .getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testListAllFiles() throws Exception {
        List<File> list = managedFile.listAllFiles();
        assertEquals(3, list.size());
        File file = list.get(0);
        assertEquals("_aaa.txt", file.getName());
        assertEquals("v020", file.getParentFile().getParentFile().getName());
        file = list.get(1);
        assertEquals("aaa.txt", file.getName());
        assertEquals("v020#ut", file.getParentFile().getParentFile().getName());
        file = list.get(2);
        assertEquals("bbb.txt", file.getName());
        assertEquals("v020", file.getParentFile().getParentFile().getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testHasChild() throws Exception {
        assertTrue(managedFile.hasChild());
    }

}

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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DdlVersionDirectoryImplTest {

    private DdlVersionDirectoryImpl directory;

    /**
     * 
     */
    @Before
    public void setUp() {
        directory = new DdlVersionDirectoryImpl(new File("aaa"), 10, "0000",
                null);
    }

    /**
     * 
     */
    @Test
    public void testAsFile() {
        File file = directory.asFile();
        assertEquals("aaa", file.getParentFile().getName());
        assertEquals("0010", file.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAsRelativePath() throws Exception {
        String path = directory.getCreateDirectory().getRelativePath();
        assertEquals(new File(".", "create"), new File(path));
    }

    /**
     * 
     */
    @Test
    public void testGetCreateDirectory() {
        File dir = directory.getCreateDirectory().asFile();
        assertEquals("create", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetDropDirectory() {
        File dir = directory.getDropDirectory().asFile();
        assertEquals("drop", dir.getName());
    }

}

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
package org.seasar.extension.jdbc.gen.version;

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
        directory = new DdlVersionDirectoryImpl(new File("aaa"), new File(
                "bbb"), "0000");
    }

    /**
     * 
     */
    @Test
    public void testGetCurrentVersionDir() {
        File dir = directory.getCurrentVersionDir();
        assertEquals("aaa", dir.getParent());
        assertEquals("0000", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetNextVersionDir() {
        File dir = directory.getNextVersionDir();
        assertEquals("aaa", dir.getParent());
        assertEquals("0001", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetVersionDir() {
        File dir = directory.getVersionDir(10);
        assertEquals("aaa", dir.getParent());
        assertEquals("0010", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetCreateDir() {
        File versionDir = new File("0123");
        File dir = directory.getCreateDir(versionDir);
        assertEquals("create", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetDropDir() {
        File versionDir = new File("0123");
        File dir = directory.getDropDir(versionDir);
        assertEquals("drop", dir.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetDdlVersion() {
        assertNotNull(directory.getDdlVersion());
    }

}

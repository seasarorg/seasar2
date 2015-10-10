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

import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.exception.IllegalDdlInfoVersionRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionExceededRuntimeException;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DdlInfoFileImplTest {

    /**
     * 
     */
    @Test
    public void testGetVersionNo() {
        String path = getClass().getName().replace('.', '/') + "_version.txt";
        File file = ResourceUtil.getResourceAsFile(path);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        assertEquals(10, ddlInfoFile.getCurrentVersionNo());
        assertEquals(10, ddlInfoFile.getCurrentVersionNo());
    }

    /**
     * 
     */
    @Test
    public void testGetVersionNo_fileNotExistent() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File(
                "notExistent"));
        assertEquals(0, ddlInfoFile.getCurrentVersionNo());
    }

    /**
     * 
     */
    @Test
    public void testGetVersionNo_illegalVersionNoFormat() {
        String fileName = getClass().getName().replace('.', '/')
                + "_illegalVersion.txt";
        File file = ResourceUtil.getResourceAsFile(fileName);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        try {
            ddlInfoFile.getCurrentVersionNo();
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetNextVersion_maxVersionNo() throws Exception {
        String path = getClass().getName().replace('.', '/')
                + "_maxVersion.txt";
        File file = ResourceUtil.getResourceAsFile(path);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        try {
            ddlInfoFile.getNextVersionNo();
            fail();
        } catch (NextVersionExceededRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testConvertToInt() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("file"));
        assertEquals(10, ddlInfoFile.convertToInt("10"));
    }

    /**
     * 
     */
    @Test
    public void testConvertToInt_null() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("file"));
        try {
            ddlInfoFile.convertToInt(null);
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testConvertToInt_notNumber() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("file"));
        try {
            ddlInfoFile.convertToInt("aaa");
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testConvertToInt_minus() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("file"));
        try {
            ddlInfoFile.convertToInt("-10");
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    public void testToInt_greaterThanInteger() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("file"));
        long value = (long) Integer.MAX_VALUE + 1;
        try {
            ddlInfoFile.convertToInt(String.valueOf(value));
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }
}

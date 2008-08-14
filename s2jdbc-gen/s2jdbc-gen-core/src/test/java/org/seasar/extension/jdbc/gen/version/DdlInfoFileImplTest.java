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

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.IllegalVersionValueRuntimeException;
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
        assertEquals(10, ddlInfoFile.getVersionNo());
        assertEquals(10, ddlInfoFile.getVersionNo());
    }

    /**
     * 
     */
    @Test
    public void testGetVersionNo_fileNotExistent() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File(
                "notExistent"));
        assertEquals(0, ddlInfoFile.getVersionNo());
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
            ddlInfoFile.getVersionNo();
            fail();
        } catch (IllegalVersionValueRuntimeException expected) {
        }
    }
}

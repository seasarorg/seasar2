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
package org.seasar.extension.jdbc.gen.javacode;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class AbstJavaCodeTest {

    protected AbstractJavaCode code;

    @Before
    public void setUp() {
        code = new AbstractJavaCode("hoge.Foo", "bar.ftl") {
        };
    }

    @Test
    public void testGetPackageName() throws Exception {
        assertEquals("hoge", code.getPackageName());
    }

    @Test
    public void testGetClassName() throws Exception {
        assertEquals("hoge.Foo", code.getClassName());
    }

    @Test
    public void testGetTemplateName() throws Exception {
        assertEquals("bar.ftl", code.getTemplateName());
    }

    @Test
    public void testGetPackageDir() throws Exception {
        File packageDir = code.getPackageDir(new File("base"));
        String path = "base" + File.separator + "hoge";
        assertEquals(path, packageDir.getPath());
    }

    @Test
    public void testGetFile() throws Exception {
        File file = code.getFile(new File("base"));
        String path = "base" + File.separator + "hoge" + File.separator
                + "Foo.java";
        assertEquals(path, file.getPath());
    }

}

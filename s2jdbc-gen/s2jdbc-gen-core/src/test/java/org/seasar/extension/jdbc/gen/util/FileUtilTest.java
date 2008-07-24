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
package org.seasar.extension.jdbc.gen.util;

import java.io.File;

import org.junit.Test;

/**
 * @author taedium
 * 
 */
public class FileUtilTest {

    /**
     * Test method for
     * {@link org.seasar.extension.jdbc.gen.util.FileUtil#copyDir(java.io.File, java.io.File)}
     * .
     */
    @Test
    public void testCopyDir() {
        File f = new File("src");
        for (File child : f.listFiles()) {
            System.out.println(child.getAbsolutePath());
            if (child.isDirectory()) {

            }
        }
    }
}

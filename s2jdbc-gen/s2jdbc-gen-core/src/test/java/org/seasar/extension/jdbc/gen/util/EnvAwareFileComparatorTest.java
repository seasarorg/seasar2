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
import org.seasar.extension.jdbc.gen.util.EnvAwareFileComparator;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EnvAwareFileComparatorTest {

    private EnvAwareFileComparator comparator = new EnvAwareFileComparator(
            "ut");

    @Test
    public void testRemoveExtension() {
        assertEquals("hoge", comparator.removeExtension("hoge.txt"));
    }

    @Test
    public void testRemoveEnv() {
        assertEquals("hoge", comparator.removeEnv("hoge_ut"));
    }

    public void testCompareTo() throws Exception {
        File file1 = new File("hoge_ut.txt");
        File file2 = new File("hoge.txt");
        assertTrue(comparator.compare(file1, file2) < 0);
    }

}

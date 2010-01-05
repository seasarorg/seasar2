/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.ResourcesUtil.FileSystemResources;
import org.seasar.framework.util.ResourcesUtil.JarFileResources;
import org.seasar.framework.util.ResourcesUtil.Resources;
import org.seasar.framework.util.xxx.DummyTest;

/**
 * @author koichik
 */
public class ResourcesUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testFromClass_FileSystem() throws Exception {
        Resources resources = ResourcesUtil.getResourcesType(DummyTest.class);
        assertNotNull(resources);
        assertTrue(resources instanceof FileSystemResources);

        assertTrue(resources.isExistClass(DummyTest.class.getName()));
        assertFalse(resources.isExistClass(TestCase.class.getName()));

        final Set set = new HashSet();
        resources.forEach(new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                set.add(ClassUtil.concatName(packageName, shortClassName));
            }
        });
        assertTrue(set.size() > 0);
        assertTrue(set.contains(DummyTest.class.getName()));
        assertTrue(set.contains(ResourcesUtilTest.class.getName()));
        assertFalse(set.contains(TestCase.class.getName()));
    }

    /**
     * @throws Exception
     */
    public void testFromClass_JarFile() throws Exception {
        Resources resources = ResourcesUtil.getResourcesType(TestCase.class);
        assertNotNull(resources);
        assertTrue(resources instanceof JarFileResources);

        assertTrue(resources.isExistClass(TestCase.class.getName()));
        assertFalse(resources.isExistClass(DummyTest.class.getName()));

        final Set set = new HashSet();
        resources.forEach(new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                set.add(ClassUtil.concatName(packageName, shortClassName));
            }
        });
        assertTrue(set.size() > 0);
        assertTrue(set.contains(TestCase.class.getName()));
        assertFalse(set.contains(DummyTest.class.getName()));
    }

    /**
     * @throws Exception
     */
    public void testFromDir_FileSystem() throws Exception {
        Resources resources = ResourcesUtil
                .getResourcesType("org/seasar/framework/util/xxx");
        assertNotNull(resources);
        assertTrue(resources instanceof FileSystemResources);

        final List list = new ArrayList();
        resources.forEach(new ResourceHandler() {
            public void processResource(String path, InputStream is) {
                list.add(path);
            }
        });
        assertEquals(1, list.size());
        assertTrue(((String) list.get(0)).endsWith("DummyTest.class"));
    }

    /**
     * @throws Exception
     */
    public void testFromDir_JarFile() throws Exception {
        Resources resources = ResourcesUtil.getResourcesType("junit/textui");
        assertNotNull(resources);
        assertTrue(resources instanceof JarFileResources);

        final List list = new ArrayList();
        resources.forEach(new ResourceHandler() {
            public void processResource(String path, InputStream is) {
                list.add(path);
            }
        });
        assertEquals(2, list.size());
        assertEquals("junit/textui/ResultPrinter.class", list.get(0));
        assertEquals("junit/textui/TestRunner.class", list.get(1));
    }

    /**
     * @throws Exception
     */
    public void testFromRootPackage_FileSystem() throws Exception {
        Resources[] resourcesArray = ResourcesUtil.getResourcesTypes("org.seasar.framework.util.xxx");
        assertNotNull(resourcesArray);
        assertEquals(1, resourcesArray.length);

        Resources resources = resourcesArray[0];
        assertTrue(resources instanceof FileSystemResources);

        assertTrue(resources.isExistClass("DummyTest"));
        assertFalse(resources.isExistClass("TestCase"));

        final Set set = new HashSet();
        resources.forEach(new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                set.add(ClassUtil.concatName(packageName, shortClassName));
            }
        });
        assertTrue(set.size() > 0);
        assertTrue(set.contains(DummyTest.class.getName()));
        assertFalse(set.contains(ResourcesUtilTest.class.getName()));
        assertFalse(set.contains(TestCase.class.getName()));
    }

    /**
     * @throws Exception
     */
    public void testFromRootPackage_JarFile() throws Exception {
        Resources[] resourcesArray = ResourcesUtil.getResourcesTypes("junit.textui");
        assertNotNull(resourcesArray);
        assertEquals(1, resourcesArray.length);

        Resources resources = resourcesArray[0];
        assertTrue(resources instanceof JarFileResources);

        assertTrue(resources.isExistClass("TestRunner"));
        assertFalse(resources.isExistClass("DummyTest"));

        final Set set = new HashSet();
        resources.forEach(new ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                set.add(ClassUtil.concatName(packageName, shortClassName));
            }
        });
        assertEquals(2, set.size());
        assertTrue(set.contains(ResultPrinter.class.getName()));
        assertTrue(set.contains(TestRunner.class.getName()));
        assertFalse(set
                .contains(junit.extensions.TestDecorator.class.getName()));
    }

}

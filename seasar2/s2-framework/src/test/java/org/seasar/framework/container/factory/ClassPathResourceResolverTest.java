/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.seasar.framework.env.Env;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author skirnir
 */
public class ClassPathResourceResolverTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/ClassPathResourceResolverTest.dicon";

    private static final String PATH2 = "org/seasar/framework/container/factory/aaa.dicon";

    private static final String ENV_PATH = "org/seasar/framework/container/factory/env.txt";

    private ClassPathResourceResolver target = new ClassPathResourceResolver();

    protected void tearDown() {
        Env.initialize();
    }

    /**
     * @throws Exception
     */
    public void testToURL1() throws Exception {

        URL actual = target.toURL(PATH);
        assertNotNull(actual);
        assertEquals(getClass().getClassLoader().getResource(PATH), actual);
    }

    /**
     * @throws Exception
     */
    public void testToURL2() throws Exception {

        final URL expected = new File(ResourceUtil.getBuildDir(getClass()),
                PATH).toURI().toURL();

        URL actual = target.toURL(expected.toExternalForm());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception
     */
    public void testGetURL() throws Exception {
        URL url = target.getURL(PATH2);
        System.out.println(url.getPath());
        assertTrue(url.getPath().endsWith("aaa.dicon"));
        Env.setFilePath(ENV_PATH);
        url = target.getURL(PATH2);
        System.out.println(url.getPath());
        assertTrue(url.getPath().endsWith("aaa_ut.dicon"));
    }
}

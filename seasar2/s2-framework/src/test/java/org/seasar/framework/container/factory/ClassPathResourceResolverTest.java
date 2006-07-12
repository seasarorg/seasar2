/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.framework.util.ResourceUtil;

import junit.framework.TestCase;

/**
 * @author skirnir
 */
public class ClassPathResourceResolverTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/ClassPathResourceResolverTest.dicon";

    private ClassPathResourceResolver target = new ClassPathResourceResolver();

    public void testToURL1() throws Exception {

        URL actual = target.toURL(PATH);
        assertNotNull(actual);
        assertEquals(getClass().getClassLoader().getResource(PATH), actual);
    }

    public void testToURL2() throws Exception {

        final URL expected = new File(ResourceUtil.getBuildDir(getClass()),
                PATH).toURI().toURL();

        URL actual = target.toURL(expected.toExternalForm());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}

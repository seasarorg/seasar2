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
package org.seasar.framework.message;

import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAll() throws Exception {
        Properties parentProp = new Properties();
        parentProp.setProperty("b", "B");
        MessageResourceBundleImpl parent = new MessageResourceBundleImpl(parentProp);

        Properties prop = new Properties();
        prop.setProperty("a", "A");
        MessageResourceBundleImpl bundle = new MessageResourceBundleImpl(prop, parent);

        assertEquals("A", bundle.get("a"));
        assertEquals("B", bundle.get("b"));
        assertEquals(null, bundle.get("c"));
        assertEquals(null, bundle.get(null));
    }
}

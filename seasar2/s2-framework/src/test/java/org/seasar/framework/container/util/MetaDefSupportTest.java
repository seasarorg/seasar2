/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import junit.framework.TestCase;

import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.MetaDefImpl;

/**
 * @author higa
 * 
 */
public class MetaDefSupportTest extends TestCase {

    public void testGetMetaDefs() throws Exception {
        MetaDefSupport support = new MetaDefSupport();
        support.addMetaDef(new MetaDefImpl("aaa"));
        support.addMetaDef(new MetaDefImpl("bbb"));
        support.addMetaDef(new MetaDefImpl("aaa"));
        MetaDef[] metaDefs = support.getMetaDefs("aaa");
        assertEquals("1", 2, metaDefs.length);
    }
}
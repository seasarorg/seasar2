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
package org.seasar.framework.convention.impl;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class NamingConventionImplTest extends TestCase {

    public void testFromClassNameToComponentName() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        assertEquals("abcLogic", convention
                .fromClassNameToComponentName("aaa.logic.impl.AbcLogicImpl"));
        assertEquals("abcInterceptor", convention
                .fromClassNameToComponentName("aaa.interceptor.AbcInterceptor"));
        assertEquals("add_addPage", convention
                .fromClassNameToComponentName("aaa.web.add.AddPage"));
        assertEquals("add_xxx_addPage", convention
                .fromClassNameToComponentName("aaa.web.add.xxx.AddPage"));
        assertEquals("add_addDxo", convention
                .fromClassNameToComponentName("aaa.web.add.impl.AddDxoImpl"));
    }
    
    public void testFromPathToPageName() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        assertEquals("hogePage", convention.fromPathToPageName("/view/hoge.html"));
        assertEquals("hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/foo.html"));
        assertEquals("aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/foo.html"));
    }

    public void testFromPathToActionName() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        assertEquals("hogeAction", convention.fromPathToActionName("/view/hoge.html"));
        assertEquals("hoge_fooAction", convention
                .fromPathToActionName("/view/hoge/foo.html"));
        assertEquals("aaa_hoge_fooAction", convention
                .fromPathToActionName("/view/aaa/hoge/foo.html"));
    }
    
    public void testFromPageNameToPath() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        assertEquals("/view/hoge.html", convention.fromPageNameToPath("hogePage"));
        assertEquals("/view/aaa/hoge.html", convention.fromPageNameToPath("aaa_hogePage"));
        assertEquals("/view/aaa/bbb/hoge.html", convention.fromPageNameToPath("aaa_bbb_hogePage"));
    }
    
    public void testFromActionNameToPath() throws Exception {
        NamingConventionImpl convention = new NamingConventionImpl();
        assertEquals("/view/hoge.html", convention.fromActionNameToPath("hogeAction"));
        assertEquals("/view/aaa/hoge.html", convention.fromActionNameToPath("aaa_hogeAction"));
        assertEquals("/view/aaa/bbb/hoge.html", convention.fromActionNameToPath("aaa_bbb_hogeAction"));
    }
}
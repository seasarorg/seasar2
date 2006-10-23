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

import junit.framework.Test;
import junit.framework.TestCase;

import org.seasar.framework.convention.impl.NamingConventionImpl.ExistChecker;
import org.seasar.framework.convention.impl.NamingConventionImpl.FileExistChecker;
import org.seasar.framework.convention.impl.dao.AaaDao;
import org.seasar.framework.convention.impl.dao.BbbDao;
import org.seasar.framework.convention.impl.dao.impl.BbbDaoImpl;
import org.seasar.framework.convention.impl.dao.xxx.CccDao;
import org.seasar.framework.convention.impl.web.add.DddPage;
import org.seasar.framework.convention.impl.web.add.xxx.AddXxxPage;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class NamingConventionImplTest extends TestCase {

    private NamingConventionImpl convention;

    private String rootPackageName;

    protected void setUp() {
        convention = new NamingConventionImpl();
        rootPackageName = ClassUtil.getPackageName(getClass());
        convention.addRootPackageName(rootPackageName);
    }

    public void testAddAndGetRootPackageName() throws Exception {
        ExistChecker[] checkers = convention
                .getExistCheckerArray(rootPackageName);
        assertNotNull(checkers);
        assertEquals(FileExistChecker.class, checkers[0].getClass());
    }

    public void testFromSuffixToPackageName() throws Exception {
        assertEquals("logic", convention.fromSuffixToPackageName("Logic"));
    }

    public void testGetImplementationPackageName() throws Exception {
        assertEquals("impl", convention.getImplementationPackageName());
    }

    public void testGetDxoPackageName() throws Exception {
        assertEquals("dxo", convention.getDxoPackageName());
    }

    public void testGetLogicPackageName() throws Exception {
        assertEquals("logic", convention.getLogicPackageName());
    }

    public void testGetDaoPackageName() throws Exception {
        assertEquals("dao", convention.getDaoPackageName());
    }

    public void testGetDtoPackageName() throws Exception {
        assertEquals("dto", convention.getDtoPackageName());
    }

    public void testGetServicePackageName() throws Exception {
        assertEquals("service", convention.getServicePackageName());
    }

    public void testGetInterceptorPackageName() throws Exception {
        assertEquals("interceptor", convention.getInterceptorPackageName());
    }

    public void testGetValidatorPackageName() throws Exception {
        assertEquals("validator", convention.getValidatorPackageName());
    }

    public void testGetConverterPackageName() throws Exception {
        assertEquals("converter", convention.getConverterPackageName());
    }

    public void testGetHelperPackageName() throws Exception {
        assertEquals("helper", convention.getHelperPackageName());
    }

    public void testGetConnectorPackageName() throws Exception {
        assertEquals("connector", convention.getConnectorPackageName());
    }

    public void testFromClassNameToComponentName() throws Exception {
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
        assertEquals("bbb_cccDao", convention
                .fromClassNameToComponentName("aaa.dao.bbb.CccDao"));
    }

    public void testFromClassNameToComponentName_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.fromClassNameToComponentName("aaa.web.add.AddPage");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("fromClassNameToComponentName:" + num + "=" + time);
    }

    public void testFromComponentNameToSuffix() throws Exception {
        assertEquals("Logic", convention.fromComponentNameToSuffix("aaaLogic"));
        assertNull(convention.fromComponentNameToSuffix("aaa"));
    }

    public void testFromComponentNameToPartOfClassName() throws Exception {
        assertEquals("AaaLogic", convention
                .fromComponentNameToPartOfClassName("aaaLogic"));
        assertEquals("xxx.AaaLogic", convention
                .fromComponentNameToPartOfClassName("xxx_aaaLogic"));
        assertEquals("xxx.yyy.AaaLogic", convention
                .fromComponentNameToPartOfClassName("xxx_yyy_aaaLogic"));
    }

    public void testToImplementationClassName() throws Exception {
        assertEquals("impl.AaaLogicImpl", convention
                .toImplementationClassName("AaaLogic"));
        assertEquals("abc.logic.impl.AaaLogicImpl", convention
                .toImplementationClassName("abc.logic.AaaLogic"));
    }

    public void testToInterfaceClassName() throws Exception {
        assertEquals("aaa.dao.BbbDao", convention
                .toInterfaceClassName("aaa.dao.BbbDao"));
        assertEquals("aaa.dao.BbbDao", convention
                .toInterfaceClassName("aaa.dao.impl.BbbDaoImpl"));
    }

    public void testToCompleteClass() throws Exception {
        assertEquals(AaaDao.class, convention.toCompleteClass(AaaDao.class));
        assertEquals(BbbDaoImpl.class, convention.toCompleteClass(BbbDao.class));
        assertEquals(DddPage.class, convention.toCompleteClass(DddPage.class));
    }

    public void testFromComponentNameToClass() throws Exception {
        assertEquals(AaaDao.class, convention
                .fromComponentNameToClass("aaaDao"));
        assertEquals(BbbDaoImpl.class, convention
                .fromComponentNameToClass("bbbDao"));
        assertEquals(CccDao.class, convention
                .fromComponentNameToClass("xxx_cccDao"));
        assertEquals(DddPage.class, convention
                .fromComponentNameToClass("add_dddPage"));
        assertEquals(AddXxxPage.class, convention
                .fromComponentNameToClass("add_xxx_addXxxPage"));
    }

    public void testFromComponentNameToClass_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.fromComponentNameToClass("bbbDao");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("fromComponentNameToClass:" + num + "=" + time);
    }

    public void testFindClass() throws Exception {
        assertEquals(AaaDao.class, convention.findClass(rootPackageName, "dao",
                "AaaDao"));
        assertEquals(BbbDaoImpl.class, convention.findClass(rootPackageName,
                "dao", "BbbDao"));

        convention.dispose();
        assertEquals(AaaDao.class, convention.findClass(rootPackageName, "dao",
                "AaaDao"));
    }

    public void testFindClass_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.findClass(rootPackageName, "dao", "BbbDao");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("findClass:" + num + "=" + time);
    }

    public void testFromClassNameToShortComponentName() throws Exception {
        assertEquals(
                "abcLogic",
                convention
                        .fromClassNameToShortComponentName("aaa.logic.impl.AbcLogicImpl"));
        assertEquals(
                "abcInterceptor",
                convention
                        .fromClassNameToShortComponentName("aaa.interceptor.AbcInterceptor"));
    }

    public void testIsTargetClassName() throws Exception {
        assertTrue(convention
                .isTargetClassName(rootPackageName + ".dao.AaaDao"));
        assertFalse(convention.isTargetClassName("hoge.dao.AaaDao"));
    }

    public void testFromPathToPageName() throws Exception {
        assertEquals("hogePage", convention
                .fromPathToPageName("/view/hoge.html"));
        assertEquals("hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/foo.html"));
        assertEquals("aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/foo.html"));
    }

    public void testFromPathToActionName() throws Exception {
        assertEquals("hogeAction", convention
                .fromPathToActionName("/view/hoge.html"));
        assertEquals("hoge_fooAction", convention
                .fromPathToActionName("/view/hoge/foo.html"));
        assertEquals("aaa_hoge_fooAction", convention
                .fromPathToActionName("/view/aaa/hoge/foo.html"));
    }

    public void testFromPageNameToPath() throws Exception {
        assertEquals("/view/hoge.html", convention
                .fromPageNameToPath("hogePage"));
        assertEquals("/view/aaa/hoge.html", convention
                .fromPageNameToPath("aaa_hogePage"));
        assertEquals("/view/aaa/bbb/hoge.html", convention
                .fromPageNameToPath("aaa_bbb_hogePage"));
    }

    public void testFromActionNameToPath() throws Exception {
        assertEquals("/view/hoge.html", convention
                .fromActionNameToPath("hogeAction"));
        assertEquals("/view/aaa/hoge.html", convention
                .fromActionNameToPath("aaa_hogeAction"));
        assertEquals("/view/aaa/bbb/hoge.html", convention
                .fromActionNameToPath("aaa_bbb_hogeAction"));
    }

    public void testFromActionNameToPageName() throws Exception {
        assertEquals("hogePage", convention
                .fromActionNameToPageName("hogeAction"));
        assertEquals("aaa_hogePage", convention
                .fromActionNameToPageName("aaa_hogeAction"));
        assertEquals("aaa_bbb_hogePage", convention
                .fromActionNameToPageName("aaa_bbb_hogeAction"));
    }

    public void testIsExist() throws Exception {
        assertEquals(AaaDao.class, convention.findClass(rootPackageName, "dao",
                "AaaDao"));
        assertEquals(BbbDaoImpl.class, convention.findClass(rootPackageName,
                "dao", "BbbDao"));
        assertNull(convention.findClass(rootPackageName, "dao", "xxxDao"));
    }

    public void testIsExist_jar() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.addRootPackageName("junit.framework");
        assertEquals(Test.class, nc.findClass("junit.framework", "", "Test"));
        assertNull(nc.findClass("junit.framework", "", "xxx"));
    }

    public void testIsValidViewRootPath() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/view");
        nc.setViewExtension(".html");
        assertTrue(nc.isValidViewRootPath("/view/hoge.html"));
    }

    public void testIsValidViewRootPath_viewExtIsNotSuitable() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/view");
        nc.setViewExtension(".jsp");
        assertFalse(nc.isValidViewRootPath("/view/hoge.html"));
    }

    public void testIsValidViewRootPath_viewRootPathIsNotSuitable()
            throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/pages");
        nc.setViewExtension(".html");
        assertFalse(nc.isValidViewRootPath("/view/hoge.html"));
    }

}

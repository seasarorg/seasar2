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
package org.seasar.framework.convention.impl;

import junit.framework.Test;
import junit.framework.TestCase;

import org.seasar.framework.convention.impl.dao.AaaDao;
import org.seasar.framework.convention.impl.dao.BbbDao;
import org.seasar.framework.convention.impl.dao.impl.BbbDaoImpl;
import org.seasar.framework.convention.impl.dao.impl.Ddd1DaoImpl;
import org.seasar.framework.convention.impl.dao.impl.Ddd2DaoImpl;
import org.seasar.framework.convention.impl.dao.xxx.CccDao;
import org.seasar.framework.convention.impl.web.add.DddPage;
import org.seasar.framework.convention.impl.web.add.xxx.AddXxxPage;
import org.seasar.framework.convention.impl.web.web.foo.FffPage;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourcesUtil.FileSystemResources;
import org.seasar.framework.util.ResourcesUtil.Resources;

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
        convention.addIgnorePackageName(rootPackageName + ".web.ignore");
    }

    /**
     * @throws Exception
     */
    public void testAddAndGetRootPackageName() throws Exception {
        Resources[] checkers = convention
                .getExistCheckerArray(rootPackageName);
        assertNotNull(checkers);
        assertEquals(FileSystemResources.class, checkers[0].getClass());
    }

    /**
     * @throws Exception
     */
    public void testAddAndGetIgnorePackageName() throws Exception {
        String[] ignorePackageNames = convention.getIgnorePackageNames();
        ignorePackageNames = convention.getIgnorePackageNames();
        assertEquals(1, ignorePackageNames.length);
        assertEquals("org.seasar.framework.convention.impl.web.ignore",
                ignorePackageNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testFromSuffixToPackageName() throws Exception {
        assertEquals("logic", convention.fromSuffixToPackageName("Logic"));
        assertEquals("directorydao", convention
                .fromSuffixToPackageName("DirectoryDao"));
    }

    /**
     * @throws Exception
     */
    public void testGetImplementationPackageName() throws Exception {
        assertEquals("impl", convention.getImplementationPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetDxoPackageName() throws Exception {
        assertEquals("dxo", convention.getDxoPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetLogicPackageName() throws Exception {
        assertEquals("logic", convention.getLogicPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetDaoPackageName() throws Exception {
        assertEquals("dao", convention.getDaoPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetDtoPackageName() throws Exception {
        assertEquals("dto", convention.getDtoPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetServicePackageName() throws Exception {
        assertEquals("service", convention.getServicePackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetInterceptorPackageName() throws Exception {
        assertEquals("interceptor", convention.getInterceptorPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetValidatorPackageName() throws Exception {
        assertEquals("validator", convention.getValidatorPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetConverterPackageName() throws Exception {
        assertEquals("converter", convention.getConverterPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetHelperPackageName() throws Exception {
        assertEquals("helper", convention.getHelperPackageName());
    }

    /**
     * @throws Exception
     */
    public void testGetConnectorPackageName() throws Exception {
        assertEquals("connector", convention.getConnectorPackageName());
    }

    /**
     * @throws Exception
     */
    public void testFromClassNameToComponentName() throws Exception {
        assertEquals(
                "abcLogic",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.logic.impl.AbcLogicImpl"));
        assertEquals(
                "abcInterceptor",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.interceptor.AbcInterceptor"));
        assertEquals(
                "add_addPage",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.web.add.AddPage"));
        assertEquals(
                "add_xxx_addPage",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.web.add.xxx.AddPage"));
        assertEquals(
                "add_addDxo",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.web.add.impl.AddDxoImpl"));
        assertEquals(
                "bbb_cccDao",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.dao.bbb.CccDao"));

        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(ClassUtil.getPackageName(getClass())
                + ".web");
        assertEquals(
                "foo_fffPage",
                convention
                        .fromClassNameToComponentName("org.seasar.framework.convention.impl.web.web.foo.FffPage"));
    }

    /**
     * @throws Exception
     */
    public void testFromClassNameToComponentName_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.fromClassNameToComponentName("aaa.web.add.AddPage");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("fromClassNameToComponentName:" + num + "=" + time);
    }

    /**
     * @throws Exception
     */
    public void testFromComponentNameToSuffix() throws Exception {
        assertEquals("Logic", convention.fromComponentNameToSuffix("aaaLogic"));
        assertNull(convention.fromComponentNameToSuffix("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testFromComponentNameToPartOfClassName() throws Exception {
        assertEquals("AaaLogic", convention
                .fromComponentNameToPartOfClassName("aaaLogic"));
        assertEquals("xxx.AaaLogic", convention
                .fromComponentNameToPartOfClassName("xxx_aaaLogic"));
        assertEquals("xxx.yyy.AaaLogic", convention
                .fromComponentNameToPartOfClassName("xxx_yyy_aaaLogic"));
    }

    /**
     * @throws Exception
     */
    public void testToImplementationClassName() throws Exception {
        convention.addInterfaceToImplementationClassName("BbbLogic",
                "mock.BbbLogicMock");
        convention.addInterfaceToImplementationClassName("abc.logic.CccLogic",
                "abc.logic.mock.CccLogicMock");

        assertEquals("impl.AaaLogicImpl", convention
                .toImplementationClassName("AaaLogic"));
        assertEquals("abc.logic.impl.AaaLogicImpl", convention
                .toImplementationClassName("abc.logic.AaaLogic"));

        assertEquals("mock.BbbLogicMock", convention
                .toImplementationClassName("BbbLogic"));
        assertEquals("abc.logic.mock.CccLogicMock", convention
                .toImplementationClassName("abc.logic.CccLogic"));
    }

    /**
     * @throws Exception
     */
    public void testToInterfaceClassName() throws Exception {
        convention.addInterfaceToImplementationClassName("AaaDao",
                "mock.AaaDaoMock");
        convention.addInterfaceToImplementationClassName("abc.dao.CccDao",
                "abc.dao.mock.CccDaoMock");

        assertEquals("aaa.dao.BbbDao", convention
                .toInterfaceClassName("aaa.dao.BbbDao"));
        assertEquals("aaa.dao.BbbDao", convention
                .toInterfaceClassName("aaa.dao.impl.BbbDaoImpl"));

        assertEquals("AaaDao", convention
                .toInterfaceClassName("mock.AaaDaoMock"));
        assertEquals("abc.dao.CccDao", convention
                .toInterfaceClassName("abc.dao.mock.CccDaoMock"));
    }

    /**
     * @throws Exception
     */
    public void testToCompleteClass() throws Exception {
        assertEquals(AaaDao.class, convention.toCompleteClass(AaaDao.class));
        assertEquals(BbbDaoImpl.class, convention.toCompleteClass(BbbDao.class));
        assertEquals(DddPage.class, convention.toCompleteClass(DddPage.class));
    }

    /**
     * @throws Exception
     */
    public void testFromComponentNameToClass() throws Exception {
        assertEquals(AaaDao.class, convention
                .fromComponentNameToClass("aaaDao"));
        assertEquals(BbbDaoImpl.class, convention
                .fromComponentNameToClass("bbbDao"));
        assertEquals(CccDao.class, convention
                .fromComponentNameToClass("xxx_cccDao"));

        assertEquals(Ddd1DaoImpl.class, convention
                .fromComponentNameToClass("ddd1Dao"));
        assertEquals(Ddd2DaoImpl.class, convention
                .fromComponentNameToClass("ddd2Dao"));

        assertEquals(DddPage.class, convention
                .fromComponentNameToClass("add_dddPage"));
        assertEquals(AddXxxPage.class, convention
                .fromComponentNameToClass("add_xxx_addXxxPage"));

        NamingConventionImpl convention = new NamingConventionImpl();
        convention.addRootPackageName(ClassUtil.getPackageName(getClass())
                + ".web");
        assertEquals(FffPage.class, convention
                .fromComponentNameToClass("foo_fffPage"));
    }

    /**
     * @throws Exception
     */
    public void testFromComponentNameToClass_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.fromComponentNameToClass("bbbDao");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("fromComponentNameToClass:" + num + "=" + time);
    }

    /**
     * @throws Exception
     */
    public void testFindClass() throws Exception {
        assertEquals(AaaDao.class, convention.findClass(rootPackageName, "dao",
                "AaaDao"));
        assertEquals(BbbDaoImpl.class, convention.findClass(rootPackageName,
                "dao", "BbbDao"));
        assertEquals(DddPage.class, convention.findClass(rootPackageName,
                "web.add", "DddPage"));
        assertNull(convention.findClass(rootPackageName, "web.ignore",
                "EeePage"));

        convention.dispose();
        assertEquals(AaaDao.class, convention.findClass(rootPackageName, "dao",
                "AaaDao"));
    }

    /**
     * @throws Exception
     */
    public void testFindClass_performance() throws Exception {
        int num = 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; ++i) {
            convention.findClass(rootPackageName, "dao", "BbbDao");
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("findClass:" + num + "=" + time);
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testIsTargetClassName() throws Exception {
        assertTrue(convention
                .isTargetClassName(rootPackageName + ".dao.AaaDao"));
        assertFalse(convention.isTargetClassName(rootPackageName
                + ".web.ignore.EeePage"));
        assertFalse(convention.isTargetClassName("hoge.dao.AaaDao"));

        assertTrue(convention.isTargetClassName(
                rootPackageName + ".dao.AaaDao", "Dao"));
        assertFalse(convention.isTargetClassName(rootPackageName
                + ".dao.AaaDao", "Service"));
        assertFalse(convention.isTargetClassName("foo.bar.dao.AaaDao", "Dao"));
    }

    /**
     * @throws Exception
     */
    public void testIsIgnoreClassName() throws Exception {
        assertTrue(convention.isIgnoreClassName(rootPackageName
                + ".web.ignore.EeePage"));
        assertFalse(convention.isIgnoreClassName(rootPackageName
                + ".web.add.DddPage"));
    }

    /**
     * @throws Exception
     */
    public void testFromPathToPageName() throws Exception {
        assertEquals("hogePage", convention
                .fromPathToPageName("/view/hoge.html"));
        assertEquals("hogePage", convention
                .fromPathToPageName("/view/Hoge.html"));
        assertEquals("hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/foo.html"));
        assertEquals("hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/Foo.html"));
        assertEquals("aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/foo.html"));
        assertEquals("aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/Foo.html"));
    }

    /**
     * viewRootPathに空文字が設定された場合のTest
     * 
     * @throws Exception
     */
    public void testFromPathToPageName2() throws Exception {
        convention.setViewRootPath("");
        assertEquals("view_hogePage", convention
                .fromPathToPageName("/view/hoge.html"));
        assertEquals("view_hogePage", convention
                .fromPathToPageName("/view/Hoge.html"));
        assertEquals("view_hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/foo.html"));
        assertEquals("view_hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/Foo.html"));
        assertEquals("view_aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/foo.html"));
        assertEquals("view_aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/Foo.html"));
    }

    /**
     * viewRootPathに"/"が設定された場合のTest
     * 
     * @throws Exception
     */
    public void testFromPathToPageName3() throws Exception {
        convention.setViewRootPath("/");
        assertEquals("view_hogePage", convention
                .fromPathToPageName("/view/hoge.html"));
        assertEquals("view_hogePage", convention
                .fromPathToPageName("/view/Hoge.html"));
        assertEquals("view_hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/foo.html"));
        assertEquals("view_hoge_fooPage", convention
                .fromPathToPageName("/view/hoge/Foo.html"));
        assertEquals("view_aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/foo.html"));
        assertEquals("view_aaa_hoge_fooPage", convention
                .fromPathToPageName("/view/aaa/hoge/Foo.html"));
    }

    /**
     * @throws Exception
     */
    public void testFromPathToActionName() throws Exception {
        assertEquals("hogeAction", convention
                .fromPathToActionName("/view/hoge.html"));
        assertEquals("hoge_fooAction", convention
                .fromPathToActionName("/view/hoge/foo.html"));
        assertEquals("aaa_hoge_fooAction", convention
                .fromPathToActionName("/view/aaa/hoge/foo.html"));
    }

    /**
     * @throws Exception
     */
    public void testFromPageNameToPath() throws Exception {
        assertEquals("/view/hoge.html", convention
                .fromPageNameToPath("hogePage"));
        assertEquals("/view/aaa/hoge.html", convention
                .fromPageNameToPath("aaa_hogePage"));
        assertEquals("/view/aaa/bbb/hoge.html", convention
                .fromPageNameToPath("aaa_bbb_hogePage"));
    }

    /**
     * @throws Exception
     */
    public void testFromPageNameToPath2() throws Exception {
        convention.setViewRootPath("/");
        assertEquals("/hoge.html", convention.fromPageNameToPath("hogePage"));
        assertEquals("/aaa/hoge.html", convention
                .fromPageNameToPath("aaa_hogePage"));
        assertEquals("/aaa/bbb/hoge.html", convention
                .fromPageNameToPath("aaa_bbb_hogePage"));
    }

    /**
     * @throws Exception
     */
    public void testFromPageClassToPath() throws Exception {
        convention.setViewRootPath("/view");
        assertEquals("/view/add/ddd.html", convention
                .fromPageClassToPath(DddPage.class));
        convention.setViewRootPath("/");
        assertEquals("/add/ddd.html", convention
                .fromPageClassToPath(DddPage.class));
    }

    /**
     * @throws Exception
     */
    public void testFromActionNameToPath() throws Exception {
        assertEquals("/view/hoge.html", convention
                .fromActionNameToPath("hogeAction"));
        assertEquals("/view/aaa/hoge.html", convention
                .fromActionNameToPath("aaa_hogeAction"));
        assertEquals("/view/aaa/bbb/hoge.html", convention
                .fromActionNameToPath("aaa_bbb_hogeAction"));
    }

    /**
     * @throws Exception
     */
    public void testFromActionNameToPath2() throws Exception {
        convention.setViewRootPath("/");
        assertEquals("/hoge.html", convention
                .fromActionNameToPath("hogeAction"));
        assertEquals("/aaa/hoge.html", convention
                .fromActionNameToPath("aaa_hogeAction"));
        assertEquals("/aaa/bbb/hoge.html", convention
                .fromActionNameToPath("aaa_bbb_hogeAction"));
    }

    /**
     * @throws Exception
     */
    public void testFromActionNameToPageName() throws Exception {
        assertEquals("hogePage", convention
                .fromActionNameToPageName("hogeAction"));
        assertEquals("aaa_hogePage", convention
                .fromActionNameToPageName("aaa_hogeAction"));
        assertEquals("aaa_bbb_hogePage", convention
                .fromActionNameToPageName("aaa_bbb_hogeAction"));
    }

    /**
     * @throws Exception
     */
    public void testIsExist() throws Exception {
        assertTrue(convention.isExist(rootPackageName, "dao.AaaDao"));
        assertTrue(convention.isExist(rootPackageName, "dao.BbbDao"));
        assertTrue(convention.isExist(rootPackageName, "web.add.DddPage"));
        assertTrue(convention.isExist(rootPackageName, "web.ignore.EeePage"));
        assertFalse(convention.isExist(rootPackageName, "dao.xxxDao"));
    }

    /**
     * @throws Exception
     */
    public void testIsExist_jar() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.addRootPackageName("junit.framework");
        assertEquals(Test.class, nc.findClass("junit.framework", "", "Test"));
        assertNull(nc.findClass("junit.framework", "", "xxx"));
    }

    /**
     * @throws Exception
     */
    public void testIsValidViewRootPath() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/view");
        nc.setViewExtension(".html");
        assertTrue(nc.isValidViewRootPath("/view/hoge.html"));
    }

    /**
     * @throws Exception
     */
    public void testIsValidViewRootPath_viewExtIsNotSuitable() throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/view");
        nc.setViewExtension(".jsp");
        assertFalse(nc.isValidViewRootPath("/view/hoge.html"));
    }

    /**
     * @throws Exception
     */
    public void testIsValidViewRootPath_viewRootPathIsNotSuitable()
            throws Exception {
        NamingConventionImpl nc = new NamingConventionImpl();
        nc.setViewRootPath("/pages");
        nc.setViewExtension(".html");
        assertFalse(nc.isValidViewRootPath("/view/hoge.html"));
    }

}

/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import java.net.URL;
import java.util.Set;

import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.jpa.util.ChildFirstClassLoader;
import org.seasar.framework.jpa.util.ClassLoaderEvent;
import org.seasar.framework.jpa.util.ClassLoaderListener;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.unit.UnitClassLoader;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link PersistenceClassTransformerImpl}のテストクラスです。
 * 
 * @author taedium
 */
public class PersistenceClassTransformerImplTest extends S2TigerTestCase {

    String hoge = getClass().getName() + "$Hoge";

    String foo = getClass().getName() + "$Foo";

    String bar = getClass().getName() + "$Bar";

    String aaa = "org.seasar.framework.jpa.Aaa";

    ChildFirstClassLoader loader;

    Set<String> names = CollectionsUtil.newHashSet();

    URL rootUrl;

    URL jarFileUrl;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loader = new ChildFirstClassLoader();
        loader.addClassLoaderListener(new ClassLoaderListener() {

            public void classFinded(ClassLoaderEvent event) {
                names.add(event.getClassName());
            }
        });

        rootUrl = URLUtil.toFile(loader.getResource("s2junit4.dicon"))
                .getParentFile().toURL();

        jarFileUrl = URLUtil.create(rootUrl,
                "org/seasar/framework/jpa/impl/aaa.jar");
    }

    /**
     * {@link PersistenceClassTransformerImpl#loadPersistenceClasses(PersistenceUnitInfo, ClassLoader)}
     * をテストします。
     * 
     * @throws Exception
     */
    public void testLoadPersistenceClasses() throws Exception {
        PersistenceUnitInfoImpl unitInfo = new PersistenceUnitInfoImpl();
        unitInfo.setPersistenceUnitRootUrl(rootUrl);
        unitInfo.addManagedClassNames(hoge);
        unitInfo.addManagedClassNames(foo);
        unitInfo.addJarFileUrls(jarFileUrl);

        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();
        transformer.loadPersistenceClasses(unitInfo, loader);

        assertTrue(names.contains(hoge)); // listed class
        assertTrue(names.contains(foo)); // listed class
        assertTrue(names.contains(aaa)); // from jar
        assertTrue(names.contains(bar)); // unlisted class from root url
    }

    /**
     * {@link PersistenceClassTransformerImpl#loadPersistenceClasses(PersistenceUnitInfo, ClassLoader)}
     * をテストします。
     * 
     * @throws Exception
     */
    public void testLoadPersistenceClassesFromJar() throws Exception {
        PersistenceUnitInfoImpl unitInfo = new PersistenceUnitInfoImpl();
        unitInfo.setPersistenceUnitRootUrl(new URL("jar:"
                + jarFileUrl.toExternalForm() + "!/"));
        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();
        transformer.loadPersistenceClasses(unitInfo, loader);

        assertEquals(1, names.size());
        assertTrue(names.contains(aaa)); // unlisted class from root url
    }

    /**
     * {@link PersistenceClassTransformerImpl#loadPersistenceClasses(PersistenceUnitInfo, ClassLoader)}
     * をテストします。
     * 
     * @throws Exception
     */
    public void testLoadPersistenceClassesExcludeUnlistedClasses()
            throws Exception {
        PersistenceUnitInfoImpl unitInfo = new PersistenceUnitInfoImpl();
        unitInfo.setPersistenceUnitRootUrl(rootUrl);
        unitInfo.addManagedClassNames(hoge);
        unitInfo.addManagedClassNames(foo);
        unitInfo.addJarFileUrls(jarFileUrl);
        unitInfo.setExcludeUnlistedClasses(true);

        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();
        transformer.loadPersistenceClasses(unitInfo, loader);

        assertTrue(names.contains(hoge)); // listed class
        assertTrue(names.contains(foo)); // listed class
        assertTrue(names.contains(aaa)); // from jar
        assertFalse(names.contains(bar)); // unlisted class from root url
    }

    /**
     * {@link PersistenceClassTransformerImpl#getTargetClassLoader(ClassLoader)}
     * をテストします。
     * 
     * @throws Exception
     */
    public void testGetTargetClassLoader() throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        assertEquals(UnitClassLoader.class, loader.getClass());
        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();
        transformer.addIgnoreLoaderClassName(UnitClassLoader.class.getName());
        ClassLoader targetLoader = transformer.getTargetClassLoader(loader);
        assertNotSame(loader, targetLoader);
    }

    /**
     * @{link {@link #testTransformClasses()} で利用するクラスです。
     * 
     * @author taedium
     */
    public static class Foo {
    }

    /**
     * @{link {@link #testTransformClasses()} で利用するクラスです。
     * 
     * @author taedium
     */
    public static class Bar {
    }

    /**
     * @{link {@link #testTransformClasses()} で利用するクラスです。
     * 
     * @author taedium
     */
    public static class Hoge extends Foo {
    }

}

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
package org.seasar.framework.jpa.impl;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import javax.persistence.spi.ClassTransformer;

import org.easymock.IAnswer;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.unit.UnitClassLoader;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;

import static org.easymock.EasyMock.*;

/**
 * {@link PersistenceClassTransformerImpl}のテストクラスです。
 * 
 * @author taedium
 */
public class PersistenceClassTransformerImplTest extends S2TigerTestCase {

    @EasyMock
    private ClassTransformer classTransformer;

    private ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();

    /**
     * {@link PersistenceClassTransformerImpl#transformClasses(List, ClassLoader, List)}をテストします。
     * 
     * @throws Exception
     */
    public void testTransformClasses() throws Exception {
        List<ClassTransformer> transformers = Arrays.asList(classTransformer);
        String hoge = getClass().getName() + "$Hoge";
        String foo = getClass().getName() + "$Foo";
        List<String> classNames = Arrays.asList(hoge, foo);
        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();

        assertNull(ClassLoaderUtil.findLoadedClass(classLoader, hoge));
        assertNull(ClassLoaderUtil.findLoadedClass(classLoader, foo));

        transformer.transformClasses(transformers, classLoader, classNames);
        assertNotNull(ClassLoaderUtil.findLoadedClass(classLoader, hoge));
        assertNotNull(ClassLoaderUtil.findLoadedClass(classLoader, foo));

        transformer.transformClasses(transformers, classLoader, classNames);
        assertNotNull(ClassLoaderUtil.findLoadedClass(classLoader, hoge));
        assertNotNull(ClassLoaderUtil.findLoadedClass(classLoader, foo));
    }

    /**
     * @{link {@link #testTransformClasses()}}で利用するモックの振る舞いを記録します。
     * 
     * @throws Exception
     */
    public void recordTransformClasses() throws Exception {
        expect(
                classTransformer.transform(isA(ClassLoader.class),
                        isA(String.class), (Class) isNull(),
                        (ProtectionDomain) isNull(), isA(byte[].class)))
                .andAnswer(new IAnswer<byte[]>() {

                    public byte[] answer() throws Throwable {
                        return (byte[]) getCurrentArguments()[4];
                    }

                }).times(2);
    }

    /**
     * {@link PersistenceClassTransformerImpl#transformJarFiles(List, ClassLoader, List)}をテストします。
     * 
     * @throws Exception
     */
    public void testTransformJarFiles() throws Exception {
        List<ClassTransformer> transformers = Arrays.asList(classTransformer);
        URL url = ResourceUtil.getResource("junit/framework/TestCase.class");
        File jarFile = new File(JarFileUtil.toJarFilePath(url));
        URL jarFileUrl = FileUtil.toURL(jarFile);

        PersistenceClassTransformerImpl transformer = new PersistenceClassTransformerImpl();
        transformer.transformJarFiles(transformers, classLoader, Arrays
                .asList(jarFileUrl));
    }

    /**
     * @{link {@link #testTransformJarFiles()}}で利用するモックの振る舞いを記録します。
     * 
     * @throws Exception
     */
    public void recordTransformJarFiles() throws Exception {
        expect(
                classTransformer.transform(isA(ClassLoader.class),
                        isA(String.class), (Class) isNull(),
                        (ProtectionDomain) isNull(), isA(byte[].class)))
                .andAnswer(new IAnswer<byte[]>() {

                    public byte[] answer() throws Throwable {
                        return (byte[]) getCurrentArguments()[4];
                    }

                }).atLeastOnce();
    }

    /**
     * {@link PersistenceClassTransformerImpl#getTargetClassLoader(ClassLoader)}をテストします。
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
     * @{link {@link #testTransformClasses()}}で利用するクラスです。
     * 
     * @author taedium
     */
    static class Foo {
    }

    /**
     * @{link {@link #testTransformClasses()}}で利用するクラスです。
     * 
     * @author taedium
     */
    public static class Hoge extends Foo {
    }
}

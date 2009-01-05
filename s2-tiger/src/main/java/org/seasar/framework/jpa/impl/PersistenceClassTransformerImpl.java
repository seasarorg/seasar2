/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.jpa.PersistenceClassTransformer;
import org.seasar.framework.jpa.util.ChildFirstClassLoader;
import org.seasar.framework.jpa.util.ClassLoaderEvent;
import org.seasar.framework.jpa.util.ClassLoaderListener;
import org.seasar.framework.jpa.util.ClassTransformerUtil;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

import static org.seasar.framework.util.tiger.IterableAdapter.*;

/**
 * 永続クラスをトランスフォームします。
 * 
 * @author taedium
 */
public class PersistenceClassTransformerImpl implements
        PersistenceClassTransformer {

    /** トランスフォームした永続クラスをロードする対象から除外するクラスローダのクラス名 */
    protected Set<String> ignoreLoaderClassNames = CollectionsUtil.newHashSet();

    /**
     * インスタンスを構築します。
     * 
     */
    public PersistenceClassTransformerImpl() {
    }

    /**
     * トランスフォームした永続クラスをロードする対象から除外するクラスローダのクラス名を追加します。
     * 
     * @param ignoreLoaderClassName
     *            トランスフォームした永続クラスをロードする対象から除外するクラスローダのクラス名
     */
    public void addIgnoreLoaderClassName(final String ignoreLoaderClassName) {
        ignoreLoaderClassNames.add(ignoreLoaderClassName);
    }

    public void transform(final PersistenceUnitInfo unitInfo) {
        final List<ClassTransformer> transformers = PersistenceUnitInfoImpl.class
                .cast(unitInfo).getTransformers();
        final ClassLoader classLoader = unitInfo.getClassLoader();
        final ClassLoader targetLoader = getTargetClassLoader(classLoader);
        final ChildFirstClassLoader tempLoader = new ChildFirstClassLoader(
                targetLoader);
        tempLoader.addClassLoaderListener(new ClassLoaderListener() {

            public void classFinded(final ClassLoaderEvent event) {
                final String className = event.getClassName();
                byte[] bytes = event.getBytecode();
                for (final ClassTransformer transformer : transformers) {
                    bytes = transform(transformer, targetLoader, className,
                            bytes);
                }
                ClassLoaderUtil.defineClass(targetLoader, className, bytes, 0,
                        bytes.length);
            }
        });
        loadPersistenceClasses(unitInfo, tempLoader);
    }

    public Class<?> transform(final PersistenceUnitInfo unitInfo,
            final String className, final byte[] bytecode) {
        final List<ClassTransformer> transformers = PersistenceUnitInfoImpl.class
                .cast(unitInfo).getTransformers();
        final ClassLoader classLoader = unitInfo.getClassLoader();
        final ClassLoader targetLoader = getTargetClassLoader(classLoader);
        byte[] bytes = bytecode;
        for (final ClassTransformer transformer : transformers) {
            bytes = transform(transformer, targetLoader, className, bytes);
        }
        return ClassLoaderUtil.defineClass(targetLoader, className, bytes, 0,
                bytes.length);
    }

    /**
     * 永続ユニット情報で管理されるクラスを指定のクラスローダにロードします。
     * 
     * @param unitInfo
     *            永続ユニット情報
     * @param loader
     *            クラスローダ
     */
    protected void loadPersistenceClasses(final PersistenceUnitInfo unitInfo,
            final ClassLoader loader) {
        for (final String className : unitInfo.getManagedClassNames()) {
            loadClass(loader, className);
        }
        for (final URL jarFileUrl : unitInfo.getJarFileUrls()) {
            loadClass(loader, jarFileUrl);
        }
        if (!unitInfo.excludeUnlistedClasses()) {
            final URL rootUrl = unitInfo.getPersistenceUnitRootUrl();
            if ("file".equals(rootUrl.getProtocol())) {
                loadClass(loader, URLUtil.toFile(rootUrl), null);
            } else {
                loadClass(loader, FileUtil.toURL(new File(JarFileUtil
                        .toJarFilePath(rootUrl))));
            }
        }
    }

    /**
     * クラスをロードします。
     * <p>
     * クラスが見つからない場合はクラス名をパッケージ名として解釈し、<code>package-info</code>クラスをロードします。
     * </p>
     * 
     * @param loader
     *            クラスローダ
     * @param className
     *            クラス名
     */
    protected void loadClass(final ClassLoader loader, final String className) {
        try {
            loader.loadClass(className);
        } catch (final ClassNotFoundException e) {
            ClassLoaderUtil.loadClass(loader, className + ".package-info");
        }
    }

    /**
     * Jarファイルからクラスをロードします。
     * 
     * @param loader
     *            クラスローダ
     * @param jarFileUrl
     *            JarファイルのURL
     */
    protected void loadClass(final ClassLoader loader, final URL jarFileUrl) {
        final JarFile jarFile = JarFileUtil.create(jarFileUrl.getPath());
        try {
            for (final JarEntry entry : iterable(jarFile.entries())) {
                final String entryName = entry.getName();
                if (entry.isDirectory() || !entryName.endsWith(".class")) {
                    continue;
                }
                final String className = removeExtension(entryName).replace(
                        '/', '.');
                loadClass(loader, className);
            }
        } finally {
            JarFileUtil.close(jarFile);
        }
    }

    /**
     * ファイルシステムからクラスをロードします。
     * 
     * @param loader
     *            クラスローダ
     * @param dir
     *            ディレクトリ
     * @param path
     *            クラスパスの基点から現在のディレクトリまでのパス (ピリオド区切り)
     */
    protected void loadClass(final ClassLoader loader, final File dir,
            final String path) {
        for (final File file : dir.listFiles()) {
            final String fileName = file.getName();
            if (file.isDirectory()) {
                loadClass(loader, file, ClassUtil.concatName(path, fileName));
            } else if (fileName.endsWith(".class")) {
                final String className = ClassUtil.concatName(path,
                        removeExtension(fileName));
                loadClass(loader, className);
            }
        }
    }

    /**
     * 永続クラスのバイト列をトランスフォームしたバイト列を返します。
     * 
     * @param transformer
     *            トランスフォーマ
     * @param classLoader
     *            変換されるクラスを定義するローダ
     * @param className
     *            クラス名
     * @param bytes
     *            クラスファイル形式のバイト列
     * @return 変換されたクラスファイル形式のバイト列。変換されなかった場合は引数のバイト列
     */
    protected byte[] transform(final ClassTransformer transformer,
            final ClassLoader classLoader, final String className,
            final byte[] bytes) {
        final byte[] transformed = ClassTransformerUtil.transform(transformer,
                classLoader, className.replace('.', '/'), null, null, bytes);
        return transformed == null ? bytes : transformed;
    }

    /**
     * トランスフォームした永続クラスをロードする対象のクラスローダを返します。
     * 
     * @param originLoader
     *            原点となるクラスローダ
     * @return トランスフォームした永続クラスをロードする対象のクラスローダ
     */
    protected ClassLoader getTargetClassLoader(final ClassLoader originLoader) {
        ClassLoader loader = originLoader;
        while (loader != null
                && ignoreLoaderClassNames.contains(loader.getClass().getName())) {
            loader = loader.getParent();
        }
        if (loader != null) {
            return loader;
        }
        return originLoader;
    }

    /**
     * ファイル名から拡張子を取り除いた名前を返します。
     * 
     * @param name
     *            ファイル名
     * @return ファイル名から拡張子を取り除いた名前
     */
    protected String removeExtension(final String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }

}

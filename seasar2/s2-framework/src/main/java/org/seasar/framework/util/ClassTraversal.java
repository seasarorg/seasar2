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
package org.seasar.framework.util;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * クラスを横断して処理するためのハンドラです。
 * <p>
 * このクラスを直接使うより、{@link ResourcesUtil}を使用してください。
 * </p>
 * 
 * @author koichik
 * @see ResourcesUtil
 */
public class ClassTraversal {
    private static final String CLASS_SUFFIX = ".class";

    private static final String WAR_FILE_EXTENSION = ".war";

    private static final String WEB_INF_CLASSES_PATH = "WEB-INF/classes/";

    /**
     * クラスを横断して処理するためのハンドラです。
     * 
     */
    public interface ClassHandler {
        /**
         * クラスを処理します。
         * 
         * @param packageName
         * @param shortClassName
         */
        void processClass(String packageName, String shortClassName);
    }

    /**
     * rootディレクトリ配下を処理します。
     * 
     * @param rootDir
     * @param handler
     */
    public static void forEach(final File rootDir, final ClassHandler handler) {
        forEach(rootDir, null, handler);
    }

    /**
     * ファイルシステムに含まれるクラスをトラバースします。
     * 
     * @param rootDir
     *            ルートディレクトリ
     * @param rootPackage
     *            ルートパッケージ
     * @param handler
     *            クラスを処理するハンドラ
     */
    public static void forEach(final File rootDir, final String rootPackage,
            final ClassHandler handler) {
        final File packageDir = getPackageDir(rootDir, rootPackage);
        if (packageDir.exists()) {
            traverseFileSystem(packageDir, rootPackage, handler);
        }
    }

    /**
     * Jarファイルに含まれるクラスをトラバースします。
     * <p>
     * 指定されたJarファイルが拡張子<code>.war</code>を持つ場合は、 Jarファイル内のエントリのうち、 接頭辞
     * <code>WEB-INF/classes</code>で始まるパスを持つエントリがトラバースの対象となります。
     * クラスを処理するハンドラには、接頭辞を除くエントリ名が渡されます。 例えばJarファイル内に
     * <code>/WEB-INF/classes/ccc/ddd/Eee.class</code>というクラスファイルが存在すると、 ハンドラには
     * パッケージ名<code>ccc.ddd</code>およびクラス名<code>Eee</code>が渡されます。
     * </p>
     * 
     * @param jarFile
     *            Jarファイル
     * @param handler
     *            クラスを処理するハンドラ
     */
    public static void forEach(final JarFile jarFile, final ClassHandler handler) {
        if (jarFile.getName().toLowerCase().endsWith(WAR_FILE_EXTENSION)) {
            forEach(jarFile, WEB_INF_CLASSES_PATH, handler);
        } else {
            forEach(jarFile, "", handler);
        }
    }

    /**
     * Jarファイルに含まれるクラスをトラバースします。
     * <p>
     * Jarファイル内のエントリのうち、接頭辞で始まるパスを持つエントリがトラバースの対象となります。
     * クラスを処理するハンドラには、接頭辞を除くエントリ名が渡されます。 例えば接頭辞が <code>/aaa/bbb/</code>
     * で、Jarファイル内に <code>/aaa/bbb/ccc/ddd/Eee.class</code>というクラスファイルが存在すると、
     * ハンドラには パッケージ名<code>ccc.ddd</code>およびクラス名<code>Eee</code>が渡されます。
     * </p>
     * 
     * @param jarFile
     *            Jarファイル
     * @param prefix
     *            トラバースするリソースの名前が含む接頭辞。スラッシュ('/')で終了していなければなりません。
     * @param handler
     *            クラスを処理するハンドラ
     */
    public static void forEach(final JarFile jarFile, final String prefix,
            final ClassHandler handler) {
        final int startPos = prefix.length();
        final Enumeration enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            final JarEntry entry = (JarEntry) enumeration.nextElement();
            final String entryName = entry.getName().replace('\\', '/');
            if (entryName.startsWith(prefix)
                    && entryName.endsWith(CLASS_SUFFIX)) {
                final String className = entryName.substring(startPos,
                        entryName.length() - CLASS_SUFFIX.length()).replace(
                        '/', '.');
                final int pos = className.lastIndexOf('.');
                final String packageName = (pos == -1) ? null : className
                        .substring(0, pos);
                final String shortClassName = (pos == -1) ? className
                        : className.substring(pos + 1);
                handler.processClass(packageName, shortClassName);
            }
        }
    }

    /**
     * ZIPファイル形式の入力ストリームに含まれるクラスをトラバースします。
     * 
     * @param zipInputStream
     *            ZIPファイル形式の入力ストリーム
     * @param prefix
     *            トラバースするリソースの名前が含む接頭辞。スラッシュ('/')で終了していなければなりません。
     * @param handler
     *            クラスを処理するハンドラ
     */
    public static void forEach(final ZipInputStream zipInputStream,
            final ClassHandler handler) {
        forEach(zipInputStream, "", handler);
    }

    /**
     * ZIPファイル形式の入力ストリームに含まれるクラスをトラバースします。
     * <p>
     * 入力ストリーム内のエントリのうち、接頭辞で始まるパスを持つエントリがトラバースの対象となります。
     * クラスを処理するハンドラには、接頭辞を除くエントリ名が渡されます。 例えば接頭辞が <code>/aaa/bbb/</code>
     * で、入力ストリーム内に <code>/aaa/bbb/ccc/ddd/Eee.class</code>というクラスファイルが存在すると、
     * ハンドラには パッケージ名<code>ccc.ddd</code>およびクラス名<code>Eee</code>が渡されます。
     * </p>
     * 
     * @param zipInputStream
     *            ZIPファイル形式の入力ストリーム
     * @param prefix
     *            トラバースするリソースの名前が含む接頭辞。スラッシュ('/')で終了していなければなりません。
     * @param handler
     *            クラスを処理するハンドラ
     */
    public static void forEach(final ZipInputStream zipInputStream,
            final String prefix, final ClassHandler handler) {
        final int startPos = prefix.length();
        ZipEntry entry = null;
        while ((entry = ZipInputStreamUtil.getNextEntry(zipInputStream)) != null) {
            try {
                final String entryName = entry.getName().replace('\\', '/');
                if (entryName.startsWith(prefix)
                        && entryName.endsWith(CLASS_SUFFIX)) {
                    final String className = entryName.substring(startPos,
                            entryName.length() - CLASS_SUFFIX.length())
                            .replace('/', '.');
                    final int pos = className.lastIndexOf('.');
                    final String packageName = (pos == -1) ? null : className
                            .substring(0, pos);
                    final String shortClassName = (pos == -1) ? className
                            : className.substring(pos + 1);
                    handler.processClass(packageName, shortClassName);
                }
            } finally {
                ZipInputStreamUtil.closeEntry(zipInputStream);
            }
        }
    }

    private static void traverseFileSystem(final File dir,
            final String packageName, final ClassHandler handler) {
        final File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            final File file = files[i];
            final String fileName = file.getName();
            if (file.isDirectory()) {
                traverseFileSystem(file, ClassUtil.concatName(packageName,
                        fileName), handler);
            } else if (fileName.endsWith(".class")) {
                final String shortClassName = fileName.substring(0, fileName
                        .length()
                        - CLASS_SUFFIX.length());
                handler.processClass(packageName, shortClassName);
            }
        }
    }

    private static File getPackageDir(final File rootDir,
            final String rootPackage) {
        File packageDir = rootDir;
        if (rootPackage != null) {
            final String[] names = rootPackage.split("\\.");
            for (int i = 0; i < names.length; i++) {
                packageDir = new File(packageDir, names[i]);
            }
        }
        return packageDir;
    }
}

/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.autodetector.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.ZipFileUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * {@link ClassAutoDetector}の抽象クラスです。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractClassAutoDetector implements ClassAutoDetector {

    private Map strategies = new HashMap();

    private List targetPackageNames = new ArrayList();;

    /**
     * {@link AbstractClassAutoDetector}のデフォルトコンストラクタです。
     */
    public AbstractClassAutoDetector() {
        strategies.put("file", new FileSystemStrategy());
        strategies.put("jar", new JarFileStrategy());
        strategies.put("zip", new ZipFileStrategy());
        strategies.put("code-source", new CodeSourceFileStrategy());
    }

    /**
     * {@link Strategy}を追加します。
     * 
     * @param protocol
     * @param strategy
     */
    public void addStrategy(final String protocol, final Strategy strategy) {
        strategies.put(protocol, strategy);
    }

    /**
     * {@link Strategy}を返します。
     * 
     * @param protocol
     * @return
     */
    public Strategy getStrategy(final String protocol) {
        return (Strategy) strategies.get(URLUtil.toCanonicalProtocol(protocol));
    }

    /**
     * ターゲットのパッケージ名を追加します。
     * 
     * @param targetPackageName
     */
    public void addTargetPackageName(final String targetPackageName) {
        targetPackageNames.add(targetPackageName);
    }

    /**
     * ターゲットのパッケージ名の数を返します。
     * 
     * @return
     */
    public int getTargetPackageNameSize() {
        return targetPackageNames.size();
    }

    /**
     * ターゲットのパッケージ名を返します。
     * 
     * @param index
     * @return
     */
    public String getTargetPackageName(final int index) {
        return (String) targetPackageNames.get(index);
    }

    /**
     * 戦略をあらわすインターフェースです。
     * 
     */
    protected interface Strategy {

        /**
         * {@link Class}を認識します。
         * 
         * @param packageName
         * @param url
         * @param handler
         */
        void detect(String packageName, URL url, ClassHandler handler);
    }

    /**
     * ファイルシステム用の戦略です。
     * 
     */
    protected static class FileSystemStrategy implements Strategy {

        public void detect(final String packageName, final URL url,
                final ClassHandler handler) {

            final File rootDir = getRootDir(packageName, url);
            ClassTraversal.forEach(rootDir, packageName, handler);
        }

        /**
         * ルートディレクトリを返します。
         * 
         * @param path
         *            パス
         * @param url
         *            URL
         * @return ルートディレクトリ
         */
        protected File getRootDir(final String path, final URL url) {
            File file = ResourceUtil.getFile(url);
            final String[] names = StringUtil.split(path, ".");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    /**
     * jarファイル用の戦略です。
     * 
     */
    protected static class JarFileStrategy implements Strategy {

        public void detect(final String packageName, final URL url,
                final ClassHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, handler);
        }

        /**
         * jarファイルを作成します。
         * 
         * @param url
         *            URL
         * @return jarファイル
         */
        protected JarFile createJarFile(final URL url) {
            return JarFileUtil.toJarFile(url);
        }
    }

    /**
     * zipファイル用の戦略です。
     * 
     */
    protected static class ZipFileStrategy implements Strategy {

        public void detect(final String packageName, final URL url,
                final ClassHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, handler);
        }

        /**
         * jarファイルを作成します。
         * 
         * @param url
         *            URL
         * @return jarファイル
         */
        protected JarFile createJarFile(final URL url) {
            final String jarFileName = ZipFileUtil.toZipFilePath(url);
            return JarFileUtil.create(new File(jarFileName));
        }
    }

    /**
     * OC4J用の戦略です。
     * 
     */
    protected static class CodeSourceFileStrategy implements Strategy {

        public void detect(final String packageName, final URL url,
                final ClassHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, handler);
        }

        /**
         * jarファイルを作成します。
         * 
         * @param url
         *            URL
         * @return jarファイル
         */
        protected JarFile createJarFile(final URL url) {
            final URL jarUrl = URLUtil.create("jar:file:" + url.getPath());
            return JarFileUtil.toJarFile(jarUrl);
        }
    }
}

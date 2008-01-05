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
import java.util.regex.Pattern;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceTraversal;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.ZipFileUtil;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * {@link ResourceAutoDetector}の抽象クラスです。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractResourceAutoDetector implements
        ResourceAutoDetector {

    private Map strategies = new HashMap();

    private List targetDirPaths = new ArrayList();

    private List resourceNamePatterns = new ArrayList();

    private List ignoreResourceNamePatterns = new ArrayList();

    /**
     * {@link AbstractResourceAutoDetector}のデフォルトコンストラクタです。
     */
    public AbstractResourceAutoDetector() {
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
     * @return {@link Strategy}
     */
    public Strategy getStrategy(final String protocol) {
        return (Strategy) strategies.get(URLUtil.toCanonicalProtocol(protocol));
    }

    /**
     * ターゲットのディレクトリのパスを追加します。
     * 
     * @param targetDirPath
     */
    public void addTargetDirPath(final String targetDirPath) {
        targetDirPaths.add(targetDirPath);
    }

    /**
     * ターゲットのディレクトリのパスを返します。
     * 
     * @return ターゲットのディレクトリのパス
     */
    public int getTargetDirPathSize() {
        return targetDirPaths.size();
    }

    /**
     * ターゲットのディレクトリのパスを返します。
     * 
     * @param index
     * @return ターゲットのディレクトリのパス
     */
    public String getTargetDirPath(final int index) {
        return (String) targetDirPaths.get(index);
    }

    /**
     * リソース名のパターンを追加します。
     * 
     * @param resourceName
     */
    public void addResourceNamePattern(final String resourceName) {
        resourceNamePatterns.add(Pattern.compile(resourceName));
    }

    /**
     * 無視するリソース名のパターンを追加します。
     * 
     * @param resourceName
     */
    public void addIgnoreResourceNamePattern(final String resourceName) {
        ignoreResourceNamePatterns.add(Pattern.compile(resourceName));
    }

    /**
     * リソース名のパターンを返します。
     * 
     * @param index
     * @return リソース名のパターン
     */
    public Pattern getResourceNamePattern(final int index) {
        return (Pattern) resourceNamePatterns.get(index);
    }

    /**
     * リソース名のパターン数を返します。
     * 
     * @return リソース名のパターン数
     */
    public int getResourceNamePatternSize() {
        return resourceNamePatterns.size();
    }

    /**
     * 無視するリソース名のパターンを返します。
     * 
     * @param index
     * @return 無視するリソース名のパターン
     */
    public Pattern getIgnoreResourceNamePattern(final int index) {
        return (Pattern) ignoreResourceNamePatterns.get(index);
    }

    /**
     * 無視するリソース名のパターン数を返します。
     * 
     * @return 無視するリソース名のパターン数
     */
    public int getIgnoreResourceNamePatternSize() {
        return ignoreResourceNamePatterns.size();
    }

    /**
     * 適用されるかどうかを返します。
     * 
     * @param resourceName
     *            リソース名
     * @return 適用されるかどうか
     */
    protected boolean isApplied(final String resourceName) {
        for (int i = 0; i < getResourceNamePatternSize(); i++) {
            final Pattern pattern = getResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 無視されるかどうかを返します。
     * 
     * @param resourceName
     *            リソース名
     * @return 無視されるかどうか
     */
    protected boolean isIgnored(final String resourceName) {
        for (int i = 0; i < getIgnoreResourceNamePatternSize(); i++) {
            final Pattern pattern = getIgnoreResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 戦略をあらわすインターフェースです。
     * 
     */
    protected interface Strategy {

        /**
         * リソースを認識します。
         * 
         * @param path
         * @param url
         * @param handler
         */
        void detect(String path, URL url, ResourceHandler handler);
    }

    /**
     * ファイルシステム用の戦略です。
     * 
     */
    protected static class FileSystemStrategy implements Strategy {

        public void detect(final String path, final URL url,
                final ResourceHandler handler) {

            final File rootDir = getRootDir(path, url);
            ResourceTraversal.forEach(rootDir, path, handler);
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
            final String[] names = StringUtil.split(path, "/");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    /**
     * jarファイル用の戦略を返します。
     * 
     */
    protected static class JarFileStrategy implements Strategy {

        public void detect(final String path, final URL url,
                final ResourceHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ResourceTraversal.forEach(jarFile, handler);
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

        public void detect(final String path, final URL url,
                final ResourceHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ResourceTraversal.forEach(jarFile, handler);
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

        public void detect(final String path, final URL url,
                final ResourceHandler handler) {

            final JarFile jarFile = createJarFile(url);
            ResourceTraversal.forEach(jarFile, handler);
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

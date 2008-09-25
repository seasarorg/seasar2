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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileComparetor;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.util.StringUtil;

/**
 * {@link ManagedFile}の実装クラスです。
 * 
 * @author taedium
 */
public class ManagedFileImpl implements ManagedFile {

    /** ファイル情報 */
    protected FileInfo fileInfo;

    /** 環境名つきファイル情報 */
    protected FileInfo envNamedFileInfo;

    /**
     * インスタンスを構築します。
     * 
     * @param basePath
     *            ベースパス
     * @param path
     *            パス
     * @param env
     *            環境名
     */
    public ManagedFileImpl(String basePath, String path, String env) {
        if (basePath == null) {
            throw new NullPointerException("basePath");
        }
        if (path == null) {
            throw new NullPointerException("path");
        }
        fileInfo = new FileInfo(basePath, basePath, path, env);
        if (env != null) {
            envNamedFileInfo = new FileInfo(basePath + "#" + env, basePath,
                    path, env);
        }
    }

    public File asFile() {
        return getFile();
    }

    public ManagedFile createChild(String relativePath) {
        return createChildInternal(relativePath);
    }

    /**
     * このインスタンスの子となるバージョン管理されたファイルを作成します。
     * 
     * @param relativePath
     *            このインスタンスが表すファイルからの相対パス
     * @return バージョン管理されたファイル
     */
    protected ManagedFile createChildInternal(String relativePath) {
        FileInfo holder = getFileInfo();
        return new ManagedFileImpl(holder.logicalBasePath, holder.path
                + File.separator + relativePath, holder.env);
    }

    public List<File> listAllFiles() {
        final Map<String, File> fileMap = new LinkedHashMap<String, File>();
        if (envNamedFileInfo != null) {
            traverseDirectory(envNamedFileInfo, fileMap);
        }
        traverseDirectory(fileInfo, fileMap);
        File[] files = fileMap.values().toArray(new File[fileMap.size()]);
        return Arrays.asList(files);
    }

    /**
     * ディレクトリを横断します。
     * 
     * @param fileInfo
     *            ディレクトリの情報
     * @param fileMap
     *            ディレクトリからの相対パスをキー、ファイルを値とするマップ
     */
    protected void traverseDirectory(final FileInfo fileInfo,
            final Map<String, File> fileMap) {

        FileUtil.traverseDirectory(fileInfo.file,
                new DefaultExcludesFilenameFilter(), new FileComparetor(),
                new FileUtil.FileHandler() {

                    public void handle(File file) {
                        String canonicalPath = FileUtil.getCanonicalPath(file);
                        String path = StringUtil.trimPrefix(canonicalPath,
                                fileInfo.actualBasePath + File.separator);
                        if (!fileMap.containsKey(path)) {
                            fileMap.put(path, file);
                        }
                    }
                });
    }

    /**
     * ファイル情報を返します。
     * 
     * @return ファイル情報
     */
    protected FileInfo getFileInfo() {
        return envNamedFileInfo != null ? envNamedFileInfo : fileInfo;
    }

    /**
     * ファイルを返します。
     * 
     * @return ファイル
     */
    protected File getFile() {
        return getFileInfo().file;
    }

    /**
     * ファイル情報です。
     * 
     * @author taedium
     */
    protected static class FileInfo {

        /** 実際のベースパス */
        protected String actualBasePath;

        /** 論理的なベースパス */
        protected String logicalBasePath;

        /** パス */
        protected String path;

        /** 環境名 */
        protected String env;

        /** ファイル */
        protected File file;

        /**
         * インスタンスを構築します。
         * 
         * @param actualBasePath
         *            実際のベースパス
         * @param logicalBasePath
         *            論理的なベースパス
         * @param path
         *            パス
         * @param env
         *            環境名
         */
        public FileInfo(String actualBasePath, String logicalBasePath,
                String path, String env) {
            this.actualBasePath = actualBasePath;
            this.logicalBasePath = logicalBasePath;
            this.path = path;
            this.env = env;
            File f = new File(actualBasePath, path);
            this.file = FileUtil.getCanonicalFile(f);
        }
    }
}

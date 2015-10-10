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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    /** 親の{@link ManagedFile} */
    protected ManagedFile parent;

    /** ファイル情報 */
    protected FileInfo fileInfo;

    /** 環境名つきファイル情報 */
    protected FileInfo envNamedFileInfo;

    /**
     * インスタンスを構築します。
     * 
     * @param basePath
     *            ベースパス
     * @param relativePath
     *            ベースパスからの相対パス
     * @param env
     *            環境名
     */
    protected ManagedFileImpl(String basePath, String relativePath, String env) {
        this(null, basePath, relativePath, env);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param parent
     *            親の{@link ManagedFile}、このインスタンスがバージョンディレクトリの場合{@code null}
     * @param basePath
     *            ベースパス
     * @param relativePath
     *            ベースパスからの相対パス
     * @param env
     *            環境名
     */
    protected ManagedFileImpl(ManagedFile parent, String basePath,
            String relativePath, String env) {
        if (basePath == null) {
            throw new NullPointerException("basePath");
        }
        if (relativePath == null) {
            throw new NullPointerException("relativePath");
        }
        this.parent = parent;
        fileInfo = new FileInfo(basePath, basePath, relativePath, env);
        if (env != null) {
            envNamedFileInfo = new FileInfo(basePath + "#" + env, basePath,
                    relativePath, env);
        }
    }

    public File asFile() {
        return getFile();
    }

    public String getRelativePath() {
        return getFileInfo().relativePath;
    }

    public String getName() {
        return getFile().getName();
    }

    public boolean delete() {
        return getFile().delete();
    }

    public boolean exists() {
        return getFile().exists();
    }

    public boolean isDirectory() {
        return getFile().isDirectory();
    }

    public ManagedFile getParent() {
        return parent;
    }

    public boolean mkdir() {
        return getFile().mkdir();
    }

    public boolean mkdirs() {
        return getFile().mkdirs();
    }

    public boolean createNewFile() {
        return FileUtil.createNewFile(getFile());
    }

    public List<ManagedFile> listManagedFiles() {
        File[] files = getFile().listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        List<ManagedFile> list = new ArrayList<ManagedFile>(files.length);
        for (File file : files) {
            list.add(createChildInternal(file.getName()));
        }
        return list;
    }

    public List<ManagedFile> listManagedFiles(FilenameFilter filter) {
        File[] files = getFile().listFiles(filter);
        if (files == null) {
            return Collections.emptyList();
        }
        List<ManagedFile> list = new ArrayList<ManagedFile>(files.length);
        for (File file : files) {
            list.add(createChildInternal(file.getName()));
        }
        return list;
    }

    public ManagedFile createChild(String childName) {
        return createChildInternal(childName);
    }

    /**
     * このインスタンスの子となるバージョン管理されたディレクトリを作成します。
     * 
     * @param relativePath
     *            このインスタンスが表すファイルからの相対パス
     * @return バージョン管理されたファイル
     */
    protected ManagedFile createChildInternal(String relativePath) {
        FileInfo info = getFileInfo();
        ManagedFileImpl file = new ManagedFileImpl(this, info.logicalBasePath,
                info.relativePath + File.separator + relativePath, info.env);
        return file;
    }

    public List<File> listAllFiles() {
        final Map<String, File> fileMap = new LinkedHashMap<String, File>();
        if (envNamedFileInfo != null) {
            traverseDirectory(envNamedFileInfo, fileMap);
        }
        traverseDirectory(fileInfo, fileMap);
        return getFileList(fileMap);
    }

    /**
     * ファイルのリストを返します。
     * 
     * @param fileMap
     *            パスの文字列をキー、 {@link File}を値とするマップ
     * @return ファイルのリストを
     */
    protected List<File> getFileList(Map<String, File> fileMap) {
        if (envNamedFileInfo == null) {
            File[] files = fileMap.values().toArray(new File[fileMap.size()]);
            return Arrays.asList(files);
        }
        List<String> pathList = new ArrayList<String>(fileMap.keySet());
        Collections.sort(pathList);
        List<File> fileList = new ArrayList<File>(pathList.size());
        for (String path : pathList) {
            fileList.add(fileMap.get(path));
        }
        return fileList;
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

    public boolean hasChild() {
        String[] paths = getFile().list();
        return paths != null && paths.length > 0;
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
     * ファイル情報を返します。
     * 
     * @return ファイル情報
     */
    protected FileInfo getFileInfo() {
        return envNamedFileInfo != null ? envNamedFileInfo : fileInfo;
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

        /** ベースパスからの相対パス */
        protected String relativePath;

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
         * @param relativePath
         *            ベースパスからの相対パス
         * @param env
         *            環境名
         */
        public FileInfo(String actualBasePath, String logicalBasePath,
                String relativePath, String env) {
            this.actualBasePath = actualBasePath;
            this.logicalBasePath = logicalBasePath;
            this.relativePath = relativePath;
            this.env = env;
            File f = new File(actualBasePath, relativePath);
            this.file = FileUtil.getCanonicalFile(f);
        }
    }
}

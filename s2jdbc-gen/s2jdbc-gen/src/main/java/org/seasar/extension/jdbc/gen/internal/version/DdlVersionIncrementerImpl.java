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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionDirectoryExistsRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.internal.util.TableUtil;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementer;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;

/**
 * {@link DdlVersionIncrementer}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlVersionIncrementerImpl implements DdlVersionIncrementer {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(DdlVersionIncrementerImpl.class);

    /** DDLのバージョンを管理するディレクトリ */
    protected DdlVersionDirectoryTree ddlVersionDirectoryTree;

    /** 方言 */
    protected GenDialect dialect;

    /** データソース */
    protected DataSource dataSource;

    /** createディレクトリ名のリスト */
    protected List<String> createDirNameList = new ArrayList<String>();

    /** dropディレクトリ名のリスト */
    protected List<String> dropDirNameList = new ArrayList<String>();

    protected List<File> recoveryDirList = new ArrayList<File>();

    /**
     * インスタンスを構築します。
     * 
     * @param ddlVersionDirectoryTree
     *            DDLのバージョンを管理するディレクトリ
     * @param dialect
     *            方言
     * @param dataSource
     *            データソース
     * @param createDirNameList
     *            コピー非対象のcreateディレクトリ名のリスト
     * @param dropDirNameList
     *            コピー非対象のdropディレクトリ名のリスト
     */
    public DdlVersionIncrementerImpl(
            DdlVersionDirectoryTree ddlVersionDirectoryTree,
            GenDialect dialect, DataSource dataSource,
            List<String> createDirNameList, List<String> dropDirNameList) {
        if (ddlVersionDirectoryTree == null) {
            throw new NullPointerException("versionDirectories");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (createDirNameList == null) {
            throw new NullPointerException("createFileNameList");
        }
        if (dropDirNameList == null) {
            throw new NullPointerException("dropFileNameList");
        }
        this.ddlVersionDirectoryTree = ddlVersionDirectoryTree;
        this.dialect = dialect;
        this.dataSource = dataSource;
        this.createDirNameList.addAll(createDirNameList);
        this.dropDirNameList.addAll(dropDirNameList);
    }

    public void increment(Callback callback) {
        DdlVersionDirectory currentVersionDir = ddlVersionDirectoryTree
                .getCurrentVersionDirectory();
        DdlVersionDirectory nextVersionDir = ddlVersionDirectoryTree
                .getNextVersionDirectory();
        if (nextVersionDir.asFile().exists()) {
            throw new NextVersionDirectoryExistsRuntimeException(nextVersionDir
                    .asFile().getPath());
        }
        try {
            makeVersionDirs(currentVersionDir);
            makeVersionDirs(nextVersionDir);
            copyVersionDirectory(currentVersionDir, nextVersionDir);
            incrementInternal(callback, nextVersionDir);
            if (currentVersionDir.isFirstVersion()) {
                makeFirstVersionDir(currentVersionDir, nextVersionDir);
            }
            incrementVersionNo();
        } catch (RuntimeException e) {
            recover();
            throw e;
        }
    }

    protected void makeVersionDirs(DdlVersionDirectory versionDir) {
        File verDir = versionDir.asFile();
        if (verDir.mkdirs()) {
            recoveryDirList.add(verDir);
        }
        File createDir = versionDir.getCreateDirectory().asFile();
        if (createDir.mkdirs()) {
            recoveryDirList.add(createDir);
        }
        File dropDir = versionDir.getDropDirectory().asFile();
        if (dropDir.mkdirs()) {
            recoveryDirList.add(dropDir);
        }
    }

    /**
     * ディレクトリをコピーします。
     * 
     * @param srcDir
     *            コピー元のディレクトリ
     * @param destDir
     *            コピー先のディレクトリ
     */
    protected void copyVersionDirectory(DdlVersionDirectory current,
            DdlVersionDirectory next) {
        File srcCreateDir = current.getCreateDirectory().asFile();
        File destCreateDir = next.getCreateDirectory().asFile();
        FileUtil.copyDirectory(srcCreateDir, destCreateDir,
                new PathFilenameFilter(srcCreateDir, createDirNameList));

        File srcDropDir = current.getDropDirectory().asFile();
        File destDropDir = next.getDropDirectory().asFile();
        FileUtil.copyDirectory(srcDropDir, destDropDir, new PathFilenameFilter(
                srcDropDir, dropDirNameList));

    }

    /**
     * 最初のバージョンのdropディレクトリを作成します。
     * 
     * @param srcDir
     *            コピー元のディレクトリ
     * @param destDir
     *            コピー先のディレクトリ
     */
    protected void makeFirstVersionDir(DdlVersionDirectory current,
            DdlVersionDirectory next) {
        File src = next.getDropDirectory().asFile();
        File dest = current.getDropDirectory().asFile();
        FileUtil.copyDirectory(src, dest, new TableExistenceFilenameFilter());
    }

    /**
     * バージョンのインクリメント処理を実行します。
     * 
     * @param callback
     *            コールバック
     * @param createDir
     *            createディレクトリ
     * @param dropDir
     *            dropディレクトリ
     */
    protected void incrementInternal(Callback callback,
            DdlVersionDirectory directory) {
        File src = directory.getCreateDirectory().asFile();
        File dest = directory.getDropDirectory().asFile();
        callback.execute(src, dest, ddlVersionDirectoryTree.getDdlInfoFile()
                .getNextVersionNo());
    }

    /**
     * 作成したディレクトリを削除します。
     */
    protected void recover() {
        Collections.reverse(recoveryDirList);
        for (File dir : recoveryDirList) {
            if (dir.exists()) {
                try {
                    FileUtil.deleteDirectory(dir);
                } catch (Exception e) {
                    logger.log(e);
                }
            }
        }
    }

    /**
     * バージョン番号を増分します。
     */
    protected void incrementVersionNo() {
        ddlVersionDirectoryTree.getDdlInfoFile().applyNextVersionNo();
    }

    /**
     * コピー対象外のパスで始まるファイル名をフィルタします。
     * 
     * @author taedium
     */
    protected class PathFilenameFilter implements FilenameFilter {

        /** コピー対象外のパスのリスト */
        protected List<String> filterPathList = new ArrayList<String>();

        /** ファイル名のフィルタ */
        protected FilenameFilter filenameFilter;

        /**
         * インスタンスを構築します。
         */
        protected PathFilenameFilter(File baseDir, List<String> dirNameList) {
            filenameFilter = new DefaultExcludesFilenameFilter();
            setupFilterPathList(baseDir, dirNameList);
        }

        /**
         * コピー対象外のパスのリストをセットアップします。
         * 
         * @param dir
         *            ディレクトリ
         * @param dirNameList
         *            ディレクトリ名のリスト
         */
        protected void setupFilterPathList(File dir, List<String> dirNameList) {
            for (String name : dirNameList) {
                File file = new File(dir, name);
                filterPathList.add(FileUtil.getCanonicalPath(file));
            }
        }

        public boolean accept(File dir, String name) {
            if (!filenameFilter.accept(dir, name)) {
                return false;
            }
            for (String path : filterPathList) {
                File file = new File(dir, name);
                if (FileUtil.getCanonicalPath(file).startsWith(path)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * ファイルに対応するテーブルがデータベース存在しない場合にフィルタします。
     * 
     * @author taedium
     * 
     */
    protected class TableExistenceFilenameFilter implements FilenameFilter {

        /** ファイル名のフィルタ */
        protected FilenameFilter filenameFilter;

        /** テーブルの集合 */
        protected TableUtil.TableSet tableSet;

        /**
         * インスタンスを構築します。
         */
        protected TableExistenceFilenameFilter() {
            filenameFilter = new DefaultExcludesFilenameFilter();
            tableSet = TableUtil.getTableSet(dialect, dataSource);
        }

        public boolean accept(File dir, String name) {
            if (!filenameFilter.accept(dir, name)) {
                return false;
            }
            File file = new File(dir, name);
            if (file.isDirectory()) {
                return file.list(this).length > 0;
            }
            String suffix = null;
            if (name.endsWith(".sql")) {
                suffix = ".sql";
            } else if (name.endsWith(".ddl")) {
                suffix = ".ddl";
            } else {
                return false;
            }
            String canonicalTableName = StringUtil.trimSuffix(name, suffix);
            String[] elements = TableUtil
                    .splitCanonicalTableName(canonicalTableName);
            return tableSet.exists(elements[0], elements[1], elements[2]);
        }
    }
}

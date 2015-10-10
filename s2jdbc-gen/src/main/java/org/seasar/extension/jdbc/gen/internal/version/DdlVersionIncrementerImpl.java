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
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.event.GenDdlEvent;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionDirectoryExistsRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.internal.version.wrapper.DdlVersionDirectoryWrapper;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementer;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.log.Logger;

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

    /** バージョンディレクトリやファイルが生成されたイベントを受け取るためのリスナー */
    protected GenDdlListener genDdlListener;

    /** 方言 */
    protected GenDialect dialect;

    /** データソース */
    protected DataSource dataSource;

    /** createディレクトリ名のリスト */
    protected List<String> createDirNameList = new ArrayList<String>();

    /** dropディレクトリ名のリスト */
    protected List<String> dropDirNameList = new ArrayList<String>();

    /** リカバリ対象のディレクトリのリスト */
    protected List<DdlVersionDirectory> recoveryDirList = new ArrayList<DdlVersionDirectory>();

    /**
     * インスタンスを構築します。
     * 
     * @param ddlVersionDirectoryTree
     *            DDLのバージョンを管理するディレクトリ
     * @param genDdlListener
     *            バージョンディレクトリやファイルが生成されたイベントを受け取るためのリスナー
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
            GenDdlListener genDdlListener, GenDialect dialect,
            DataSource dataSource, List<String> createDirNameList,
            List<String> dropDirNameList) {
        if (ddlVersionDirectoryTree == null) {
            throw new NullPointerException("ddlVersionDirectoryTree");
        }
        if (genDdlListener == null) {
            throw new NullPointerException("genDdlListener");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        if (createDirNameList == null) {
            throw new NullPointerException("createDirNameList");
        }
        if (dropDirNameList == null) {
            throw new NullPointerException("dropDirNameList");
        }
        this.ddlVersionDirectoryTree = ddlVersionDirectoryTree;
        this.genDdlListener = genDdlListener;
        this.dialect = dialect;
        this.dataSource = dataSource;
        this.createDirNameList.addAll(createDirNameList);
        this.dropDirNameList.addAll(dropDirNameList);
    }

    public void increment(String comment, Callback callback) {
        try {
            DdlVersionDirectory currentVersionDir = getCurrentDdlVersionDirectory();
            DdlVersionDirectory nextVersionDir = getNextDdlVersionDirectory(currentVersionDir);
            copyDirectory(currentVersionDir, nextVersionDir);
            callback.execute(nextVersionDir);
            if (currentVersionDir.isFirstVersion()) {
                copyDropDirectory(nextVersionDir, currentVersionDir);
            }
            incrementVersionNo(comment);
        } catch (RuntimeException e) {
            recover();
            throw e;
        }
    }

    /**
     * 現バージョンに対応するディレクトリを返します。
     * 
     * @return 現バージョンに対応するディレクトリ
     */
    protected DdlVersionDirectory getCurrentDdlVersionDirectory() {
        DdlVersionDirectory currentVersionDir = ddlVersionDirectoryTree
                .getCurrentVersionDirectory();
        makeDirectory(currentVersionDir);
        return currentVersionDir;
    }

    /**
     * 次バージョンに対応するディレクトリを返します。
     * 
     * @param currentVersionDir
     *            現バージョンに対応するディレクトリ
     * @return 次バージョンに対応するディレクトリ
     */
    protected DdlVersionDirectory getNextDdlVersionDirectory(
            final DdlVersionDirectory currentVersionDir) {
        final DdlVersionDirectory nextVersionDir = ddlVersionDirectoryTree
                .getNextVersionDirectory();
        if (nextVersionDir.exists()) {
            throw new NextVersionDirectoryExistsRuntimeException(nextVersionDir
                    .asFile().getPath());
        }

        DdlVersionDirectory wrapper = new DdlVersionDirectoryWrapper(
                nextVersionDir, genDdlListener, currentVersionDir,
                nextVersionDir) {

            @Override
            public boolean mkdir() {
                if (getManagedFile().exists()) {
                    return false;
                }
                GenDdlEvent event = new GenDdlEvent(this, currentVersionDir,
                        nextVersionDir);
                genDdlListener.preCreateNextVersionDir(event);
                boolean made = getManagedFile().mkdir();
                if (made) {
                    genDdlListener.postCreateNextVersionDir(event);
                }
                return made;
            }

            @Override
            public boolean delete() {
                if (!getManagedFile().exists()) {
                    return false;
                }
                GenDdlEvent event = new GenDdlEvent(this, currentVersionDir,
                        nextVersionDir);
                genDdlListener.preRemoveNextVersionDir(event);
                boolean deleted = super.delete();
                if (deleted) {
                    genDdlListener.postRemoveNextVersionDir(event);
                }
                return deleted;
            }
        };

        makeDirectory(wrapper);
        return wrapper;
    }

    /**
     * バージョンディレクトリを作成します。
     * 
     * @param versionDir
     *            バージョンディレクトリ
     */
    protected void makeDirectory(DdlVersionDirectory versionDir) {
        if (versionDir.mkdirs()) {
            recoveryDirList.add(versionDir);
        }
    }

    /**
     * バージョンディレクトリをコピーします。
     * 
     * @param src
     *            コピー元のバージョンディレクトリ
     * @param dest
     *            コピー先のバージョンディレクトリ
     */
    protected void copyDirectory(DdlVersionDirectory src,
            DdlVersionDirectory dest) {
        ManagedFile srcCreateDir = src.getCreateDirectory();
        ManagedFile destCreateDir = dest.getCreateDirectory();
        copyDir(srcCreateDir, destCreateDir, new PathFilenameFilter(
                srcCreateDir.asFile(), createDirNameList));

        ManagedFile srcDropDir = src.getDropDirectory();
        ManagedFile destDropDir = dest.getDropDirectory();
        copyDir(srcDropDir, destDropDir, new PathFilenameFilter(srcDropDir
                .asFile(), dropDirNameList));
    }

    /**
     * dropディレクトリを作成します。
     * 
     * @param src
     *            コピー元のバージョンディレクトリ
     * @param dest
     *            コピー先のバージョンディレクトリ
     */
    protected void copyDropDirectory(DdlVersionDirectory src,
            DdlVersionDirectory dest) {
        ManagedFile srcDropDir = src.getDropDirectory();
        ManagedFile destDropDir = dest.getDropDirectory();
        copyDir(srcDropDir, destDropDir, new DefaultExcludesFilenameFilter());
    }

    /**
     * ディレクトリをコピーします。
     * 
     * @param srcDir
     *            コピー元のディレクトリ
     * @param destDir
     *            コピー先のディレクトリ
     * @param filter
     *            フィルタ
     */
    protected void copyDir(ManagedFile srcDir, ManagedFile destDir,
            FilenameFilter filter) {
        destDir.mkdirs();
        for (ManagedFile src : srcDir.listManagedFiles(filter)) {
            ManagedFile dest = destDir.createChild(src.getName());
            if (src.isDirectory()) {
                copyDir(src, dest, filter);
            } else {
                dest.createNewFile();
                FileUtil.copy(src.asFile(), dest.asFile());
            }
        }
    }

    /**
     * 作成したバージョンディレクトリを削除します。
     */
    protected void recover() {
        for (DdlVersionDirectory dir : recoveryDirList) {
            if (!dir.exists()) {
                return;
            }
            try {
                deleteDir(dir);
            } catch (Exception e) {
                logger.log(e);
            }
        }
    }

    /**
     * ディレクトリを削除します。
     * 
     * @param dir
     *            ディレクトリ
     */
    protected void deleteDir(ManagedFile dir) {
        for (ManagedFile file : dir.listManagedFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
                file.delete();
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    /**
     * バージョン番号を増分します。
     * 
     * @param comment
     *            バージョンを増分する理由を示すコメント
     */
    protected void incrementVersionNo(String comment) {
        ddlVersionDirectoryTree.getDdlInfoFile().applyNextVersionNo(comment);
    }

    /**
     * 除外対象のパスで始まるファイル名をフィルタします。
     * 
     * @author taedium
     */
    protected static class PathFilenameFilter implements FilenameFilter {

        /** 除外対象パスのリスト */
        protected List<String> excludePathList = new ArrayList<String>();

        /** ファイル名のフィルタ */
        protected FilenameFilter filenameFilter;

        /**
         * インスタンスを構築します。
         * 
         * @param baseDir
         *            ベースディレクトリ
         * @param excludeDirNameList
         *            除外ディレクトリ名のリスト
         */
        protected PathFilenameFilter(File baseDir,
                List<String> excludeDirNameList) {
            filenameFilter = new DefaultExcludesFilenameFilter();
            setupFilterPathList(baseDir, excludeDirNameList);
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
                excludePathList.add(FileUtil.getCanonicalPath(file));
            }
        }

        public boolean accept(File dir, String name) {
            if (!filenameFilter.accept(dir, name)) {
                return false;
            }
            for (String path : excludePathList) {
                File file = new File(dir, name);
                if (FileUtil.getCanonicalPath(file).startsWith(path)) {
                    return false;
                }
            }
            return true;
        }
    }
}

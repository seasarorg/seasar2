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
package org.seasar.extension.jdbc.gen.version;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.gen.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.DdlVersionIncrementer;
import org.seasar.extension.jdbc.gen.util.ExcludeFilenameFilter;
import org.seasar.extension.jdbc.gen.util.FileUtil;
import org.seasar.framework.log.Logger;

/**
 * {@link DdlVersionIncrementer}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlVersionIncrementerImpl implements DdlVersionIncrementer {

    /** ロガー */
    protected Logger logger = Logger.getLogger(DdlVersionIncrementerImpl.class);

    /** DDLのバージョンを管理するディレクトリ */
    protected DdlVersionDirectory ddlVersionDirectory;

    /** createファイル名のリスト */
    protected List<String> createFileNameList = new ArrayList<String>();

    /** dropファイル名のリスト */
    protected List<String> dropFileNameList = new ArrayList<String>();

    /** 作成されたディレクトリのリスト */
    protected List<File> makedDirList = new ArrayList<File>();

    /**
     * インスタンスを構築しいます。
     * 
     * @param ddlVersionDirectory
     *            DDLのバージョンを管理するディレクトリ
     * @param createFileNameList
     *            createファイル名のリスト
     * @param dropFileNameList
     *            dropファイル名のリスト
     */
    public DdlVersionIncrementerImpl(DdlVersionDirectory ddlVersionDirectory,
            List<String> createFileNameList, List<String> dropFileNameList) {
        if (ddlVersionDirectory == null) {
            throw new NullPointerException("versionDirectories");
        }
        if (createFileNameList == null) {
            throw new NullPointerException("createFileNameList");
        }
        if (dropFileNameList == null) {
            throw new NullPointerException("dropFileNameList");
        }
        this.ddlVersionDirectory = ddlVersionDirectory;
        this.createFileNameList.addAll(createFileNameList);
        this.dropFileNameList.addAll(dropFileNameList);
    }

    public void increment(Callback callback) {
        try {
            makeCurrentVersionDirs();
            File currentVersionDir = ddlVersionDirectory.getCurrentVersionDir();
            File nextVersionDir = ddlVersionDirectory.getNextVersionDir();
            copyDirectory(currentVersionDir, nextVersionDir);
            makedDirList.add(nextVersionDir);
            File createDir = ddlVersionDirectory.getCreateDir(nextVersionDir);
            File dropDir = ddlVersionDirectory.getDropDir(nextVersionDir);
            callback.execute(createDir, dropDir, ddlVersionDirectory
                    .getDdlInfoFile().getNextVersionNo());
            incrementVersionNo();
        } catch (RuntimeException e) {
            recover();
            throw e;
        }
    }

    /**
     * 現在のバージョンディレクトリを作成します。
     */
    protected void makeCurrentVersionDirs() {
        File currentVersionDir = ddlVersionDirectory.getCurrentVersionDir();
        if (currentVersionDir.mkdirs()) {
            makedDirList.add(currentVersionDir);
        }
        File createDir = ddlVersionDirectory.getCreateDir(currentVersionDir);
        if (createDir.mkdirs()) {
            makedDirList.add(createDir);
        }
        File dropDir = ddlVersionDirectory.getDropDir(currentVersionDir);
        if (dropDir.mkdirs()) {
            makedDirList.add(dropDir);
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
    protected void copyDirectory(File srcDir, File destDir) {
        FileUtil.copyDirectory(srcDir, destDir, new ReservedFilenameFilter());
    }

    /**
     * 作成したディレクトリを削除します。
     */
    protected void recover() {
        try {
            Collections.reverse(makedDirList);
            for (File dir : makedDirList) {
                if (dir.exists()) {
                    FileUtil.deleteDirectory(dir);
                }
            }
        } catch (Exception e) {
            logger.log(e);
        }
    }

    /**
     * バージョン番号を増分します。
     */
    protected void incrementVersionNo() {
        ddlVersionDirectory.getDdlInfoFile().incrementVersionNo();
    }

    /**
     * コピーするファイルのフィルタです。
     * 
     * @author taedium
     */
    protected class ReservedFilenameFilter implements FilenameFilter {

        /** コピー対象外のパスのリスト */
        protected List<String> ignorePathList = new ArrayList<String>();

        /** フィルタ */
        protected ExcludeFilenameFilter excludeFilenameFilter = new ExcludeFilenameFilter();

        /**
         * インスタンスを構築します。
         */
        protected ReservedFilenameFilter() {
            File currentVersionDir = ddlVersionDirectory.getCurrentVersionDir();
            File createDir = ddlVersionDirectory
                    .getCreateDir(currentVersionDir);
            File dropDir = ddlVersionDirectory.getDropDir(currentVersionDir);
            setupIgnorePathList(createDir, createFileNameList);
            setupIgnorePathList(dropDir, dropFileNameList);
        }

        /**
         * コピー対象外のパスのリストをセットアップします。
         * 
         * @param dir
         *            ディレクトリ
         * @param fileNameList
         *            ファイル名のリスト
         */
        protected void setupIgnorePathList(File dir, List<String> fileNameList) {
            for (String name : fileNameList) {
                File file = new File(dir, name);
                ignorePathList.add(FileUtil.getCanonicalPath(file));
            }
        }

        public boolean accept(File dir, String name) {
            if (!excludeFilenameFilter.accept(dir, name)) {
                return false;
            }
            for (String path : ignorePathList) {
                File file = new File(dir, name);
                if (FileUtil.getCanonicalPath(file).startsWith(path)) {
                    return false;
                }
            }
            return true;
        }

    }

}

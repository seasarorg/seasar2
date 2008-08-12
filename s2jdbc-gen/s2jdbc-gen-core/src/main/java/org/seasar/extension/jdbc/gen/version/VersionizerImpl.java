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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.gen.DdlVersion;
import org.seasar.extension.jdbc.gen.Versionizer;
import org.seasar.extension.jdbc.gen.exception.NextVersionDirExistsRuntimeException;
import org.seasar.extension.jdbc.gen.util.ExclusionFilenameFilter;
import org.seasar.extension.jdbc.gen.util.FileUtil;
import org.seasar.extension.jdbc.gen.util.VersionUtil;
import org.seasar.framework.log.Logger;

/**
 * {@link Versionizer}の実装クラスです。
 * 
 * @author taedium
 */
public class VersionizerImpl implements Versionizer {

    /** createディレクトリの名前 */
    protected static String CREATE_DIR_NAME = "create";

    /** dropディレクトリの名前 */
    protected static String DROP_DIR_NAME = "drop";

    /** ロガー */
    protected Logger logger = Logger.getLogger(VersionizerImpl.class);

    /** マイグレーションのディレクトリ */
    protected File migrateDir;

    /** バージョン番号のパターン */
    protected String versionNoPattern;

    /** DDLのバージョン */
    protected DdlVersion ddlVersion;

    /** 現在のバージョンディレクトリ */
    protected File currentVersionDir;

    /** 次のバージョンディレクトリ */
    protected File nextVersionDir;

    /** 現在のバージョン番号 */
    protected int currentVersionNo;

    /** 次のバージョン番号 */
    protected int nextVersionNo;

    protected List<String> createFileNameList = new ArrayList<String>();

    protected List<String> dropFileNameList = new ArrayList<String>();

    protected List<File> makedDirList = new ArrayList<File>();

    /**
     * @param ddlVersion
     * @param migrateDir
     * @param versionNoPattern
     */
    public VersionizerImpl(DdlVersion ddlVersion, File migrateDir,
            String versionNoPattern) {
        this(ddlVersion, migrateDir, versionNoPattern, Collections
                .<String> emptyList(), Collections.<String> emptyList());
    }

    /**
     * @param ddlVersion
     * @param migrateDir
     * @param versionNoPattern
     */
    public VersionizerImpl(DdlVersion ddlVersion, File migrateDir,
            String versionNoPattern, List<String> createFileNameList,
            List<String> dropFileNameList) {
        if (ddlVersion == null) {
            throw new NullPointerException("ddlVersion");
        }
        if (migrateDir == null) {
            throw new NullPointerException("migrateDir");
        }
        if (versionNoPattern == null) {
            throw new NullPointerException("versionNoPattern");
        }
        if (createFileNameList == null) {
            throw new NullPointerException("createFileNameList");
        }
        if (dropFileNameList == null) {
            throw new NullPointerException("dropFileNameList");
        }
        this.ddlVersion = ddlVersion;
        this.migrateDir = migrateDir;
        this.versionNoPattern = versionNoPattern;
        this.createFileNameList.addAll(createFileNameList);
        this.dropFileNameList.addAll(dropFileNameList);
        currentVersionNo = ddlVersion.getVersionNo();
        currentVersionDir = new File(migrateDir, VersionUtil.toString(
                currentVersionNo, versionNoPattern));
        nextVersionNo = currentVersionNo + 1;
        nextVersionDir = new File(migrateDir, VersionUtil.toString(
                nextVersionNo, versionNoPattern));
        if (nextVersionDir.exists()) {
            throw new NextVersionDirExistsRuntimeException(nextVersionDir
                    .getPath(), ddlVersion.getVersionFile().getPath());
        }
    }

    public File getCreateDir(int versionNo) {
        File vresionDir = new File(migrateDir, VersionUtil.toString(versionNo,
                versionNoPattern));
        return new File(vresionDir, CREATE_DIR_NAME);
    }

    public File getDropDir(int versionNo) {
        File vresionDir = new File(migrateDir, VersionUtil.toString(versionNo,
                versionNoPattern));
        return new File(vresionDir, DROP_DIR_NAME);
    }

    public void increment(Callback callback) {
        try {
            makeCurrentVersionDirs();
            FileUtil.copyDirectory(currentVersionDir, nextVersionDir,
                    new Filter());
            makedDirList.add(nextVersionDir);
            File createDir = new File(nextVersionDir, CREATE_DIR_NAME);
            File dropDir = new File(nextVersionDir, DROP_DIR_NAME);
            callback.execute(createDir, dropDir, nextVersionNo);
            ddlVersion.setVersionNo(nextVersionNo);
        } catch (RuntimeException e) {
            recover();
            throw e;
        }
    }

    /**
     * 現在のバージョンディレクトリを作成します。
     */
    protected void makeCurrentVersionDirs() {
        if (currentVersionDir.mkdirs()) {
            makedDirList.add(currentVersionDir);
        }
        File createDir = new File(currentVersionDir, CREATE_DIR_NAME);
        if (createDir.mkdirs()) {
            makedDirList.add(createDir);
        }
        File dropDir = new File(currentVersionDir, DROP_DIR_NAME);
        if (dropDir.mkdirs()) {
            makedDirList.add(dropDir);
        }
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
     * コピーするファイルのフィルタです。
     * 
     * @author taedium
     */
    protected class Filter extends ExclusionFilenameFilter {

        /** コピー対象外のパスのリスト */
        protected List<String> ignorePathList = new ArrayList<String>();

        /**
         * インスタンスを構築します。
         */
        protected Filter() {
            File createDir = new File(currentVersionDir, CREATE_DIR_NAME);
            File dropDir = new File(currentVersionDir, DROP_DIR_NAME);
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

        @Override
        public boolean accept(File dir, String name) {
            if (!super.accept(dir, name)) {
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

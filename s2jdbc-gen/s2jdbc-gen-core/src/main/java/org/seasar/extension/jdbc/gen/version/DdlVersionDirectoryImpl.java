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

import org.seasar.extension.jdbc.gen.DdlInfoFile;
import org.seasar.extension.jdbc.gen.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.exception.NextVersionDirectoryExistsRuntimeException;
import org.seasar.extension.jdbc.gen.util.VersionUtil;
import org.seasar.framework.log.Logger;

/**
 * @author taedium
 * 
 */
public class DdlVersionDirectoryImpl implements DdlVersionDirectory {

    /** createディレクトリの名前 */
    protected static String CREATE_DIR_NAME = "create";

    /** dropディレクトリの名前 */
    protected static String DROP_DIR_NAME = "drop";

    /** ロガー */
    protected Logger logger = Logger.getLogger(DdlVersionDirectoryImpl.class);

    /** バージョン管理のベースディレクトリ */
    protected File baseDir;

    /** バージョン番号のパターン */
    protected String versionNoPattern;

    /** DDLのバージョン */
    protected DdlInfoFile ddlInfoFile;

    /** 現在のバージョンディレクトリ */
    protected File currentVersionDir;

    /** 次のバージョンディレクトリ */
    protected File nextVersionDir;

    /**
     * インスタンスを構築します。
     * 
     * @param baseDir
     *            バージョン管理のベースディレクトリ
     * @param versionFile
     *            バージョンファイル
     * @param versionNoPattern
     *            バージョン番号のパターン
     */
    public DdlVersionDirectoryImpl(File baseDir, File versionFile,
            String versionNoPattern) {
        if (baseDir == null) {
            throw new NullPointerException("baseDir");
        }
        if (versionFile == null) {
            throw new NullPointerException("versionFile");
        }
        if (versionNoPattern == null) {
            throw new NullPointerException("versionNoPattern");
        }
        this.baseDir = baseDir;
        this.versionNoPattern = versionNoPattern;
        ddlInfoFile = createDdlInfoFile(versionFile);

        String currentVersionDirName = VersionUtil.toString(ddlInfoFile
                .getVersionNo(), versionNoPattern);
        currentVersionDir = new File(baseDir, currentVersionDirName);

        String nextVersionDirName = VersionUtil.toString(ddlInfoFile
                .getNextVersionNo(), versionNoPattern);
        nextVersionDir = new File(baseDir, nextVersionDirName);
        if (nextVersionDir.exists()) {
            throw new NextVersionDirectoryExistsRuntimeException(nextVersionDir
                    .getPath(), versionFile.getPath());
        }
    }

    /**
     * {@link DdlInfoFile}の実装を作成します。
     * 
     * @param file
     *            ファイル
     * @return {@link DdlInfoFile}の実装
     */
    protected DdlInfoFile createDdlInfoFile(File file) {
        return new DdlInfoFileImpl(file);
    }

    public File getCurrentVersionDir() {
        return currentVersionDir;
    }

    public File getNextVersionDir() {
        return nextVersionDir;
    }

    public File getVersionDir(int versionNo) {
        return new File(baseDir, VersionUtil.toString(versionNo,
                versionNoPattern));
    }

    public File getCreateDir(File versionDir) {
        return new File(versionDir, CREATE_DIR_NAME);
    }

    public File getDropDir(File versionDir) {
        return new File(versionDir, DROP_DIR_NAME);
    }

    public DdlInfoFile getDdlInfoFile() {
        return ddlInfoFile;
    }

}

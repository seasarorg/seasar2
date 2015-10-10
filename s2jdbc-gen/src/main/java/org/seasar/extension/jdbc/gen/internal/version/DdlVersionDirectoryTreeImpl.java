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

import org.seasar.extension.jdbc.gen.version.DdlInfoFile;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.framework.log.Logger;

/**
 * {@link DdlVersionDirectoryTree}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlVersionDirectoryTreeImpl implements DdlVersionDirectoryTree {

    /** createディレクトリの名前 */
    protected static String CREATE_DIR_NAME = "create";

    /** dropディレクトリの名前 */
    protected static String DROP_DIR_NAME = "drop";

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(DdlVersionDirectoryTreeImpl.class);

    /** バージョン管理のベースディレクトリ */
    protected File baseDir;

    /** バージョン番号のパターン */
    protected String versionNoPattern;

    /** 環境名 */
    protected String env;

    /** DDLのバージョン */
    protected DdlInfoFile ddlInfoFile;

    /**
     * インスタンスを構築します。
     * 
     * @param baseDir
     *            バージョン管理のベースディレクトリ
     * @param versionFile
     *            バージョンファイル
     * @param versionNoPattern
     *            バージョン番号のパターン
     * @param env
     *            環境名、指定されない場合{@code null}
     */
    public DdlVersionDirectoryTreeImpl(File baseDir, File versionFile,
            String versionNoPattern, String env) {
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
        this.env = env;
        ddlInfoFile = createDdlInfoFile(versionFile);
    }

    public DdlVersionDirectory getCurrentVersionDirectory() {
        return createDdlVersionDirectory(ddlInfoFile.getCurrentVersionNo());
    }

    public DdlVersionDirectory getNextVersionDirectory() {
        return createDdlVersionDirectory(ddlInfoFile.getNextVersionNo());
    }

    public DdlVersionDirectory getVersionDirectory(int versionNo) {
        return createDdlVersionDirectory(versionNo);
    }

    public DdlInfoFile getDdlInfoFile() {
        return ddlInfoFile;
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

    /**
     * {@link DdlVersionDirectory}の実装を作成します。
     * 
     * @param versionNo
     *            バージョン番号
     * @return {@link DdlVersionDirectory}の実装
     */
    protected DdlVersionDirectory createDdlVersionDirectory(int versionNo) {
        return new DdlVersionDirectoryImpl(baseDir, versionNo,
                versionNoPattern, env);
    }

}

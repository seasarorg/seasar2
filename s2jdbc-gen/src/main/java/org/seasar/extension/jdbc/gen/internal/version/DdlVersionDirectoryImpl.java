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

import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.util.StringConversionUtil;

/**
 * {@link DdlVersionDirectory}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlVersionDirectoryImpl extends ManagedFileImpl implements
        DdlVersionDirectory {

    /** createディレクトリの名前 */
    protected static String CREATE_DIR_NAME = "create";

    /** dropディレクトリの名前 */
    protected static String DROP_DIR_NAME = "drop";

    /** バージョン番号 */
    protected int versionNo;

    /**
     * インスタンスを構築します。
     * 
     * @param baseDir
     *            ベースディレクトリ
     * @param versionNo
     *            バージョン番号
     * @param versionNoPattern
     *            バージョン番号パターン
     * @param env
     *            環境名
     */
    public DdlVersionDirectoryImpl(File baseDir, int versionNo,
            String versionNoPattern, String env) {
        super(getVersionDirPath(baseDir, versionNo, versionNoPattern), ".", env);
        this.versionNo = versionNo;
    }

    /**
     * バージョンディレクトリのパスを返します。
     * 
     * @param baseDir
     *            ベースディレクトリ
     * @param versionNo
     *            バージョン番号
     * @param versionNoPattern
     *            バージョン番号パターン
     * @return バージョンディレクトリのパス
     */
    protected static String getVersionDirPath(File baseDir, int versionNo,
            String versionNoPattern) {
        String versionName = StringConversionUtil.toString(versionNo,
                versionNoPattern);
        File versionDir = new File(baseDir, versionName);
        return FileUtil.getCanonicalPath(versionDir);
    }

    public ManagedFile getCreateDirectory() {
        return createChildInternal(CREATE_DIR_NAME);
    }

    public ManagedFile getDropDirectory() {
        return createChildInternal(DROP_DIR_NAME);
    }

    public int getVersionNo() {
        return versionNo;
    }

    public boolean isFirstVersion() {
        return versionNo == 0;
    }

}

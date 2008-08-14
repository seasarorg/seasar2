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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 次のバージョンのディレクトリがすでに存在する場合にスローされる例外です。
 * 
 * @author taedium
 */
public class NextVersionDirectoryExistsRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** バージョンディレクトリのパス */
    protected String versionDirPath;

    /** バージョンファイルのパス */
    protected String versionFilePath;

    /**
     * インスタンスを構築します。
     * 
     * @param versionDirPath
     *            バージョンディレクトリのパス
     * @param versionFilePath
     *            バージョンファイルのパス
     */
    public NextVersionDirectoryExistsRuntimeException(String versionDirPath,
            String versionFilePath) {
        super("ES2JDBCGen0008",
                new Object[] { versionDirPath, versionFilePath });
        this.versionDirPath = versionDirPath;
        this.versionFilePath = versionFilePath;
    }

    /**
     * バージョンディレクトリのパスを返します。
     * 
     * @return バージョンディレクトリのパス
     */
    public String getVersionDirPath() {
        return versionDirPath;
    }

    /**
     * バージョンファイルのパスを返します。
     * 
     * @return バージョンファイルのパス
     */
    public String getVersionFilePath() {
        return versionFilePath;
    }

}

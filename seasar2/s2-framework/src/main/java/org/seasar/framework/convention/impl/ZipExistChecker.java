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
package org.seasar.framework.convention.impl;

import java.net.URL;
import java.util.zip.ZipFile;

import org.seasar.framework.util.ZipFileUtil;

/**
 * zip用の存在チェッカです。
 * 
 * @author y-komori
 */
public class ZipExistChecker extends AbstractExistChecker {
    private ZipFile zipFile;

    private String rootPath;

    /**
     * インスタンスを作成します。
     * 
     * @param zipUrl
     *            zip URL
     * @param rootPackageName
     *            ルートパッケージ名
     */
    protected ZipExistChecker(final URL zipUrl, final String rootPackageName) {
        zipFile = ZipFileUtil.toZipFile(zipUrl);
        this.rootPath = rootPackageName.replace('.', '/') + "/";
    }

    public boolean isExist(final String lastClassName) {
        return zipFile.getEntry(rootPath + getPathName(lastClassName)) != null;
    }

    public void close() {
        ZipFileUtil.close(zipFile);
    }
}

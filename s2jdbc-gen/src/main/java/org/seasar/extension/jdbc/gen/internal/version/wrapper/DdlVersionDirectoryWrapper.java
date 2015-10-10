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
package org.seasar.extension.jdbc.gen.internal.version.wrapper;

import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.ManagedFile;

/**
 * {@link DdlVersionDirectory}のラッパーです。
 * 
 * @author taedium
 */
public class DdlVersionDirectoryWrapper extends ManagedFileWrapper implements
        DdlVersionDirectory {

    /**
     * インスタンスを構築します。
     * 
     * @param target
     *            ラップの対象
     * @param genDdlListener
     *            リスナー
     * @param currentVersionDir
     *            現バージョンに対応するディレクトリ
     * @param nextVersionDir
     *            次バージョンに対応するディレクトリを
     */
    public DdlVersionDirectoryWrapper(DdlVersionDirectory target,
            GenDdlListener genDdlListener,
            DdlVersionDirectory currentVersionDir,
            DdlVersionDirectory nextVersionDir) {
        super(target, genDdlListener, currentVersionDir, nextVersionDir);
    }

    public ManagedFile getCreateDirectory() {
        ManagedFile dir = getDirectory().getCreateDirectory();
        return new ManagedFileWrapper(this, dir, genDdlListener,
                currentVersionDir, nextVersionDir);
    }

    public ManagedFile getDropDirectory() {
        ManagedFile dir = getDirectory().getDropDirectory();
        return new ManagedFileWrapper(this, dir, genDdlListener,
                currentVersionDir, nextVersionDir);
    }

    public int getVersionNo() {
        return getDirectory().getVersionNo();
    }

    public boolean isFirstVersion() {
        return getDirectory().isFirstVersion();
    }

    /**
     * バージョンディレクトリを返します。
     * 
     * @return バージョンディレクトリ
     */
    protected DdlVersionDirectory getDirectory() {
        return DdlVersionDirectory.class.cast(target);
    }
}

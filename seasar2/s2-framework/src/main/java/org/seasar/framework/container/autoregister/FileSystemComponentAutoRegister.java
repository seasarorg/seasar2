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
package org.seasar.framework.container.autoregister;

import java.io.File;

import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * ファイルシステム上(例えばWEBINF/classes)のコンポーネントを自動登録するクラスです。
 * 
 * @author higa
 */
public class FileSystemComponentAutoRegister extends
        AbstractComponentAutoRegister {

    public void registerAll() {
        final File packageDir = getRootDir();
        final String[] referencePackages = getTargetPackages();
        for (int i = 0; i < referencePackages.length; ++i) {
            ClassTraversal.forEach(packageDir, referencePackages[i], this);
        }
    }

    /**
     * コンポーネントを検索する基点となるディレクトリを返します。
     * <p>
     * 基点となるディレクトリはこのコンポーネント自身を定義したdiconファイルのパスが見つかったディレクトリになります。
     * 例えばdiconファイルのパスが<code>"foo/bar.dicon"</code>で、このdiconファイルの絶対パスが
     * <code>/aaa/bbb/foo/bar.dicon</code>であれば、基点となるディレクトリは<code>/aaa/bbb</code>となります。
     * </p>
     * 
     * @return コンポーネントを検索する基点となるディレクトリ
     */
    protected File getRootDir() {
        final String path = getContainer().getPath();
        final String[] names = StringUtil.split(path, "/");
        File file = ResourceUtil.getResourceAsFile(path);
        for (int i = 0; i < names.length; ++i) {
            file = file.getParentFile();
        }
        return file;
    }

}

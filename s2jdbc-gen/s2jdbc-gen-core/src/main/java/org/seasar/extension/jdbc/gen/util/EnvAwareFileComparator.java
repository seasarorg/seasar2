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
package org.seasar.extension.jdbc.gen.util;

import java.io.File;
import java.util.Comparator;

/**
 * 環境名を考慮してファイルを比較する{@link Comparator}の実装クラスです。
 * <p>
 * 環境名が「ut」でhoge.txtという名前のファイルとhoge_ut.txtという名前のファイルを比較した場合、hoge.
 * txtがhoge_ut.txtより小さいと判定されます。
 * </p>
 * 
 * @author taedium
 */
public class EnvAwareFileComparator implements Comparator<File> {

    /** 環境名のサフィックス */
    protected String envSuffix;

    /**
     * @param env
     */
    public EnvAwareFileComparator(String env) {
        this.envSuffix = "_" + env;
    }

    public int compare(File file1, File file2) {
        String envIncludedName1 = removeExtension(file1.getName());
        String envIncludedName2 = removeExtension(file2.getName());
        String envExcludedName1 = removeEnvSuffix(envIncludedName1);
        String envExcludedName2 = removeEnvSuffix(envIncludedName2);

        int result = envExcludedName1.compareTo(envExcludedName2);
        if (result == 0) {
            if (envIncludedName1.endsWith(envSuffix)) {
                return envIncludedName2.endsWith(envSuffix) ? 0 : 1;
            }
            return envIncludedName2.endsWith(envSuffix) ? -1 : 0;
        }
        return result;
    }

    /**
     * 拡張子を除去します。
     * 
     * @param name
     *            名前
     * @return 拡張子が除去された値
     */
    protected String removeExtension(String name) {
        int pos = name.lastIndexOf('.');
        if (pos > -1) {
            return name.substring(0, pos);
        }
        return name;
    }

    /**
     * 環境名のサフィックスを除去します。
     * 
     * @param name
     *            名前
     * @return 環境名のサフィックスが除去された値
     */
    protected String removeEnvSuffix(String name) {
        if (name.endsWith(envSuffix)) {
            return name.substring(0, name.length() - envSuffix.length());
        }
        return name;
    }

}

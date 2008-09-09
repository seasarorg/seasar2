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
package org.seasar.extension.jdbc.gen.internal.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 環境名を考慮してフィルタする{@link FilenameFilter}の実装クラスです。
 * <p>
 * 
 *{@link #env}以外の環境名をもつファイルを除外します。 環境名を持たないファイルと{@link #env}
 * の環境名を持つファイルは除外しません。
 * </p>
 * 
 * @author taedium
 */
public class EnvAwareFilenameFilter implements FilenameFilter {

    /** 環境名 */
    protected String env;

    /** ファイル名のフィルタ */
    protected FilenameFilter filenameFilter;

    /**
     * インスタンスを構築します。
     * 
     * @param env
     *            環境名
     */
    public EnvAwareFilenameFilter(String env) {
        if (env == null) {
            throw new NullPointerException("env");
        }
        this.env = env;
        filenameFilter = new DefaultExcludesFilenameFilter();
    }

    public boolean accept(File dir, String name) {
        if (filenameFilter.accept(dir, name)) {
            String nameEnv = getEnv(name);
            if (nameEnv == null || env.equals(nameEnv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ファイル名に含まれる環境名を返します。
     * 
     * @param name
     *            ファイル名
     * @return 環境名
     */
    protected String getEnv(String name) {
        int pos = name.lastIndexOf('.');
        if (pos < 0) {
            return null;
        }
        String s = name.substring(0, pos);
        pos = name.lastIndexOf(EnvUtil.DELIMITER);
        if (pos < 1) {
            return null;
        }
        return name.substring(pos + 1, s.length());
    }
}

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
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * 一般的に除外すべき名前をフィルタする{@link ExcludeFilenameFilter}の実装クラスです。
 * 
 * @author taedium
 */
public class ExcludeFilenameFilter implements FilenameFilter {

    /** デフォルトの除外名の配列 */
    protected static String[] defaultExclusionNames = new String[] { ".*~",
            "#.*#", "\\.#.*", "%.*%", "\\._.*", "CVS", "\\.cvsignore", "SCCS",
            "vssver\\.scc", "\\.svn", "\\.DS_Store" };

    /** 除外パターン */
    protected Pattern exclusionPattern;

    /**
     * インスタンスを構築します。
     */
    public ExcludeFilenameFilter() {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        for (String s : defaultExclusionNames) {
            buf.append(s);
            buf.append("|");
        }
        buf.setLength(buf.length() - 1);
        buf.append(")");
        exclusionPattern = Pattern.compile(buf.toString());
    }

    public boolean accept(File dir, String name) {
        return !exclusionPattern.matcher(name).matches();
    }

}

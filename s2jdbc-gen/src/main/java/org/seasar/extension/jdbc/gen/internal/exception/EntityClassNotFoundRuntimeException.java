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
package org.seasar.extension.jdbc.gen.internal.exception;

import java.io.File;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 条件に合うエンティティクラスが1つも見つからない場合にスローされる例外です。
 * 
 * @author taedium
 */
public class EntityClassNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** パッケージ名 */
    protected String packageName;

    /** 対象とするエンティティクラス名の正規表現 */
    protected String shortClassNamePattern;

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreShortClassNamePattern;

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param packageName
     *            パッケージ名
     * @param shortClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     * @param ignoreShortClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public EntityClassNotFoundRuntimeException(File classpathDir,
            String packageName, String shortClassNamePattern,
            String ignoreShortClassNamePattern) {
        super("ES2JDBCGen0014",
                new Object[] { classpathDir.getPath(), packageName,
                        shortClassNamePattern, ignoreShortClassNamePattern });
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.shortClassNamePattern = shortClassNamePattern;
        this.ignoreShortClassNamePattern = ignoreShortClassNamePattern;
    }

    /**
     * クラスパスのディレクトリを返します。
     * 
     * @return クラスパスのディレクトリ
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * パッケージ名を返します。
     * 
     * @return パッケージ名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を返します。
     * 
     * @return 対象とするエンティティクラス名の正規表現
     */
    public String getShortClassNamePattern() {
        return shortClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を返します。
     * 
     * @return 対象としないエンティティクラス名の正規表現
     */
    public String getIgnoreShortClassNamePattern() {
        return ignoreShortClassNamePattern;
    }

}

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

    /** 対象とするエンティティ名の正規表現 */
    protected String entityNamePattern;

    /** 対象としないエンティティ名の正規表現 */
    protected String ignoreEntityNamePattern;

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param packageName
     *            パッケージ名
     * @param entityNamePattern
     *            対象とするエンティティ名の正規表現
     * @param ignoreEntityNamePattern
     *            対象としないエンティティ名の正規表現
     */
    public EntityClassNotFoundRuntimeException(File classpathDir,
            String packageName, String entityNamePattern,
            String ignoreEntityNamePattern) {
        super("ES2JDBCGen0014", new Object[] { classpathDir.getPath(),
                packageName, entityNamePattern, ignoreEntityNamePattern });
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.entityNamePattern = entityNamePattern;
        this.ignoreEntityNamePattern = ignoreEntityNamePattern;
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
     * 対象とするエンティティ名の正規表現を返します。
     * 
     * @return 対象とするエンティティ名の正規表現
     */
    public String getEntityNamePattern() {
        return entityNamePattern;
    }

    /**
     * 対象としないエンティティ名の正規表現を返します。
     * 
     * @return 対象としないエンティティ名の正規表現
     */
    public String getIgnoreEntityNamePattern() {
        return ignoreEntityNamePattern;
    }

}

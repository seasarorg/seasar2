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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.internal.exception.EntityClassNotFoundRuntimeException;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * {@link EntityMetaReader}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityMetaReaderImpl implements EntityMetaReader {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(EntityMetaReaderImpl.class);

    /** ルートディレクトリ */
    protected File classpathDir;

    /** 読み取り対象とするパッケージ名 */
    protected String packageName;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** 読み取り対象とするエンティティクラス名のパターン */
    protected Pattern shortClassNamePattern;

    /** 読み取り非対象とするエンティティクラス名のパターン */
    protected Pattern ignoreShortClassNamePattern;

    /**
     * インタスタンスを構築します。
     * 
     * @param classpathDir
     *            ルートディレクトリ
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     * @param shortClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     * @param ignoreShortClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public EntityMetaReaderImpl(File classpathDir, String packageName,
            EntityMetaFactory entityMetaFactory, String shortClassNamePattern,
            String ignoreShortClassNamePattern) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        if (shortClassNamePattern == null) {
            throw new NullPointerException("shortClassNamePattern");
        }
        if (ignoreShortClassNamePattern == null) {
            throw new NullPointerException("setEntityClassNamePattern");
        }
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.entityMetaFactory = entityMetaFactory;
        this.shortClassNamePattern = Pattern.compile(shortClassNamePattern);
        this.ignoreShortClassNamePattern = Pattern
                .compile(ignoreShortClassNamePattern);
    }

    public List<EntityMeta> read() {
        final List<EntityMeta> entityMetaList = new ArrayList<EntityMeta>();

        ClassTraversal.forEach(classpathDir, new ClassHandler() {

            public void processClass(String packageName, String shortClassName) {
                if (isTargetPackage(packageName)
                        && isTargetClass(shortClassName)) {
                    String className = ClassUtil.concatName(packageName,
                            shortClassName);
                    Class<?> clazz = ClassUtil.forName(className);
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        EntityMeta entityMeta = entityMetaFactory
                                .getEntityMeta(clazz);
                        entityMetaList.add(entityMeta);
                    }
                }
            }
        });

        if (entityMetaList.isEmpty()) {
            throw new EntityClassNotFoundRuntimeException(classpathDir,
                    packageName, shortClassNamePattern.pattern(),
                    ignoreShortClassNamePattern.pattern());
        }
        return entityMetaList;
    }

    /**
     * 読み取りの対象パッケージの場合{@code true}を返します。
     * 
     * @param packageName
     *            パッケージ名
     * @return 読み取りの対象パッケージの場合{@code true}
     */
    protected boolean isTargetPackage(String packageName) {
        if (packageName == null) {
            return true;
        }
        if (packageName.equals(this.packageName)) {
            return true;
        }
        if (packageName.startsWith(this.packageName + ".")) {
            return true;
        }
        return false;
    }

    /**
     * 読み取りの対象クラスの場合{@code true}を返します。
     * 
     * @param shortClassName
     *            クラスの単純名
     * @return 読み取りの対象クラスの場合{@code true}
     */
    protected boolean isTargetClass(String shortClassName) {
        if (!shortClassNamePattern.matcher(shortClassName).matches()) {
            return false;
        }
        if (ignoreShortClassNamePattern.matcher(shortClassName).matches()) {
            return false;
        }
        return true;
    }

}

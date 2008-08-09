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
package org.seasar.extension.jdbc.gen.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
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

    /** 読み取り対象とするエンティティ名のパターン */
    protected Pattern entityNamePattern;

    /** 読み取り非対象とするエンティティ名のパターン */
    protected Pattern ignoreEntityNamePattern;

    /**
     * インタスタンスを構築します。
     * 
     * @param classpathDir
     *            ルートディレクトリ
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     * @param entityNamePattern
     *            対象とするエンティティ名の正規表現
     * @param ignoreEntityNamePattern
     *            対象としないエンティティ名の正規表現
     */
    public EntityMetaReaderImpl(File classpathDir, String packageName,
            EntityMetaFactory entityMetaFactory, String entityNamePattern,
            String ignoreEntityNamePattern) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        if (entityNamePattern == null) {
            throw new NullPointerException("entityNamePattern");
        }
        if (ignoreEntityNamePattern == null) {
            throw new NullPointerException("ignoreEntityNamePattern");
        }
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.entityMetaFactory = entityMetaFactory;
        this.entityNamePattern = Pattern.compile(entityNamePattern);
        this.ignoreEntityNamePattern = Pattern.compile(ignoreEntityNamePattern);
    }

    public List<EntityMeta> read() {
        final List<EntityMeta> entityMetaList = new ArrayList<EntityMeta>();

        ClassTraversal.forEach(classpathDir, new ClassHandler() {

            public void processClass(String packageName, String shortClassName) {
                if (isTargetClass(packageName, shortClassName)) {
                    String className = ClassUtil.concatName(packageName,
                            shortClassName);
                    Class<?> clazz = ClassUtil.forName(className);
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        EntityMeta entityMeta = entityMetaFactory
                                .getEntityMeta(clazz);
                        if (isTargetEntity(entityMeta)) {
                            entityMetaList.add(entityMeta);
                        }
                    }
                }
            }
        });

        if (entityMetaList.isEmpty()) {
            logger.log("ES2JDBCGen0014", new Object[] { classpathDir.getPath(),
                    packageName, entityNamePattern.pattern(),
                    ignoreEntityNamePattern.pattern() });
        }
        return entityMetaList;
    }

    /**
     * 読み取りの対象クラスの場合{@code true}を返します。
     * 
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            クラスの単純名
     * @return 読み取りの対象クラスの場合{@code true}
     */
    protected boolean isTargetClass(String packageName, String shortClassName) {
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
     * 読み取り対象エンティティの場合{@code true}を返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @return 読み取り対象エンティティの場合{@code true}
     */
    protected boolean isTargetEntity(EntityMeta entityMeta) {
        String name = entityMeta.getName();
        if (!entityNamePattern.matcher(name).matches()) {
            return false;
        }
        if (ignoreEntityNamePattern.matcher(name).matches()) {
            return false;
        }
        return true;
    }
}

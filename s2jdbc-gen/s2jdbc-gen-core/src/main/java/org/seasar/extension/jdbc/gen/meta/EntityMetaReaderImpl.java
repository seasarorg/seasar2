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

import javax.persistence.Entity;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * {@link EntityMetaReader}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityMetaReaderImpl implements EntityMetaReader {

    /** ルートディレクトリ */
    protected File rootDir;

    /** 読み取り対象とするパッケージ名 */
    protected String packageName;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /**
     * インタスタンスを構築します。
     * 
     * @param rootDir
     *            ルートディレクトリ
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     */
    public EntityMetaReaderImpl(File rootDir, String packageName,
            EntityMetaFactory entityMetaFactory) {
        if (rootDir == null) {
            throw new NullPointerException("rootDir");
        }
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        this.rootDir = rootDir;
        this.packageName = packageName;
        this.entityMetaFactory = entityMetaFactory;
    }

    public List<EntityMeta> read() {
        final List<EntityMeta> entityMetaList = new ArrayList<EntityMeta>();

        ClassTraversal.forEach(rootDir, new ClassHandler() {

            public void processClass(String packageName, String shortClassName) {
                if (!isTarget(packageName, shortClassName)) {
                    return;
                }
                String className = ClassUtil.concatName(packageName,
                        shortClassName);
                Class<?> clazz = ClassUtil.forName(className);
                if (clazz.isAnnotationPresent(Entity.class)) {
                    EntityMeta entityMeta = entityMetaFactory
                            .getEntityMeta(clazz);
                    entityMetaList.add(entityMeta);
                }
            }
        });

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
    protected boolean isTarget(String packageName, String shortClassName) {
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
}

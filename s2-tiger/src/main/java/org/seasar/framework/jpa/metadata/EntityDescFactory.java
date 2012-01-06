/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.metadata;

import java.util.concurrent.ConcurrentMap;

import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitManagerLocater;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link EntityDesc}のファクトリです。
 * 
 * @author koichik
 */
public class EntityDescFactory {

    /** クラスに対応する{@link EntityDesc}が存在しないことを示すオブジェクト */
    protected static final EntityDesc NOT_FOUND = new NotFound();

    /** 初期化されているかどうかを表すフラグ */
    protected static boolean initialized;

    /** エンティティクラスをキー、エンティティ定義を値とするマップ */
    protected static final ConcurrentMap<Class<?>, EntityDesc> entityDescs = CollectionsUtil
            .newConcurrentHashMap();

    /**
     * このクラスを初期化します。
     * 
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    /**
     * このクラスがキャッシュしている値を消去し、初期化以前の状態にします。
     * 
     */
    public static void clear() {
        entityDescs.clear();
        initialized = false;
    }

    /**
     * エンティティクラスを表現する{@link EntityDesc}を返します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return エンティティクラスを表現する{@link EntityDesc}
     */
    public static EntityDesc getEntityDesc(final Class<?> entityClass) {
        initialize();
        final EntityDesc entityDesc = entityDescs.get(entityClass);
        if (entityDesc != null) {
            return entityDesc == NOT_FOUND ? null : entityDesc;
        }
        return createEntityDesc(entityClass);
    }

    /**
     * エンティティクラスを表現する{@link EntityDesc}を作成します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return エンティティクラスを表現する{@link EntityDesc}
     */
    protected static EntityDesc createEntityDesc(final Class<?> entityClass) {
        final PersistenceUnitManager manager = PersistenceUnitManagerLocater
                .getInstance();
        final PersistenceUnitProvider unitProvider = manager
                .getPersistenceUnitProvider(manager
                        .getConcretePersistenceUnitName(entityClass));
        final EntityDesc entityDesc = unitProvider.getEntityDescProvider()
                .createEntityDesc(unitProvider.getEntityManagerFactory(),
                        entityClass);
        if (entityDesc == null) {
            CollectionsUtil.putIfAbsent(entityDescs, entityClass, NOT_FOUND);
            return null;
        }
        return CollectionsUtil
                .putIfAbsent(entityDescs, entityClass, entityDesc);
    }

    private static class NotFound implements EntityDesc {

        public AttributeDesc getAttributeDesc(String attributeName) {
            return null;
        }

        public AttributeDesc[] getAttributeDescs() {
            return null;
        }

        public String[] getAttributeNames() {
            return null;
        }

        public Class<?> getEntityClass() {
            return null;
        }

        public String getEntityName() {
            return null;
        }

        public AttributeDesc getIdAttributeDesc() {
            return null;
        }

    }

}

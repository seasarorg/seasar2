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
package org.seasar.extension.jdbc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * マッピング中の状態を管理するためのコンテキストです。
 * 
 * @author higa
 * 
 */
public class MappingContext {

    /**
     * エンティティのキャッシュです。
     */
    protected Map<String, Map<Object, Object>> cache;

    /**
     * 処理済みのエンティティのマップです。
     */
    protected Map<Object, Map<Object, Set<Object>>> doneEntityMap;

    /**
     * {@link MappingContext}を作成します。
     * 
     */
    public MappingContext() {
        this(100);
    }

    /**
     * {@link MappingContext}を作成します。
     * 
     * @param initialCapacity
     *            キャッシュの初期容量
     */
    public MappingContext(int initialCapacity) {
        cache = new HashMap<String, Map<Object, Object>>(initialCapacity);
        doneEntityMap = new HashMap<Object, Map<Object, Set<Object>>>(
                initialCapacity * 2);
    }

    /**
     * キャッシュしているエンティティを返します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param key
     *            キー
     * @return キャッシュしているエンティティ
     */
    public Object getCache(Class<?> entityClass, Object key) {
        Map<Object, Object> m = cache.get(entityClass.getName());
        if (m == null) {
            return null;
        }
        return m.get(key);
    }

    /**
     * エンティティをキャッシュします。
     * 
     * @param entityClass
     *            エンティティクラス
     * @param key
     *            キー
     * @param entity
     *            エンティティ
     */
    public void setCache(Class<?> entityClass, Object key, Object entity) {
        Map<Object, Object> m = cache.get(entityClass.getName());
        if (m == null) {
            m = new HashMap<Object, Object>();
            cache.put(entityClass.getName(), m);
        }
        m.put(key, entity);
    }

    /**
     * 同一のエンティティマッパーで処理しているエンティティが処理済みかどうかを返します。
     * 
     * @param entityMapper
     *            エンティティマッパーもしくは関連エンティティマッパー
     * @param target
     *            関連元のエンティティ
     * @param entity
     *            エンティティ
     * @return 同一のエンティティマッパーで処理しているエンティティが処理済みか
     */
    public boolean checkDone(Object entityMapper, Object target, Object entity) {
        Map<Object, Set<Object>> m = doneEntityMap.get(entityMapper);
        if (m == null) {
            m = new HashMap<Object, Set<Object>>();
            doneEntityMap.put(entityMapper, m);
        }
        Set<Object> entities = m.get(target);
        if (entities == null) {
            entities = new HashSet<Object>();
            m.put(target, entities);
        }
        return !entities.add(entity);
    }

    /**
     * 状態をクリアします。
     */
    public void clear() {
        cache.clear();
        doneEntityMap.clear();
    }
}
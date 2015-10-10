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
package org.seasar.extension.jdbc.gen.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.ArrayMap;

/**
 * エンティティ集合記述です。
 * 
 * @author taedium
 */
public class EntitySetDesc {

    /** エンティティ記述のリスト */
    protected List<EntityDesc> entityDescList = new ArrayList<EntityDesc>();

    /** 完全なテーブル名をキー、エンティティ記述を値とするマップ */
    @SuppressWarnings("unchecked")
    protected Map<String, EntityDesc> entityDescMap = new ArrayMap();

    /**
     * エンティティ記述のリストを返します。
     * 
     * @return エンティティ記述のリスト
     */
    public List<EntityDesc> getEntityDescList() {
        return Collections.unmodifiableList(entityDescList);
    }

    /**
     * エンティティ記述を追加します。
     * 
     * @param entityDesc
     *            エンティティ記述
     */
    public void addEntityDesc(EntityDesc entityDesc) {
        entityDescList.add(entityDesc);
        entityDescMap.put(entityDesc.getFullTableName(), entityDesc);
    }

    /**
     * 完全なテーブル名に対応するエンティティ記述を返します。
     * 
     * @param fullTableName
     *            完全なテーブル名
     * @return エンティティ記述、存在しない場合{@code null}
     */
    public EntityDesc getEntityDesc(String fullTableName) {
        return entityDescMap.get(fullTableName);
    }

}

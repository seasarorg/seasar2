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
package org.seasar.extension.jdbc.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * エンティティ記述です。
 * 
 * @author taedium
 */
public class EntityDesc {

    /** 名前 */
    protected String name;

    /** テーブルの名前 */
    protected String tableName;

    /** 複合識別子をもつならば{@code true} */
    protected boolean compositeId;

    /** 属性記述のリスト */
    protected List<AttributeDesc> attributeDescList = new ArrayList<AttributeDesc>();

    /** 識別子である属性記述のリスト */
    protected List<AttributeDesc> idAttributeDescList = new ArrayList<AttributeDesc>();

    /**
     * インスタンスを構築します。
     */
    public EntityDesc() {
    }

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * テーブルの名前を返します。
     * 
     * @return テーブルの名前
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブルの名前を設定します。
     * 
     * @param tableName
     *            テーブルの名前
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 属性を追加します。
     * 
     * @param attributeDesc
     *            属性
     */
    public void addAttribute(AttributeDesc attributeDesc) {
        attributeDescList.add(attributeDesc);
        if (attributeDesc.isId()) {
            idAttributeDescList.add(attributeDesc);
            if (idAttributeDescList.size() > 1) {
                compositeId = true;
            }
        }
    }

    /**
     * 属性のリストを返します。
     * 
     * @return 属性のリスト
     */
    public List<AttributeDesc> getAttributeDescList() {
        return Collections.unmodifiableList(attributeDescList);
    }

    /**
     * 識別子である属性のリストを返します。
     * 
     * @return 識別子である属性のリスト
     */
    public List<AttributeDesc> getIdAttributeDescList() {
        return Collections.unmodifiableList(idAttributeDescList);
    }

    /**
     * 複合識別子を持つならば{@code true}を返します。
     * 
     * @return 複合識別子を持つならば{@code true}、そうでないならば{@code false}
     */
    public boolean hasCompositeId() {
        return compositeId;
    }
}

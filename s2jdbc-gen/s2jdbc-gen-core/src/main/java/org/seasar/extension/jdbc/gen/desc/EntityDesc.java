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
package org.seasar.extension.jdbc.gen.desc;

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

    /** カタログ名 */
    protected String catalogName;

    /** スキーマ名 */
    protected String schemaName;

    /** テーブル名 */
    protected String tableName;

    protected String fullTableName;

    /** 複合識別子をもつならば{@code true} */
    protected boolean compositeId;

    /** 属性記述のリスト */
    protected List<AttributeDesc> attributeDescList = new ArrayList<AttributeDesc>();

    /** 識別子である属性記述のリスト */
    protected List<AttributeDesc> idAttributeDescList = new ArrayList<AttributeDesc>();

    protected List<AssociationDesc> associationDescList = new ArrayList<AssociationDesc>();

    protected List<InverseAssociationDesc> inverseAssociationDescList = new ArrayList<InverseAssociationDesc>();

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
     * カタログ名を返します。
     * 
     * @return カタログ名
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * カタログ名を設定します。
     * 
     * @param catalogName
     *            カタログ名
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * スキーマ名を返します
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブル名を設定します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return Returns the fullTableName.
     */
    public String getFullTableName() {
        return fullTableName;
    }

    /**
     * @param fullTableName
     *            The fullTableName to set.
     */
    public void setFullTableName(String fullTableName) {
        this.fullTableName = fullTableName;
    }

    /**
     * 属性記述を追加します。
     * 
     * @param attributeDesc
     *            属性記述
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
     * 属性記述のリストを返します。
     * 
     * @return 属性記述のリスト
     */
    public List<AttributeDesc> getAttributeDescList() {
        return Collections.unmodifiableList(attributeDescList);
    }

    /**
     * 識別子である属性記述のリストを返します。
     * 
     * @return 識別子である属性記述のリスト
     */
    public List<AttributeDesc> getIdAttributeDescList() {
        return Collections.unmodifiableList(idAttributeDescList);
    }

    /**
     * @return Returns the associationDescList.
     */
    public List<AssociationDesc> getAssociationDescList() {
        return Collections.unmodifiableList(associationDescList);
    }

    /**
     * @param associationDescList
     *            The associationDescList to set.
     */
    public void addAssociationDesc(AssociationDesc associationDesc) {
        associationDescList.add(associationDesc);
    }

    /**
     * @return Returns the inverseAssociationDescList.
     */
    public List<InverseAssociationDesc> getInverseAssociationDescList() {
        return inverseAssociationDescList;
    }

    /**
     * @param inverseAssociationDescList
     *            The inverseAssociationDescList to set.
     */
    public void addInverseAssociationDesc(
            InverseAssociationDesc inverseAssociationDesc) {
        inverseAssociationDescList.add(inverseAssociationDesc);
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

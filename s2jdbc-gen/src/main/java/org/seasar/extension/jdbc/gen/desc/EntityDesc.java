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

    /** 複合識別子をもつならば{@code true} */
    protected boolean compositeId;

    /** コメント */
    protected String comment;

    /** 属性記述のリスト */
    protected List<AttributeDesc> attributeDescList = new ArrayList<AttributeDesc>();

    /** 識別子である属性記述のリスト */
    protected List<AttributeDesc> idAttributeDescList = new ArrayList<AttributeDesc>();

    /** 関連記述のリスト */
    protected List<AssociationDesc> associationDescList = new ArrayList<AssociationDesc>();

    /** 一意制約記述のリスト */
    protected List<CompositeUniqueConstraintDesc> compositeUniqueConstraintDescList = new ArrayList<CompositeUniqueConstraintDesc>();

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
     * 完全なテーブル名を返します。
     * 
     * @return 完全なテーブル名
     */
    public String getFullTableName() {
        StringBuilder buf = new StringBuilder();
        if (catalogName != null) {
            buf.append(catalogName).append(".");
        }
        if (schemaName != null) {
            buf.append(schemaName).append(".");
        }
        return buf.append(tableName).toString();
    }

    /**
     * コメントを返します。
     * 
     * @return コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * コメントを設定します。
     * 
     * @param comment
     *            コメント
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 属性記述を追加します。
     * 
     * @param attributeDesc
     *            属性記述
     */
    public void addAttributeDesc(AttributeDesc attributeDesc) {
        attributeDescList.add(attributeDesc);
        if (attributeDesc.isId()) {
            idAttributeDescList.add(attributeDesc);
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
     * 指定された属性名の属性記述を持っている場合{@code true}を返します。
     * 
     * @param attributeName
     *            属性名
     * @return 指定された属性名の属性記述を持っている場合{@code true}、そうでない場合{@code false}
     */
    public boolean hasAttributeDesc(String attributeName) {
        for (AttributeDesc attributeDesc : attributeDescList) {
            if (attributeDesc.getName().equals(attributeName)) {
                return true;
            }
        }
        return false;
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
     * 関連記述のリストを返します。
     * 
     * @return 関連記述のリスト
     */
    public List<AssociationDesc> getAssociationDescList() {
        return Collections.unmodifiableList(associationDescList);
    }

    /**
     * 関連記述を追加します。
     * 
     * @param associationDesc
     *            関連記述
     */
    public void addAssociationDesc(AssociationDesc associationDesc) {
        associationDescList.add(associationDesc);
    }

    /**
     * 指定された関連名の関連記述を持っている場合{@code true}を返します。
     * 
     * @param associationName
     *            関連名
     * @return 指定された関連名の関連記述を持っている場合{@code true}、そうでない場合{@code false}
     */
    public boolean hasAssociationDesc(String associationName) {
        for (AssociationDesc associationDesc : associationDescList) {
            if (associationDesc.getName().equals(associationName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 複合一意制約記述のリストを返します。
     * 
     * @return 複合一意制約記述のリスト
     */
    public List<CompositeUniqueConstraintDesc> getCompositeUniqueConstraintDescList() {
        return Collections.unmodifiableList(compositeUniqueConstraintDescList);
    }

    /**
     * 複合一意制約記述を追加します。
     * 
     * @param compositeUniqueConstraintDesc
     *            複合一意制約記述
     */
    public void addCompositeUniqueConstraintDesc(
            CompositeUniqueConstraintDesc compositeUniqueConstraintDesc) {
        compositeUniqueConstraintDescList.add(compositeUniqueConstraintDesc);
    }

    /**
     * 複合識別子を持つならば{@code true}を返します。
     * 
     * @return 複合識別子を持つならば{@code true}、そうでないならば{@code false}
     */
    public boolean hasCompositeId() {
        return idAttributeDescList.size() > 1;
    }

}

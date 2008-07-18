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
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * エンティティクラスのモデルです。
 * 
 * @author taedium
 */
public class EntityModel {

    /** カタログ名 */
    protected String catalogName;

    /** スキーマ名 */
    protected String schemaName;

    /** インポート名のソートされたセット */
    protected SortedSet<String> importNameSet = new TreeSet<String>();

    /** パッケージ名 */
    protected String packageName;

    /** クラスの単純名 */
    protected String shortClassName;

    /** 複合識別子を持つ場合{@code true} */
    protected boolean compositeId;

    /** 属性モデルのリスト */
    protected List<AttributeModel> attributeModelList = new ArrayList<AttributeModel>();

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
     * スキーマ名を返します。
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
     * パッケージ名を返します。
     * 
     * @return パッケージ名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * パッケージ名を設定します。
     * 
     * @param packageName
     *            パッケージ名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * クラスの単純名を返します。
     * 
     * @return クラスの単純名
     */
    public String getShortClassName() {
        return shortClassName;
    }

    /**
     * クラスの単純名を設定します。
     * 
     * @param shortClassName
     *            クラスの単純名
     */
    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    /**
     * インポート名のソートされたセットを返します。
     * 
     * @return インポート名のソートされたセット
     */
    public SortedSet<String> getImportNameSet() {
        return Collections.unmodifiableSortedSet(importNameSet);
    }

    /**
     *インポート名を追加します。
     * 
     * @param name
     *            インポート名
     */
    public void addImportName(String name) {
        importNameSet.add(name);
    }

    /**
     * 属性モデルを追加します。
     * 
     * @param attributeModel
     *            属性モデル
     */
    public void addAttributeModel(AttributeModel attributeModel) {
        attributeModelList.add(attributeModel);
    }

    /**
     * 属性モデルを返します。
     * 
     * @return 属性モデル
     */
    public List<AttributeModel> getAttributeModelList() {
        return Collections.unmodifiableList(attributeModelList);
    }

    /**
     * 複合識別子を持つ場合{@code true}を返します。
     * 
     * @return 複合識別子を持つ場合{@code true}
     */
    public boolean hasCompositeId() {
        return compositeId;
    }

    /**
     * 複合識別子を持つ場合{@code true}を設定します。
     * 
     * @param compositeId
     *            複合識別子を持つ場合{@code true}
     */
    public void setCompositeId(boolean compositeId) {
        this.compositeId = compositeId;
    }

}
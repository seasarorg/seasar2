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

import org.seasar.extension.jdbc.EntityMeta;

/**
 * 条件クラスのモデルです。
 * 
 * @author taedium
 */
public class ConditionModel {

    /** パッケージ名 */
    protected String packageName;

    /** クラスの単純名 */
    protected String shortClassName;

    /** エンティティメタデータ */
    protected EntityMeta entityMeta;

    /** インポートパッケージ名のソートされたセット */
    protected SortedSet<String> importPackageNameSet = new TreeSet<String>();

    /** 条件クラスの属性モデルのリスト */
    protected List<ConditionAttributeModel> conditionAttributeModelList = new ArrayList<ConditionAttributeModel>();

    /** 条件クラスの属性メソッドのリスト */
    protected List<ConditionMethodModel> conditionMethodModelList = new ArrayList<ConditionMethodModel>();

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
     * @return
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
     * インポートパッケージ名のソートされたセットを返します。
     * 
     * @return インポートパッケージ名のソートされたセット
     */
    public SortedSet<String> getImportPackageNameSet() {
        return Collections.unmodifiableSortedSet(importPackageNameSet);
    }

    /**
     * インポートパッケージ名を追加します。
     * 
     * @param name
     *            インポートパッケージ名
     */
    public void addImportPackageName(String name) {
        importPackageNameSet.add(name);
    }

    /**
     * 条件クラスの属性モデルのリストを返します。
     * 
     * @return 条件クラスの属性モデルのリスト
     */
    public List<ConditionAttributeModel> getConditionAttributeModelList() {
        return Collections.unmodifiableList(conditionAttributeModelList);
    }

    /**
     * 条件クラスの属性モデルを追加します。
     * 
     * @param conditionAttributeModel
     *            条件クラスの属性モデル
     */
    public void addConditionAttributeModel(
            ConditionAttributeModel conditionAttributeModel) {
        conditionAttributeModelList.add(conditionAttributeModel);
    }

    /**
     * 条件クラスのメソッドモデルのリストを返します。
     * 
     * @return 条件クラスのメソッドモデルのリスト
     */
    public List<ConditionMethodModel> getConditionMethodModelList() {
        return Collections.unmodifiableList(conditionMethodModelList);
    }

    /**
     * 条件クラスのメソッドモデルを追加します。
     * 
     * @param conditionMethodModel
     *            条件クラスのメソッドモデル
     */
    public void addConditionMethodModel(
            ConditionMethodModel conditionMethodModel) {
        conditionMethodModelList.add(conditionMethodModel);
    }

    /**
     * エンティティメタデータを返します。
     * 
     * @return
     */
    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    /**
     * エンティティメタデータを設定します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     */
    public void setEntityMeta(EntityMeta entityMeta) {
        this.entityMeta = entityMeta;
    }

}

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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.seasar.extension.jdbc.PropertyMeta;

/**
 * サービスモデルです。
 * 
 * @author taedium
 */
public class ServiceModel implements ClassModel {

    /** パッケージ名 */
    protected String packageName;

    /** クラスの単純名 */
    protected String shortClassName;

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** 名前モデル */
    protected NamesModel namesModel;

    /** インポート名のソートされたセット */
    protected SortedSet<String> importNameSet = new TreeSet<String>();

    /** 識別子のプロパティメタデータのリスト */
    protected List<PropertyMeta> idPropertyMetaList = new ArrayList<PropertyMeta>();

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
     * エンティティクラスの単純名を返します。
     * 
     * @return エンティティクラスの単純名
     */
    public String getShortEntityClassName() {
        return shortEntityClassName;
    }

    /**
     * エンティティクラスの単純名を設定します。
     * 
     * @param shortEntityClassName
     *            エンティティクラスの単純名
     */
    public void setShortEntityClassName(String shortEntityClassName) {
        this.shortEntityClassName = shortEntityClassName;
    }

    /**
     * 名前モデルを返します。
     * 
     * @return 名前モデル
     */
    public NamesModel getNamesModel() {
        return namesModel;
    }

    /**
     * 名前モデルを設定します。
     * 
     * @param namesModel
     *            名前モデル
     */
    public void setNamesModel(NamesModel namesModel) {
        this.namesModel = namesModel;
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
     * インポート名を追加します。
     * 
     * @param name
     *            インポート名
     */
    public void addImportName(String name) {
        importNameSet.add(name);
    }

    /**
     * 識別子のプロパティメタデータを追加します。
     * 
     * @param idPropertyMeta
     *            識別子のプロパティメタデータ
     */
    public void addIdPropertyMeta(PropertyMeta idPropertyMeta) {
        idPropertyMetaList.add(idPropertyMeta);
    }

    /**
     * 識別子のプロパティメタデータのリストを返します。
     * 
     * @return 識別子のプロパティメタデータのリスト
     */
    public List<PropertyMeta> getIdPropertyMetaList() {
        return Collections.unmodifiableList(idPropertyMetaList);
    }

}

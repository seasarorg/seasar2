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

import org.seasar.extension.jdbc.gen.desc.AssociationType;

/**
 * 関連モデルです。
 * 
 * @author taedium
 */
public class AssociationModel {

    /** 名前 */
    protected String name;

    /** クラスの単純名 */
    protected String shortClassName;

    /** 関連タイプ */
    protected AssociationType associationType;

    /** 関連の所有者側のプロパティの名前 */
    protected String mappedBy;

    /** 結合カラムモデル */
    protected JoinColumnModel joinColumnModel;

    /** 結合カラムモデルのリスト */
    protected List<JoinColumnModel> joinColumnModelList = new ArrayList<JoinColumnModel>();

    /** 結合カラムが2つ以上ある場合{@code true}、そうでない場合{@code false} */
    protected boolean compositeJoin;

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
     * 関連タイプを返します。
     * 
     * @return 関連タイプ
     */
    public AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * 関連タイプを設定します。
     * 
     * @param associationType
     *            関連タイプ
     */
    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    /**
     * 関連の所有者側のプロパティの名前を返します。
     * 
     * @return 関連の所有者側のプロパティの名前
     */
    public String getMappedBy() {
        return mappedBy;
    }

    /**
     * 関連の所有者側のプロパティの名前を設定します。
     * 
     * @param mappedBy
     *            関連の所有者側のプロパティの名前
     */
    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    /**
     * 結合カラムモデルを返します。
     * 
     * @return 結合カラムモデル
     */
    public JoinColumnModel getJoinColumnModel() {
        return joinColumnModel;
    }

    /**
     * 結合カラムモデルを設定します。
     * 
     * @param joinColumnModel
     *            結合カラムモデル
     */
    public void setJoinColumnModel(JoinColumnModel joinColumnModel) {
        if (!joinColumnModelList.isEmpty()) {
            throw new IllegalStateException("joinColumnModelList");
        }
        this.joinColumnModel = joinColumnModel;
    }

    /**
     * 結合カラムモデルのリストを返します。
     * 
     * @return 結合カラムモデルのリストを設定します。
     */
    public List<JoinColumnModel> getJoinColumnModelList() {
        return Collections.unmodifiableList(joinColumnModelList);
    }

    /**
     * 結合カラムモデルを追加します。
     * 
     * @param joinColumnModel
     *            結合カラムモデル
     */
    public void addJoinColumnModel(JoinColumnModel joinColumnModel) {
        if (this.joinColumnModel != null) {
            throw new IllegalStateException("joinColumnModel");
        }
        joinColumnModelList.add(joinColumnModel);
    }

    /**
     * 結合カラムが2つ以上ある場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return 結合カラムが2つ以上ある場合{@code true}、そうでない場合{@code false}
     */
    public boolean isCompositeJoin() {
        return joinColumnModelList.size() > 1;
    }

}

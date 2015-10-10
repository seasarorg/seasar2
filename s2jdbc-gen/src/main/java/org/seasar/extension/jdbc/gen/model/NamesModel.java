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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 名前モデルです。
 * 
 * @author taedium
 */
public class NamesModel extends ClassModel {

    /** エンティティクラスの単純名 */
    protected String entityClassName;

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** 内部クラスの単純名 */
    protected String shortInnerClassName;

    /** 名前の属性モデルのリスト */
    protected List<NamesAttributeModel> namesAttributeModelList = new ArrayList<NamesAttributeModel>();

    /** 名前の関連モデルのリスト */
    protected List<NamesAssociationModel> namesAssociationModelList = new ArrayList<NamesAssociationModel>();

    /**
     * エンティティクラス名を返します。
     * 
     * @return エンティティクラス名
     */
    public String getEntityClassName() {
        return entityClassName;
    }

    /**
     * エンティティクラス名を設定します。
     * 
     * @param entityClassName
     *            エンティティクラス名
     */
    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
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
     * 内部クラスの単純名を返します。
     * 
     * @return 内部クラスの単純名
     */
    public String getShortInnerClassName() {
        return shortInnerClassName;
    }

    /**
     * 内部クラスの単純名を設定します。
     * 
     * @param shortInnerClassName
     *            内部クラスの単純名
     */
    public void setShortInnerClassName(String shortInnerClassName) {
        this.shortInnerClassName = shortInnerClassName;
    }

    /**
     * 名前の属性モデルのリストを返します。
     * 
     * @return 名前の属性モデルのリスト
     */
    public List<NamesAttributeModel> getNamesAttributeModelList() {
        return Collections.unmodifiableList(namesAttributeModelList);
    }

    /**
     * 名前の属性モデルを追加します。
     * 
     * @param namesAttributeModel
     *            名前の属性モデル
     */
    public void addNamesAttributeModel(NamesAttributeModel namesAttributeModel) {
        namesAttributeModelList.add(namesAttributeModel);
    }

    /**
     * 名前の関連モデルのリストを返します。
     * 
     * @return 名前の関連モデルのリスト
     */
    public List<NamesAssociationModel> getNamesAssociationModelList() {
        return Collections.unmodifiableList(namesAssociationModelList);
    }

    /**
     * 名前の関連モデルを追加します。
     * 
     * @param namesAssociationModel
     *            名前の関連モデル
     */
    public void adddNamesAssociationModel(
            NamesAssociationModel namesAssociationModel) {
        namesAssociationModelList.add(namesAssociationModel);
    }
}

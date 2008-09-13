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

/**
 * 条件クラスのモデルです。
 * 
 * @author taedium
 */
public class ConditionModel extends ClassModel {

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** 条件クラスの属性モデルのリスト */
    protected List<ConditionAttributeModel> conditionAttributeModelList = new ArrayList<ConditionAttributeModel>();

    /** 条件クラスの属性メソッドのリスト */
    protected List<ConditionMethodModel> conditionMethodModelList = new ArrayList<ConditionMethodModel>();

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

}

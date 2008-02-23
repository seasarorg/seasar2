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
 * エンティティのモデルです。
 * 
 * @author taedium
 */
public class EntityModel {

    /** 名前 */
    protected String name;

    /** 複合IDをもつならば{@code true} */
    protected boolean compositeId;

    /** プロパティモデルのリスト */
    protected List<PropertyModel> propertyModelList = new ArrayList<PropertyModel>();

    /** IDであるプロパティモデルのリスト */
    protected List<PropertyModel> idPropertyModelList = new ArrayList<PropertyModel>();

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
     * プロパティモデルを追加します。
     * 
     * @param propertyModel
     *            プロパティモデル
     */
    public void addPropertyModel(PropertyModel propertyModel) {
        propertyModelList.add(propertyModel);
        if (propertyModel.isId()) {
            idPropertyModelList.add(propertyModel);
            if (idPropertyModelList.size() > 1) {
                compositeId = true;
            }
        }
    }

    /**
     * プロパティモデルのリストを返します。
     * 
     * @return プロパティモデルのリスト
     */
    public List<PropertyModel> getPropertyModelList() {
        return Collections.unmodifiableList(propertyModelList);
    }

    /**
     * 識別子のプロパティモデルのリストを返します。
     * 
     * @return 識別子のプロパティモデルのリスト
     */
    public List<PropertyModel> getIdPropertyModelList() {
        return Collections.unmodifiableList(idPropertyModelList);
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

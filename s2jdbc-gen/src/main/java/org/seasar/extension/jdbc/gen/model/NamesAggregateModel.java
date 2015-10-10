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
 * 名前集約モデルです。
 * 
 * @author taedium
 */
public class NamesAggregateModel extends ClassModel {

    /** 名前モデルのリスト */
    protected List<NamesModel> namesModelList = new ArrayList<NamesModel>();

    /**
     * 名前モデルのリストを返します。
     * 
     * @return 名前モデルのリスト
     */
    public List<NamesModel> getNamesModelList() {
        return Collections.unmodifiableList(namesModelList);
    }

    /**
     * 名前モデルを追加します。
     * 
     * @param namesModel
     *            名前モデル
     */
    public void addNamesModel(NamesModel namesModel) {
        namesModelList.add(namesModel);
    }

}

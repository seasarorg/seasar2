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
 * 複合結合カラムモデルです。
 * 
 * @author taedium
 */
public class JoinColumnsModel {

    /** 結合カラムモデルのリスト */
    protected List<JoinColumnModel> joinColumnModelList = new ArrayList<JoinColumnModel>();

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
        joinColumnModelList.add(joinColumnModel);
    }

}

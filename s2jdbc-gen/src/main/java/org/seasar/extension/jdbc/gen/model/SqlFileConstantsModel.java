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
 * SQLファイル定数モデルです。
 * 
 * @author taedium
 */
public class SqlFileConstantsModel extends TestClassModel {

    /** SQLファイル定数フィールドモデルのリスト */
    protected List<SqlFileConstantFieldModel> sqlFileConstantFieldModelList = new ArrayList<SqlFileConstantFieldModel>();

    /**
     * SQLファイル定数フィールドモデルのリストを返します。
     * 
     * @return SQLファイル定数フィールドモデルのリスト
     */
    public List<SqlFileConstantFieldModel> getSqlFileConstantFieldModelList() {
        return Collections.unmodifiableList(sqlFileConstantFieldModelList);
    }

    /**
     * SQLファイル定数フィールドモデルを追加します。
     * 
     * @param sqlFileConstantFieldModel
     *            SQLファイルフィールド定数モデル
     */
    public void addSqlFileConstantFieldModel(
            SqlFileConstantFieldModel sqlFileConstantFieldModel) {
        sqlFileConstantFieldModelList.add(sqlFileConstantFieldModel);
    }

}

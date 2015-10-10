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
 * 生成されるモデルです。
 * 
 * @author taedium
 */
public abstract class GeneratedModel {

    /** 生成情報のリスト */
    protected List<String> generatedInfoList = new ArrayList<String>();

    /**
     * 生成情報のリストを返します。
     * 
     * @return 生成情報のリスト
     */
    public List<String> getGeneratedInfoList() {
        return Collections.unmodifiableList(generatedInfoList);
    }

    /**
     * 生成情報を追加します。
     * 
     * @param generatedInfo
     *            生成情報
     */
    public void addGeneratedInfo(String generatedInfo) {
        this.generatedInfoList.add(generatedInfo);
    }

}

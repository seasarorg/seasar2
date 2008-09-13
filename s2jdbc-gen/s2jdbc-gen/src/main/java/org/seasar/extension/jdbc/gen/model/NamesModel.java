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
 * 名前モデルです。
 * 
 * @author taedium
 */
public class NamesModel extends ClassModel {

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** 名前のリスト */
    protected List<String> nameList = new ArrayList<String>();

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
     * 名前のリストを返します。
     * 
     * @return 名前のリスト
     */
    public List<String> getNameList() {
        return Collections.unmodifiableList(nameList);
    }

    /**
     * 名前を追加します。
     * 
     * @param name
     *            名前
     */
    public void addName(String name) {
        nameList.add(name);
    }

}

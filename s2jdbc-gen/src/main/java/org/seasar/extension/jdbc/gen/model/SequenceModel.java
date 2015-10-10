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

/**
 * シーケンスのモデルです。
 * 
 * @author taedium
 */
public class SequenceModel {

    /** 名前 */
    protected String name;

    /** 定義 */
    protected String definition;

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
     * 定義を設定します。
     * 
     * @return 定義
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * 定義を返します。
     * 
     * @param definition
     *            定義
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

}

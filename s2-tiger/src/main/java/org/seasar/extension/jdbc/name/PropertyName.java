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
package org.seasar.extension.jdbc.name;

/**
 * プロパティ名をあらわすクラスです。ネストしたプロパティに対応しています。
 * 
 * @author higa
 * 
 */
public class PropertyName {

    /**
     * 自分自身の名前です。
     */
    protected String name;

    /**
     * 親です。
     */
    protected PropertyName parent;

    /**
     * コンストラクタです。
     */
    public PropertyName() {
        this(null);
    }

    /**
     * コンストラクタです。
     * 
     * @param name
     *            名前
     */
    public PropertyName(String name) {
        this.name = name;
    }

    /**
     * コンストラクタです。
     * 
     * @param parent
     *            親
     * @param name
     *            名前
     */
    public PropertyName(PropertyName parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * 親を含んだプロパティ名に変換します。
     * 
     * @return 親を含んだプロパティ名
     */
    public String toPropertyName() {
        if (parent == null) {
            return name;
        }
        String parentName = parent.toPropertyName();
        if (parentName == null) {
            return name;
        }
        return parentName + "." + name;
    }
}
/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.annotation.tiger;

/**
 * プロパティタイプの列挙型です。
 * 
 * @author y-komori
 */
public enum PropertyType {

    /**
     * 参照のみ可能なプロパティです。
     * <p>
     * getterメソッドのみ生成されます。
     * </p>
     */
    READ,

    /**
     * 更新のみ可能なプロパティです。
     * <p>
     * setterメソッドのみ生成されます。
     * </p>
     */
    WRITE,

    /**
     * 参照・更新のみ可能なプロパティです。
     * <p>
     * getter/setterメソッドとも生成されます。
     * </p>
     */
    READWRITE,

    /**
     * プロパティではありません。
     * <p>
     * getter/setterメソッドとも生成されません。
     * </p>
     */
    NONE;

    /**
     * プロパティタイプの名前を返します。
     * 
     * @return プロパティタイプの名前
     */
    public String getName() {
        return toString().toLowerCase();
    }

}

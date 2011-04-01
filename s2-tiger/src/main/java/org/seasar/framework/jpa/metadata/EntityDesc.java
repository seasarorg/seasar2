/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.metadata;

/**
 * エンティティ定義を表すインターフェースです。
 * 
 * @author koichik
 */
public interface EntityDesc {

    /**
     * エンティティクラスを返します。
     * 
     * @return エンティティクラス
     */
    Class<?> getEntityClass();

    /**
     * エンティティ名を返します。
     * 
     * @return エンティティ名
     */
    String getEntityName();

    /**
     * 属性名の配列を返します。
     * 
     * @return 属性名の配列
     */
    String[] getAttributeNames();

    /**
     * IDの属性定義を返します。
     * 
     * @return 属性定義
     */
    AttributeDesc getIdAttributeDesc();

    /**
     * 指定された名前をもつ属性定義を返します。
     * 
     * @param attributeName
     *            属性名
     * @return 属性定義
     */
    AttributeDesc getAttributeDesc(String attributeName);

    /**
     * 属性定義の配列を返します。
     * 
     * @return 属性定義の配列
     */
    AttributeDesc[] getAttributeDescs();

}

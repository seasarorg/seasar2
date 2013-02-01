/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import java.util.Calendar;
import java.util.Date;

import javax.persistence.TemporalType;

/**
 * エンティティの属性定義を表すインターフェースです。
 * 
 * @author koichik
 */
public interface AttributeDesc {

    /**
     * 属性の名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * 属性の型を返します。
     * 
     * @return 属性の型
     */
    Class<?> getType();

    /**
     * 属性がコレクションを表している場合に要素の型を返します。
     * 
     * @return 属性がコレクションを表している場合は要素の型、それ以外の場合は{@code null}
     */
    Class<?> getElementType();

    /**
     * 属性にマッピングされたJDBCのSQL型を返します。
     * 
     * @return JDBCのSQL型
     */
    int getSqlType();

    /**
     * 属性の型が{@link Date}もしくは{@link Calendar}の場合、属性に注釈されている
     * {@link TemporalType}を返します。
     * 
     * @return 属性の型が{@link Date}もしくは{@link Calendar}の場合は属性に注釈されている
     *         {@link TemporalType}、それ以外の型の場合は{@code null}
     */
    TemporalType getTemporalType();

    /**
     * 子属性を表す{@link AttributeDesc}の配列を返します。
     * <p>
     * この属性がコンポーネントである場合は長さが1以上の配列、コンポーネントでない場合は長さが0の配列を返します。
     * </p>
     * 
     * @return 子属性を表す{@link AttributeDesc}の配列
     */
    AttributeDesc[] getChildAttributeDescs();

    /**
     * 子属性を表す{@link AttributeDesc}の配列を返します。
     * <p>
     * この属性がコンポーネントである場合は長さが1以上の配列、コンポーネントでない場合は長さが0の配列を返します。
     * </p>
     * 
     * @param name
     *            子属性の名前
     * @return 子属性を表す{@link AttributeDesc}の配列
     */
    AttributeDesc getChildAttributeDesc(String name);

    /**
     * 属性がIDを表している場合{@code true}を返します。
     * 
     * @return 属性がIDを表している場合{@code true}、表していない場合{@code false}
     */
    boolean isId();

    /**
     * 属性が関連を表している場合{@code true}を返します。
     * 
     * @return 属性が関連を表している場合{@code true}、表していない場合{@code false}
     */
    boolean isAssociation();

    /**
     * 属性がコレクションを表している場合{@code true}を返します。
     * 
     * @return 属性がコレクションを表している場合{@code true}、表していない場合{@code false}
     */
    boolean isCollection();

    /**
     * 属性がコンポーネントを表している場合
     * 
     * @return 属性がコンポーネントを表している場合{@code true}、表していない場合{@code false}
     */
    boolean isComponent();

    /**
     * 属性がバージョン番号を表している場合{@code true}を返します。
     * 
     * @return 属性がバージョン番号を表している場合{@code true}、表していない場合{@code false}
     */
    boolean isVersion();

    /**
     * 属性の値を返します。
     * 
     * @param owner
     *            属性を所有するエンティティもしくはコンポーネントのインスタンス
     * @return 属性の値
     */
    Object getValue(Object owner);

    /**
     * 属性に値を設定します。
     * 
     * @param owner
     *            属性を所有するエンティティもしくはコンポーネントのインスタンス
     * @param value
     *            値
     */
    void setValue(Object owner, Object value);

}

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
package org.seasar.framework.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * プロパティを扱うためのインターフェースです。
 * 
 * @author higa
 */
public interface PropertyDesc {

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    String getPropertyName();

    /**
     * プロパティの型を返します。
     * 
     * @return プロパティの型
     */
    Class getPropertyType();

    /**
     * getterメソッドを返します。
     * 
     * @return getterメソッド
     */
    Method getReadMethod();

    /**
     * getterメソッドを設定します。
     * 
     * @param readMethod
     */
    void setReadMethod(Method readMethod);

    /**
     * getterメソッドを持っているかどうか返します。
     * 
     * @return getterメソッドを持っているかどうか
     */
    boolean hasReadMethod();

    /**
     * setterメソッドを返します。
     * 
     * @return setterメソッド
     */
    Method getWriteMethod();

    /**
     * setterメソッドを設定します。
     * 
     * @param writeMethod
     */
    void setWriteMethod(Method writeMethod);

    /**
     * setterメソッドを持っているかどうか返します。
     * 
     * @return setterメソッドを持っているかどうか
     */
    boolean hasWriteMethod();

    /**
     * プロパティの値が取得できるかどうかを返します。
     * 
     * @return プロパティの値が取得できるかどうか
     */
    boolean isReadable();

    /**
     * プロパティの値が設定できるかどうかを返します。
     * 
     * @return プロパティの値が設定できるかどうか
     */
    boolean isWritable();

    /**
     * プロパティとして認識しているpublicフィールドを返します。
     * 
     * @return プロパティとして認識しているpublicフィールド
     */
    Field getField();

    /**
     * プロパティとして認識しているpublicフィールドを設定します。
     * 
     * @param field
     */
    void setField(Field field);

    /**
     * プロパティの値を返します。
     * 
     * @param target
     * @return プロパティの値
     * @throws IllegalStateException
     *             プロパティがreadableではない場合。
     * 
     */
    Object getValue(Object target) throws IllegalStateException;

    /**
     * プロパティに値を設定します。
     * 
     * @param target
     * @param value
     * @throws IllegalPropertyRuntimeException
     *             値の設定に失敗した場合。
     * @throws IllegalStateException
     *             writableではない場合。
     */
    void setValue(Object target, Object value)
            throws IllegalPropertyRuntimeException, IllegalStateException;

    /**
     * プロパティの型に応じて必要なら適切に変換します。
     * 
     * @param value
     * @return 変換された値
     */
    Object convertIfNeed(Object value);

    /**
     * Bean記述を返します。
     * 
     * @return Bean記述
     */
    BeanDesc getBeanDesc();

    /**
     * 実行環境がJava5以降で、このプロパティがパラメタ化された型の場合に<code>true</code>を返します。
     * 
     * @return このプロパティがパラメタ化された型の場合に<code>true</code>
     * @since 2.4.18
     */
    boolean isParameterized();

    /**
     * 実行環境がJava5以降で、このプロパティがパラメタ化された型の場合、その情報を返します。
     * <p>
     * 実行環境がJava5以降でない場合、このプロパティがパラメタ化された型でない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return このプロパティがパラメタ化された型の場合、その情報
     * @since 2.4.18
     */
    ParameterizedClassDesc getParameterizedClassDesc();

    /**
     * 実行環境がJava5以降で、このプロパティがパラメタ化された{@link Collection}の場合、その要素型を返します。
     * <p>
     * 実行環境がJava5以降でない場合、このプロパティがパラメタ化された<code>Collection</code>でない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return このプロパティがパラメタ化された{@link Collection}の場合、その要素型
     * @since 2.4.18
     */
    Class getElementClassOfCollection();

    /**
     * 実行環境がJava5以降で、このプロパティがパラメタ化された{@link Map}の場合、そのキー型を返します。
     * <p>
     * 実行環境がJava5以降でない場合、このプロパティがパラメタ化された<code>Map</code>でない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return このプロパティがパラメタ化された{@link Map}の場合、そのキー型
     * @since 2.4.18
     */
    Class getKeyClassOfMap();

    /**
     * 実行環境がJava5以降で、このプロパティがパラメタ化された{@link Map}の場合、その値型を返します。
     * <p>
     * 実行環境がJava5以降でない場合、このプロパティがパラメタ化された<code>Map</code>でない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return このプロパティがパラメタ化された{@link Map}の場合、その値型
     * @since 2.4.18
     */
    Class getValueClassOfMap();

}

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
package org.seasar.extension.dxo.converter;

/**
 * 変換中のコンテキストを表すインターフェースです。
 * <p>
 * 変換コンテキストは評価済みのオブジェクトを持ちます。 評価済みのオブジェクトとは、
 * {@link org.seasar.extension.dxo.DxoConstants#CONVERSION_RULE}アノテーションで
 * 指定されたOGNL式の評価結果である{@link java.util.Map}です。
 * </p>
 * 
 * @author koichik
 */
public interface ConversionContext {

    /**
     * コンバータファクトリを返します。
     * 
     * @return コンバータファクトリ
     */
    ConverterFactory getConverterFactory();

    /**
     * 変換先クラスの<code>destPropertyName</code>で示されるプロパティにコンバータが指定されていれば返します。
     * コンバータが指定されていない場合は<code>null</code>を返します。
     * 
     * @param destClass
     *            変換先のクラス
     * @param destPropertyName
     *            変換先クラスのプロパティ名
     * @return コンバータ
     */
    Converter getConverter(Class destClass, String destPropertyName);

    /**
     * 変換元オブジェクトから変換済みのオブジェクトがあればそれを返します。 変換済みのオブジェクトがない場合は<code>null</code>を返します。
     * <p>
     * このメソッドは、 循環を含むオブジェクトグラフの変換で無限ループになることを防ぐために使用されます。 このメソッドが<code>null</code>を返した場合は変換元の変換を行い、
     * 変換したオブジェクトを{@link #addConvertedObject(Object, Object)}</code>によりコンテキストに登録します。
     * 次に同じソースオブジェクトでこのメソッドが呼び出された場合は変換済みのオブジェクトが返されます。
     * </p>
     * 
     * @param source
     *            変換元のオブジェクト
     * @return 変換済みのオブジェクト
     */
    Object getConvertedObject(Object source);

    /**
     * 変換済みのオブジェクトを変換コンテキストに追加します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @param dest
     *            変換済みのオブジェクト
     */
    void addConvertedObject(Object source, Object dest);

    /**
     * コンテキスト情報からキーにマッピングされている値を返します。
     * 
     * @param key
     *            キー
     * @return 値
     * @see org.seasar.extension.dxo.DxoConstants
     */
    Object getContextInfo(String key);

    /**
     * 名前に対応する評価済みのオブジェクトがあれば<code>true</code>を返します。
     * 
     * @param name
     *            名前
     * @return 評価済みのオブジェクトがあれば<code>true</code>、そうでない場合は<code>false</code>
     */
    boolean hasEvalueatedValue(String name);

    /**
     * 名前に対応する評価済みのオブジェクトを返します。 評価結果が<code>null</code>の場合もあります。
     * 
     * @param name
     *            名前
     * @return 評価済みのオブジェクト
     */
    Object getEvaluatedValue(String name);

    /**
     * 評価済みのオブジェクトを追加します。
     * 
     * @param name
     *            名前
     * @param value
     *            評価済みのオブジェクト
     */
    void addEvaluatedValue(String name, Object value);

    /**
     * 変換先のJavaBeansに<code>null</code>の値を設定しない場合は<code>true</code>を返します。
     * <p>
     * この値はDxoインターフェースの{@link org.seasar.extension.dxo.DxoConstants#EXCLUDE_NULL}アノテーションに
     * <code>true</code>が指定された場合にのみ<code>true</code>となります。
     * その場合、変換元のプロパティが<code>null</code>だと変換先のプロパティには値を設定しません。
     * </p>
     * 
     * @return 変換先のJavaBeansに<code>null</code>の値を設定しない場合は<code>true</code>、そうでない場合は<code>false</code>
     */
    boolean isExcludeNull();

    /**
     * 変換先のJavaBeansに<code>null</code>の値を設定する場合は<code>true</code>を返します。
     * <p>
     * この値は{@link #isExcludeNull()}の否定です。
     * </p>
     * 
     * @return 変換先のJavaBeansに<code>null</code>の値を設定する場合は<code>true</code>、そうでない場合は<code>false</code>
     */
    boolean isIncludeNull();

    /**
     * ネストしたプロパティの情報を返します。 該当するプロパティが存在しない場合は<code>null</code>を返します。
     * 
     * @param srcClass
     *            変換元のクラス
     * @param propertyName
     *            変換元のプロパティ名
     * @return ネストしたプロパティの情報
     */
    NestedPropertyInfo getNestedProertyInfo(Class srcClass, String propertyName);

    /**
     * 日時プロパティの情報を返します。 該当するプロパティが存在しない場合は<code>null</code>を返します。
     * 
     * @param srcClass
     *            変換元のクラス
     * @param propertyName
     *            プロパティ名
     * @return 日時プロパティの情報
     */
    DatePropertyInfo getDatePropertyInfo(Class srcClass, String propertyName);

}

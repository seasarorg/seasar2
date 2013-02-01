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
package org.seasar.extension.dxo.annotation;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Dxoからアノテーションを読み取るためのインタフェースです。
 * 
 * @author Satsohi Kimura
 * @author koichik
 */
public interface AnnotationReader {

    /**
     * <code>Date</code>と<code>String</code>の変換フォーマットを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return <code>Date</code>と<code>String</code>の変換フォーマット
     */
    String getDatePattern(Class dxoClass, Method method);

    /**
     * <code>Time</code>と<code>String</code>の変換フォーマットを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return <code>Time</code>と<code>String</code>の変換フォーマット
     */
    String getTimePattern(Class dxoClass, Method method);

    /**
     * <code>Timestamp</code>と<code>String</code>の変換フォーマットを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return <code>Timestamp</code>と<code>String</code>の変換フォーマット
     */
    String getTimestampPattern(Class dxoClass, Method method);

    /**
     * 変換ルールを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return 変換ルール
     */
    String getConversionRule(Class dxoClass, Method method);

    /**
     * 変換元プロパティの値が<code>null</code>の場合に変換先プロパティに値を設定しない場合は<code>true</code>を返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return 変換元プロパティの値が<code>null</code>の場合に変換先プロパティに値を設定しない場合は<code>true</code>
     */
    boolean isExcludeNull(Class dxoClass, Method method);

    /**
     * 変換元プロパティの値が空白(スペース，復帰，改行，タブ文字のみ)の場合に変換先プロパティに値を設定しない場合は<code>true</code>を返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return 変換元プロパティの値が<code>null</code>の場合に変換先プロパティに値を設定しない場合は<code>true</code>
     */
    boolean isExcludeWhitespace(Class dxoClass, Method method);

    /**
     * 変換元プロパティのprefixを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return 変換元プロパティのprefix
     */
    String getSourcePrefix(Class dxoClass, Method method);

    /**
     * 変換先プロパティのprefixを返します。
     * 
     * @param dxoClass
     *            Dxoクラスまたはインタフェース
     * @param method
     *            Dxoメソッド
     * @return 変換先プロパティのprefix
     */
    String getDestPrefix(Class dxoClass, Method method);

    /**
     * 変換先クラスに指定されたコンバータの{@link Map}を返します。
     * 
     * @param destClass
     *            変換先クラス
     * @return 変換先クラスに指定されたコンバータの{@link Map}
     */
    Map getConverters(Class destClass);

}

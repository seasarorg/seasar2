/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit;

import java.lang.reflect.Method;
import java.util.List;

import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.convention.NamingConvention;

/**
 * テストクラスを分析するイントロスペクターです。
 * 
 * @author taedium
 */
public interface S2TestIntrospector {

    /**
     * 指定されたテストクラス内のすべてのテストメソッドを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return テストメソッドのリスト
     */
    List<Method> getTestMethods(Class<?> clazz);

    /**
     * メソッドが無視の対象の場合<code>true</code>を返します。
     * 
     * @param method
     *            テストメソッド
     * @return メソッドが無視の対象の場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean isIgnored(Method method);

    /**
     * メソッドがスローすると期待される例外もしくはエラーのクラスを返します。
     * 
     * @param method
     *            テストメソッド
     * @return 期待される例外もしくはエラーがある場合はそのクラス、ない場合は<code>null</code>
     */
    Class<? extends Throwable> expectedException(Method method);

    /**
     * メソッドの実行のタイムアウトを表すミリ秒を返します。
     * 
     * @param method
     *            テストメソッド
     * @return タイムアウトを表すミリ秒
     */
    long getTimeout(Method method);

    /**
     * テストケース実行の事前条件が満たされている場合<code>true</code>を返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @param test
     *            テストオブジェクト
     * @return 事前条件が満たされている場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean isFulfilled(Class<?> clazz, Method method, Object test);

    /**
     * トランザクションが必要とされる場合<code>true</code>を返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return トランザクションが必要とされる場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean needsTransaction(Class<?> clazz, Method method);

    /**
     * トランザクションのコミットが必要とされる場合<code>true</code>を返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return トランザクションのコミットが必要とされる場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean requiresTransactionCommitment(Class<?> clazz, Method method);

    /**
     * WARM deployが必要とされる場合<code>true</code>を返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return WARM deployが必要とされる場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean needsWarmDeploy(Class<?> clazz, Method method);

    /**
     * {@link NamingConvention}が登録される場合<code>true</code>を返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return {@link NamingConvention}が登録される場合<code>true</code>、登録されない場合<code>false</code>を返します。
     */
    boolean isRegisterNamingConvention(Class<?> clazz, Method method);

    /**
     * {@link MockInterceptor}を利用したモックを作成します。
     * 
     * @param method
     *            テストメソッド
     * @param test
     *            テストクラスのインスタンス
     * @param context
     *            テストコンテキスト
     */
    void createMock(Method method, Object test, InternalTestContext context);

    /**
     * ルートコンテナを表すdiconファイルのパスを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return ルートコンテナを表すdiconファイルのパス
     */
    String getRootDicon(Class<?> clazz, Method method);

    /**
     * テストクラスの初期化メソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return 初期化メソッドのリスト
     */
    List<Method> getBeforeClassMethods(Class<?> clazz);

    /**
     * テストクラスの解放メソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return 解放メソッドのリスト
     */
    List<Method> getAfterClassMethods(Class<?> clazz);

    /**
     * すべてのテストケース共通の初期化メソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return 初期化メソッドのリスト
     */
    List<Method> getBeforeMethods(Class<?> clazz);

    /**
     * すべてのテストケース共通の解放メソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return 解放メソッドのリスト
     */
    List<Method> getAfterMethods(Class<?> clazz);

    /**
     * すべてのテストケース共通のバインドフィールド直後のメソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @return バインドフィールドメソッド
     */
    public List<Method> getPostBindFieldsMethods(final Class<?> clazz);

    /**
     * すべてのテストケース共通のアンバインドフィールド直前のメソッドのリストを返します。
     * 
     * @param clazz
     * @return
     */
    public List<Method> getPreUnbindFieldsMethods(final Class<?> clazz);

    /**
     * テストケース個別の初期化メソッドを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return 初期化メソッド
     */
    Method getEachBeforeMethod(Class<?> clazz, Method method);

    /**
     * テストケース個別の解放メソッドを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return 解放メソッド
     */
    Method getEachAfterMethod(Class<?> clazz, Method method);

    /**
     * テストケース個別の記録メソッドを返します。
     * <p>
     * 記録メソッドはEasyMockを利用して作成されたモックの振る舞いを記録するメソッドです。
     * </p>
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return 記録メソッド
     */
    Method getEachRecordMethod(Class<?> clazz, Method method);
}

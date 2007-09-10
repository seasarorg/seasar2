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
package org.seasar.extension.tx;

/**
 * トランザクションを制御するオブジェクトのインターフェースです。
 * <p>
 * 実装クラス固有のポリシーおよび実現方法でトランザクションを制御し、 その範囲内で{@link TransactionCallback}をコールバックします。
 * </p>
 * 
 * @author koichik
 */
public interface TransactionControl {

    /**
     * トランザクション属性がMANDATORYの制御下で{@link TransactionCallback}をコールバックします。
     * 
     * @param callback
     *            トランザクション制御下でコールバックされるオブジェクト
     * @return callbackの戻り値
     * @throws Throwable
     *             callbackの呼び出し中に例外がスローされた場合
     */
    Object mandatory(TransactionCallback callback) throws Throwable;

    /**
     * トランザクション属性がNEVERの制御下で{@link TransactionCallback}をコールバックします。
     * 
     * @param callback
     *            トランザクション制御下でコールバックされるオブジェクト
     * @return callbackの戻り値
     * @throws Throwable
     *             callbackの呼び出し中に例外がスローされた場合
     */
    Object never(TransactionCallback callback) throws Throwable;

    /**
     * トランザクション属性がNOT_SUPPORTEDの制御下で{@link TransactionCallback}をコールバックします。
     * 
     * @param callback
     *            トランザクション制御下でコールバックされるオブジェクト
     * @return callbackの戻り値
     * @throws Throwable
     *             callbackの呼び出し中に例外がスローされた場合
     */
    Object notSupported(TransactionCallback callback) throws Throwable;

    /**
     * トランザクション属性がREQUIREDの制御下で{@link TransactionCallback}をコールバックします。
     * 
     * @param callback
     *            トランザクション制御下でコールバックされるオブジェクト
     * @return callbackの戻り値
     * @throws Throwable
     *             callbackの呼び出し中に例外がスローされた場合
     */
    Object required(TransactionCallback callback) throws Throwable;

    /**
     * トランザクション属性がREQUIRES_NEWの制御下で{@link TransactionCallback}をコールバックします。
     * 
     * @param callback
     *            トランザクション制御下でコールバックされるオブジェクト
     * @return callbackの戻り値
     * @throws Throwable
     *             callbackの呼び出し中に例外がスローされた場合
     */
    Object requiresNew(TransactionCallback callback) throws Throwable;

    /**
     * トランザクションをロールバックするようマークします。
     * <p>
     * このメソッドはマークに失敗しても例外をスローしません。
     * </p>
     */
    void setRollbackOnly();

    /**
     * トランザクションをコミットした場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
     * 
     * @return トランザクションをコミットした場合は<code>true</code>、それ以外の場合は<code>false</code>
     */
    boolean isCommitted();

}

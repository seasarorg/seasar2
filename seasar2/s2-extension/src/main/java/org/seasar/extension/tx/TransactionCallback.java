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
package org.seasar.extension.tx;

/**
 * トランザクション制御下でコールバックされるオブジェクトのインターフェースです。
 * 
 * @author koichik
 * @since 2.4.18
 */
public interface TransactionCallback {

    /**
     * トランザクション制御下で呼び出されます。
     * 
     * @param adapter
     *            トランザクションマネージャへのアダプタ
     * @return 任意の戻り値
     * @throws Throwable
     *             コールバック処理中に例外が発生した場合
     */
    Object execute(TransactionManagerAdapter adapter) throws Throwable;

}

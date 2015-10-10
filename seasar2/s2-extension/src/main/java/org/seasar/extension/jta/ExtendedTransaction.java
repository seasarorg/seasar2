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
package org.seasar.extension.jta;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/**
 * {Transaction}を拡張したインタフェースです。
 * 
 * @author koichik
 */
public interface ExtendedTransaction extends Transaction {

    /**
     * トランザクションを開始します。
     * 
     * @throws NotSupportedException
     *             現在のスレッドが既にトランザクションに関連づけられていて、
     *             ネストしたトランザクションがサポートされない場合にスローされます
     * @throws SystemException
     *             回復できないエラーが発生した場合にスローされます
     */
    void begin() throws NotSupportedException, SystemException;

    /**
     * トランザクションを再開します。
     * 
     * @throws IllegalStateException
     *             現在のスレッドが既にトランザクションに関連づけられている場合にスローされます
     * @throws SystemException
     *             回復できないエラーが発生した場合にスローされます
     */
    void resume() throws IllegalStateException, SystemException;

    /**
     * トランザクションを中断します。
     * 
     * @throws SystemException
     *             回復できないエラーが発生した場合にスローされます
     */
    void suspend() throws SystemException;

}

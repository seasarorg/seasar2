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

import javax.transaction.Synchronization;

/**
 * 
 * @author koichik
 */
public interface SynchronizationRegister {
    /**
     * 特定の順序で呼び出される<code>Synchronization</code>インスタンスを登録します。
     * 
     * @param sync
     *            <code>Synchronization</code>インスタンス
     * @throws IllegalStateException
     *             トランザクションが一停止状態または非活動中の場合
     */
    void registerInterposedSynchronization(Synchronization sync)
            throws IllegalStateException;

    /**
     * 指定されたキーで指定された値をトランザクションに関連付けます。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     * @throws IllegalStateException
     *             トランザクションが一停止状態または非活動中の場合
     */
    void putResource(Object key, Object value) throws IllegalStateException;

    /**
     * 指定されたキーでトランザクションに関連付けられた値を返します。
     * 
     * @param key
     *            キー
     * @return トランザクションに関連付けられた値
     * @throws IllegalStateException
     *             トランザクションが一停止状態または非活動中の場合
     */
    Object getResource(Object key) throws IllegalStateException;

}

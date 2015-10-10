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
package org.seasar.extension.dbcp;

import java.sql.SQLException;

import javax.transaction.Transaction;

/**
 * コネクションプーリングのためのインターフェースです。
 * 
 * @author higa
 * 
 */
public interface ConnectionPool {

    /**
     * コネクションを取り出します。
     * 
     * @return コネクション
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    ConnectionWrapper checkOut() throws SQLException;

    /**
     * コネクションを戻します。
     * 
     * @param connectionWrapper
     *            コネクション
     */
    void checkIn(ConnectionWrapper connectionWrapper);

    /**
     * トランザクションに関連付けられたコネクションを戻します。
     * 
     * @param tx
     *            トランザクション
     */
    void checkInTx(Transaction tx);

    /**
     * コネクションを解放します。
     * 
     * @param connectionWrapper
     *            コネクション
     */
    void release(ConnectionWrapper connectionWrapper);

    /**
     * プールしているすべてのコネクションを開放します。
     */
    void close();

    /**
     * トランザクション外でアクティブなコネクションの数を返します。
     * 
     * @return トランザクション外でアクティブなコネクションの数
     */
    int getActivePoolSize();

    /**
     * トランザクション中でアクティブなコネクションの数を返します。
     * 
     * @return トランザクション中でアクティブなコネクションの数
     */
    int getTxActivePoolSize();

    /**
     * プーリングされているコネクションの数を返します。
     * 
     * @return プーリングされているコネクションの数
     */
    int getFreePoolSize();

    /**
     * コネクションをプールする上限を返します。
     * 
     * @return コネクションをプールする上限
     */
    int getMaxPoolSize();

    /**
     * コネクションをプールする下限を返します。
     * 
     * @return コネクションをプールする下限
     */
    int getMinPoolSize();
}
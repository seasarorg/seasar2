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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

/**
 * コネクションをラップするためのインターフェースです。 {@link ConnectionPool}で管理するのはこのインターフェースのオブジェクトです。
 * 
 * @author higa
 * 
 */
public interface ConnectionWrapper extends Connection {

    /**
     * 本当にクローズします。 {@link Connection#close()}を呼び出すとプールに返るだけで実際にはクローズしません。
     */
    void closeReally();

    /**
     * 解放します。
     * 
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    void release() throws SQLException;

    /**
     * 初期化します。
     * 
     * @param tx
     *            トランザクション
     */
    void init(Transaction tx);

    /**
     * クリーンアップします。
     */
    void cleanup();

    /**
     * 物理的なコネクションを返します。
     * 
     * @return 物理的なコネクション
     */
    Connection getPhysicalConnection();

    /**
     * XAリソースを返します。
     * 
     * @return XAリソース
     */
    XAResource getXAResource();

    /**
     * XAコネクションを返します。
     * 
     * @return XAコネクション
     */
    XAConnection getXAConnection();
}
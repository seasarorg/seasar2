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
package org.seasar.framework.jpa;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

/**
 * 永続ユニットのコンテキストです。
 * 
 * @author koichik
 */
public interface PersistenceUnitContext {

    /**
     * 指定されたトランザクションに関連付いた{@link EntityManager}を返します。
     * 
     * @param tx
     *            トランザクション
     * @return {@link EntityManager}のコンポーネント
     */
    EntityManager getEntityManager(Transaction tx);

    /**
     * 指定されたトランザクションに関連付けて{@link EntityManager}を登録します。
     * 
     * @param tx
     *            トランザクション
     * @param em
     *            {@link EntityManager}のコンポーネント
     */
    void registerEntityManager(Transaction tx, EntityManager em);

    /**
     * 指定されたトランザクションに関連付けられた{@link EntityManager}の登録を取り消します。
     * 
     * @param tx
     *            トランザクション
     */
    void unregisterEntityManager(Transaction tx);

}

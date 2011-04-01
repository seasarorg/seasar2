/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import java.sql.Connection;

import javax.persistence.EntityManager;

/**
 * 永続ユニットプロバイダ(JPA実装)の差違を吸収するためのインタフェースです。
 * 
 * @author koichik
 */
public interface Dialect {

    /**
     * {@link EntityManager エンティティマネージャ}から{@link Connection JDBCコネクション}を取得して返します。
     * 
     * @param em
     *            {@link EntityManager エンティティマネージャ}
     * @return {@link EntityManager エンティティマネージャ}が使用している{@link Connection JDBCコネクション}
     */
    Connection getConnection(EntityManager em);

    /**
     * 管理されたエンティティを永続コンテキストから分離します．
     * 
     * @param em
     *            {@link EntityManager エンティティマネージャ}
     * @param managedEntity
     *            管理されたエンティティ
     * @throws UnsupportedOperationException
     */
    void detach(EntityManager em, Object managedEntity);

}

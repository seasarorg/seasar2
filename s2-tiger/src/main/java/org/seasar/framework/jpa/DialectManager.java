/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

/**
 * 永続ユニットプロバイダ(JPA実装)の差違を吸収する{@link Dialect}を管理するためのインタフェースです。
 * 
 * @author koichik
 */
public interface DialectManager {

    /**
     * {@link Dialect}を追加します。
     * 
     * @param delegateClass
     *            JPA実装が提供する{@link EntityManager エンティティマネージャ}の実装クラス
     * @param dialect
     *            {@link Dialect}
     */
    void addDialect(Class<?> delegateClass, Dialect dialect);

    /**
     * {@link Dialect}を削除します。
     * 
     * @param delegateClass
     *            JPA実装が提供する{@link EntityManager エンティティマネージャ}の実装クラス
     */
    void removeDialect(Class<?> delegateClass);

    /**
     * {@link EntityManager エンティティマネージャ}から対応する{@link Dialect}を返します。
     * 
     * @param em
     *            {@link EntityManager エンティティマネージャ}
     * @return {@link Dialect}
     */
    Dialect getDialect(EntityManager em);

}

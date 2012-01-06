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
package org.seasar.framework.jpa;

import javax.persistence.EntityManagerFactory;

import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.jpa.unit.EntityReaderProvider;

/**
 * 永続ユニットを提供するプロバイダのインターフェースです。
 * 
 * @author koichik
 */
public interface PersistenceUnitProvider {

    /**
     * {@link EntityManagerFactory エンティティマネジャーファクトリ}を返します。
     * <p>
     * このメソッドは{@link EntityManagerFactory エンティティマネジャーファクトリ}をコンポーネントとして登録するために、
     * diconファイル中のOGNL式から呼び出されます。
     * </p>
     * 
     * <pre>
     * &lt;component name="persistenceUnitProvider" class="ContainerPersistenceUnitProvider"&gt;
     *   ...
     * &lt;/component&gt;
     * 
     * &lt;component name="entityManagerFactory" class="javax.persistence.EntityManagerFactory"&gt;
     *   persistenceUnitProvider.entityManagerFactory
     * &lt;/component&gt;
     * </pre>
     * 
     * @return エンティティマネジャーファクトリ
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * エンティティマネジャーファクトリを作成します。
     * <p>
     * このメソッドは{@link EntityManagerFactory}を作成するために{@link PersistenceUnitManager}から呼び出されます。
     * </p>
     * 
     * @param unitName
     *            永続ユニット名
     * @return エンティティマネジャーファクトリ
     */
    EntityManagerFactory createEntityManagerFactory(String unitName);

    /**
     * 抽象永続ユニット名と具象永続ユニット名からエンティティマネジャーファクトリを作成します。
     * <p>
     * 単一の抽象永続ユニットで複数の具象永続ユニットをグループ化して扱いたい場合に使用します。
     * {@link org.seasar.framework.jpa.impl.SelectableEntityManagerProxy }との併用が想定されています。
     * </p>
     * <p>
     * このメソッドは{@link EntityManagerFactory}を作成するために{@link PersistenceUnitManager}から呼び出されます。
     * </p>
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param concreteUnitName
     *            具象永続ユニット名
     * @return エンティティマネジャーファクトリ
     */
    EntityManagerFactory createEntityManagerFactory(String abstractUnitName,
            String concreteUnitName);

    /**
     * {@link EntityDescProvider}を返します。
     * 
     * @return {@link EntityDescProvider}を返します
     */
    EntityDescProvider getEntityDescProvider();

    /**
     * {@link EntityReaderProvider}を返します。
     * 
     * @return {@link EntityReaderProvider}を返します
     */
    EntityReaderProvider getEntityReaderProvider();

}

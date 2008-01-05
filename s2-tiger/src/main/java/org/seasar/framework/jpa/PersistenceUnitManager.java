/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * 永続ユニットを管理するコンポーネントのインターフェースです。
 * 
 * @author koichik
 */
public interface PersistenceUnitManager {

    /** デフォルトの永続ユニット名 */
    String DEFAULT_PERSISTENCE_UNIT_NAME = "persistenceUnit";

    /**
     * 指定されたユニット名を持つ{@link EntityManagerFactory}を返します。
     * 
     * @param unitName
     *            ユニット名
     * @return 指定されたユニット名を持つ{@link EntityManagerFactory}
     */
    EntityManagerFactory getEntityManagerFactory(String unitName);

    /**
     * 指定されたユニット名を持ち、指定の{PersistenceUnitProvider}から作成された{@link EntityManagerFactory}を返します。
     * 
     * @param unitName
     *            ユニット名
     * @param provider
     *            {@link EntityManagerFactory}を作成する{PersistenceUnitProvider}
     * @return 指定されたユニット名を持ち、指定の{PersistenceUnitProvider}から作成された{@link EntityManagerFactory}
     */
    EntityManagerFactory getEntityManagerFactory(String unitName,
            PersistenceUnitProvider provider);

    /**
     * 指定された具象ユニット名を持つ{@link EntityManagerFactory}を返します。
     * 
     * @param abstractUnitName
     *            抽象ユニット名
     * @param concreteUnitName
     *            具象ユニット名
     * @return 指定されたユニット名を持つ{@link EntityManagerFactory}
     */
    EntityManagerFactory getEntityManagerFactory(String abstractUnitName,
            String concreteUnitName);

    /**
     * 指定された具象ユニット名を持ち、指定の{PersistenceUnitProvider}から作成された{@link EntityManagerFactory}を返します。
     * 
     * @param abstractUnitName
     *            抽象ユニット名
     * @param concreteUnitName
     *            具象ユニット名
     * @param provider
     *            {@link EntityManagerFactory}を作成する{PersistenceUnitProvider}
     * @return 指定されたユニット名を持ち、指定の{PersistenceUnitProvider}から作成された{@link EntityManagerFactory}
     */
    EntityManagerFactory getEntityManagerFactory(String abstractUnitName,
            String concreteUnitName, PersistenceUnitProvider provider);

    /**
     * 指定のエンティティクラスを扱う<strong>抽象</strong>永続ユニット名を返します。
     * <dl>
     * <dt>SMART deployモードの場合</dt>
     * <dd>
     * <ol>
     * <li>エンティティクラスが{@link NamingConvention}に設定されているエンティティパッケージ直下のクラスの場合は
     * デフォルトの永続ユニット名。</li>
     * <li>エンティティパッケージのサブパッケージ下のクラスの場合は、サブパッケージ名をプレフィックスとしてデフォルトの永続ユニットに付加した名称。</li>
     * </ol>
     * </dd>
     * <dt>SMART deployモードでない場合</dt>
     * <dd>常にデフォルトの永続ユニット名。</dd>
     * </dl>
     * 
     * @param entityClass
     *            エンティティクラス
     * @return 指定のエンティティクラスを扱う<strong>抽象</strong>永続ユニット名
     */
    String getAbstractPersistenceUnitName(Class<?> entityClass);

    /**
     * 指定のマッピングファイルを扱う<strong>抽象</strong>永続ユニット名を返します。
     * <dl>
     * <dt>SMART deployモードの場合</dt>
     * <dd>
     * <ol>
     * <li>マッピングファイルが{@link NamingConvention}に設定されているエンティティパッケージ直下のファイルの場合は、デフォルトの永続ユニット名。</li>
     * <li>エンティティパッケージのサブパッケージ下のファイルの場合は、サブパッケージ名をプレフィックスとしてデフォルトの永続ユニットに付加した名称。</li>
     * </ol>
     * </dd>
     * <dt>SMART deployモードでない場合</dt>
     * <dd>常にデフォルトの永続ユニット名。</dd>
     * </dl>
     * 
     * @param mappingFile
     *            マッピングファイル
     * @return 指定のマッピングファイルを扱う<strong>抽象</strong>永続ユニット名
     */
    String getAbstractPersistenceUnitName(String mappingFile);

    /**
     * 指定のエンティティクラスを扱う<strong>具象</strong>永続ユニット名を返します。
     * <dl>
     * <dt>SMART deployモードの場合</dt>
     * <dd>
     * <ol>
     * <li>エンティティクラスが{@link NamingConvention}に設定されているエンティティパッケージ直下のクラスの場合
     * <ol>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていれば、
     * そのプレフィックスをデフォルトの永続ユニット名に付加した名称。</li>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていなければ、
     * デフォルトの永続ユニット名。</li>
     * </ol>
     * </li>
     * <li>エンティティパッケージのサブパッケージ下のクラスの場合は、サブパッケージ名をプレフィックスとしてデフォルトの永続ユニットに付加した名称。
     * </ol>
     * </dd>
     * <dt>SMART deployモードでない場合</dt>
     * <dd>常にデフォルトの永続ユニット名。</dd>
     * </dl>
     * 
     * @param entityClass
     *            エンティティクラス
     * @return 指定のエンティティクラスを扱う<strong>具象</strong>永続ユニット名
     */
    String getConcretePersistenceUnitName(Class<?> entityClass);

    /**
     * 指定のマッピングファイルを扱う<strong>具象</strong>永続ユニット名を返します。
     * <dl>
     * <dt>SMART deployモードの場合</dt>
     * <dd>
     * <ol>
     * <li>マッピングファイルが{@link NamingConvention}に設定されているエンティティパッケージ直下のファイルの場合
     * <ol>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていれば、
     * そのプレフィックスをデフォルトの永続ユニット名に付加した名称。</li>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていなければ、
     * デフォルトの永続ユニット名。</li>
     * </ol>
     * </li>
     * <li>エンティティパッケージのサブパッケージ下のファイルの場合は、サブパッケージ名をプレフィックスとしてデフォルトの永続ユニットに付加した名称。
     * </ol>
     * </dd>
     * <dt>SMART deployモードでない場合</dt>
     * <dd>常にデフォルトの永続ユニット名。</dd>
     * </dl>
     * 
     * @param mappingFile
     *            マッピングファイル
     * @return 指定のマッピングファイルを扱う<strong>具象</strong>永続ユニット名
     */
    String getConcretePersistenceUnitName(String mappingFile);

    /**
     * 指定のエンティティクラスを扱う{@link PersistenceUnitProvider}のコンポーネントを返します。
     * <dl>
     * <dt>SMART deployモードの場合</dt>
     * <dd>
     * <ol>
     * <li>エンティティクラスが{@link NamingConvention}に設定されているエンティティパッケージ直下のクラスの場合
     * <ol>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていれば、
     * そのプレフィックスをデフォルトの永続ユニット名に付加した名称を持つ永続プロバイダ。</li>
     * <li>{@link DataSourceFactory#setSelectableDataSourceName(String)}にプレフィックスが設定されていなければ、
     * デフォルトの永続ユニット名を持つ永続プロバイダ。</li>
     * </ol>
     * </li>
     * <li>エンティティパッケージのサブパッケージ下のクラスの場合は、サブパッケージ名をプレフィックスとしてデフォルトの永続ユニットに付加した名称を持つ永続プロバイダ。
     * </ol>
     * </dd>
     * <dt>SMART deployモードでない場合</dt>
     * <dd>常にデフォルトの永続ユニット名を持つ永続プロバイダ。</dd>
     * </dl>
     * 
     * @param entityClass
     *            エンティティクラス
     * @return 指定のエンティティクラスを扱う{@link PersistenceUnitProvider}のコンポーネント
     */
    PersistenceUnitProvider getPersistenceUnitProvider(Class<?> entityClass);

    /**
     * <strong>具象名</strong>で指定された永続ユニットを提供する{@link PersistenceUnitProvider}のコンポーネントを返します。
     * 
     * @param unitName
     *            永続ユニットの具象名
     * @return <strong>具象名</strong>で指定された永続ユニットを提供する{@link PersistenceUnitProvider}のコンポーネント
     */
    PersistenceUnitProvider getPersistenceUnitProvider(String unitName);

}

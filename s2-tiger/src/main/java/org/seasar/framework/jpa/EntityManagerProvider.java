/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import org.seasar.framework.jpa.impl.SelectableEntityManagerProxy;

/**
 * {@link EntityManager}を提供するコンポーネントのインターフェースです。
 * 
 * @author koichik
 */
public interface EntityManagerProvider {

    /** デフォルトの{@link EntityManager}のコンポーネント名 */
    String DEFAULT_ENTITY_MANAGER_NAME = "entityManager";

    /**
     * {@link SelectableEntityManagerProxy}が現在選択している物理的な{@link EntityManager}のプレフィックスを返します。
     * <p>
     * 物理的な{@link EntityManager}が選択されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @return {@link SelectableEntityManagerProxy}が現在選択している物理的な{@link EntityManager}のプレフィックス
     */
    String getSelectableEntityManagerPrefix();

    /**
     * プレフィックスを持つ{@link EntityManager}のコンポーネントを返します。
     * <p>
     * プレフィックスが<code>null</code>の場合はデフォルトのコンポーネント名を持つ{@link EntityManager}が返されます。
     * </p>
     * 
     * @param prefix
     *            プレフィックス
     * @return プレフィックスを持つ{@link EntityManager}のコンポーネント
     */
    EntityManager getEntityManger(String prefix);

}

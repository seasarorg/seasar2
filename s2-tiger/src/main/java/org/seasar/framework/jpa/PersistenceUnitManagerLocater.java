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


/**
 * 永続ユニットマネージャを取得するためのロケータです。
 * 
 * @author koichik
 */
public class PersistenceUnitManagerLocater {

    /** 永続ユニットマネージャ */
    protected static PersistenceUnitManager instance;

    /**
     * 永続ユニットマネージャを返します。
     * 
     * @return 永続ユニットマネージャ
     */
    public static PersistenceUnitManager getInstance() {
        return instance;
    }

    /**
     * 永続ユニットマネージャを設定します。
     * 
     * @param instance
     *            永続ユニットマネージャ
     */
    public static void setInstance(final PersistenceUnitManager instance) {
        PersistenceUnitManagerLocater.instance = instance;
    }

}
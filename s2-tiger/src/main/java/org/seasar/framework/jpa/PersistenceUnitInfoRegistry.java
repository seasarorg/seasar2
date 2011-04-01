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

import javax.persistence.spi.PersistenceUnitInfo;

/**
 * 永続ユニット情報のレジストリです。
 * 
 * @author koichik
 */
public interface PersistenceUnitInfoRegistry {

    /**
     * 名前で指定された永続ユニット情報を返します。
     * 
     * @param name
     *            永続ユニットの名前
     * @return 名前で指定された永続ユニット情報
     */
    PersistenceUnitInfo getPersistenceUnitInfo(String name);

}

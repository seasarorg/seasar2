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

import java.net.URL;
import java.util.List;

import javax.persistence.spi.PersistenceUnitInfo;

/**
 * 指定された<code>META-INF/persistence.xml</code>を読み込んで
 * {@link PersistenceUnitInfo 永続ユニット情報}を作成するファクトリのインタフェースです。
 * 
 * @author koichik
 */
public interface PersistenceUnitInfoFactory {

    /**
     * <code>persistence.xml</code>を読み込んで{@link PersistenceUnitInfo 永続ユニット情報}を作成し、
     * そのリストを返します。
     * <p>
     * <code>persistence.xml</code>のURLの末尾から<code>"META-INF/persistence.xml"</code>を
     * 取り除いたURLを永続ユニットのルートURLとして使用します。
     * </p>
     * 
     * @param persistenceXml
     *            <code>persistence.xml</code>のURL
     * @return {@link PersistenceUnitInfo 永続ユニット情報}のリスト
     */
    List<PersistenceUnitInfo> createPersistenceUnitInfo(URL persistenceXml);

    /**
     * <code>persistence.xml</code>を読み込んで{@link PersistenceUnitInfo 永続ユニット情報}を作成し、
     * そのリストを返します。
     * 
     * @param persistenceXmlUrl
     *            <code>persistence.xml</code>のURL
     * @param persistenceUnitRootUrl
     *            永続ユニットのルートURL
     * @return {@link PersistenceUnitInfo 永続ユニット情報}のリスト
     */
    List<PersistenceUnitInfo> createPersistenceUnitInfo(URL persistenceXmlUrl,
            URL persistenceUnitRootUrl);

}

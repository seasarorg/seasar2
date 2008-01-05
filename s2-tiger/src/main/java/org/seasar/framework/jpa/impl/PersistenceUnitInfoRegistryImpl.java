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
package org.seasar.framework.jpa.impl;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitInfoFactory;
import org.seasar.framework.jpa.PersistenceUnitInfoRegistry;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 永続ユニット情報のレジストリです。
 * 
 * @author koichik
 */
public class PersistenceUnitInfoRegistryImpl implements
        PersistenceUnitInfoRegistry {

    /** <code>persistence.xml</code>のパス名 */
    public static final String PERSISTENCE_XML = "META-INF/persistence.xml";

    private static final Logger logger = Logger
            .getLogger(PersistenceUnitInfoRegistryImpl.class);

    /** 永続ユニット情報のファクトリ */
    protected PersistenceUnitInfoFactory persistenceUnitInfoFactory;

    /** 永続ユニット名と永続ユニット情報のマップ */
    protected Map<String, PersistenceUnitInfo> unitInfoMap = CollectionsUtil
            .newHashMap();

    /**
     * インスタンスを構築します。
     */
    public PersistenceUnitInfoRegistryImpl() {
    }

    /**
     * 永続ユニット情報のファクトリを設定します。
     * 
     * @param persistenceUnitInfoFactory
     *            永続ユニット情報のファクトリを設定します。
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitInfoFactory(
            final PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }

    /**
     * 永続ユニット情報をロードします。
     */
    @InitMethod
    public void load() {
        @SuppressWarnings("unchecked")
        final Iterator<URL> it = ClassLoaderUtil.getResources(PERSISTENCE_XML);
        while (it.hasNext()) {
            final URL url = it.next();
            logger.log("ISSR0006", new Object[] { url });
            try {
                for (final PersistenceUnitInfo unitInfo : persistenceUnitInfoFactory
                        .createPersistenceUnitInfo(url)) {
                    final String unitName = unitInfo.getPersistenceUnitName();
                    if (!unitInfoMap.containsKey(unitName)) {
                        logger.log("ISSR0007", new Object[] { unitName });
                        unitInfoMap.put(unitName, unitInfo);
                    }
                }
            } catch (final Exception e) {
                logger.log("WSSR0012", new Object[] { e }, e);
            }
        }
    }

    public PersistenceUnitInfo getPersistenceUnitInfo(final String name) {
        return unitInfoMap.get(name);
    }

}

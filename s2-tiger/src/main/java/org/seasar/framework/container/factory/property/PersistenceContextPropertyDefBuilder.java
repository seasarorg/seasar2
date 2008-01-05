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
package org.seasar.framework.container.factory.property;

import javax.persistence.PersistenceContext;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.jpa.impl.TxScopedEntityManagerProxy;
import org.seasar.framework.util.StringUtil;

/**
 * {@link PersistenceContext}アノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class PersistenceContextPropertyDefBuilder extends
        AbstractPropertyDefBuilder<PersistenceContext> {

    /**
     * インスタンスを構築します。
     */
    public PersistenceContextPropertyDefBuilder() {
    }

    @Override
    protected Class<PersistenceContext> getAnnotationType() {
        return PersistenceContext.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef,
            final PersistenceContext persistenceContext) {
        final String name = persistenceContext.name();
        if (!StringUtil.isEmpty(name)) {
            // specified 'name' element (binding by name)
            final ComponentDef emComponentDef = createComponentDef(TxScopedEntityManagerProxy.class);
            final PropertyDef emfPropertyDef = createPropertyDef("emf",
                    AccessTypeDefFactory.FIELD, name);
            emComponentDef.addPropertyDef(emfPropertyDef);
            return createPropertyDef(propertyName, accessTypeDef,
                    emComponentDef);
        }

        final String unitName = persistenceContext.unitName();
        if (!StringUtil.isEmpty(unitName)) {
            // specified 'unitName' element
            final ComponentDef emfComponentDef = PersistenceUnitPropertyDefBuilder
                    .createPersistenceUnitCompoentDef(unitName);
            final ComponentDef emComponentDef = createComponentDef(TxScopedEntityManagerProxy.class);
            final PropertyDef emfPropertyDef = createPropertyDef("emf",
                    AccessTypeDefFactory.FIELD, emfComponentDef);
            emComponentDef.addPropertyDef(emfPropertyDef);
            return createPropertyDef(propertyName, accessTypeDef,
                    emComponentDef);
        }

        // not specified element (binding by type)
        return createPropertyDef(propertyName, accessTypeDef);
    }
}

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
package org.seasar.framework.container.factory.property;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.AbstractExpression;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.util.StringUtil;

/**
 * {@link PersistenceUnit}アノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class PersistenceUnitPropertyDefBuilder extends
        AbstractPropertyDefBuilder<PersistenceUnit> {

    /**
     * インスタンスを構築します。
     */
    public PersistenceUnitPropertyDefBuilder() {
    }

    @Override
    protected Class<PersistenceUnit> getAnnotationType() {
        return PersistenceUnit.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef,
            final PersistenceUnit persistenceUnit) {
        final String name = persistenceUnit.name();
        if (!StringUtil.isEmpty(name)) {
            return createPropertyDef(propertyName, accessTypeDef, name);
        }

        final String unitName = persistenceUnit.unitName();
        if (StringUtil.isEmpty(unitName)) {
            return createPropertyDef(propertyName, accessTypeDef);
        }

        return createPropertyDef(propertyName, accessTypeDef,
                createPersistenceUnitComponentDef(unitName));
    }

    /**
     * {@link EntityManagerFactory 永続ユニット}を取得するための{@link ComponentDef}を作成して返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return {@link EntityManagerFactory 永続ユニット}を取得するための{@link ComponentDef}
     */
    protected static ComponentDef createPersistenceUnitComponentDef(
            final String unitName) {
        final ComponentDef componentDef = new ComponentDefImpl(
                EntityManagerFactory.class);
        componentDef.setExpression(getExpression(unitName));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("close"));
        return componentDef;
    }

    /**
     * 評価されると{@link EntityManagerFactory 永続ユニット}を返す{@link Expression}を作成して返します。
     * 
     * @param unitName
     *            永続ユニット名
     * @return 評価されると{@link EntityManagerFactory 永続ユニット}を返す{@link Expression}
     */
    protected static Expression getExpression(final String unitName) {
        return new PersistenceUnitExpression(unitName);
    }

    /**
     * 評価されると{@link EntityManagerFactory 永続ユニット}を返す{@link Expression}の実装クラスです。
     * 
     * @author koichik
     */
    public static class PersistenceUnitExpression extends AbstractExpression {

        String unitName;

        /**
         * インスタンスを構築します。
         * 
         * @param unitName
         *            永続ユニット名
         */
        public PersistenceUnitExpression(String unitName) {
            super(unitName);
            this.unitName = unitName;
        }

        /**
         * 永続ユニット名を返します。
         * 
         * @return 永続ユニット名
         */
        public String getUnitName() {
            return unitName;
        }

        @SuppressWarnings("unchecked")
        public Object evaluate(final S2Container container, final Map context) {
            final PersistenceUnitManager pum = (PersistenceUnitManager) container
                    .getComponent("jpa.persistenceUnitManager");
            return pum.getEntityManagerFactory(unitName);
        }

    }

}

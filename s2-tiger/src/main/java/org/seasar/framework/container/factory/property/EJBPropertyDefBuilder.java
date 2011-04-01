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

import javax.ejb.EJB;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.util.StringUtil;

/**
 * {@link EJB}アノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class EJBPropertyDefBuilder extends AbstractPropertyDefBuilder<EJB> {

    /**
     * インスタンスを構築します。
     */
    public EJBPropertyDefBuilder() {
    }

    @Override
    protected Class<EJB> getAnnotationType() {
        return EJB.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final EJB ejb) {
        return createPropertyDef(propertyName, accessTypeDef,
                getExpression(ejb));
    }

    /**
     * {@link EJB}アノテーションで指定されたセッションビーンを取得するOGNL式を返します。
     * 
     * @param ejb
     *            {@link EJB}アノテーション
     * @return {@link EJB}アノテーションで指定されたセッションビーンを取得するOGNL式
     */
    protected String getExpression(final EJB ejb) {
        String name = ejb.beanName();
        if (StringUtil.isEmpty(name)) {
            name = ejb.name();
        }
        return name.replace('/', '.');
    }
}

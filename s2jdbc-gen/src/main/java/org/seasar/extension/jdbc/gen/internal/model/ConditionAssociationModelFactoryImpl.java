/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.ConditionAssociationModel;
import org.seasar.extension.jdbc.gen.model.ConditionAssociationModelFactory;

/**
 * {@link ConditionAssociationModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ConditionAssociationModelFactoryImpl implements
        ConditionAssociationModelFactory {

    /** 条件クラス名のサフィックス */
    protected String conditionClassNameSuffix;

    /**
     * インスタンスを構築します。
     * 
     * @param conditionClassNameSuffix
     *            条件クラス名のサフィックス
     */
    public ConditionAssociationModelFactoryImpl(String conditionClassNameSuffix) {
        if (conditionClassNameSuffix == null) {
            throw new NullPointerException("conditionClassNameSuffix");
        }
        this.conditionClassNameSuffix = conditionClassNameSuffix;
    }

    public ConditionAssociationModel getConditionAssociationModel(
            PropertyMeta propertyMeta) {
        ConditionAssociationModel model = new ConditionAssociationModel();
        model.setName(propertyMeta.getName());
        Class<?> relationshipClass = propertyMeta.getRelationshipClass();
        String shortReturnClassName = relationshipClass.getSimpleName()
                + conditionClassNameSuffix;
        model.setShortConditionClassName(shortReturnClassName);
        return model;
    }
}

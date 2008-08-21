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
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.model.AttributeModel;
import org.seasar.extension.jdbc.gen.model.AttributeModelFactory;

/**
 * {@link AttributeModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class AttributeModelFactoryImpl implements AttributeModelFactory {

    public AttributeModel getAttributeModel(AttributeDesc attributeDesc) {
        AttributeModel attributeModel = new AttributeModel();
        attributeModel.setName(attributeDesc.getName());
        attributeModel.setId(attributeDesc.isId());
        attributeModel.setLob(attributeDesc.isLob());
        attributeModel.setTransient(attributeDesc.isTransient());
        attributeModel.setVersion(attributeDesc.isVersion());
        attributeModel.setNullable(attributeDesc.isNullable());
        attributeModel.setUnique(attributeDesc.isUnique());
        attributeModel.setTemporalType(attributeDesc.getTemporalType());
        attributeModel.setAttributeClass(attributeDesc.getAttributeClass());
        attributeModel.setColumnDefinition(attributeDesc.getColumnDefinition());
        attributeModel.setColumnTypeName(attributeDesc.getColumnTypeName());
        return attributeModel;
    }

}

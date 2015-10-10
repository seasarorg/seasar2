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

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModel;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModelFactory;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableStringCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link ConditionAttributeModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ConditionAttributeModelFactoryImpl implements
        ConditionAttributeModelFactory {

    public ConditionAttributeModel getConditionAttributeModel(
            PropertyMeta propertyMeta) {
        ConditionAttributeModel attributeModel = new ConditionAttributeModel();
        attributeModel.setName(propertyMeta.getName());
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(propertyMeta
                .getPropertyClass());
        attributeModel.setAttributeClass(clazz);
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            column = AnnotationUtil.getDefaultColumn();
        }
        Class<?> conditionClass = null;
        if (clazz == String.class) {
            conditionClass = column.nullable() ? NullableStringCondition.class
                    : NotNullableStringCondition.class;
        } else {
            conditionClass = column.nullable() ? NullableCondition.class
                    : NotNullableCondition.class;
            attributeModel.setParameterized(true);
        }
        attributeModel.setConditionClass(conditionClass);
        return attributeModel;
    }
}

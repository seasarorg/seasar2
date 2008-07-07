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
package org.seasar.extension.jdbc.gen.model;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.ConditionAttributeModel;
import org.seasar.extension.jdbc.gen.ConditionAttributeModelFactory;
import org.seasar.extension.jdbc.gen.util.AnnotationUtil;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableStringCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class ConditionAttributeModelFactoryImpl implements
        ConditionAttributeModelFactory {

    public ConditionAttributeModel getConditionAttributeModel(
            PropertyMeta propertyMeta) {
        ConditionAttributeModel model = new ConditionAttributeModel();
        model.setName(propertyMeta.getName());
        Class<?> clazz = propertyMeta.getPropertyClass();
        model.setAttributeClass(ClassUtil.getWrapperClassIfPrimitive(clazz));
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            column = AnnotationUtil.getDefaultColumn();
        }
        if (clazz == String.class) {
            if (column.nullable()) {
                model.setConditionClass(NullableStringCondition.class);
            } else {
                model.setConditionClass(NotNullableStringCondition.class);
            }
        } else {
            if (column.nullable()) {
                model.setConditionClass(NullableCondition.class);
            } else {
                model.setConditionClass(NotNullableCondition.class);
            }
        }
        return model;
    }

}

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

import java.math.BigDecimal;

import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.AttributeModel;
import org.seasar.extension.jdbc.gen.AttributeModelFactory;
import org.seasar.framework.util.ClassUtil;

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
        attributeModel.setTemporalType(attributeDesc.getTemporalType());
        attributeModel.setAttributeClass(attributeDesc.getAttributeClass());
        doLength(attributeDesc, attributeModel);
        doPrecision(attributeDesc, attributeModel);
        doScale(attributeDesc, attributeModel);
        return attributeModel;
    }

    /**
     * 長さを処理します。
     * 
     * @param attributeDesc
     *            属性記述
     * @param attributeModel
     *            属性モデル
     */
    protected void doLength(AttributeDesc attributeDesc,
            AttributeModel attributeModel) {
        Class<?> attributeClass = attributeDesc.getAttributeClass();
        int length = attributeDesc.getLength();
        if (!isNumber(attributeClass) && length > 0) {
            attributeModel.setLengthAvailable(true);
            attributeModel.setLength(length);
        }
    }

    /**
     * 精度を処理します。
     * 
     * @param attributeDesc
     *            属性記述
     * @param attributeModel
     *            属性モデル
     */
    protected void doPrecision(AttributeDesc attributeDesc,
            AttributeModel attributeModel) {
        Class<?> attributeClass = attributeDesc.getAttributeClass();
        int precision = attributeDesc.getPrecision();
        if (isNumber(attributeClass) && precision > 0) {
            attributeModel.setPrecisionAvailable(true);
            attributeModel.setPrecision(precision);
        }
    }

    /**
     * スケールを処理します。
     * 
     * @param attributeDesc
     *            属性記述
     * @param attributeModel
     *            属性モデル
     */
    protected void doScale(AttributeDesc attributeDesc,
            AttributeModel attributeModel) {
        Class<?> attributeClass = attributeDesc.getAttributeClass();
        int scale = attributeDesc.getScale();
        if (isDecimalNumber(attributeClass) && scale > 0) {
            attributeModel.setScaleAvailable(true);
            attributeModel.setScale(scale);
        }
    }

    /**
     * 数を表す場合{@code true}を返します。
     * 
     * @param attributeClass
     *            属性のクラス
     * @return 数を表す場合{@code true}
     */
    protected boolean isNumber(Class<?> attributeClass) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(attributeClass);
        return Number.class.isAssignableFrom(clazz);
    }

    /**
     * 小数を表す場合{@code true}を返します。
     * 
     * @param attributeClass
     *            属性のクラス
     * @return 小数を表す場合{@code true}を返します。
     */
    protected boolean isDecimalNumber(Class<?> attributeClass) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(attributeClass);
        return clazz == BigDecimal.class || clazz == Float.class
                || clazz == Double.class;
    }
}

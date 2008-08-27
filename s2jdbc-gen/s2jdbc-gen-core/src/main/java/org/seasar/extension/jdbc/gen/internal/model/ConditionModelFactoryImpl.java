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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModel;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModelFactory;
import org.seasar.extension.jdbc.gen.model.ConditionMethodModel;
import org.seasar.extension.jdbc.gen.model.ConditionMethodModelFactory;
import org.seasar.extension.jdbc.gen.model.ConditionModel;
import org.seasar.extension.jdbc.gen.model.ConditionModelFactory;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.condition.AbstractEntityCondition;

/**
 * {@link ConditionModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ConditionModelFactoryImpl implements ConditionModelFactory {

    /** 条件クラスの属性モデルのファクトリ */
    protected ConditionAttributeModelFactory conditionAttributeModelFactory;

    /** 条件クラスのメソッドモデルのファクトリ */
    protected ConditionMethodModelFactory conditionMethodModelFactory;

    /** パッケージ名 */
    protected String packageName;

    /** 条件クラス名のサフィックス */
    protected String conditionClassNameSuffix;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param conditionAttributeModelFactory
     *            条件クラスの属性モデルのファクトリ
     * @param conditionMethodModelFactory
     *            条件クラスのメソッドモデルのファクトリ
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param conditionClassNameSuffix
     *            条件クラス名のサフィックス
     */
    public ConditionModelFactoryImpl(
            ConditionAttributeModelFactory conditionAttributeModelFactory,
            ConditionMethodModelFactory conditionMethodModelFactory,
            String packageName, String conditionClassNameSuffix) {
        if (conditionAttributeModelFactory == null) {
            throw new NullPointerException("conditionAttributeModelFactory");
        }
        if (conditionMethodModelFactory == null) {
            throw new NullPointerException("conditionMethodModelFactory");
        }
        if (conditionClassNameSuffix == null) {
            throw new NullPointerException("conditionClassNameSuffix");
        }
        this.conditionAttributeModelFactory = conditionAttributeModelFactory;
        this.conditionMethodModelFactory = conditionMethodModelFactory;
        this.packageName = packageName;
        this.conditionClassNameSuffix = conditionClassNameSuffix;
    }

    public ConditionModel getConditionModel(EntityMeta entityMeta) {
        ConditionModel conditionModel = new ConditionModel();
        conditionModel.setPackageName(packageName);
        conditionModel.setShortClassName(entityMeta.getName()
                + conditionClassNameSuffix);
        conditionModel.setShortEntityClassName(entityMeta.getEntityClass()
                .getSimpleName());
        for (int i = 0; i < entityMeta.getPropertyMetaSize(); i++) {
            PropertyMeta propertyMeta = entityMeta.getPropertyMeta(i);
            if (propertyMeta.isTransient()) {
                continue;
            }
            if (propertyMeta.isRelationship()) {
                doConditionMethodModel(conditionModel, propertyMeta);
            } else {
                doConditionAttributeModel(conditionModel, propertyMeta);
            }
        }
        doImportName(conditionModel, entityMeta);
        return conditionModel;
    }

    /**
     * 条件クラスの属性モデルを処理します。
     * 
     * @param conditionModel
     *            条件クラスのモデル
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void doConditionAttributeModel(ConditionModel conditionModel,
            PropertyMeta propertyMeta) {
        ConditionAttributeModel attributeModel = conditionAttributeModelFactory
                .getConditionAttributeModel(propertyMeta);
        conditionModel.addConditionAttributeModel(attributeModel);
    }

    /**
     * 条件クラスのメソッドモデルを処理します。
     * 
     * @param conditionModel
     *            条件クラスのモデル
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void doConditionMethodModel(ConditionModel conditionModel,
            PropertyMeta propertyMeta) {
        ConditionMethodModel methodModel = conditionMethodModelFactory
                .getConditionMethodModel(propertyMeta);
        conditionModel.addConditionMethodModel(methodModel);
    }

    /**
     * インポート名を処理します。
     * 
     * @param conditionModel
     *            条件クラスのモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(ConditionModel conditionModel,
            EntityMeta entityMeta) {
        classModelSupport.addImportName(conditionModel, ComplexWhere.class);
        classModelSupport.addImportName(conditionModel,
                AbstractEntityCondition.class);
        for (ConditionAttributeModel attributeModel : conditionModel
                .getConditionAttributeModelList()) {
            classModelSupport.addImportName(conditionModel, attributeModel
                    .getAttributeClass());
            classModelSupport.addImportName(conditionModel, attributeModel
                    .getConditionClass());
        }
    }

}

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

import javax.annotation.Generated;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.NamesAssociationModel;
import org.seasar.extension.jdbc.gen.model.NamesAttributeModel;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;
import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link NamesModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class NamesModelFactoryImpl implements NamesModelFactory {

    /** 内部クラスのプレフィックス */
    protected static String INNER_CLASS_NAME_PREFIX = "_";

    /** パッケージ名、デフォルトパッケージの場合は{@code null} */
    protected String packageName;

    /** 名前クラス名のサフィックス */
    protected String namesClassNameSuffix;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param namesClassNameSuffix
     *            名前クラス名のサフィックス
     * @param packageName
     *            パッケージ名、デフォルトパッケージの場合は{@code null}
     */
    public NamesModelFactoryImpl(String packageName, String namesClassNameSuffix) {
        if (namesClassNameSuffix == null) {
            throw new NullPointerException("namesClassNameSuffix");
        }
        this.packageName = packageName;
        this.namesClassNameSuffix = namesClassNameSuffix;
    }

    public NamesModel getNamesModel(EntityMeta entityMeta) {
        NamesModel namesModel = new NamesModel();
        namesModel.setPackageName(packageName);
        String shortClassName = entityMeta.getName() + namesClassNameSuffix;
        namesModel.setShortClassName(shortClassName);
        namesModel.setShortInnerClassName(INNER_CLASS_NAME_PREFIX
                + shortClassName);
        namesModel.setEntityClassName(entityMeta.getEntityClass().getName());
        namesModel.setShortEntityClassName(entityMeta.getEntityClass()
                .getSimpleName());
        for (PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            if (propertyMeta.isTransient()) {
                continue;
            }
            if (propertyMeta.isRelationship()) {
                doNamesAssociationModel(namesModel, propertyMeta);
            } else {
                doNamesAttributeModel(namesModel, propertyMeta);
            }
        }
        doImportName(namesModel, entityMeta);
        doGeneratedInfo(namesModel, entityMeta);
        return namesModel;
    }

    /**
     * 名前の属性モデルを処理します。
     * 
     * @param namesModel
     *            名前モデル
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void doNamesAttributeModel(NamesModel namesModel,
            PropertyMeta propertyMeta) {
        NamesAttributeModel namesAttributeModel = new NamesAttributeModel();
        namesAttributeModel.setName(propertyMeta.getName());
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(propertyMeta
                .getPropertyClass());
        namesAttributeModel.setAttributeClass(clazz);
        namesModel.addNamesAttributeModel(namesAttributeModel);
    }

    /**
     * 名前の関連モデルを処理します。
     * 
     * @param namesModel
     *            名前モデル
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void doNamesAssociationModel(NamesModel namesModel,
            PropertyMeta propertyMeta) {
        NamesAssociationModel namesAssociationModel = new NamesAssociationModel();
        namesAssociationModel.setName(propertyMeta.getName());
        String shortClassName = INNER_CLASS_NAME_PREFIX
                + propertyMeta.getRelationshipClass().getSimpleName()
                + namesClassNameSuffix;
        namesAssociationModel.setShortClassName(shortClassName);
        StringBuilder buf = new StringBuilder();
        buf.append(packageName);
        buf.append(".");
        buf.append(propertyMeta.getRelationshipClass().getSimpleName());
        buf.append(namesClassNameSuffix);
        buf.append(".");
        buf.append(shortClassName);
        namesAssociationModel.setClassName(buf.toString());
        namesModel.adddNamesAssociationModel(namesAssociationModel);
    }

    /**
     * インポート名を処理します。
     * 
     * @param namesModel
     *            名前モデル
     * @param entityMeta
     *            エンティティメターデータ
     */
    protected void doImportName(NamesModel namesModel, EntityMeta entityMeta) {
        classModelSupport
                .addImportName(namesModel, entityMeta.getEntityClass());
        classModelSupport.addImportName(namesModel, PropertyName.class);
        classModelSupport.addImportName(namesModel, Generated.class);
        for (NamesAttributeModel attributeModel : namesModel
                .getNamesAttributeModelList()) {
            classModelSupport.addImportName(namesModel, attributeModel
                    .getAttributeClass());
        }
        for (NamesAssociationModel associationModel : namesModel
                .getNamesAssociationModelList()) {
            classModelSupport.addImportName(namesModel, associationModel
                    .getClassName());
        }
    }

    /**
     * 生成情報を処理します。
     * 
     * @param namesModel
     *            名前モデル
     * @param entityMeta
     *            エンティティメターデータ
     */
    protected void doGeneratedInfo(NamesModel namesModel, EntityMeta entityMeta) {
        generatedModelSupport.fillGeneratedInfo(this, namesModel);
    }
}

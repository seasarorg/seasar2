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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.AssociationModel;
import org.seasar.extension.jdbc.gen.model.AssociationModelFactory;
import org.seasar.extension.jdbc.gen.model.AttributeModel;
import org.seasar.extension.jdbc.gen.model.AttributeModelFactory;
import org.seasar.extension.jdbc.gen.model.CompositeUniqueConstraintModel;
import org.seasar.extension.jdbc.gen.model.CompositeUniqueConstraintModelFactory;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.EntityModelFactory;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link EntityModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityModelFactoryImpl implements EntityModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** テーブル名をカタログ名とスキーマ名で修飾する場合{@code true}、修飾しない場合{@code false} */
    protected boolean tableNameQualified;

    /** 属性モデルのファクトリ */
    protected AttributeModelFactory attributeModelFactory;

    /** 関連モデルのファクトリ */
    protected AssociationModelFactory associationModelFactory;

    /** 複合一意制約モデルのファクトリ */
    protected CompositeUniqueConstraintModelFactory compositeUniqueConstraintModelFactory;

    /**
     * インスタンスを構築しますｌ
     * 
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param tableNameQualified
     *            テーブル名をカタログ名とスキーマ名で修飾する場合{@code true}、修飾しない場合{@code false}
     * @param attributeModelFactory
     *            属性モデルのファクトリ
     * @param associationModelFactory
     *            関連モデルのファクトリ
     * @param compositeUniqueConstraintModelFactory
     *            複合一意制約モデルのファクトリ
     */
    public EntityModelFactoryImpl(
            String packageName,
            boolean tableNameQualified,
            AttributeModelFactory attributeModelFactory,
            AssociationModelFactory associationModelFactory,
            CompositeUniqueConstraintModelFactory compositeUniqueConstraintModelFactory) {
        if (attributeModelFactory == null) {
            throw new NullPointerException("attributeModelFactory");
        }
        if (associationModelFactory == null) {
            throw new NullPointerException("associationModelFactory");
        }
        if (compositeUniqueConstraintModelFactory == null) {
            throw new NullPointerException(
                    "compositeUniqueConstraintModelFactory");
        }
        this.packageName = packageName;
        this.tableNameQualified = tableNameQualified;
        this.attributeModelFactory = attributeModelFactory;
        this.associationModelFactory = associationModelFactory;
        this.compositeUniqueConstraintModelFactory = compositeUniqueConstraintModelFactory;
    }

    public EntityModel getEntityModel(EntityDesc entityDesc) {
        EntityModel entityModel = new EntityModel();
        if (tableNameQualified) {
            entityModel.setCatalogName(entityDesc.getCatalogName());
            entityModel.setSchemaName(entityDesc.getSchemaName());
        }
        entityModel.setPackageName(packageName);
        entityModel.setShortClassName(entityDesc.getName());
        entityModel.setCompositeId(entityDesc.hasCompositeId());
        doAttributeModel(entityModel, entityDesc);
        doAssociationModel(entityModel, entityDesc);
        doCompositeUniqueConstraintModel(entityModel, entityDesc);
        doImportName(entityModel, entityDesc);
        return entityModel;
    }

    /**
     * 属性モデルを処理します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doAttributeModel(EntityModel entityModel,
            EntityDesc entityDesc) {
        for (AttributeDesc attributeDesc : entityDesc.getAttributeDescList()) {
            AttributeModel attributeModel = attributeModelFactory
                    .getAttributeModel(attributeDesc);
            entityModel.addAttributeModel(attributeModel);
        }
    }

    /**
     * 関連モデルを処理します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doAssociationModel(EntityModel entityModel,
            EntityDesc entityDesc) {
        for (AssociationDesc associationDesc : entityDesc
                .getAssociationDescList()) {
            AssociationModel associationModel = associationModelFactory
                    .getAssociationModel(associationDesc);
            entityModel.addAssociationModel(associationModel);
        }
    }

    /**
     * 複合一意制約モデルを処理します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doCompositeUniqueConstraintModel(EntityModel entityModel,
            EntityDesc entityDesc) {
        for (CompositeUniqueConstraintDesc compositeUniqueConstraintDesc : entityDesc
                .getCompositeUniqueConstraintDescList()) {
            CompositeUniqueConstraintModel compositeUniqueConstraintModel = compositeUniqueConstraintModelFactory
                    .getUniqueConstraintModel(compositeUniqueConstraintDesc);
            entityModel
                    .addCompositeUniqueConstraintModel(compositeUniqueConstraintModel);
        }
    }

    /**
     * インポート名を処理します。
     * 
     * @param model
     *            エンティティクラスのモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doImportName(EntityModel model, EntityDesc entityDesc) {
        model.addImportName(Entity.class.getName());
        if (model.getCatalogName() != null || model.getSchemaName() != null) {
            model.addImportName(Table.class.getName());
        }
        for (AttributeModel attr : model.getAttributeModelList()) {
            if (attr.isId()) {
                model.addImportName(Id.class.getName());
                if (!model.hasCompositeId()) {
                    model.addImportName(GeneratedValue.class.getName());
                }
            }
            if (attr.isLob()) {
                model.addImportName(Lob.class.getName());
            }
            if (attr.getTemporalType() != null) {
                model.addImportName(Temporal.class.getName());
                model.addImportName(TemporalType.class.getName());
            }
            if (attr.isTransient()) {
                model.addImportName(Transient.class.getName());
            } else {
                model.addImportName(Column.class.getName());
            }
            if (attr.isVersion()) {
                model.addImportName(Version.class.getName());
            }

            String name = ClassUtil.getPackageName(attr.getAttributeClass());
            if (name != null && !"java.lang".equals(name)) {
                model.addImportName(attr.getAttributeClass().getName());
            }
        }
        for (AssociationModel asso : model.getAssociationModelList()) {
            AssociationType associationType = asso.getAssociationType();
            if (associationType == AssociationType.ONE_TO_MANY) {
                model.addImportName(List.class.getName());
            }
            model.addImportName(associationType.getAnnotation().getName());
            if (asso.getJoinColumnModel() != null) {
                model.addImportName(JoinColumn.class.getName());
            }
            if (asso.getJoinColumnsModel() != null) {
                model.addImportName(JoinColumn.class.getName());
                model.addImportName(JoinColumns.class.getName());
            }
        }
        if (!model.getCompositeUniqueConstraintModelList().isEmpty()) {
            model.addImportName(Table.class.getName());
            model.addImportName(UniqueConstraint.class.getName());
        }
    }
}

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.AttributeModel;
import org.seasar.extension.jdbc.gen.AttributeModelFactory;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.EntityModel;
import org.seasar.extension.jdbc.gen.EntityModelFactory;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link EntityModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityModelFactoryImpl implements EntityModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** 属性モデルのファクトリ */
    protected AttributeModelFactory attributeModelFactory;

    /**
     * インスタンスを構築しますｌ
     * 
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param attributeModelFactory
     *            属性モデルのファクトリ
     */
    public EntityModelFactoryImpl(String packageName,
            AttributeModelFactory attributeModelFactory) {
        if (attributeModelFactory == null) {
            throw new NullPointerException("attributeModelFactory");
        }
        this.packageName = packageName;
        this.attributeModelFactory = attributeModelFactory;
    }

    public EntityModel getEntityModel(EntityDesc entityDesc) {
        EntityModel entityModel = new EntityModel();
        entityModel.setCatalogName(entityDesc.getCatalogName());
        entityModel.setSchemaName(entityDesc.getSchemaName());
        entityModel.setPackageName(packageName);
        entityModel.setShortClassName(entityDesc.getName());
        entityModel.setCompositeId(entityDesc.hasCompositeId());
        doAttributeModel(entityModel, entityDesc);
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
        for (AttributeDesc attr : entityDesc.getAttributeDescList()) {
            if (attr.isId()) {
                model.addImportName(Id.class.getName());
                if (!entityDesc.hasCompositeId()) {
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
    }
}

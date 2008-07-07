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

    public EntityModel getEntityModel(EntityDesc entityDesc, String className) {
        EntityModel model = new EntityModel();
        model.setClassName(className);
        model.setEntityDesc(entityDesc);
        if (entityDesc.getCatalogName() != null
                || entityDesc.getSchemaName() != null) {
            model.setTableQualified(true);
        }
        doImportPackageNames(model, entityDesc);
        return model;
    }

    /**
     * インポートするパッケージ名を処理します。
     * 
     * @param model
     *            エンティティクラスのモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doImportPackageNames(EntityModel model, EntityDesc entityDesc) {
        model.addImportPackageName(Entity.class.getName());
        if (model.isTableQualified()) {
            model.addImportPackageName(Table.class.getName());
        }
        for (AttributeDesc attr : entityDesc.getAttributeDescList()) {
            if (attr.isId()) {
                model.addImportPackageName(Id.class.getName());
                if (!entityDesc.hasCompositeId()) {
                    model.addImportPackageName(GeneratedValue.class.getName());
                }
            }
            if (attr.isLob()) {
                model.addImportPackageName(Lob.class.getName());
            }
            if (attr.getTemporalType() != null) {
                model.addImportPackageName(Temporal.class.getName());
                model.addImportPackageName(TemporalType.class.getName());
            }
            if (attr.isTransient()) {
                model.addImportPackageName(Transient.class.getName());
            } else {
                model.addImportPackageName(Column.class.getName());
            }
            if (attr.isVersion()) {
                model.addImportPackageName(Version.class.getName());
            }

            String name = ClassUtil.getPackageName(attr.getAttributeClass());
            if (name != null && !"java.lang".equals(name)) {
                model.addImportPackageName(attr.getAttributeClass().getName());
            }
        }
    }
}

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
package org.seasar.extension.jdbc.gen.converter;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.gen.ForeignKeyModelConverter;
import org.seasar.extension.jdbc.gen.model.ForeignKeyModel;
import org.seasar.extension.jdbc.gen.model.TableModel;

/**
 * @author taedium
 * 
 */
public class ForeignKeyModelConverterImpl implements ForeignKeyModelConverter {

    protected EntityMetaFactory entityMetaFactory;

    public ForeignKeyModelConverterImpl(EntityMetaFactory entityMetaFactory) {
        this.entityMetaFactory = entityMetaFactory;
    }

    public ForeignKeyModel convert(PropertyMeta propertyMeta,
            TableModel tableModel) {
        if (propertyMeta.getJoinColumnMetaList().isEmpty()) {
            return null;
        }
        ForeignKeyModel foreignKeyModel = new ForeignKeyModel();
        doName(propertyMeta, tableModel, foreignKeyModel);
        doColumn(propertyMeta, tableModel, foreignKeyModel);
        doTable(propertyMeta, tableModel, foreignKeyModel);
        return foreignKeyModel;
    }

    protected void doName(PropertyMeta propertyMeta, TableModel tableModel,
            ForeignKeyModel foreignKeyModel) {
        String name = tableModel.getName() + "_FK"
                + tableModel.getForeignKeyModelList().size();
        foreignKeyModel.setName(name);
    }

    protected void doColumn(PropertyMeta propertyMeta, TableModel tableModel,
            ForeignKeyModel foreignKeyModel) {
        for (JoinColumnMeta jcm : propertyMeta.getJoinColumnMetaList()) {
            foreignKeyModel.addColumnName(jcm.getName());
            foreignKeyModel.addReferencedColumnName(jcm
                    .getReferencedColumnName());
        }
    }

    protected void doTable(PropertyMeta propertyMeta, TableModel tableModel,
            ForeignKeyModel foreignKeyModel) {
        EntityMeta inverseEntityMeta = entityMetaFactory
                .getEntityMeta(propertyMeta.getRelationshipClass());
        TableMeta referencedTableMeta = inverseEntityMeta.getTableMeta();
        foreignKeyModel
                .setReferencedSchemaName(referencedTableMeta.getSchema());
        foreignKeyModel.setReferencedTableName(referencedTableMeta.getName());
    }
}

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

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.PrimaryKeyModelConverter;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
import org.seasar.extension.jdbc.gen.model.TableModel;

/**
 * @author taedium
 * 
 */
public class PrimaryKeyModelConverterImpl implements PrimaryKeyModelConverter {

    protected GenDialect dialect;

    public PrimaryKeyModelConverterImpl(GenDialect dialect) {
        this.dialect = dialect;
    }

    public PrimaryKeyModel convert(EntityMeta entityMeta, TableModel tableModel) {
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            return null;
        }
        PrimaryKeyModel primaryKeyModel = new PrimaryKeyModel();
        doName(entityMeta, tableModel, primaryKeyModel);
        doColumnName(entityMeta, tableModel, primaryKeyModel);
        doIdentity(entityMeta, tableModel, primaryKeyModel);
        return primaryKeyModel;
    }

    protected void doName(EntityMeta entityMeta, TableModel tableModel,
            PrimaryKeyModel primaryKeyModel) {
        String name = tableModel.getName() + "_PK";
        primaryKeyModel.setName(name);
    }

    protected void doColumnName(EntityMeta entityMeta, TableModel tableModel,
            PrimaryKeyModel primaryKeyModel) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            ColumnMeta columnMeta = propertyMeta.getColumnMeta();
            primaryKeyModel.addColumnName(columnMeta.getName());
        }
    }

    protected void doIdentity(EntityMeta entityMeta, TableModel tableModel,
            PrimaryKeyModel primaryKeyModel) {
        if (entityMeta.getIdPropertyMetaList().size() == 1) {
            PropertyMeta propertyMeta = entityMeta.getIdPropertyMetaList().get(
                    0);
            GenerationType generationType = getGenerationType(propertyMeta);
            if (generationType == GenerationType.IDENTITY) {
                primaryKeyModel.setIdentity(true);
            }
        }
    }

    protected GenerationType getGenerationType(PropertyMeta propertyMeta) {
        DbmsDialect dbmsDialect = dialect.getDbmsDialect();
        GenerationType generationType = propertyMeta.getGenerationType();
        return generationType == GenerationType.AUTO ? dbmsDialect
                .getDefaultGenerationType() : generationType;
    }

}

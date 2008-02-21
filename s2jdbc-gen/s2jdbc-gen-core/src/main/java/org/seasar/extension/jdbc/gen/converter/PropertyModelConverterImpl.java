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

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.model.DbColumnDesc;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * @author taedium
 * 
 */
public class PropertyModelConverterImpl implements PropertyModelConverter {

    protected PersistenceConvention persistenceConvention;

    protected GenDialect dialect;

    protected String versionColumn;

    public PropertyModelConverterImpl(
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumn) {
        this.persistenceConvention = persistenceConvention;
        this.dialect = dialect;
        this.versionColumn = versionColumn;
    }

    public PropertyModel convert(DbColumnDesc columnDesc) {
        PropertyModel propertyModel = new PropertyModel();
        doName(columnDesc, propertyModel);
        doId(columnDesc, propertyModel);
        doLob(columnDesc, propertyModel);
        doPropertyClass(columnDesc, propertyModel);
        doTemporalType(columnDesc, propertyModel);
        doTransient(columnDesc, propertyModel);
        doVersion(columnDesc, propertyModel);
        return propertyModel;
    }

    protected void doName(DbColumnDesc columnDesc, PropertyModel propertyModel) {
        propertyModel.setName(persistenceConvention
                .fromColumnNameToPropertyName(columnDesc.getName()));
    }

    protected void doId(DbColumnDesc columnDesc, PropertyModel propertyModel) {
        propertyModel.setId(columnDesc.isPrimaryKey());
    }

    protected void doPropertyClass(DbColumnDesc columnDesc,
            PropertyModel propertyModel) {
        Class<?> clazz = dialect.getJavaType(columnDesc.getSqlType(),
                columnDesc.getTypeName(), columnDesc.isNullable());
        propertyModel.setPropertyClass(clazz);
    }

    protected void doLob(DbColumnDesc columnDesc, PropertyModel propertyModel) {
        propertyModel.setLob(dialect.isLobType(columnDesc.getSqlType(),
                columnDesc.getTypeName()));
    }

    protected void doTemporalType(DbColumnDesc columnDesc,
            PropertyModel propertyModel) {
        propertyModel.setTemporalType(dialect.getTemporalType(columnDesc
                .getSqlType(), columnDesc.getTypeName()));
    }

    protected void doTransient(DbColumnDesc columnDesc,
            PropertyModel propertyModel) {
    }

    protected void doVersion(DbColumnDesc columnDesc,
            PropertyModel propertyModel) {
        if (versionColumn.equalsIgnoreCase(columnDesc.getName())) {
            propertyModel.setVersion(true);
        }
    }
}

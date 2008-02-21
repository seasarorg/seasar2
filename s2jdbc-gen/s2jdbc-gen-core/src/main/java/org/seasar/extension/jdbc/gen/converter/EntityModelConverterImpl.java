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

import org.seasar.extension.jdbc.gen.EntityModelConverter;
import org.seasar.extension.jdbc.gen.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.model.DbColumnDesc;
import org.seasar.extension.jdbc.gen.model.DbTableDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * @author taedium
 * 
 */
public class EntityModelConverterImpl implements EntityModelConverter {

    protected PersistenceConvention persistenceConvention;

    protected PropertyModelConverter propertyModelConverter;

    public EntityModelConverterImpl(
            PersistenceConvention persistenceConvention,
            PropertyModelConverter propertyModelConverter) {
        this.persistenceConvention = persistenceConvention;
        this.propertyModelConverter = propertyModelConverter;
    }

    public EntityModel convert(DbTableDesc tableDesc) {
        EntityModel entityModel = new EntityModel();
        doName(tableDesc, entityModel);
        for (DbColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            PropertyModel propertyModel = propertyModelConverter
                    .convert(columnDesc);
            entityModel.addPropertyModel(propertyModel);
        }
        return entityModel;
    }

    public void doName(DbTableDesc tableDesc, EntityModel entityModel) {
        entityModel.setName(persistenceConvention
                .fromTableNameToEntityName(tableDesc.getName()));
    }
}

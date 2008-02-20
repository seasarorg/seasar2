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

import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * @author taedium
 * 
 */
public class EntityModelConverter {

    protected PersistenceConvention persistenceConvention;

    protected PropertyModelConverter propertyModelConverter;

    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }

    public void setPropertyModelConverter(
            PropertyModelConverter propertyModelConverter) {
        this.propertyModelConverter = propertyModelConverter;
    }

    public EntityModel convert(TableModel tableModel) {
        EntityModel entityModel = new EntityModel();
        doName(tableModel, entityModel);
        for (ColumnModel columnModel : tableModel.getColumnModelList()) {
            PropertyModel propertyModel = propertyModelConverter
                    .convert(columnModel);
            entityModel.addPropertyModel(propertyModel);
        }
        return entityModel;
    }

    public void doName(TableModel tableModel, EntityModel entityModel) {
        entityModel.setName(persistenceConvention
                .fromTableNameToEntityName(tableModel.getName()));
    }
}

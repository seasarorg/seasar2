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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.util.Map;

import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.desc.InverseAssociationDesc;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class AssociationResolver {

    protected Map<String, EntityDesc> entityDescMap = new ArrayMap();

    public AssociationResolver(EntitySetDesc entitySetDesc) {
        for (EntityDesc entityDesc : entitySetDesc.getEntityDescList()) {
            entityDescMap.put(entityDesc.getFullTableName(), entityDesc);
        }
    }

    public void resolveRelationship(DbForeignKeyMeta fkMeta) {
        EntityDesc entityDesc = null;
        EntityDesc referencedEntityDesc = null;
        String name = buildFullTableName(fkMeta.getForeignKeyCatalogName(),
                fkMeta.getForeignKeySchemaName(), fkMeta
                        .getForeignKeyTableName());
        if (entityDescMap.containsKey(name)) {
            entityDesc = entityDescMap.get(name);
        }
        String referencedName = buildFullTableName(fkMeta
                .getPrimaryKeyCatalogName(), fkMeta.getPrimaryKeySchemaName(),
                fkMeta.getPrimaryKeyTableName());
        if (entityDescMap.containsKey(referencedName)) {
            referencedEntityDesc = entityDescMap.get(referencedName);
        }
        if (entityDesc != null && referencedEntityDesc != null) {
            AssociationDesc assoDesc = new AssociationDesc();
            assoDesc
                    .setReferencedCatalogName(fkMeta.getPrimaryKeyCatalogName());
            assoDesc.setReferencedSchemaName(fkMeta.getPrimaryKeySchemaName());
            assoDesc.setReferencedTableName(fkMeta.getPrimaryKeyTableName());
            assoDesc.setAssociationType(AssociationType.MANY_TO_ONE);
            for (String referencedColumnName : fkMeta
                    .getPrimaryKeyColumnNameList()) {
                assoDesc.addReferencedColumnName(referencedColumnName);
            }
            for (String columnName : fkMeta.getForeignKeyColumnNameList()) {
                assoDesc.addColumnName(columnName);
            }
            assoDesc.setReferencedEntityDesc(referencedEntityDesc);
            assoDesc.setName(StringUtil.decapitalize(referencedEntityDesc
                    .getName()));
            entityDesc.addAssociationDesc(assoDesc);

            InverseAssociationDesc inverseAssoDesc = new InverseAssociationDesc();
            inverseAssoDesc.setAssociationType(AssociationType.ONE_TO_MANY);
            inverseAssoDesc.setName(StringUtil
                    .decapitalize(entityDesc.getName())
                    + "List");
            inverseAssoDesc.setMappedBy(assoDesc.getName());
            inverseAssoDesc.setReferencingEntityDesc(entityDesc);
            referencedEntityDesc.addInverseAssociationDesc(inverseAssoDesc);
        }
    }

    protected String buildFullTableName(String catalog, String schema,
            String name) {
        StringBuilder buf = new StringBuilder();
        if (catalog != null) {
            buf.append(catalog).append(".");
        }
        if (schema != null) {
            buf.append(schema).append(".");
        }
        return buf.append(name).toString();
    }

}

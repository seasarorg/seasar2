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

import java.util.List;

import org.seasar.extension.jdbc.gen.desc.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * @author taedium
 * 
 */
public class EntitySetDescFactoryImpl implements EntitySetDescFactory {

    protected DbTableMetaReader dbTableMetaReader;

    protected PersistenceConvention persistenceConvention;

    protected GenDialect dialect;

    protected String versionColumnName;

    protected String schemaName;

    protected EntityDescFactory entityDescFactory;

    /**
     * @param dbTableMetaReader
     * @param dialect
     * @param persistenceConvention
     * @param versionColumnName
     * @param schemaName
     *            指定されていない場合{@code}
     */
    public EntitySetDescFactoryImpl(DbTableMetaReader dbTableMetaReader,
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnName, String schemaName) {
        if (dbTableMetaReader == null) {
            throw new NullPointerException("dbTableMetaReader");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (versionColumnName == null) {
            throw new NullPointerException("versionColumnName");
        }
        this.dbTableMetaReader = dbTableMetaReader;
        this.dialect = dialect;
        this.persistenceConvention = persistenceConvention;
        this.schemaName = schemaName;
        this.versionColumnName = versionColumnName;
        entityDescFactory = createEntityDescFactory();
    }

    public EntitySetDesc getEntitySetDesc() {
        EntitySetDesc entitySetDesc = new EntitySetDesc();
        List<DbTableMeta> dbTableMetaList = dbTableMetaReader.read();
        for (DbTableMeta tableMeta : dbTableMetaList) {
            EntityDesc entityDesc = entityDescFactory.getEntityDesc(tableMeta);
            entitySetDesc.addEntityDesc(entityDesc);
        }
        for (DbTableMeta tableMeta : dbTableMetaList) {
            for (DbForeignKeyMeta fkMeta : tableMeta.getForeignKeyMetaList()) {
                AssociationResolver resolver = createRelationshipResolver(entitySetDesc);
                resolver.resolveRelationship(fkMeta);
            }
        }
        return entitySetDesc;
    }

    protected EntityDescFactory createEntityDescFactory() {
        AttributeDescFactory attributeDescFactory = new AttributeDescFactoryImpl(
                persistenceConvention, dialect, versionColumnName);
        return new EntityDescFactoryImpl(persistenceConvention,
                attributeDescFactory, schemaName != null);
    }

    protected AssociationResolver createRelationshipResolver(
            EntitySetDesc entitySetDesc) {
        return new AssociationResolver(entitySetDesc);
    }

}

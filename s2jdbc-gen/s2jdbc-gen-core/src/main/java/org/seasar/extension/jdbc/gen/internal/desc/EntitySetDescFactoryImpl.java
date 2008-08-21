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
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDescFactory;
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
 * {@link EntitySetDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntitySetDescFactoryImpl implements EntitySetDescFactory {

    /** テーブルメタデータのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** 方言 */
    protected GenDialect dialect;

    /** バージョンカラム名 */
    protected String versionColumnName;

    /** エンティティ記述のファクトリ */
    protected EntityDescFactory entityDescFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param dbTableMetaReader
     *            テーブルメタデータのリーダ
     * @param dialect
     *            方言
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param versionColumnName
     *            バージョンカラム名
     */
    public EntitySetDescFactoryImpl(DbTableMetaReader dbTableMetaReader,
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnName) {
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
        AssociationResolver resolver = createRelationshipResolver(entitySetDesc);
        for (DbTableMeta tableMeta : dbTableMetaList) {
            for (DbForeignKeyMeta fkMeta : tableMeta.getForeignKeyMetaList()) {
                resolver.resolve(tableMeta, fkMeta);
            }
        }
        return entitySetDesc;
    }

    /**
     * {@link EntityDescFactory}の実装を作成します。
     * 
     * @return {@link EntityDescFactory}の実装
     */
    protected EntityDescFactory createEntityDescFactory() {
        AttributeDescFactory attributeDescFactory = new AttributeDescFactoryImpl(
                persistenceConvention, dialect, versionColumnName);
        CompositeUniqueConstraintDescFactory compositeUniqueConstraintDescFactory = new CompositeUniqueConstraintDescFactoryImpl();
        return new EntityDescFactoryImpl(persistenceConvention,
                attributeDescFactory, compositeUniqueConstraintDescFactory);
    }

    /**
     * 関連のリゾルバを作成します。
     * 
     * @param entitySetDesc
     *            エンティティ集合記述
     * @return 関連のリゾルバ
     */
    protected AssociationResolver createRelationshipResolver(
            EntitySetDesc entitySetDesc) {
        return new AssociationResolver(entitySetDesc);
    }

}

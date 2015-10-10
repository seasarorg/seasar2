/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDesc;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactory;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbUniqueKeyMeta;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * {@link EntityDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityDescFactoryImpl implements EntityDescFactory {

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** 属性記述のファクトリ */
    protected AttributeDescFactory attributeDescFactory;

    /** 複合一意制約記述のファクトリ */
    protected CompositeUniqueConstraintDescFactory compositeUniqueConstraintDescFactory;

    /** スキーマが指定されている場合{@code true} */
    protected boolean schemaSpecified;

    /**
     * インスタンスを生成します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param compositeUniqueConstraintDescFactory
     *            複合一意制約記述のファクトリ
     * @param attributeDescFactory
     *            属性記述のファクトリ
     */
    public EntityDescFactoryImpl(
            PersistenceConvention persistenceConvention,
            AttributeDescFactory attributeDescFactory,
            CompositeUniqueConstraintDescFactory compositeUniqueConstraintDescFactory) {
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (attributeDescFactory == null) {
            throw new NullPointerException("attributeDescFactory");
        }
        if (compositeUniqueConstraintDescFactory == null) {
            throw new NullPointerException("uniqueConstraintDescFactory");
        }
        this.persistenceConvention = persistenceConvention;
        this.attributeDescFactory = attributeDescFactory;
        this.compositeUniqueConstraintDescFactory = compositeUniqueConstraintDescFactory;
    }

    public EntityDesc getEntityDesc(DbTableMeta tableMeta) {
        EntityDesc entityDesc = new EntityDesc();
        doName(tableMeta, entityDesc);
        doTable(tableMeta, entityDesc);
        doAttributeDesc(tableMeta, entityDesc);
        doCompositeUniqueConstraintDesc(tableMeta, entityDesc);
        return entityDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param tableMeta
     *            テーブルメタ情報
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doName(DbTableMeta tableMeta, EntityDesc entityDesc) {
        entityDesc.setName(persistenceConvention
                .fromTableNameToEntityName(tableMeta.getName()));
    }

    /**
     * テーブルを処理します。
     * 
     * @param tableMeta
     *            テーブルメタ情報
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doTable(DbTableMeta tableMeta, EntityDesc entityDesc) {
        entityDesc.setCatalogName(tableMeta.getCatalogName());
        entityDesc.setSchemaName(tableMeta.getSchemaName());
        entityDesc.setTableName(tableMeta.getName());
        entityDesc.setComment(tableMeta.getComment());
    }

    /**
     * 属性記述を処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doAttributeDesc(DbTableMeta tableMeta, EntityDesc entityDesc) {
        for (DbColumnMeta columnMeta : tableMeta.getColumnMetaList()) {
            AttributeDesc attributeDesc = attributeDescFactory
                    .getAttributeDesc(tableMeta, columnMeta);
            entityDesc.addAttributeDesc(attributeDesc);
        }
    }

    /**
     * 複合一意制約記述を処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doCompositeUniqueConstraintDesc(DbTableMeta tableMeta,
            EntityDesc entityDesc) {
        for (DbUniqueKeyMeta uniqueKeyMeta : tableMeta.getUniqueKeyMetaList()) {
            CompositeUniqueConstraintDesc compositeUniqueConstraintDesc = compositeUniqueConstraintDescFactory
                    .getCompositeUniqueConstraintDesc(uniqueKeyMeta);
            if (compositeUniqueConstraintDesc != null) {
                entityDesc
                        .addCompositeUniqueConstraintDesc(compositeUniqueConstraintDesc);
            }
        }
    }

}

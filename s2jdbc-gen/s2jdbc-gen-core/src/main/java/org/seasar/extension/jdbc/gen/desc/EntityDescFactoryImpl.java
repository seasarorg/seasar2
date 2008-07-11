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
package org.seasar.extension.jdbc.gen.desc;

import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.DbColumnMeta;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
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

    protected boolean schemaSpecified;

    /**
     * インスタンスを生成します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param attributeDescFactory
     *            属性記述のファクトリ
     */
    public EntityDescFactoryImpl(PersistenceConvention persistenceConvention,
            AttributeDescFactory attributeDescFactory, boolean schemaSpecified) {
        this.persistenceConvention = persistenceConvention;
        this.attributeDescFactory = attributeDescFactory;
        this.schemaSpecified = schemaSpecified;
    }

    public EntityDesc getEntityDesc(DbTableMeta tableMeta) {
        EntityDesc entityDesc = new EntityDesc();
        doName(tableMeta, entityDesc);
        doTable(tableMeta, entityDesc);
        for (DbColumnMeta columnMeta : tableMeta.getColumnMetaList()) {
            AttributeDesc attributeDesc = attributeDescFactory
                    .getAttributeDesc(columnMeta);
            entityDesc.addAttribute(attributeDesc);
        }
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
    public void doName(DbTableMeta tableMeta, EntityDesc entityDesc) {
        entityDesc.setName(persistenceConvention
                .fromTableNameToEntityName(tableMeta.getName()));
    }

    /**
     * テーブルの名前を処理します。
     * 
     * @param tableMeta
     *            テーブルメタ情報
     * @param entityDesc
     *            エンティティ記述
     */
    public void doTable(DbTableMeta tableMeta, EntityDesc entityDesc) {
        entityDesc.setCatalogName(tableMeta.getCatalogName());
        if (schemaSpecified) {
            entityDesc.setSchemaName(tableMeta.getSchemaName());
        }
        entityDesc.setTableName(tableMeta.getName());
    }
}

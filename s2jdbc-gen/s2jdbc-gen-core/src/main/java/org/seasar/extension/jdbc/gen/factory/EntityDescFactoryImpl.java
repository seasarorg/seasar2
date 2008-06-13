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
package org.seasar.extension.jdbc.gen.factory;

import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
import org.seasar.extension.jdbc.gen.model.AttributeDesc;
import org.seasar.extension.jdbc.gen.model.DbColumnMeta;
import org.seasar.extension.jdbc.gen.model.DbTableMeta;
import org.seasar.extension.jdbc.gen.model.EntityDesc;
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

    /**
     * インスタンスを生成します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param attributeDescFactory
     *            属性記述のファクトリ
     */
    public EntityDescFactoryImpl(PersistenceConvention persistenceConvention,
            AttributeDescFactory attributeDescFactory) {
        this.persistenceConvention = persistenceConvention;
        this.attributeDescFactory = attributeDescFactory;
    }

    public EntityDesc getEntityDesc(DbTableMeta tableMeta) {
        EntityDesc entityDesc = new EntityDesc();
        doName(tableMeta, entityDesc);
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
     *            テーブルメタデータ
     * @param entityDesc
     *            エンティティ記述
     */
    public void doName(DbTableMeta tableMeta, EntityDesc entityDesc) {
        entityDesc.setName(persistenceConvention
                .fromTableNameToEntityName(tableMeta.getName()));
    }
}

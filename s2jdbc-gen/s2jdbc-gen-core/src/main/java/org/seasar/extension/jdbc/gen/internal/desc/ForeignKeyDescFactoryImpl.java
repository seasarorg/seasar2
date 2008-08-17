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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.GenDialect;

/**
 * {@link ForeignKeyDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ForeignKeyDescFactoryImpl implements ForeignKeyDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param entityMetaFactory
     */
    public ForeignKeyDescFactoryImpl(GenDialect dialect,
            EntityMetaFactory entityMetaFactory) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        this.dialect = dialect;
        this.entityMetaFactory = entityMetaFactory;
    }

    public ForeignKeyDesc getForeignKeyDesc(PropertyMeta propertyMeta) {
        if (!propertyMeta.isRelationship()
                || propertyMeta.getMappedBy() != null) {
            return null;
        }
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        doColumn(propertyMeta, foreignKeyDesc);
        doTable(propertyMeta, foreignKeyDesc);
        return foreignKeyDesc;
    }

    /**
     * カラムを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param foreignKeyDesc
     *            外部キー記述
     */
    protected void doColumn(PropertyMeta propertyMeta,
            ForeignKeyDesc foreignKeyDesc) {
        for (JoinColumnMeta jcm : propertyMeta.getJoinColumnMetaList()) {
            foreignKeyDesc.addColumnName(dialect.unquote(jcm.getName()));
            foreignKeyDesc.addReferencedColumnName(dialect.unquote(jcm
                    .getReferencedColumnName()));
        }
    }

    /**
     * テーブルを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param foreignKeyDesc
     *            外部キー記述
     */
    protected void doTable(PropertyMeta propertyMeta,
            ForeignKeyDesc foreignKeyDesc) {
        EntityMeta inverseEntityMeta = entityMetaFactory
                .getEntityMeta(propertyMeta.getRelationshipClass());
        TableMeta tableMeta = inverseEntityMeta.getTableMeta();
        foreignKeyDesc.setReferencedCatalogName(dialect.unquote(tableMeta
                .getCatalog()));
        foreignKeyDesc.setReferencedSchemaName(dialect.unquote(tableMeta
                .getSchema()));
        foreignKeyDesc.setReferencedTableName(dialect.unquote(tableMeta
                .getName()));
    }
}

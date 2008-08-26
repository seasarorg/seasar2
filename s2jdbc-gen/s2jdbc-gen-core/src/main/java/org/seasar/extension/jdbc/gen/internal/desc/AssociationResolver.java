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

import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.util.StringUtil;

/**
 * エンティティ記述の関連を解決するクラスです。
 * 
 * @author taedium
 */
public class AssociationResolver {

    /** 多側に対する関連名のサフィックス */
    protected static String TO_MANY_ASSOCIATION_NAME_SUFFIX = "List";

    /** エンティティ集合記述 */
    protected EntitySetDesc entitySetDesc;

    /** 単語を複数形に変換するための辞書 */
    protected PluralFormDictinary pluralFormDictinary;

    /**
     * インスタンスを構築します。
     * 
     * @param entitySetDesc
     *            エンティティ集合記述
     * @param pluralFormDictinary
     *            単語を複数形に変換するための辞書
     */
    public AssociationResolver(EntitySetDesc entitySetDesc,
            PluralFormDictinary pluralFormDictinary) {
        if (entitySetDesc == null) {
            throw new NullPointerException("entitySetDesc");
        }
        if (pluralFormDictinary == null) {
            throw new NullPointerException("pluralFormDictinary");
        }
        this.entitySetDesc = entitySetDesc;
        this.pluralFormDictinary = pluralFormDictinary;
    }

    /**
     * 関連を解決します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param fkMeta
     *            外部キーメタデータ
     */
    public void resolve(DbTableMeta tableMeta, DbForeignKeyMeta fkMeta) {
        EntityDesc ownerEntityDesc = entitySetDesc.getEntityDesc(tableMeta
                .getFullTableName());
        if (ownerEntityDesc == null) {
            return;
        }
        EntityDesc inverseEntityDesc = entitySetDesc.getEntityDesc(fkMeta
                .getPrimaryKeyFullTableName());
        if (inverseEntityDesc == null) {
            return;
        }
        AssociationDesc ownerAssociationDesc = doOwnerAssociationDesc(fkMeta,
                ownerEntityDesc, inverseEntityDesc);
        doInverseAssociationDesc(fkMeta, ownerEntityDesc, inverseEntityDesc,
                ownerAssociationDesc.getName());
    }

    /**
     * 所有側の関連を処理します。
     * 
     * @param fkMeta
     *            外部キーメタデータ
     * @param ownerEntityDesc
     *            関連の所有者側のエンティティ記述
     * @param inverseEntityDesc
     *            関連の被所有者側のエンティティ記述
     * @return
     */
    protected AssociationDesc doOwnerAssociationDesc(DbForeignKeyMeta fkMeta,
            EntityDesc ownerEntityDesc, EntityDesc inverseEntityDesc) {
        AssociationDesc associationDesc = new AssociationDesc();
        associationDesc.setReferencedCatalogName(fkMeta
                .getPrimaryKeyCatalogName());
        associationDesc.setReferencedSchemaName(fkMeta
                .getPrimaryKeySchemaName());
        associationDesc.setReferencedTableName(fkMeta.getPrimaryKeyTableName());
        String name = getAssociationName(ownerEntityDesc, inverseEntityDesc,
                false);
        associationDesc.setName(name);
        if (fkMeta.isUnique()) {
            associationDesc.setAssociationType(AssociationType.ONE_TO_ONE);
        } else {
            associationDesc.setAssociationType(AssociationType.MANY_TO_ONE);
        }
        for (String referencedColumnName : fkMeta.getPrimaryKeyColumnNameList()) {
            associationDesc.addReferencedColumnName(referencedColumnName);
        }
        for (String columnName : fkMeta.getForeignKeyColumnNameList()) {
            associationDesc.addColumnName(columnName);
        }
        associationDesc.setReferencedEntityDesc(inverseEntityDesc);
        ownerEntityDesc.addAssociationDesc(associationDesc);
        return associationDesc;
    }

    /**
     * 被所有側の関連を処理します。
     * 
     * @param fkMeta
     *            外部キーメタデータ
     * @param ownerEntityDesc
     *            関連の所有者側のエンティティ記述
     * @param inverseEntityDesc
     *            関連の被所有者側のエンティティ記述
     * @param mappedBy
     *            関連の所有者側のプロパティ名
     */
    protected void doInverseAssociationDesc(DbForeignKeyMeta fkMeta,
            EntityDesc ownerEntityDesc, EntityDesc inverseEntityDesc,
            String mappedBy) {
        AssociationDesc inverseAssociationDesc = new AssociationDesc();
        if (fkMeta.isUnique()) {
            String name = getAssociationName(inverseEntityDesc,
                    ownerEntityDesc, false);
            inverseAssociationDesc.setName(name);
            inverseAssociationDesc
                    .setAssociationType(AssociationType.ONE_TO_ONE);
        } else {
            String name = getAssociationName(inverseEntityDesc,
                    ownerEntityDesc, true);
            inverseAssociationDesc.setName(name);
            inverseAssociationDesc
                    .setAssociationType(AssociationType.ONE_TO_MANY);
        }
        inverseAssociationDesc.setMappedBy(mappedBy);
        inverseAssociationDesc.setReferencedEntityDesc(ownerEntityDesc);
        inverseEntityDesc.addAssociationDesc(inverseAssociationDesc);
    }

    /**
     * 関連名を返します。
     * 
     * @param referencingEntityDesc
     *            参照する側のエンティティ記述
     * @param referencedEntityDesc
     *            参照される側のエンティティ記述
     * @param oneToMany
     *            関連がOneToManyの場合{@code true}
     * @return 関連名
     */
    protected String getAssociationName(EntityDesc referencingEntityDesc,
            EntityDesc referencedEntityDesc, boolean oneToMany) {
        String entityName = referencedEntityDesc.getName();
        String associationName = StringUtil.decapitalize(entityName);
        if (oneToMany) {
            associationName = pluralizeName(associationName);
        }
        if (referencingEntityDesc.hasAssociationDesc(associationName)) {
            for (int i = 2;; i++) {
                if (!referencingEntityDesc.hasAssociationDesc(associationName
                        + i)) {
                    return associationName + i;
                }
            }
        }
        return associationName;
    }

    /**
     * 名前を複数形に変換します。
     * 
     * @param name
     *            名前
     * @return 複数形に変換された名前
     */
    protected String pluralizeName(String name) {
        String pluralizeNamed = pluralFormDictinary.lookup(name);
        if (pluralizeNamed != null) {
            return pluralizeNamed;
        }
        return name + TO_MANY_ASSOCIATION_NAME_SUFFIX;
    }
}

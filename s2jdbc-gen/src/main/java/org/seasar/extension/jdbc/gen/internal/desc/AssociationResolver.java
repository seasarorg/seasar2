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

import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.convention.PersistenceConvention;
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

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /**
     * インスタンスを構築します。
     * 
     * @param entitySetDesc
     *            エンティティ集合記述
     * @param pluralFormDictinary
     *            単語を複数形に変換するための辞書
     * @param persistenceConvention
     *            永続化層の命名規約
     */
    public AssociationResolver(EntitySetDesc entitySetDesc,
            PluralFormDictinary pluralFormDictinary,
            PersistenceConvention persistenceConvention) {
        if (entitySetDesc == null) {
            throw new NullPointerException("entitySetDesc");
        }
        if (pluralFormDictinary == null) {
            throw new NullPointerException("pluralFormDictinary");
        }
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        this.entitySetDesc = entitySetDesc;
        this.pluralFormDictinary = pluralFormDictinary;
        this.persistenceConvention = persistenceConvention;
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
        String name = getOwnerAssociationName(fkMeta, ownerEntityDesc,
                inverseEntityDesc);
        associationDesc.setName(name);
        adjustAttributeNames(fkMeta, ownerEntityDesc, name);
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
     * エンティティ記述のすべての属性について必要であれば名前を調整します。
     * 
     * @param fkMeta
     *            外部キーメタデータ
     * @param entityDesc
     *            エンティティ記述
     * @param associationName
     *            関連名
     */
    protected void adjustAttributeNames(DbForeignKeyMeta fkMeta,
            EntityDesc entityDesc, String associationName) {
        if (fkMeta.isComposite()) {
            return;
        }
        for (AttributeDesc attributeDesc : entityDesc.getAttributeDescList()) {
            if (associationName.equalsIgnoreCase(attributeDesc.getName())) {
                String pkColumnName = fkMeta.getPrimaryKeyColumnNameList().get(
                        0);
                String pkPropertyName = persistenceConvention
                        .fromColumnNameToPropertyName(pkColumnName);
                String candidateName = attributeDesc.getName()
                        + StringUtil.capitalize(pkPropertyName);
                String newName = toUniqueAttributeName(entityDesc,
                        candidateName);
                attributeDesc.setName(newName);
            }
        }
    }

    /**
     * 一意な属性名に変換します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @param candidateName
     *            候補の属性名
     * @return 一意な属性名
     */
    protected String toUniqueAttributeName(EntityDesc entityDesc,
            String candidateName) {
        if (entityDesc.hasAttributeDesc(candidateName)) {
            for (int i = 2;; i++) {
                if (!entityDesc.hasAttributeDesc(candidateName + i)) {
                    return candidateName + i;
                }
            }
        }
        return candidateName;
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
            String name = getInverseAssociationName(inverseEntityDesc,
                    ownerEntityDesc, false);
            inverseAssociationDesc.setName(name);
            inverseAssociationDesc
                    .setAssociationType(AssociationType.ONE_TO_ONE);
        } else {
            String name = getInverseAssociationName(inverseEntityDesc,
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
     * 関連の所有側の関連名を返します。
     * 
     * @param fkMeta
     *            外部キーメタデータ
     * @param ownerEntityDesc
     *            関連の所有者側のエンティティ記述
     * @param inverseEntityDesc
     *            関連の被所有者側のエンティティ記述
     * @return 関連の所有側の関連名
     */
    protected String getOwnerAssociationName(DbForeignKeyMeta fkMeta,
            EntityDesc ownerEntityDesc, EntityDesc inverseEntityDesc) {
        String associationName = StringUtil.decapitalize(inverseEntityDesc
                .getName());
        if (!fkMeta.isComposite()) {
            String fkColumnName = fkMeta.getForeignKeyColumnNameList().get(0);
            String pkColumnName = fkMeta.getPrimaryKeyColumnNameList().get(0);
            if (StringUtil.endsWithIgnoreCase(fkColumnName, pkColumnName)) {
                if (fkColumnName.length() > pkColumnName.length()) {
                    String name = fkColumnName.substring(0, fkColumnName
                            .length()
                            - pkColumnName.length());
                    name = StringUtil.trimSuffix(name, "_");
                    name = StringUtil.camelize(name);
                    if (StringUtil.startsWithIgnoreCase(pkColumnName,
                            inverseEntityDesc.getTableName())) {
                        associationName = StringUtil.decapitalize(name)
                                + inverseEntityDesc.getName();
                    } else {
                        associationName = StringUtil.decapitalize(name);
                    }
                }
            }
        }
        return toUniqueAssociationName(ownerEntityDesc, associationName);
    }

    /**
     * 関連名を返します。
     * 
     * @param inverseEntityDesc
     *            参照する側のエンティティ記述
     * @param ownerEntityDesc
     *            参照される側のエンティティ記述
     * @param oneToMany
     *            関連がOneToManyの場合{@code true}
     * @return 関連名
     */
    protected String getInverseAssociationName(EntityDesc inverseEntityDesc,
            EntityDesc ownerEntityDesc, boolean oneToMany) {
        String associationName = StringUtil.decapitalize(ownerEntityDesc
                .getName());
        if (oneToMany) {
            associationName = pluralizeName(associationName);
        }
        return toUniqueAssociationName(inverseEntityDesc, associationName);
    }

    /**
     * 一意の関連名に変換します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @param candidateName
     *            候補の関連名
     * @return 一意の関連名
     */
    protected String toUniqueAssociationName(EntityDesc entityDesc,
            String candidateName) {
        if (entityDesc.hasAssociationDesc(candidateName)) {
            for (int i = 2;; i++) {
                if (!entityDesc.hasAssociationDesc(candidateName + i)) {
                    return candidateName + i;
                }
            }
        }
        return candidateName;
    }

    /**
     * 名前を複数形に変換します。
     * 
     * @param name
     *            名前
     * @return 複数形に変換された名前
     */
    protected String pluralizeName(String name) {
        String pluralizedName = pluralFormDictinary.lookup(name);
        if (pluralizedName != null) {
            return pluralizedName;
        }
        return name + TO_MANY_ASSOCIATION_NAME_SUFFIX;
    }

}

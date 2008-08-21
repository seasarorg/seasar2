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

import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialect.ColumnType;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * {@link AttributeDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class AttributeDescFactoryImpl implements AttributeDescFactory {

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** 方言 */
    protected GenDialect dialect;

    /** バージョンカラム */
    protected String versionColumnName;

    /**
     * インスタンスを構築します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param dialect
     *            方言
     * @param versionColumnName
     *            バージョンカラム
     */
    public AttributeDescFactoryImpl(
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnName) {
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (versionColumnName == null) {
            throw new NullPointerException("versionColumnName");
        }
        this.persistenceConvention = persistenceConvention;
        this.dialect = dialect;
        this.versionColumnName = versionColumnName;
    }

    public AttributeDesc getAttributeDesc(DbColumnMeta columnMeta) {
        AttributeDesc attributeDesc = new AttributeDesc();
        doName(columnMeta, attributeDesc);
        doId(columnMeta, attributeDesc);
        doTransient(columnMeta, attributeDesc);
        doVersion(columnMeta, attributeDesc);
        doColumn(columnMeta, attributeDesc);
        return attributeDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doName(DbColumnMeta columnMeta, AttributeDesc attributeDesc) {
        attributeDesc.setName(persistenceConvention
                .fromColumnNameToPropertyName(columnMeta.getName()));
    }

    /**
     * 識別子を処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doId(DbColumnMeta columnMeta, AttributeDesc attributeDesc) {
        attributeDesc.setId(columnMeta.isPrimaryKey());
    }

    /**
     * 一時的なプロパティを処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doTransient(DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
    }

    /**
     * バージョンを処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doVersion(DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        if (versionColumnName.equalsIgnoreCase(columnMeta.getName())) {
            attributeDesc.setVersion(true);
        }
    }

    /**
     * カラムを処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doColumn(DbColumnMeta columnMeta, AttributeDesc attributeDesc) {
        attributeDesc.setColumnName(columnMeta.getName());
        attributeDesc.setColumnTypeName(columnMeta.getTypeName());
        attributeDesc.setLength(columnMeta.getLength());
        attributeDesc.setPrecision(columnMeta.getLength());
        attributeDesc.setScale(columnMeta.getScale());
        attributeDesc.setNullable(columnMeta.isNullable());
        attributeDesc.setUnique(columnMeta.isUnique());
        ColumnType columnType = dialect.getColumnType(columnMeta.getTypeName());
        if (columnType != null) {
            Class<?> clazz = columnType
                    .getAttributeClass(columnMeta.getLength(), columnMeta
                            .getLength(), columnMeta.getScale());
            attributeDesc.setAttributeClass(clazz);
            String definition = columnType
                    .getColumnDefinition(columnMeta.getLength(), columnMeta
                            .getLength(), columnMeta.getScale());
            attributeDesc.setColumnDefinition(definition);
            attributeDesc.setLob(columnType.isLob());
            attributeDesc.setTemporalType(columnType.getTemporalType());
        } else {
            attributeDesc.setAttributeClass(String.class);
            attributeDesc.setColumnDefinition(null);
            attributeDesc.setLob(false);
            attributeDesc.setTemporalType(null);
        }
    }

}

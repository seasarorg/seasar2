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
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenDialect.Type;
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
    protected String versionColumn;

    /**
     * インスタンスを構築します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param dialect
     *            方言
     * @param versionColumn
     *            バージョンカラム
     */
    public AttributeDescFactoryImpl(
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumn) {
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (versionColumn == null) {
            throw new NullPointerException("versionColumn");
        }
        this.persistenceConvention = persistenceConvention;
        this.dialect = dialect;
        this.versionColumn = versionColumn;
    }

    public AttributeDesc getAttributeDesc(DbColumnMeta columnMeta) {
        AttributeDesc attributeDesc = new AttributeDesc();
        doName(columnMeta, attributeDesc);
        doId(columnMeta, attributeDesc);
        doLob(columnMeta, attributeDesc);
        doAttributeClass(columnMeta, attributeDesc);
        doTemporalType(columnMeta, attributeDesc);
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
     * プロパティのクラスを処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doAttributeClass(DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        Type type = dialect.getType(columnMeta.getSqlType());
        Class<?> clazz = type.getJavaClass(columnMeta.getLength(), columnMeta
                .getLength(), columnMeta.getScale(), columnMeta.getTypeName());
        attributeDesc.setAttributeClass(clazz);
    }

    /**
     * <code>LOB</code>を処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doLob(DbColumnMeta columnMeta, AttributeDesc attributeDesc) {
        attributeDesc.setLob(dialect.isLobType(columnMeta.getSqlType(),
                columnMeta.getTypeName()));
    }

    /**
     * 時制の種別を処理します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doTemporalType(DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        attributeDesc.setTemporalType(dialect.getTemporalType(columnMeta
                .getSqlType(), columnMeta.getTypeName()));
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
        if (versionColumn.equalsIgnoreCase(columnMeta.getName())) {
            attributeDesc.setVersion(true);
        }
    }

    /**
     * カラムの名前を処理します。
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
        Type type = dialect.getType(columnMeta.getSqlType());
        String definition = type.getColumnDefinition(columnMeta.getLength(),
                columnMeta.getLength(), columnMeta.getScale(), columnMeta
                        .getTypeName());
        attributeDesc.setColumnDefinition(definition);
    }

}

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

import java.util.regex.Pattern;

import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.IdentityNotSupportedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.SequenceNotSupportedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;

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

    /** バージョンカラム名のパターン */
    protected Pattern versionColumnNamePattern;

    /** エンティティの識別子の生成方法を示す列挙型 、生成しない場合は{@code null} */
    protected GenerationType generationType;

    /** エンティティの識別子の初期値 */
    protected int initialValue;

    /** エンティティの識別子の割り当てサイズ */
    protected int allocationSize;

    /**
     * インスタンスを構築します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param dialect
     *            方言
     * @param versionColumnNamePattern
     *            バージョンカラム名のパターン
     * @param generationType
     *            エンティティの識別子の生成方法を示す列挙型 、生成しない場合は{@code null}
     * @param initialValue
     *            エンティティの識別子の初期値、指定しない場合は{@code null}
     * @param allocationSize
     *            エンティティの識別子の割り当てサイズ、指定しない場合は{@code null}
     */
    public AttributeDescFactoryImpl(
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnNamePattern, GenerationType generationType,
            Integer initialValue, Integer allocationSize) {
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (versionColumnNamePattern == null) {
            throw new NullPointerException("versionColumnNamePattern");
        }
        this.persistenceConvention = persistenceConvention;
        this.dialect = dialect;
        this.versionColumnNamePattern = Pattern.compile(
                versionColumnNamePattern, Pattern.CASE_INSENSITIVE);

        this.generationType = generationType == GenerationType.AUTO ? dialect
                .getDefaultGenerationType() : generationType;
        if (this.generationType == GenerationType.IDENTITY) {
            if (!dialect.supportsIdentity()) {
                throw new IdentityNotSupportedRuntimeException();
            }
        } else if (this.generationType == GenerationType.SEQUENCE) {
            if (!dialect.supportsSequence()) {
                throw new SequenceNotSupportedRuntimeException();
            }
            SequenceGenerator generator = AnnotationUtil
                    .getDefaultSequenceGenerator();
            this.initialValue = initialValue != null ? initialValue : generator
                    .initialValue();
            this.allocationSize = allocationSize != null ? allocationSize
                    : generator.allocationSize();
        } else if (this.generationType == GenerationType.TABLE) {
            TableGenerator generator = AnnotationUtil
                    .getDefaultTableGenerator();
            this.initialValue = initialValue != null ? initialValue : generator
                    .initialValue();
            this.allocationSize = allocationSize != null ? allocationSize
                    : generator.allocationSize();
        }
    }

    public AttributeDesc getAttributeDesc(DbTableMeta tableMeta,
            DbColumnMeta columnMeta) {
        AttributeDesc attributeDesc = new AttributeDesc();
        doName(tableMeta, columnMeta, attributeDesc);
        doId(tableMeta, columnMeta, attributeDesc);
        doTransient(tableMeta, columnMeta, attributeDesc);
        doColumn(tableMeta, columnMeta, attributeDesc);
        doVersion(tableMeta, columnMeta, attributeDesc);
        doGenerationType(tableMeta, columnMeta, attributeDesc);
        return attributeDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doName(DbTableMeta tableMeta, DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        attributeDesc.setName(persistenceConvention
                .fromColumnNameToPropertyName(columnMeta.getName()));
    }

    /**
     * 識別子を処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doId(DbTableMeta tableMeta, DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        if (columnMeta.isPrimaryKey()) {
            attributeDesc.setId(true);
        }
    }

    /**
     * 一時的なプロパティを処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doTransient(DbTableMeta tableMeta, DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
    }

    /**
     * バージョンを処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doVersion(DbTableMeta tableMeta, DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        if (isVersionAnnotatable(attributeDesc.getAttributeClass())) {
            if (versionColumnNamePattern.matcher(columnMeta.getName())
                    .matches()) {
                attributeDesc.setVersion(true);
            }
        }
    }

    /**
     * カラムを処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doColumn(DbTableMeta tableMeta, DbColumnMeta columnMeta,
            AttributeDesc attributeDesc) {
        attributeDesc.setColumnName(columnMeta.getName());
        attributeDesc.setColumnTypeName(columnMeta.getTypeName());
        attributeDesc.setLength(columnMeta.getLength());
        attributeDesc.setPrecision(columnMeta.getLength());
        attributeDesc.setScale(columnMeta.getScale());
        attributeDesc.setNullable(columnMeta.isNullable());
        attributeDesc.setUnique(columnMeta.isUnique());
        attributeDesc.setComment(columnMeta.getComment());
        GenDialect.ColumnType columnType = dialect.getColumnType(
                columnMeta.getTypeName(), columnMeta.getSqlType());
        if (columnType != null) {
            Class<?> clazz = columnType.getAttributeClass(
                    columnMeta.getLength(), columnMeta.getLength(),
                    columnMeta.getScale());
            attributeDesc.setAttributeClass(clazz);
            String defaultValue = attributeDesc.isId() ? null : columnMeta
                    .getDefaultValue();
            String definition = columnType.getColumnDefinition(
                    columnMeta.getLength(), columnMeta.getLength(),
                    columnMeta.getScale(), defaultValue);
            attributeDesc.setColumnDefinition(definition);
            attributeDesc.setLob(columnType.isLob());
            attributeDesc.setPrimaryTemporalType(columnType.getTemporalType());
        } else {
            attributeDesc.setUnsupportedColumnType(true);
            attributeDesc.setAttributeClass(String.class);
            attributeDesc.setLob(false);
        }
    }

    /**
     * {@link GenerationType}を処理します。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @param columnMeta
     *            カラムメタデータ
     * @param attributeDesc
     *            属性記述
     */
    protected void doGenerationType(DbTableMeta tableMeta,
            DbColumnMeta columnMeta, AttributeDesc attributeDesc) {
        if (columnMeta.isPrimaryKey() && !tableMeta.hasCompositePrimaryKey()) {
            Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(attributeDesc
                    .getAttributeClass());
            if (Number.class.isAssignableFrom(clazz)) {
                if (columnMeta.isAutoIncrement()) {
                    attributeDesc.setGenerationType(GenerationType.IDENTITY);
                } else {
                    attributeDesc.setGenerationType(generationType);
                    attributeDesc.setInitialValue(initialValue);
                    attributeDesc.setAllocationSize(allocationSize);
                }
            }
        }
    }

    /**
     * {@link Version}を注釈できるクラスの場合{@code true}
     * 
     * @param clazz
     *            クラス
     * @return {@link Version}を注釈できるクラスの場合{@code true}
     */
    protected boolean isVersionAnnotatable(Class<?> clazz) {
        Class<?> wrapperClass = ClassUtil.getWrapperClassIfPrimitive(clazz);
        return wrapperClass == Integer.class || wrapperClass == Long.class;
    }

}

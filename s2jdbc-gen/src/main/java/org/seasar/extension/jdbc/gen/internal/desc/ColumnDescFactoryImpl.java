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

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.NullableUniqueNotSupportedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.UnsupportedGenerationTypeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.internal.util.PropertyMetaUtil;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.util.StringUtil;

/**
 * {@link ColumnDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ColumnDescFactoryImpl implements ColumnDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param valueTypeProvider
     *            {@link ValueType}の提供者
     */
    public ColumnDescFactoryImpl(GenDialect dialect,
            ValueTypeProvider valueTypeProvider) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (valueTypeProvider == null) {
            throw new NullPointerException("valueTypeResolver");
        }
        this.dialect = dialect;
        this.valueTypeProvider = valueTypeProvider;
    }

    public ColumnDesc getColumnDesc(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        if (propertyMeta.isTransient() || propertyMeta.isRelationship()) {
            return null;
        }
        Column column = getColumn(propertyMeta);
        ColumnDesc columnDesc = new ColumnDesc();
        doName(entityMeta, propertyMeta, columnDesc, column);
        doComment(entityMeta, propertyMeta, columnDesc, column);
        doIdentity(entityMeta, propertyMeta, columnDesc, column);
        doDefinition(entityMeta, propertyMeta, columnDesc, column);
        doNullable(entityMeta, propertyMeta, columnDesc, column);
        doUnique(entityMeta, propertyMeta, columnDesc, column);
        doSqlType(entityMeta, propertyMeta, columnDesc, column);
        return columnDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doName(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        ColumnMeta columnMeta = propertyMeta.getColumnMeta();
        columnDesc.setName(columnMeta.getName());
    }

    /**
     * コメントを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doComment(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        String comment = PropertyMetaUtil.getComment(propertyMeta);
        columnDesc.setComment(comment);
    }

    /**
     * IDENTITYカラムを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doIdentity(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.IDENTITY) {
            if (!dialect.supportsIdentity()) {
                throw new UnsupportedGenerationTypeRuntimeException(
                        GenerationType.IDENTITY, entityMeta.getName(),
                        propertyMeta.getName());
            }
            columnDesc.setIdentity(true);
        }
    }

    /**
     * 定義を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doDefinition(EntityMeta entityMeta,
            PropertyMeta propertyMeta, ColumnDesc columnDesc, Column column) {
        SqlType type = dialect.getSqlType(valueTypeProvider, propertyMeta);
        String dataType = type.getDataType(column.length(), column.precision(),
                column.scale(), columnDesc.isIdentity());

        String definition;
        if (!StringUtil.isEmpty(column.columnDefinition())) {
            if (StringUtil.startsWithIgnoreCase(column.columnDefinition(),
                    "default ")) {
                definition = dataType + " " + column.columnDefinition();
            } else if (StringUtil.startsWithIgnoreCase(column
                    .columnDefinition(), "check ")) {
                definition = dataType + " " + column.columnDefinition();
            } else {
                definition = column.columnDefinition();
            }
        } else {
            definition = dataType;
        }
        columnDesc.setDefinition(definition);
    }

    /**
     * NULL可能かどうかを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doNullable(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        if (propertyMeta.isId()) {
            columnDesc.setNullable(false);
        } else if (column != AnnotationUtil.getDefaultColumn()) {
            columnDesc.setNullable(column.nullable());
        } else {
            Class<?> clazz = propertyMeta.getField().getType();
            columnDesc.setNullable(!clazz.isPrimitive());
        }
    }

    /**
     * 一意性を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doUnique(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        columnDesc.setUnique(column.unique());
        if (!dialect.supportsNullableUnique()) {
            if (columnDesc.isNullable() && columnDesc.isUnique()) {
                throw new NullableUniqueNotSupportedRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
        }
    }

    /**
     * SQL型を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doSqlType(EntityMeta entityMeta, PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        SqlType sqlType = dialect.getSqlType(valueTypeProvider, propertyMeta);
        columnDesc.setSqlType(sqlType);
    }

    /**
     * カラムを返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return カラム
     */
    protected Column getColumn(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        return column != null ? column : AnnotationUtil.getDefaultColumn();
    }

}

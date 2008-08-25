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

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
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

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     */
    public ColumnDescFactoryImpl(GenDialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
    }

    public ColumnDesc getColumnDesc(PropertyMeta propertyMeta) {
        if (propertyMeta.isTransient() || propertyMeta.isRelationship()) {
            return null;
        }
        Column column = getColumn(propertyMeta);
        ColumnDesc columnDesc = new ColumnDesc();
        doName(propertyMeta, columnDesc, column);
        doIdentity(propertyMeta, columnDesc, column);
        doDefinition(propertyMeta, columnDesc, column);
        doNullable(propertyMeta, columnDesc, column);
        doUnique(propertyMeta, columnDesc, column);
        doSqlType(propertyMeta, columnDesc, column);
        return columnDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doName(PropertyMeta propertyMeta, ColumnDesc columnDesc,
            Column column) {
        ColumnMeta columnMeta = propertyMeta.getColumnMeta();
        columnDesc.setName(columnMeta.getName());
    }

    /**
     * IDENTITYカラムを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doIdentity(PropertyMeta propertyMeta, ColumnDesc columnDesc,
            Column column) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.IDENTITY) {
            columnDesc.setIdentity(true);
        }
    }

    /**
     * 定義を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doDefinition(PropertyMeta propertyMeta,
            ColumnDesc columnDesc, Column column) {
        int sqlType = propertyMeta.getValueType().getSqlType();
        SqlType type = dialect.getSqlType(sqlType);
        String dataType = type.getDataType(column.length(), column.precision(),
                column.scale(), columnDesc.isIdentity());

        String definition;
        if (!StringUtil.isEmpty(column.columnDefinition())) {
            if (StringUtil.startsWithIgnoreCase(column.columnDefinition(),
                    "default ")) {
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
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doNullable(PropertyMeta propertyMeta, ColumnDesc columnDesc,
            Column column) {
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
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doUnique(PropertyMeta propertyMeta, ColumnDesc columnDesc,
            Column column) {
        columnDesc.setUnique(column.unique());
    }

    /**
     * SQL型を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param columnDesc
     *            カラム記述
     * @param column
     *            カラム
     */
    protected void doSqlType(PropertyMeta propertyMeta, ColumnDesc columnDesc,
            Column column) {
        int sqlType = propertyMeta.getValueType().getSqlType();
        columnDesc.setSqlType(dialect.getSqlType(sqlType));
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

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

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.DataType;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.util.AnnotationUtil;
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
        this.dialect = dialect;
    }

    public ColumnDesc getColumnDesc(PropertyMeta propertyMeta) {
        if (propertyMeta.isTransient() || propertyMeta.isRelationship()) {
            return null;
        }
        Column column = getColumn(propertyMeta);
        ColumnDesc columnDesc = new ColumnDesc();
        doName(propertyMeta, columnDesc, column);
        doDefinition(propertyMeta, columnDesc, column);
        doNullable(propertyMeta, columnDesc, column);
        doUnique(propertyMeta, columnDesc, column);
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
        String definition = null;
        if (!StringUtil.isEmpty(column.columnDefinition())) {
            definition = column.columnDefinition();
        } else {
            int sqlType = propertyMeta.getValueType().getSqlType();
            DataType dataType = dialect.getDataType(sqlType);
            definition = dataType.getDefinition(column.length(), column
                    .precision(), column.scale());
        }
        columnDesc.setDefinition(definition);
    }

    /**
     * {@code null}可能性を処理します。
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
        if (column != AnnotationUtil.getDefaultColumn()) {
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

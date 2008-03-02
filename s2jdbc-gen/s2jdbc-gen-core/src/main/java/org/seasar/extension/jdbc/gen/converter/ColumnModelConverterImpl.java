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
package org.seasar.extension.jdbc.gen.converter;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.ColumnModelConverter;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class ColumnModelConverterImpl implements ColumnModelConverter {

    @Column
    protected static final Column DEFAULT_COLUMN = ReflectionUtil
            .getDeclaredField(ColumnModelConverterImpl.class, "DEFAULT_COLUMN")
            .getAnnotation(Column.class);

    protected GenDialect dialect;

    public ColumnModelConverterImpl(GenDialect dialect) {
        this.dialect = dialect;
    }

    public ColumnModel convert(PropertyMeta propertyMeta) {
        if (propertyMeta.isTransient()) {
            return null;
        }
        if (propertyMeta.isRelationship()) {
            return null;
        }
        ColumnModel columnModel = new ColumnModel();
        doName(propertyMeta, columnModel);
        doSqlType(propertyMeta, columnModel);
        Column column = getColumn(propertyMeta);
        doDefinition(propertyMeta, columnModel, column);
        doNullable(propertyMeta, columnModel, column);
        doUnique(propertyMeta, columnModel, column);
        return columnModel;
    }

    protected void doName(PropertyMeta propertyMeta, ColumnModel columnModel) {
        ColumnMeta columnMeta = propertyMeta.getColumnMeta();
        columnModel.setName(columnMeta.getName());
    }

    protected void doSqlType(PropertyMeta propertyMeta, ColumnModel columnModel) {
        ValueType valueType = propertyMeta.getValueType();
        columnModel.setSqlType(dialect.getSqlType(valueType.getSqlType()));
    }

    protected void doDefinition(PropertyMeta propertyMeta,
            ColumnModel columnModel, Column column) {
        String definition = null;
        if (!StringUtil.isEmpty(column.columnDefinition())) {
            definition = column.columnDefinition();
        } else {
            SqlType sqlType = columnModel.getSqlType();
            definition = sqlType.toText(column.length(), column.precision(),
                    column.scale());
        }
        columnModel.setDefinition(definition);
    }

    protected void doNullable(PropertyMeta propertyMeta,
            ColumnModel columnModel, Column column) {
        if (column == DEFAULT_COLUMN) {
            boolean primitive = propertyMeta.getField().getType().isPrimitive();
            columnModel.setNullable(!primitive);
        }
    }

    protected void doUnique(PropertyMeta propertyMeta, ColumnModel columnModel,
            Column column) {
        columnModel.setUnique(column.unique());
    }

    protected Column getColumn(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        return column != null ? column : DEFAULT_COLUMN;
    }

}

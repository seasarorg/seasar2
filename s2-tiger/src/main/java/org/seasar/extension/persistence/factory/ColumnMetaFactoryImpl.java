/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence.factory;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.persistence.ColumnMeta;
import org.seasar.extension.persistence.ColumnMetaFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class ColumnMetaFactoryImpl implements ColumnMetaFactory {

    private PersistenceConvention persistenceConvention;

    /**
     * @return Returns the persistenceConvention.
     */
    public PersistenceConvention getPersistenceConvention() {
        return persistenceConvention;
    }

    /**
     * @param persistenceConvention
     *            The persistenceConvention to set.
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }

    public ColumnMeta createColumnMeta(Field field) {
        ColumnMeta columnMeta = new ColumnMeta();
        String defaultName = persistenceConvention
                .fromPropertyNameToColumnName(field.getName());
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            String name = column.name();
            if (StringUtil.isEmpty(name)) {
                name = defaultName;
            }
            columnMeta.setName(name);
            columnMeta.setUnique(column.unique());
            columnMeta.setNullable(column.nullable());
            columnMeta.setInsertable(column.insertable());
            columnMeta.setUpdatable(column.updatable());
            String columnDefinition = column.columnDefinition();
            if (!StringUtil.isEmpty(columnDefinition)) {
                columnMeta.setColumnDefinition(columnDefinition);
            }
            String table = column.table();
            if (!StringUtil.isEmpty(table)) {
                columnMeta.setTable(table);
            }
            columnMeta.setLength(column.length());
            columnMeta.setPrecision(column.precision());
            columnMeta.setScale(column.scale());
        } else {
            columnMeta.setName(defaultName);
        }
        return columnMeta;
    }
}

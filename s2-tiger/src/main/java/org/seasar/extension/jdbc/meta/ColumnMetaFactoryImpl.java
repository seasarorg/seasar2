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
package org.seasar.extension.jdbc.meta;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.ColumnMetaFactory;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.StringUtil;

/**
 * {@link ColumnMetaFactory}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class ColumnMetaFactoryImpl implements ColumnMetaFactory {

    private PersistenceConvention persistenceConvention;

    public ColumnMeta createColumnMeta(Field field, EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        ColumnMeta columnMeta = new ColumnMeta();
        String defaultName = persistenceConvention
                .fromPropertyNameToColumnName(propertyMeta.getName());
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            String name = column.name();
            if (StringUtil.isEmpty(name)) {
                name = defaultName;
            }
            columnMeta.setName(name);
            columnMeta.setInsertable(column.insertable());
            columnMeta.setUpdatable(column.updatable());
        } else {
            columnMeta.setName(defaultName);
        }
        return columnMeta;
    }

    /**
     * 永続化用の規約を返します。
     * 
     * @return 永続化用の規約
     */
    public PersistenceConvention getPersistenceConvention() {
        return persistenceConvention;
    }

    /**
     * 永続化用の規約を設定します。
     * 
     * @param persistenceConvention
     *            永続化用の規約
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }

}

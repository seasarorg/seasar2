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

import javax.persistence.Table;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.TableMetaFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.StringUtil;

/**
 * {@link TableMetaFactory}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class TableMetaFactoryImpl implements TableMetaFactory {

    private PersistenceConvention persistenceConvention;

    public TableMeta createTableMeta(Class<?> entityClass, EntityMeta entityMeta) {
        TableMeta tableMeta = new TableMeta();
        String defaultName = persistenceConvention
                .fromEntityNameToTableName(entityMeta.getName());
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            String name = table.name();
            if (StringUtil.isEmpty(name)) {
                name = defaultName;
            }
            tableMeta.setName(name);
            String catalog = table.catalog();
            if (!StringUtil.isEmpty(catalog)) {
                tableMeta.setCatalog(catalog);
            }
            String schema = table.schema();
            if (!StringUtil.isEmpty(schema)) {
                tableMeta.setSchema(schema);
            }
        } else {
            tableMeta.setName(defaultName);
        }
        return tableMeta;
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
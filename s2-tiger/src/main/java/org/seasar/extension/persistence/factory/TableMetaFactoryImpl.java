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

import org.seasar.extension.persistence.TableMeta;
import org.seasar.extension.persistence.TableMetaFactory;
import org.seasar.extension.persistence.annotation.Table;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.AssertionUtil;

/**
 * @author higa
 * 
 */
public class TableMetaFactoryImpl implements TableMetaFactory {

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

    public TableMeta createTableMeta(Class<?> entityClass, String entityName) {
        TableMeta tableMeta = new TableMeta();
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            String name = table.name();
            AssertionUtil.assertNotEmpty("tableName", name);
            tableMeta.setName(name);
        } else {
            tableMeta.setName(persistenceConvention
                    .fromEntityNameToTableName(entityName));
        }
        return tableMeta;
    }
}

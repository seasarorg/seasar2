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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.gen.ColumnModelConverter;
import org.seasar.extension.jdbc.gen.ForeignKeyModelConverter;
import org.seasar.extension.jdbc.gen.PrimaryKeyModelConverter;
import org.seasar.extension.jdbc.gen.TableModelConverter;
import org.seasar.extension.jdbc.gen.TableModelFactory;
import org.seasar.extension.jdbc.gen.UniqueKeyModelConverter;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.ForeignKeyModel;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.UniqueKeyModel;

/**
 * @author taedium
 * 
 */
public class TableModelConverterImpl implements TableModelConverter {

    protected TableModelFactory tableModelFactory;

    protected ColumnModelConverter columnModelConverter;

    protected PrimaryKeyModelConverter primaryKeyModelConverter;

    protected ForeignKeyModelConverter foreignKeyModelConverter;

    protected UniqueKeyModelConverter uniqueKeyModelConverter;

    public TableModelConverterImpl(TableModelFactory tableModelFactory,
            ColumnModelConverter columnModelConverter,
            PrimaryKeyModelConverter primaryKeyModelConverter,
            ForeignKeyModelConverter foreignKeyModelConverter,
            UniqueKeyModelConverter uniqueKeyModelConverter) {
        this.tableModelFactory = tableModelFactory;
        this.columnModelConverter = columnModelConverter;
        this.primaryKeyModelConverter = primaryKeyModelConverter;
        this.foreignKeyModelConverter = foreignKeyModelConverter;
        this.uniqueKeyModelConverter = uniqueKeyModelConverter;
    }

    public TableModel convert(EntityMeta entityMeta) {
        TableModel tableModel = getTableModel(entityMeta);
        doSchema(entityMeta, tableModel);
        doName(entityMeta, tableModel);
        doColumnModel(entityMeta, tableModel);
        doPrimaryKeyModel(entityMeta, tableModel);
        doForeignKeyModel(entityMeta, tableModel);
        doUniqueKeyModel(entityMeta, tableModel);
        return tableModel;
    }

    protected TableModel getTableModel(EntityMeta entityMeta) {
        TableMeta tableMeta = entityMeta.getTableMeta();
        return tableModelFactory.getTableModel(tableMeta.getSchema(), tableMeta
                .getName());
    }

    protected void doSchema(EntityMeta entityMeta, TableModel tableModel) {
        if (tableModel.getSchema() != null) {
            return;
        }
        TableMeta tableMeta = entityMeta.getTableMeta();
        tableModel.setSchema(tableMeta.getSchema());
    }

    protected void doName(EntityMeta entityMeta, TableModel tableModel) {
        if (tableModel.getName() != null) {
            return;
        }
        TableMeta tableMeta = entityMeta.getTableMeta();
        tableModel.setName(tableMeta.getName());
    }

    protected void doColumnModel(EntityMeta entityMeta, TableModel tableModel) {
        for (int i = 0; i < entityMeta.getColumnPropertyMetaSize(); i++) {
            PropertyMeta propertyMeta = entityMeta.getColumnPropertyMeta(i);
            ColumnModel columnModel = columnModelConverter
                    .convert(propertyMeta);
            if (columnModel == null) {
                continue;
            }
            if (tableModel.hasColumnModel(columnModel.getName())) {
                continue;
            }
            tableModel.addColumnModel(columnModel);
        }
    }

    protected void doPrimaryKeyModel(EntityMeta entityMeta,
            TableModel tableModel) {
        PrimaryKeyModel primaryKeyModel = primaryKeyModelConverter.convert(
                entityMeta, tableModel);
        if (primaryKeyModel == null) {
            return;
        }
        if (tableModel.hasPrimaryKeyModel()) {
            return;
        }
        tableModel.setPrimaryKeyModel(primaryKeyModel);
    }

    protected void doForeignKeyModel(EntityMeta entityMeta,
            TableModel tableModel) {
        for (int i = 0; i < entityMeta.getPropertyMetaSize(); i++) {
            PropertyMeta propertyMeta = entityMeta.getPropertyMeta(i);
            ForeignKeyModel foreignKeyModel = foreignKeyModelConverter.convert(
                    propertyMeta, tableModel);
            if (foreignKeyModel == null) {
                continue;
            }
            if (tableModel.hasForeignKeyModel(foreignKeyModel.getKey())) {
                continue;
            }
            tableModel.addForeignKeyModel(foreignKeyModel);
        }
    }

    protected void doUniqueKeyModel(EntityMeta entityMeta, TableModel tableModel) {
        for (UniqueKeyModel uniqueKeyModel : uniqueKeyModelConverter.convert(
                entityMeta, tableModel)) {
            if (tableModel.hasUniqueKeyModel(uniqueKeyModel.getKey())) {
                continue;
            }
            tableModel.addUniqueKeyModel(uniqueKeyModel);
        }
    }
}

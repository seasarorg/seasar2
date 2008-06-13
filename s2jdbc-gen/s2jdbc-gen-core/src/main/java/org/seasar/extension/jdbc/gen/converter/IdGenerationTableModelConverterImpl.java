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

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.IdGenerationTableModelConverter;
import org.seasar.extension.jdbc.gen.SqlType;
import org.seasar.extension.jdbc.gen.TableModelFactory;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.Key;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
import org.seasar.extension.jdbc.gen.model.RowModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class IdGenerationTableModelConverterImpl implements
        IdGenerationTableModelConverter {

    protected TableModelFactory tableModelFactory;

    protected GenDialect dialect;

    public IdGenerationTableModelConverterImpl(
            TableModelFactory tableModelFactory, GenDialect dialect) {
        this.tableModelFactory = tableModelFactory;
        this.dialect = dialect;
    }

    public List<TableModel> convert(EntityMeta entityMeta) {
        List<TableModel> results = new ArrayList<TableModel>();
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            GenerationType generationType = getGenerationType(propertyMeta);
            if (generationType != GenerationType.TABLE) {
                continue;
            }
            TableGeneratorMeta generatorMeta = getTableGeneratorMeta(
                    entityMeta, propertyMeta);
            TableModel tableModel = getTableModel(generatorMeta);
            doSchema(tableModel, generatorMeta);
            doName(tableModel, generatorMeta);
            doPrimaryKeyModel(tableModel, generatorMeta);
            doColumnModel(tableModel, generatorMeta);
            doKeyValuePair(tableModel, generatorMeta);
            if (!results.contains(tableModel)) {
                results.add(tableModel);
            }
        }
        return results;
    }

    protected GenerationType getGenerationType(PropertyMeta propertyMeta) {
        DbmsDialect dbmsDialect = null;// dialect.getDbmsDialect();
        GenerationType generationType = propertyMeta.getGenerationType();
        return generationType == GenerationType.AUTO ? dbmsDialect
                .getDefaultGenerationType() : generationType;
    }

    protected TableGeneratorMeta getTableGeneratorMeta(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        TableGenerator generator = propertyMeta.getField().getAnnotation(
                TableGenerator.class);
        return new TableGeneratorMeta(entityMeta, propertyMeta, generator);
    }

    protected TableModel getTableModel(TableGeneratorMeta generatorMeta) {
        return tableModelFactory.getTableModel(generatorMeta.schema,
                generatorMeta.table);
    }

    protected void doSchema(TableModel tableModel,
            TableGeneratorMeta generatorMeta) {
        if (tableModel.getSchema() != null) {
            return;
        }
        tableModel.setSchema(generatorMeta.schema);
    }

    protected void doName(TableModel tableModel,
            TableGeneratorMeta generatorMeta) {
        if (tableModel.getName() != null) {
            return;
        }
        tableModel.setName(generatorMeta.table);
    }

    protected void doPrimaryKeyModel(TableModel tableModel,
            TableGeneratorMeta generatorMeta) {
        if (tableModel.hasPrimaryKeyModel()) {
            return;
        }
        PrimaryKeyModel primaryKeyModel = new PrimaryKeyModel();
        primaryKeyModel.setName(tableModel.getName() + "_PK");
        primaryKeyModel.addColumnName(generatorMeta.pkColumnName);
        tableModel.setPrimaryKeyModel(primaryKeyModel);
    }

    protected void doColumnModel(TableModel tableModel,
            TableGeneratorMeta generatorMeta) {
        if (tableModel.hasColumnModel(generatorMeta.pkColumnName)) {
            return;
        }
        ColumnModel pkColumnModel = new ColumnModel();
        pkColumnModel.setName(generatorMeta.pkColumnName);
        SqlType pkSqlType = dialect.getSqlType(Types.VARCHAR);
        pkColumnModel.setSqlType(pkSqlType);
        pkColumnModel.setDefinition(pkSqlType.toText(255, 0, 0));
        pkColumnModel.setNullable(false);
        pkColumnModel.setUnique(true);
        tableModel.addColumnModel(pkColumnModel);

        ColumnModel valueColumnModel = new ColumnModel();
        valueColumnModel.setName(generatorMeta.valueColumnName);
        SqlType valueSqlType = dialect.getSqlType(Types.BIGINT);
        valueColumnModel.setSqlType(valueSqlType);
        valueColumnModel.setDefinition(valueSqlType.toText(0,
                Integer.MAX_VALUE, 0));
        valueColumnModel.setNullable(false);
        valueColumnModel.setUnique(false);
        tableModel.addColumnModel(valueColumnModel);
    }

    protected void doKeyValuePair(TableModel tableModel,
            TableGeneratorMeta generatorMeta) {
        Key key = new Key(new Object[] { generatorMeta.pkColumnValue });
        if (tableModel.hasRowModel(key)) {
            return;
        }
        RowModel rowModel = new RowModel(tableModel.getPrimaryKeyModel());
        rowModel.addValue(generatorMeta.pkColumnValue);
        rowModel.addValue(generatorMeta.initialValue);
        tableModel.addRowModel(rowModel);
    }

    protected String getSchemaName(TableGenerator generator) {
        if (StringUtil.isEmpty(generator.schema())) {
            return null;
        }
        return generator.schema();
    }

    @TableGenerator(name = "default")
    public static class TableGeneratorMeta {

        protected static final TableGenerator DEFAULT_TABLE_GENERATOR = TableGeneratorMeta.class
                .getAnnotation(TableGenerator.class);

        protected static final String DEFAULT_TABLE = TableIdGenerator.DEFAULT_TABLE;

        protected static final String DEFAULT_PK_COLUMN_NAME = TableIdGenerator.DEFAULT_PK_COLUMN_NAME;

        protected static final String DEFAULT_VALUE_COLUMN_NAME = TableIdGenerator.DEFAULT_VALUE_COLUMN_NAME;

        protected EntityMeta entityMeta;

        protected PropertyMeta propertyMeta;

        protected TableGenerator generator;

        protected String table;

        protected String schema;

        protected String pkColumnName;

        protected String valueColumnName;

        protected String pkColumnValue;

        protected int initialValue;

        public TableGeneratorMeta(EntityMeta entityMeta,
                PropertyMeta propertyMeta, TableGenerator generator) {
            this.entityMeta = entityMeta;
            this.propertyMeta = propertyMeta;
            this.generator = generator != null ? generator
                    : DEFAULT_TABLE_GENERATOR;
            table = getTable();
            schema = getSchema();
            pkColumnName = getPkColumnName();
            valueColumnName = getValueColumnName();
            pkColumnValue = getPkColumnValue();
            initialValue = getInitialValue();
        }

        protected String getTable() {
            if (!StringUtil.isEmpty(generator.table())) {
                return generator.table();
            }
            return DEFAULT_TABLE;
        }

        protected String getSchema() {
            if (!StringUtil.isEmpty(generator.schema())) {
                return generator.schema();
            }
            return null;
        }

        protected String getPkColumnName() {
            if (!StringUtil.isEmpty(generator.pkColumnName())) {
                return generator.pkColumnName();
            }
            return DEFAULT_PK_COLUMN_NAME;
        }

        protected String getValueColumnName() {
            if (!StringUtil.isEmpty(generator.valueColumnName())) {
                return generator.valueColumnName();
            }
            return DEFAULT_VALUE_COLUMN_NAME;
        }

        protected String getPkColumnValue() {
            if (!StringUtil.isEmpty(generator.pkColumnValue())) {
                return generator.pkColumnValue();
            }
            return entityMeta.getTableMeta().getName() + "_"
                    + propertyMeta.getColumnMeta().getName();
        }

        protected int getInitialValue() {
            return generator.initialValue();
        }
    }
}

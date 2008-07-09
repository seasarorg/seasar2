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

import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.util.AnnotationUtil;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class IdTableDescFactoryImpl implements IdTableDescFactory {

    protected GenDialect dialect;

    protected EntityMetaFactory entityMetaFactory;

    protected PrimaryKeyDescFactory primaryKeyDescFactory;

    protected ColumnDescFactory columnDescFactory;

    protected UniqueKeyDescFactory uniqueKeyDescFactory;

    /**
     * @param dialect
     * @param primaryKeyDescFactory
     * @param columnDescFactory
     * @param uniqueKeyDescFactory
     */
    public IdTableDescFactoryImpl(GenDialect dialect,
            ColumnDescFactory columnDescFactory,
            PrimaryKeyDescFactory primaryKeyDescFactory,
            UniqueKeyDescFactory uniqueKeyDescFactory) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (columnDescFactory == null) {
            throw new NullPointerException("columnDescFactory");
        }
        if (primaryKeyDescFactory == null) {
            throw new NullPointerException("primaryKeyDescFactory");
        }
        if (uniqueKeyDescFactory == null) {
            throw new NullPointerException("uniqueKeyDescFactory");
        }
        this.dialect = dialect;
        this.primaryKeyDescFactory = primaryKeyDescFactory;
        this.columnDescFactory = columnDescFactory;
        this.uniqueKeyDescFactory = uniqueKeyDescFactory;
    }

    public TableDesc getTableDesc(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.TABLE) {
            TableDesc tableDesc = new TableDesc();
            TableGenerator generator = getTableGenerator(propertyMeta);
            doName(entityMeta, tableDesc, generator);
            doPrimaryKeyDesc(entityMeta, tableDesc, generator);
            doValueColumnDesc(entityMeta, tableDesc, generator);
            doUniqueKeyDesc(entityMeta, tableDesc, generator);
            return tableDesc;
        }
        return null;
    }

    protected void doName(EntityMeta entityMeta, TableDesc tableDesc,
            TableGenerator generator) {
        String catalogName = generator.catalog();
        if (StringUtil.isEmpty(catalogName)) {
            catalogName = entityMeta.getTableMeta().getCatalog();
        }
        String schemaName = generator.schema();
        if (StringUtil.isEmpty(schemaName)) {
            schemaName = entityMeta.getTableMeta().getSchema();
        }
        String tableName = generator.table();
        if (StringUtil.isEmpty(tableName)) {
            tableName = TableIdGenerator.DEFAULT_TABLE;
        }

        tableDesc.setCatalogName(catalogName);
        tableDesc.setSchemaName(schemaName);
        tableDesc.setName(tableName);
    }

    protected void doPrimaryKeyDesc(EntityMeta entityMeta, TableDesc tableDesc,
            TableGenerator generator) {
        String pkColumnName = generator.pkColumnName();
        if (StringUtil.isEmpty(pkColumnName)) {
            pkColumnName = TableIdGenerator.DEFAULT_PK_COLUMN_NAME;
        }
        PrimaryKeyDesc primaryKeyDesc = primaryKeyDescFactory
                .getPrimaryKeyDesc(pkColumnName);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        ColumnDesc columnDesc = columnDescFactory.getColumnDesc(pkColumnName);
        tableDesc.addColumnDesc(columnDesc);
    }

    protected void doValueColumnDesc(EntityMeta entityMeta,
            TableDesc tableDesc, TableGenerator generator) {
        String valueColumnName = generator.valueColumnName();
        if (StringUtil.isEmpty(valueColumnName)) {
            valueColumnName = TableIdGenerator.DEFAULT_VALUE_COLUMN_NAME;
        }
        ColumnDesc columnDesc = columnDescFactory
                .getColumnDesc(valueColumnName);
        tableDesc.addColumnDesc(columnDesc);
    }

    protected void doUniqueKeyDesc(EntityMeta entityMeta, TableDesc tableDesc,
            TableGenerator generator) {
        for (UniqueConstraint uc : generator.uniqueConstraints()) {
            UniqueKeyDesc uniqueKeyDesc = uniqueKeyDescFactory
                    .getCompositeUniqueKeyDesc(uc);
            if (uniqueKeyDesc != null) {
                tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            }
        }
    }

    protected TableGenerator getTableGenerator(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        TableGenerator tableGenerator = field
                .getAnnotation(TableGenerator.class);
        return tableGenerator != null ? tableGenerator : AnnotationUtil
                .getDefaultTableGenerator();
    }
}

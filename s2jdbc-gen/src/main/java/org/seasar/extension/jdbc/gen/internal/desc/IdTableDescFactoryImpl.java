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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.lang.reflect.Field;
import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.internal.util.TableUtil;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.framework.util.StringUtil;

/**
 * {@link IdTableDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class IdTableDescFactoryImpl implements IdTableDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** 一意キー記述のファクトリ */
    protected UniqueKeyDescFactory uniqueKeyDescFactory;

    /**
     * @param dialect
     *            方言
     * @param uniqueKeyDescFactory
     *            一意キー記述のファクトリ
     */
    public IdTableDescFactoryImpl(GenDialect dialect,
            UniqueKeyDescFactory uniqueKeyDescFactory) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (uniqueKeyDescFactory == null) {
            throw new NullPointerException("uniqueKeyDescFactory");
        }
        this.dialect = dialect;
        this.uniqueKeyDescFactory = uniqueKeyDescFactory;
    }

    public TableDesc getTableDesc(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.TABLE) {
            TableGenerator generator = getTableGenerator(entityMeta,
                    propertyMeta);
            TableDesc tableDesc = new TableDesc();
            doName(entityMeta, tableDesc, generator);
            doPrimaryKeyColumn(entityMeta, tableDesc, generator);
            doValueColumn(entityMeta, tableDesc, generator);
            doUniqueConstraints(entityMeta, tableDesc, generator);
            return tableDesc;
        }
        return null;
    }

    /**
     * 名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doName(EntityMeta entityMeta, TableDesc tableDesc,
            TableGenerator generator) {
        String catalog = generator.catalog();
        if (StringUtil.isEmpty(catalog)) {
            catalog = entityMeta.getTableMeta().getCatalog();
        }
        String schema = generator.schema();
        if (StringUtil.isEmpty(schema)) {
            schema = entityMeta.getTableMeta().getSchema();
        }
        String name = generator.table();
        if (StringUtil.isEmpty(name)) {
            name = TableIdGenerator.DEFAULT_TABLE;
        }

        tableDesc.setCatalogName(catalog);
        tableDesc.setSchemaName(schema);
        tableDesc.setName(name);
        tableDesc.setCanonicalName(buildCanonicalName(catalog, schema, name));
    }

    /**
     * 標準名を組み立てます。
     * 
     * @param catalog
     *            カタログ名
     * @param schema
     *            スキーマ名
     * @param name
     *            テーブル名
     * @return 完全な名前
     */
    protected String buildCanonicalName(String catalog, String schema,
            String name) {
        return TableUtil
                .buildCanonicalTableName(dialect, catalog, schema, name);
    }

    /**
     * 主キー記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doPrimaryKeyColumn(EntityMeta entityMeta,
            TableDesc tableDesc, TableGenerator generator) {
        String pkColumnName = generator.pkColumnName();
        if (StringUtil.isEmpty(pkColumnName)) {
            pkColumnName = TableIdGenerator.DEFAULT_PK_COLUMN_NAME;
        }
        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName(pkColumnName);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        ColumnDesc columnDesc = new ColumnDesc();
        columnDesc.setName(pkColumnName);
        SqlType sqlType = dialect.getSqlType(Types.VARCHAR);
        columnDesc.setSqlType(sqlType);
        Column column = AnnotationUtil.getDefaultColumn();
        columnDesc.setDefinition(sqlType.getDataType(column.length(), 0, 0,
                false));
        tableDesc.addColumnDesc(columnDesc);
    }

    /**
     * カラム記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMetaList
     *            プロパティメタデータのリスト
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doValueColumn(EntityMeta entityMeta, TableDesc tableDesc,
            TableGenerator generator) {
        String valueColumnName = generator.valueColumnName();
        if (StringUtil.isEmpty(valueColumnName)) {
            valueColumnName = TableIdGenerator.DEFAULT_VALUE_COLUMN_NAME;
        }
        ColumnDesc columnDesc = new ColumnDesc();
        columnDesc.setName(valueColumnName);
        SqlType sqlType = dialect.getSqlType(Types.BIGINT);
        columnDesc.setSqlType(sqlType);
        Column column = AnnotationUtil.getDefaultColumn();
        columnDesc.setDefinition(sqlType.getDataType(0, column.precision(), 0,
                false));
        columnDesc.setNullable(false);
        tableDesc.addColumnDesc(columnDesc);
    }

    /**
     * 一意キー記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doUniqueConstraints(EntityMeta entityMeta,
            TableDesc tableDesc, TableGenerator generator) {
        for (UniqueConstraint uc : generator.uniqueConstraints()) {
            UniqueKeyDesc uniqueKeyDesc = uniqueKeyDescFactory
                    .getCompositeUniqueKeyDesc(uc);
            if (uniqueKeyDesc != null) {
                tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            }
        }
    }

    /**
     * テーブルジェネレータを返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @return テーブルジェネレータ
     */
    protected TableGenerator getTableGenerator(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        GeneratedValue generatedValue = field
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            throw new IllegalStateException("@GeneratedValue not found.");
        }
        String name = generatedValue.generator();
        if (StringUtil.isEmpty(name)) {
            return AnnotationUtil.getDefaultTableGenerator();
        }
        TableGenerator tableGenerator = field
                .getAnnotation(TableGenerator.class);
        if (tableGenerator != null && name.equals(tableGenerator.name())) {
            return tableGenerator;
        }
        tableGenerator = entityMeta.getEntityClass().getAnnotation(
                TableGenerator.class);
        if (tableGenerator != null && name.equals(tableGenerator.name())) {
            return tableGenerator;
        }
        throw new IllegalStateException("@TableGenerator not found.");

    }

}

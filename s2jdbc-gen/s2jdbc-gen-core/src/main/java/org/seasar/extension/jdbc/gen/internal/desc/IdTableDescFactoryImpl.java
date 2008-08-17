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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link IdTableDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class IdTableDescFactoryImpl implements IdTableDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** 主キー記述のファクトリ */
    protected PrimaryKeyDescFactory primaryKeyDescFactory;

    /** カラム記述のファクトリ */
    protected ColumnDescFactory columnDescFactory;

    /** 一意キー記述のファクトリ */
    protected UniqueKeyDescFactory uniqueKeyDescFactory;

    /**
     * @param dialect
     *            方言
     * @param primaryKeyDescFactory
     *            主キー記述のファクトリ
     * @param columnDescFactory
     *            カラム記述のファクトリ
     * @param uniqueKeyDescFactory
     *            一意キー記述のファクトリ
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
            PropertyMeta pkPropertyMeta = getPkPropertyMeta(generator);
            PropertyMeta valuePropertyMeta = getValuePropertyMeta(generator);
            doName(entityMeta, tableDesc, generator);
            doPrimaryKeyDesc(pkPropertyMeta, tableDesc, generator);
            doColumnDesc(Arrays.asList(pkPropertyMeta, valuePropertyMeta),
                    tableDesc, generator);
            doUniqueKeyDesc(entityMeta, tableDesc, generator);
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

        tableDesc.setCatalogName(dialect.unquote(catalogName));
        tableDesc.setSchemaName(dialect.unquote(schemaName));
        tableDesc.setName(dialect.unquote(tableName));
    }

    /**
     * 主キー記述を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doPrimaryKeyDesc(PropertyMeta propertyMeta,
            TableDesc tableDesc, TableGenerator generator) {
        PrimaryKeyDesc primaryKeyDesc = primaryKeyDescFactory
                .getPrimaryKeyDesc(Arrays.asList(propertyMeta));
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
    }

    /**
     * カラム記述を処理します。
     * 
     * @param propertyMetaList
     *            プロパティメタデータのリスト
     * @param tableDesc
     *            テーブル記述
     * @param generator
     *            テーブルジェネレータ
     */
    protected void doColumnDesc(List<PropertyMeta> propertyMetaList,
            TableDesc tableDesc, TableGenerator generator) {
        for (PropertyMeta propertyMeta : propertyMetaList) {
            ColumnDesc columnDesc = columnDescFactory
                    .getColumnDesc(propertyMeta);
            tableDesc.addColumnDesc(columnDesc);
        }
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

    /**
     * テーブルジェネレータを返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return テーブルジェネレータ
     */
    protected TableGenerator getTableGenerator(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        TableGenerator tableGenerator = field
                .getAnnotation(TableGenerator.class);
        return tableGenerator != null ? tableGenerator : AnnotationUtil
                .getDefaultTableGenerator();
    }

    /**
     * テーブルジェネレータの主キーのカラムに対応するプロパティメタデータを返します。
     * 
     * @param generator
     *            テーブルジェネレータ
     * @return 主キーのカラムに対応するプロパティメタデータ
     */
    protected PropertyMeta getPkPropertyMeta(TableGenerator generator) {
        String pkColumnName = generator.pkColumnName();
        if (StringUtil.isEmpty(pkColumnName)) {
            pkColumnName = TableIdGenerator.DEFAULT_PK_COLUMN_NAME;
        }
        return createAdaptivePropertyMeta(pkColumnName);
    }

    /**
     * テーブルジェネレータの値カラムに対応するプロパティメタデータを返します。
     * 
     * @param generator
     *            テーブルジェネレータ
     * @return 値カラムに対応するプロパティメタデータ
     */
    protected PropertyMeta getValuePropertyMeta(TableGenerator generator) {
        String valueColumnName = generator.valueColumnName();
        if (StringUtil.isEmpty(valueColumnName)) {
            valueColumnName = TableIdGenerator.DEFAULT_VALUE_COLUMN_NAME;
        }
        return createAdaptivePropertyMeta(valueColumnName);
    }

    /**
     * アダプタとなるプロパティメタデータを作成します。
     * 
     * @param columnName
     *            カラム名
     * @return アダプタとなるプロパティメタデータ
     */
    protected PropertyMeta createAdaptivePropertyMeta(String columnName) {
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName(columnName);
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setColumnMeta(columnMeta);
        class Dummy {

            @SuppressWarnings("unused")
            public String field;
        }
        propertyMeta.setField(ReflectionUtil.getField(Dummy.class, "field"));
        propertyMeta.setValueType(ValueTypes.STRING);
        return propertyMeta;
    }
}

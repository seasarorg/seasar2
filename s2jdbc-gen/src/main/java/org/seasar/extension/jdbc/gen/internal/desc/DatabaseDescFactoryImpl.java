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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.TableDescFactory;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;

/**
 * {@link DatabaseDescFactory}の実装です。
 * 
 * @author taedium
 */
public class DatabaseDescFactoryImpl implements DatabaseDescFactory {

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /** 関連を外部キーとみなす場合{@code true}、みなさない場合{@code false} */
    protected boolean regardRelationshipAsFk;

    /** テーブル記述のファクトリ */
    protected TableDescFactory tableDescFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     * @param entityMetaReader
     *            エンティティメタデータのリーダ
     * @param dialect
     *            方言
     * @param valueTypeProvider
     *            {@link ValueType}の提供者
     * @param regardRelationshipAsFk
     *            関連を外部キーとみなす場合{@code true}、みなさない場合{@code false}
     */
    public DatabaseDescFactoryImpl(EntityMetaFactory entityMetaFactory,
            EntityMetaReader entityMetaReader, GenDialect dialect,
            ValueTypeProvider valueTypeProvider, boolean regardRelationshipAsFk) {
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        if (entityMetaReader == null) {
            throw new NullPointerException("entityMetaReader");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (valueTypeProvider == null) {
            throw new NullPointerException("valueTypeResolver");
        }
        this.entityMetaFactory = entityMetaFactory;
        this.entityMetaReader = entityMetaReader;
        this.dialect = dialect;
        this.valueTypeProvider = valueTypeProvider;
        this.regardRelationshipAsFk = regardRelationshipAsFk;
        this.tableDescFactory = createTableDescFactory();
    }

    public DatabaseDesc getDatabaseDesc() {
        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.setFiltered(entityMetaReader.isFiltered());
        for (EntityMeta entityMeta : entityMetaReader.read()) {
            TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
            databaseDesc.addTableDesc(tableDesc);
            for (TableDesc idTableDesc : tableDesc.getIdTableDescList()) {
                databaseDesc.addTableDesc(idTableDesc);
            }
        }
        return databaseDesc;
    }

    /**
     * テーブル記述のファクトリを作成します。
     * 
     * @return テーブル記述のファクトリ
     */
    protected TableDescFactory createTableDescFactory() {
        ColumnDescFactory colFactory = createColumnDescFactory();
        PrimaryKeyDescFactory pkFactory = createPrimaryKeyDescFactory();
        UniqueKeyDescFactory ukFactory = createUniqueKeyDescFactory();
        ForeignKeyDescFactory fkFactory = createForeignKeyDescFactory();
        SequenceDescFactory seqFactory = createSequenceDescFactory();
        IdTableDescFactory idTabFactory = createIdTableDescFactory(ukFactory);
        return new TableDescFactoryImpl(dialect, colFactory, pkFactory,
                ukFactory, fkFactory, seqFactory, idTabFactory);
    }

    /**
     * {@link ColumnDescFactory}の実装を返します。
     * 
     * @return {@link ColumnDescFactory}の実装
     */
    protected ColumnDescFactory createColumnDescFactory() {
        return new ColumnDescFactoryImpl(dialect, valueTypeProvider);
    }

    /**
     * {@link PrimaryKeyDescFactory}の実装を返します。
     * 
     * @return {@link PrimaryKeyDescFactory}の実装
     */
    protected PrimaryKeyDescFactory createPrimaryKeyDescFactory() {
        return new PrimaryKeyDescFactoryImpl(dialect);
    }

    /**
     * {@link UniqueKeyDescFactory}の実装を返します。
     * 
     * @return {@link UniqueKeyDescFactory}の実装
     */
    protected UniqueKeyDescFactory createUniqueKeyDescFactory() {
        return new UniqueKeyDescFactoryImpl(dialect);
    }

    /**
     * {@link ForeignKeyDescFactoryImpl}の実装を返します。
     * 
     * @return {@link ForeignKeyDescFactoryImpl}の実装
     */
    protected ForeignKeyDescFactory createForeignKeyDescFactory() {
        return new ForeignKeyDescFactoryImpl(dialect, entityMetaFactory,
                regardRelationshipAsFk);
    }

    /**
     * {@link SequenceDescFactory}の実装を返します。
     * 
     * @return {@link SequenceDescFactory}の実装
     */
    protected SequenceDescFactory createSequenceDescFactory() {
        return new SequenceDescFactoryImpl(dialect, valueTypeProvider);
    }

    /**
     * {@link IdTableDescFactory}の実装を返します。
     * 
     * @param ukFactory
     *            一意キー記述のファクトリ
     * @return {@link IdTableDescFactory}の実装
     */
    protected IdTableDescFactory createIdTableDescFactory(
            UniqueKeyDescFactory ukFactory) {
        return new IdTableDescFactoryImpl(dialect, ukFactory);
    }

}

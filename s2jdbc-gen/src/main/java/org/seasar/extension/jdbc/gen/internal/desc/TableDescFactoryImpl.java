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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.TableDescFactory;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.internal.util.EntityMetaUtil;
import org.seasar.extension.jdbc.gen.internal.util.TableUtil;

/**
 * {@link TableDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class TableDescFactoryImpl implements TableDescFactory {

    /** テーブルの完全修飾名をキー、テーブル記述を値とするマップ */
    protected ConcurrentMap<String, TableDesc> tableDescMap = new ConcurrentHashMap<String, TableDesc>(
            200);

    /** 方言 */
    protected GenDialect dialect;

    /** カラム記述のファクトリ */
    protected ColumnDescFactory columnDescFactory;

    /** 主キー記述のファクトリ */
    protected PrimaryKeyDescFactory primaryKeyDescFactory;

    /** 外部キー記述のファクトリ */
    protected ForeignKeyDescFactory foreignKeyDescFactory;

    /** 一意キー記述のファクトリ */
    protected UniqueKeyDescFactory uniqueKeyDescFactory;

    /** シーケンス記述のファクトリ */
    protected SequenceDescFactory sequenceDescFactory;

    /** 識別子生成用のテーブル記述のファクトリ */
    protected IdTableDescFactory idTableDescFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param columnDescFactory
     *            カラム記述のファクトリ
     * @param primaryKeyDescFactory
     *            主キー記述のファクトリ
     * @param uniqueKeyDescFactory
     *            一意キー記述のファクトリ
     * @param foreignKeyDescFactory
     *            外部キー記述のファクトリ
     * @param sequenceDescFactory
     *            シーケンス記述のファクトリ
     * @param idTableDescFactory
     *            識別子生成用のテーブル記述のファクトリ
     */
    public TableDescFactoryImpl(GenDialect dialect,
            ColumnDescFactory columnDescFactory,
            PrimaryKeyDescFactory primaryKeyDescFactory,
            UniqueKeyDescFactory uniqueKeyDescFactory,
            ForeignKeyDescFactory foreignKeyDescFactory,
            SequenceDescFactory sequenceDescFactory,
            IdTableDescFactory idTableDescFactory) {
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
        if (foreignKeyDescFactory == null) {
            throw new NullPointerException("foreignKeyDescFactory");
        }
        if (sequenceDescFactory == null) {
            throw new NullPointerException("sequenceDescFactory");
        }
        if (idTableDescFactory == null) {
            throw new NullPointerException("idTableDescFactory");
        }
        this.dialect = dialect;
        this.columnDescFactory = columnDescFactory;
        this.primaryKeyDescFactory = primaryKeyDescFactory;
        this.uniqueKeyDescFactory = uniqueKeyDescFactory;
        this.foreignKeyDescFactory = foreignKeyDescFactory;
        this.sequenceDescFactory = sequenceDescFactory;
        this.idTableDescFactory = idTableDescFactory;
    }

    public TableDesc getTableDesc(EntityMeta entityMeta) {
        String fullName = entityMeta.getTableMeta().getFullName().toLowerCase();
        TableDesc tableDesc = tableDescMap.get(fullName);
        if (tableDesc != null) {
            return tableDesc;
        }
        tableDesc = createTableDesc(entityMeta);
        tableDescMap.put(fullName, tableDesc);
        return tableDesc;
    }

    /**
     * テーブル記述を作成します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @return テーブル記述
     */
    protected TableDesc createTableDesc(EntityMeta entityMeta) {
        Table table = getTable(entityMeta);
        TableDesc tableDesc = new TableDesc();
        doName(entityMeta, tableDesc, table);
        doComment(entityMeta, tableDesc, table);
        doPrimaryKeyDesc(entityMeta, tableDesc, table);
        doColumnDesc(entityMeta, tableDesc, table);
        doForeignKeyDesc(entityMeta, tableDesc, table);
        doUniqueKeyDesc(entityMeta, tableDesc, table);
        doSequenceDesc(entityMeta, tableDesc, table);
        doIdTableDesc(entityMeta, tableDesc, table);
        return tableDesc;
    }

    /**
     * 名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doName(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        TableMeta tableMeta = entityMeta.getTableMeta();
        tableDesc.setCatalogName(tableMeta.getCatalog());
        tableDesc.setSchemaName(tableMeta.getSchema());
        tableDesc.setName(tableMeta.getName());
        tableDesc.setCanonicalName(buildCanonicalName(tableMeta));
    }

    /**
     * コメントを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doComment(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        String comment = EntityMetaUtil.getComment(entityMeta);
        tableDesc.setComment(comment);
    }

    /**
     * 標準名を組み立てます。
     * 
     * @param tableMeta
     *            テーブルメタデータ
     * @return 標準名
     */
    protected String buildCanonicalName(TableMeta tableMeta) {
        return TableUtil.buildCanonicalTableName(dialect,
                tableMeta.getCatalog(), tableMeta.getSchema(),
                tableMeta.getName());
    }

    /**
     * カラム記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doColumnDesc(EntityMeta entityMeta,
            final TableDesc tableDesc, Table table) {
        List<ColumnDescRef> columnDescRefList = new ArrayList<ColumnDescRef>();
        for (int i = 0; i < entityMeta.getColumnPropertyMetaSize(); i++) {
            PropertyMeta propertyMeta = entityMeta.getColumnPropertyMeta(i);
            ColumnDesc columnDesc = columnDescFactory.getColumnDesc(entityMeta,
                    propertyMeta);
            if (columnDesc != null) {
                columnDescRefList.add(new ColumnDescRef(i, columnDesc));
            }
        }
        Collections.sort(columnDescRefList,
                createColumnDescRefComparator(tableDesc));
        for (ColumnDescRef columnDescRef : columnDescRefList) {
            tableDesc.addColumnDesc(columnDescRef.columnDesc);
        }
    }

    /**
     * 主キー記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doPrimaryKeyDesc(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        PrimaryKeyDesc primaryKeyDesc = primaryKeyDescFactory
                .getPrimaryKeyDesc(entityMeta);
        if (primaryKeyDesc != null) {
            tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
        }
    }

    /**
     * 外部キー記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doForeignKeyDesc(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        for (int i = 0; i < entityMeta.getPropertyMetaSize(); i++) {
            PropertyMeta propertyMeta = entityMeta.getPropertyMeta(i);
            ForeignKeyDesc foreignKeyDesc = foreignKeyDescFactory
                    .getForeignKeyDesc(entityMeta, propertyMeta);
            if (foreignKeyDesc != null) {
                tableDesc.addForeignKeyDesc(foreignKeyDesc);
            }
        }
    }

    /**
     * 一意キー記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doUniqueKeyDesc(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        String singlePkColumnName = null;
        PrimaryKeyDesc primaryKeyDesc = tableDesc.getPrimaryKeyDesc();
        if (primaryKeyDesc != null
                && primaryKeyDesc.getColumnNameList().size() == 1) {
            singlePkColumnName = primaryKeyDesc.getColumnNameList().get(0);
        }
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            if (columnDesc.getName().equals(singlePkColumnName)) {
                continue;
            }
            UniqueKeyDesc uniqueKeyDesc = uniqueKeyDescFactory
                    .getSingleUniqueKeyDesc(columnDesc);
            if (uniqueKeyDesc != null) {
                tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            }
        }
        for (UniqueConstraint uc : table.uniqueConstraints()) {
            UniqueKeyDesc uniqueKeyDesc = uniqueKeyDescFactory
                    .getCompositeUniqueKeyDesc(uc);
            if (uniqueKeyDesc != null) {
                tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            }
        }
    }

    /**
     * シーケンス記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doSequenceDesc(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            SequenceDesc sequenceDesc = sequenceDescFactory.getSequenceDesc(
                    entityMeta, propertyMeta);
            if (sequenceDesc != null) {
                tableDesc.addSequenceDesc(sequenceDesc);
            }
        }
    }

    /**
     * 識別子生成用のテーブル記述を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param tableDesc
     *            テーブル記述
     * @param table
     *            テーブル
     */
    protected void doIdTableDesc(EntityMeta entityMeta, TableDesc tableDesc,
            Table table) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            TableDesc idTableDesc = idTableDescFactory.getTableDesc(entityMeta,
                    propertyMeta);
            if (idTableDesc == null) {
                continue;
            }
            tableDesc.addIdTableDesc(idTableDesc);

            String fullName = idTableDesc.getFullName().toLowerCase();
            TableDesc cache = tableDescMap.get(fullName);
            if (cache == null) {
                tableDescMap.put(fullName, idTableDesc);
            } else {
                cache.setCatalogName(idTableDesc.getCatalogName());
                cache.setSchemaName(idTableDesc.getSchemaName());
                cache.setName(idTableDesc.getName());
                cache.setPrimaryKeyDesc(idTableDesc.getPrimaryKeyDesc());
                for (ColumnDesc columnDesc : idTableDesc.getColumnDescList()) {
                    cache.addColumnDesc(columnDesc);
                }
                for (UniqueKeyDesc uniqueKeyDesc : idTableDesc
                        .getUniqueKeyDescList()) {
                    cache.addUniqueKeyDesc(uniqueKeyDesc);
                }
            }
        }
    }

    /**
     * テーブルを取得します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @return テーブル
     */
    protected Table getTable(EntityMeta entityMeta) {
        Class<?> clazz = entityMeta.getEntityClass();
        Table table = clazz.getAnnotation(Table.class);
        return table != null ? table : AnnotationUtil.getDefaultTable();
    }

    /**
     * カラム記述の{@link Comparator}を作成します。
     * 
     * @param tableDesc
     *            テーブル記述
     * @return カラム記述の{@link Comparator}
     */
    protected Comparator<ColumnDescRef> createColumnDescRefComparator(
            TableDesc tableDesc) {
        final List<String> pkColumnNameList = new ArrayList<String>();
        if (tableDesc.getPrimaryKeyDesc() != null) {
            PrimaryKeyDesc primaryKeyDesc = tableDesc.getPrimaryKeyDesc();
            pkColumnNameList.addAll(primaryKeyDesc.getColumnNameList());
        }
        return new Comparator<ColumnDescRef>() {

            public int compare(ColumnDescRef o1, ColumnDescRef o2) {
                int index1 = pkColumnNameList.indexOf(o1.columnDesc.getName());
                int index2 = pkColumnNameList.indexOf(o2.columnDesc.getName());
                int ret = 0;
                if (index1 < 0) {
                    if (index2 < 0) {
                        ret = o1.index - o2.index;
                    } else {
                        ret = 1;
                    }
                } else {
                    if (index2 < 0) {
                        ret = -1;
                    } else {
                        ret = index1 - index2;
                    }
                }
                return ret;
            }
        };
    }

    private static class ColumnDescRef {

        final int index;

        final ColumnDesc columnDesc;

        ColumnDescRef(int index, ColumnDesc desc) {
            this.index = index;
            this.columnDesc = desc;
        }
    }
}

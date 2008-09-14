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
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.model.ColumnModel;
import org.seasar.extension.jdbc.gen.model.ForeignKeyModel;
import org.seasar.extension.jdbc.gen.model.PrimaryKeyModel;
import org.seasar.extension.jdbc.gen.model.SequenceModel;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.TableModelFactory;
import org.seasar.extension.jdbc.gen.model.UniqueKeyModel;

/**
 * {@link TableModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class TableModelFactoryImpl implements TableModelFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** SQLステートメントの区切り文字 */
    protected char delimiter;

    /** テーブルオプション、存在しない場合は{@code null} */
    protected String tableOption;

    /** SQLの識別子の大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlIdentifierCaseType sqlIdentifierCaseType;

    /** SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlKeywordCaseType sqlKeywordCaseType;

    /**
     * @param dialect
     *            方言
     * @param sqlIdentifierCaseType
     *            SQLの識別子の大文字小文字を変換するかどうかを示す列挙型
     * @param sqlKeywordCaseType
     *            SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型
     * @param delimiter
     *            区切り文字
     * @param tableOption
     *            テーブルオプション、存在しない場合は{@code null}
     */
    public TableModelFactoryImpl(GenDialect dialect,
            SqlIdentifierCaseType sqlIdentifierCaseType,
            SqlKeywordCaseType sqlKeywordCaseType, char delimiter,
            String tableOption) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (sqlIdentifierCaseType == null) {
            throw new NullPointerException("sqlIdentifierCaseType");
        }
        if (sqlKeywordCaseType == null) {
            throw new NullPointerException("sqlKeywordCaseType");
        }
        this.dialect = dialect;
        this.sqlIdentifierCaseType = sqlIdentifierCaseType;
        this.sqlKeywordCaseType = sqlKeywordCaseType;
        this.delimiter = delimiter;
        this.tableOption = tableOption;
    }

    public TableModel getTableModel(TableDesc tableDesc) {
        TableModel tableModel = new TableModel();
        tableModel.setCanonicalTableName(tableDesc.getCanonicalName());
        tableModel.setName(identifier(tableDesc.getFullName()));
        tableModel.setDialect(dialect);
        tableModel.setDelimiter(delimiter);
        tableModel.setTableOption(keyword(tableOption));
        tableModel.setSqlIdentifierCaseType(sqlIdentifierCaseType);
        tableModel.setSqlKeywordCaseType(sqlKeywordCaseType);
        doPrimaryKeyModel(tableModel, tableDesc);
        doUniqueKeyModel(tableModel, tableDesc);
        doForeignKeyModel(tableModel, tableDesc);
        doSequenceModel(tableModel, tableDesc);
        doColumnModel(tableModel, tableDesc);
        return tableModel;
    }

    /**
     * 主キーモデルを処理します。
     * 
     * @param tableModel
     *            テーブルモデル
     * @param tableDesc
     *            テーブル記述
     */
    protected void doPrimaryKeyModel(TableModel tableModel, TableDesc tableDesc) {
        PrimaryKeyDesc pkDesc = tableDesc.getPrimaryKeyDesc();
        if (pkDesc == null) {
            return;
        }
        PrimaryKeyModel pkModel = new PrimaryKeyModel();
        pkModel.setName(identifier(tableDesc.getName() + "_PK"));
        for (String columnName : pkDesc.getColumnNameList()) {
            pkModel.addColumnName(identifier(columnName));
        }
        tableModel.setPrimaryKeyModel(pkModel);
    }

    /**
     * 一意キーモデルを処理します。
     * 
     * @param tableModel
     *            テーブルモデル
     * @param tableDesc
     *            テーブル記述
     */
    protected void doUniqueKeyModel(TableModel tableModel, TableDesc tableDesc) {
        int index = 1;
        for (UniqueKeyDesc ukDesc : tableDesc.getUniqueKeyDescList()) {
            UniqueKeyModel ukModel = new UniqueKeyModel();
            ukModel.setName(identifier(tableDesc.getName() + "_UK" + index));
            ukModel.setDropUniqueKeySyntax(keyword(dialect
                    .getDropUniqueKeySyntax()));
            for (String columnName : ukDesc.getColumnNameList()) {
                ukModel.addColumnName(identifier(columnName));
            }
            tableModel.addUniqueKeyModel(ukModel);
            index++;
        }
    }

    /**
     * 外部キーモデルを処理します。
     * 
     * @param tableModel
     *            テーブルモデル
     * @param tableDesc
     *            テーブル記述
     */
    protected void doForeignKeyModel(TableModel tableModel, TableDesc tableDesc) {
        int index = 1;
        for (ForeignKeyDesc fkDesc : tableDesc.getForeignKeyDescList()) {
            ForeignKeyModel fkModel = new ForeignKeyModel();
            fkModel.setName(identifier(tableDesc.getName() + "_FK" + index));
            fkModel.setReferencedTableName(fkDesc.getReferencedFullTableName());
            fkModel.setDropForeignKeySyntax(keyword(dialect
                    .getDropForeignKeySyntax()));
            for (String columnName : fkDesc.getColumnNameList()) {
                fkModel.addColumnName(identifier(columnName));
            }
            for (String referencedColumnName : fkDesc
                    .getReferencedColumnNameList()) {
                fkModel.addReferencedColumnName(referencedColumnName);
            }
            tableModel.addForeignKeyModel(fkModel);
            index++;
        }
    }

    /**
     * シーケンスモデルを処理します。
     * 
     * @param tableModel
     *            テーブルモデル
     * @param tableDesc
     *            テーブル記述
     */
    protected void doSequenceModel(TableModel tableModel, TableDesc tableDesc) {
        for (SequenceDesc sequenceDesc : tableDesc.getSequenceDescList()) {
            SequenceModel sequenceModel = new SequenceModel();
            sequenceModel.setName(identifier(sequenceDesc.getSequenceName()));
            String definition = dialect.getSequenceDefinitionFragment(
                    sequenceDesc.getDataType(), sequenceDesc.getInitialValue(),
                    sequenceDesc.getAllocationSize());
            sequenceModel.setDefinition(keyword(definition));
            tableModel.addSequenceModel(sequenceModel);
        }
    }

    /**
     * カラムモデルを処理します。
     * 
     * @param tableModel
     *            テーブルモデル
     * @param tableDesc
     *            テーブル記述
     */
    protected void doColumnModel(TableModel tableModel, TableDesc tableDesc) {
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            ColumnModel columnModel = new ColumnModel();
            columnModel.setName(identifier(columnDesc.getName()));
            String definition = columnDesc.getDefinition();
            if (columnDesc.isIdentity()) {
                definition += " " + dialect.getIdentityColumnDefinition();
            } else {
                if (!columnDesc.isNullable()) {
                    definition += " not null";
                }
            }
            columnModel.setDefinition(keyword(definition));
            tableModel.addColumnModel(columnModel);
        }
    }

    /**
     * キーワードの大文字小文字を変換します。
     * 
     * @param keyword
     *            キーワード
     * @return 変換された文字列
     */
    protected String keyword(String keyword) {
        return sqlKeywordCaseType.convert(keyword);
    }

    /**
     * 識別子の大文字小文字を変換します。
     * 
     * @param identifier
     *            識別子
     * @return 変換された文字列
     */
    protected String identifier(String identifier) {
        return sqlIdentifierCaseType.convert(identifier);
    }

}

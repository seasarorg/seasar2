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

import java.sql.Types;

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.model.SchemaInfoTableModel;
import org.seasar.extension.jdbc.gen.model.SchemaInfoTableModelFactory;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;

/**
 * {@link SchemaInfoTableModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class SchemaInfoTableModelFactoryImpl implements
        SchemaInfoTableModelFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** SQLステートメントの区切り文字 */
    protected char delimiter;

    /** スキーマ情報を格納するテーブル名 */
    protected String tableName;

    /** スキーマのバージョン番号を格納するカラム名 */
    protected String columnName;

    /** テーブルオプション、存在しない場合は{@code null} */
    protected String tableOption;

    /** SQLの識別子の大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlIdentifierCaseType sqlIdentifierCaseType;

    /** SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型 */
    protected SqlKeywordCaseType sqlKeywordCaseType;

    /**
     * @param dialect
     *            方言
     * @param tableName
     *            テーブル名
     * @param colulmnName
     *            カラム名
     * @param sqlIdentifierCaseType
     *            SQLの識別子の大文字小文字を変換するかどうかを示す列挙型
     * @param sqlKeywordCaseType
     *            SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型
     * @param delimiter
     *            区切り文字
     * @param tableOption
     *            テーブルオプション、存在しない場合は{@code null}
     */
    public SchemaInfoTableModelFactoryImpl(GenDialect dialect,
            String tableName, String colulmnName,
            SqlIdentifierCaseType sqlIdentifierCaseType,
            SqlKeywordCaseType sqlKeywordCaseType, char delimiter,
            String tableOption) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (tableName == null) {
            throw new NullPointerException("tableName");
        }
        if (colulmnName == null) {
            throw new NullPointerException("colulmnName");
        }
        if (sqlIdentifierCaseType == null) {
            throw new NullPointerException("sqlIdentifierCaseType");
        }
        if (sqlKeywordCaseType == null) {
            throw new NullPointerException("sqlKeywordCaseType");
        }
        this.dialect = dialect;
        this.tableName = tableName;
        this.columnName = colulmnName;
        this.sqlIdentifierCaseType = sqlIdentifierCaseType;
        this.sqlKeywordCaseType = sqlKeywordCaseType;
        this.delimiter = delimiter;
        this.tableOption = tableOption;
    }

    public SchemaInfoTableModel getSchemaInfoTableModel(int versionNo) {
        SchemaInfoTableModel model = new SchemaInfoTableModel();
        String canonicalName = tableName.replace("\"", "").toLowerCase();
        model.setTableCanonicalName(canonicalName);
        model.setName(identifier(tableName));
        model.setDialect(dialect);
        model.setColumnName(identifier(columnName));
        String dataType = dialect.getSqlType(Types.INTEGER).getDataType(0, 10,
                0, false);
        model.setColumnDefinition(keyword(dataType + " not null"));
        model.setDelimiter(delimiter);
        model.setTableOption(keyword(tableOption));
        model.setVersionNo(versionNo);
        model.setSqlIdentifierCaseType(sqlIdentifierCaseType);
        model.setSqlKeywordCaseType(sqlKeywordCaseType);
        return model;
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

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
package org.seasar.extension.jdbc.gen.internal.command;

import java.util.List;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.framework.log.Logger;

/**
 * {@link DbTableMeta}と{@link DbColumnMeta}をダンプする{@link Command}の実装クラスです。
 * 
 * @author taedium
 */
public class DumpDbMetaCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DumpDbMetaCommand.class);

    /** スキーマ名 */
    protected String schemaName = null;

    /** Javaコード生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern = ".*";

    /** Javaコード生成の対象としないテーブル名の正規表現 */
    protected String ignoreTableNamePattern = ".*\\$.*";

    /** {@link GenDialect}の実装クラス名 */
    protected String genDialectClassName = null;

    /** 方言 */
    protected GenDialect dialect;

    /** テーブルメタデータのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /**
     * インスタンスを構築します。
     */
    public DumpDbMetaCommand() {
    }

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Javaコード生成の対象とするテーブル名の正規表現を返します。
     * 
     * @return Javaコード生成の対象とするテーブル名の正規表現
     */
    public String getTableNamePattern() {
        return tableNamePattern;
    }

    /**
     * Javaコード生成の対象とするテーブル名の正規表現を設定します。
     * 
     * @param tableNamePattern
     *            Javaコード生成の対象とするテーブル名の正規表現
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * Javaコード生成の対象としないテーブル名の正規表現を返します。
     * 
     * @return Javaコード生成の対象としないテーブル名の正規表現
     */
    public String getIgnoreTableNamePattern() {
        return ignoreTableNamePattern;
    }

    /**
     * Javaコード生成の対象としないテーブル名の正規表現を設定します。
     * 
     * @param ignoreTableNamePattern
     *            Javaコード生成の対象としないテーブル名の正規表現
     */
    public void setIgnoreTableNamePattern(String ignoreTableNamePattern) {
        this.ignoreTableNamePattern = ignoreTableNamePattern;
    }

    /**
     * {@link GenDialect}の実装クラス名を返します。
     * 
     * @return {@link GenDialect}の実装クラス名
     */
    public String getGenDialectClassName() {
        return genDialectClassName;
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        this.genDialectClassName = genDialectClassName;
    }

    @Override
    protected void doValidate() {
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        dialect = getGenDialect(genDialectClassName);
        dbTableMetaReader = createDbTableMetaReader();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() {
        List<DbTableMeta> tableMetaList = dbTableMetaReader.read();
        for (DbTableMeta table : tableMetaList) {
            logger.log("IS2JDBCGen0001", new Object[] { table.getCatalogName(),
                    table.getSchemaName(), table.getName(),
                    table.getColumnMetaList().size() });
            for (DbColumnMeta column : table.getColumnMetaList()) {
                logger.log("IS2JDBCGen0002", new Object[] { column.getName(),
                        Integer.toString(column.getSqlType()),
                        column.getTypeName(), column.getLength(),
                        column.getScale(), column.isNullable(),
                        column.isPrimaryKey() });
            }
        }
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * {@link DbTableMetaReader}の実装を作成します。
     * 
     * @return {@link DbTableMetaReader}の実装
     */
    protected DbTableMetaReader createDbTableMetaReader() {
        return factory.createDbTableMetaReader(this, jdbcManager
                .getDataSource(), dialect, schemaName, tableNamePattern,
                ignoreTableNamePattern, false);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}

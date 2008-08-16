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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.DatabaseDesc;
import org.seasar.extension.jdbc.gen.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.DdlModel;
import org.seasar.extension.jdbc.gen.DdlModelFactory;
import org.seasar.extension.jdbc.gen.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.DdlVersionIncrementer;
import org.seasar.extension.jdbc.gen.Dumper;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.data.DumperImpl;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.DdlModelFactoryImpl;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutorImpl;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryImpl;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementerImpl;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * DDLのSQLファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからDDLのSQLファイルを生成し、データをダンプします。 そのため、
 * ココマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * <p>
 * このコマンドが生成するDDLは次の8つです。
 * <ul>
 * <li>テーブルを作成するDDL</li>
 * <li>テーブルを削除するDDL</li>
 * <li>一意キーを作成するDDL</li>
 * <li>一意キーを削除するDDL</li>
 * <li>外部キーを作成するDDL</li>
 * <li>外部キーを削除するDDL</li>
 * <li>シーケンスを作成するDDL</li>
 * <li>シーケンスを削除するDDL</li>
 * </ul>
 * </p>
 * <p>
 * このコマンドは、{@link #dump}が{@code true}の場合データベースのデータをテーブルごとにダンプします。
 * </p>
 * 
 * @author taedium
 */
public class GenerateDdlCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(GenerateDdlCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** テーブルを作成するDDLファイル名 */
    protected String createTableDdlFileName = "010-create-table.sql";

    /** 一意キーを作成するDDLファイル名 */
    protected String createUniqueKeyDdlFileName = "020-create-uniquekey.sql";

    /** シーケンスを生成するDDLファイル */
    protected String createSequenceDdlFileName = "030-create-sequence.sql";

    /** ダンプディレクトリ名 */
    protected String dumpDirName = "040-dump";

    /** 外部キーを作成するDDLファイル名 */
    protected String createForeignKeyDdlFileName = "050-create-foreignkey.sql";

    /** テーブルを削除するDDLファイル名 */
    protected String dropTableDdlFileName = "040-drop-table.sql";

    /** 一意キーを削除するDDLファイル名 */
    protected String dropUniqueKeyDdlFileName = "030-drop-uniquekey.sql";

    /** シーケンスを削除するDDLファイル名 */
    protected String dropSequenceDdlFileName = "020-drop-sequence.sql";

    /** 外部キーを削除するDDLファイル名 */
    protected String dropForeignKeyDdlFileName = "010-drop-foreignkey.sql";

    /** テーブルを作成するDDLのテンプレートファイル名 */
    protected String createTableTemplateFileName = "sql/create-table.ftl";

    /** 一意キーを作成するDDLのテンプレートファイル名 */
    protected String createUniqueKeyTemplateFileName = "sql/create-uniquekey.ftl";

    /** 外部キーを作成するDDLのテンプレートファイル名 */
    protected String createForeignKeyTemplateFileName = "sql/create-foreignkey.ftl";

    /** シーケンスを生成するDDLのテンプレートファイル */
    protected String createSequenceTemplateFileName = "sql/create-sequence.ftl";

    /** テーブルを削除するDDLのテンプレートファイル名 */
    protected String dropTableTemplateFileName = "sql/drop-table.ftl";

    /** 外部キーを削除するDDLのテンプレートファイル名 */
    protected String dropForeignKeyTemplateFileName = "sql/drop-foreignkey.ftl";

    /** 一意キーを削除するDDLのテンプレートファイル名 */
    protected String dropUniqueKeyTemplateFileName = "sql/drop-uniquekey.ftl";

    /** シーケンスを削除するDDLのテンプレートファイル名 */
    protected String dropSequenceTemplateFileName = "sql/drop-sequence.ftl";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティ名の正規表現 */
    protected String entityNamePattern = ".*";

    /** 対象としないエンティティ名の正規表現 */
    protected String ignoreEntityNamePattern = "";

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** マイグレーションのディレクトリ */
    protected File migrateDir = new File("db", "migrate");

    /** DDLファイルのエンコーディング */
    protected String ddlFileEncoding = "UTF-8";

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** スキーマ情報を格納する完全なテーブル名 */
    protected String schemaInfoFullTableName = "SCHEMA_INFO";

    /** スキーマのバージョン番号を格納するカラム名 */
    protected String schemaInfoColumnName = "VERSION";

    /** DDLのバージョンファイル */
    protected File ddlInfoFile = new File("db", "ddl-info.txt");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    /** テーブルオプション */
    protected String tableOption = null;

    /** データをダンプする場合{@code true}、しない場合{@code false} */
    protected boolean dump = true;

    /** 方言 */
    protected GenDialect dialect;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** データベースのモデルのファクトリ */
    protected DdlModelFactory ddlModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** DDLのバージョンを管理するディレクトリ */
    protected DdlVersionDirectory ddlVersionDirectory;

    /** DDLのバージョンのインクリメンタ */
    protected DdlVersionIncrementer ddlVersionIncrementer;

    /** データベース記述ファクトリ */
    protected DatabaseDescFactory databaseDescFactory;

    /** SQLのひとまとまりの処理の実行者 */
    protected SqlUnitExecutor sqlUnitExecutor;

    /** ダンパ */
    protected Dumper dumper;

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlCommand() {
    }

    /**
     * クラスパスのディレクトリを返します。
     * 
     * @return クラスパスのディレクトリ
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        this.classpathDir = classpathDir;
    }

    /**
     * テーブルを作成するDDLファイル名を返します。
     * 
     * @return テーブルを作成するDDLファイル名
     */
    public String getCreateTableDdlFileName() {
        return createTableDdlFileName;
    }

    /**
     * テーブルを作成するDDLファイル名を設定します。
     * 
     * @param createTableDdlFileName
     *            テーブルを作成するDDLファイル名
     */
    public void setCreateTableDdlFileName(String createTableDdlFileName) {
        this.createTableDdlFileName = createTableDdlFileName;
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを作成するDDLのテンプレートファイル名
     */
    public String getCreateTableTemplateFileName() {
        return createTableTemplateFileName;
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createTableTemplateFileName
     *            テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        this.createTableTemplateFileName = createTableTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLファイル名を返します。
     * 
     * @return シーケンスを作成するDDLのSQLレートファイル名
     */
    public String getCreateSequenceDdlFileName() {
        return createSequenceDdlFileName;
    }

    /**
     * シーケンスを作成するDDLファイル名を設定します。
     * 
     * @param createSequenceDdlFileName
     *            シーケンスを作成するDDLのSQLレートファイル名
     */
    public void setCreateSequenceDdlFileName(String createSequenceDdlFileName) {
        this.createSequenceDdlFileName = createSequenceDdlFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを作成するDDLのテンプレートファイル名
     */
    public String getCreateSequenceTemplateFileName() {
        return createSequenceTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを作成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        this.createSequenceTemplateFileName = createSequenceTemplateFileName;
    }

    /**
     * テーブルを削除するDDLファイル名を返します。
     * 
     * @return テーブルを削除するDDLファイル名
     */
    public String getDropTableDdlFileName() {
        return dropTableDdlFileName;
    }

    /**
     * テーブルを削除するDDLファイル名を設定します。
     * 
     * @param dropTableDdlFileName
     *            テーブルを削除するDDLファイル名
     */
    public void setDropTableDdlFileName(String dropTableDdlFileName) {
        this.dropTableDdlFileName = dropTableDdlFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを削除するDDLのテンプレートファイル名
     */
    public String getDropTableTemplateFileName() {
        return dropTableTemplateFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropTableTemplateFileName
     *            テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        this.dropTableTemplateFileName = dropTableTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLファイル名を返します。
     * 
     * @return シーケンスを削除するDDLファイル名
     */
    public String getDropSequenceDdlFileName() {
        return dropSequenceDdlFileName;
    }

    /**
     * シーケンスを削除するDDLファイル名を設定します。
     * 
     * @param dropSequenceDdlFileName
     *            シーケンスを削除するDDLファイル名
     */
    public void setDropSequenceDdlFileName(String dropSequenceDdlFileName) {
        this.dropSequenceDdlFileName = dropSequenceDdlFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを削除するDDLのテンプレートファイル名
     */
    public String getDropSequenceTemplateFileName() {
        return dropSequenceTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSequenceTemplateFileName
     *            シーケンスを削除するDDLのテンプレートファイル名
     */
    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        this.dropSequenceTemplateFileName = dropSequenceTemplateFileName;
    }

    /**
     * 一意キーを作成するDDLファイル名を返します。
     * 
     * @return 一意キーを作成するDDLファイル名
     */
    public String getCreateUniqueKeyDdlFileName() {
        return createUniqueKeyDdlFileName;
    }

    /**
     * 一意キーを作成するDDLファイル名を設定します。
     * 
     * @param createUniqueKeyDdlFileName
     *            一意キーを作成するDDLファイル名
     */
    public void setCreateUniqueKeyDdlFileName(String createUniqueKeyDdlFileName) {
        this.createUniqueKeyDdlFileName = createUniqueKeyDdlFileName;
    }

    /**
     * 外部キーを作成するDDLファイル名を返します。
     * 
     * @return 外部キーを作成するDDLファイル名
     */
    public String getCreateForeignKeyDdlFileName() {
        return createForeignKeyDdlFileName;
    }

    /**
     * 外部キーを作成するDDLファイル名を設定します。
     * 
     * @param createForeignKeyDdlFileName
     *            外部キーを作成するDDLファイル名
     */
    public void setCreateForeignKeyDdlFileName(
            String createForeignKeyDdlFileName) {
        this.createForeignKeyDdlFileName = createForeignKeyDdlFileName;
    }

    /**
     * 一意キーを削除するDDLファイル名を返します。
     * 
     * @return 一意キーを削除するDDLファイル名
     */
    public String getDropUniqueKeyDdlFileName() {
        return dropUniqueKeyDdlFileName;
    }

    /**
     * 一意キーを削除するDDLファイル名を設定します。
     * 
     * @param dropUniqueKeyDdlFileName
     *            一意キーを削除するDDLファイル名
     */
    public void setDropUniqueKeyDdlFileName(String dropUniqueKeyDdlFileName) {
        this.dropUniqueKeyDdlFileName = dropUniqueKeyDdlFileName;
    }

    /**
     * 外部キーを削除するDDLファイル名を返します。
     * 
     * @return 外部キーを削除するDDLファイル名
     */
    public String getDropForeignKeyDdlFileName() {
        return dropForeignKeyDdlFileName;
    }

    /**
     * 外部キーを削除するDDLファイル名を設定します。
     * 
     * @param dropForeignKeyDdlFileName
     *            外部キーを削除するDDLファイル名
     */
    public void setDropForeignKeyDdlFileName(String dropForeignKeyDdlFileName) {
        this.dropForeignKeyDdlFileName = dropForeignKeyDdlFileName;
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return 一意キーを作成するDDLのテンプレートファイル名
     */
    public String getCreateUniqueKeyTemplateFileName() {
        return createUniqueKeyTemplateFileName;
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createUniqueKeyTemplateFileName
     *            一意キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateUniqueKeyTemplateFileName(
            String createUniqueKeyTemplateFileName) {
        this.createUniqueKeyTemplateFileName = createUniqueKeyTemplateFileName;
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return 外部キーを作成するDDLのテンプレートファイル名
     */
    public String getCreateForeignKeyTemplateFileName() {
        return createForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createForeignKeyTemplateFileName
     *            外部キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateForeignKeyTemplateFileName(
            String createForeignKeyTemplateFileName) {
        this.createForeignKeyTemplateFileName = createForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 外部キーを削除するDDLのテンプレートファイル名
     */
    public String getDropForeignKeyTemplateFileName() {
        return dropForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropForeignKeyTemplateFileName
     *            外部キーを削除するDDLのテンプレートファイル名
     */
    public void setDropForeignKeyTemplateFileName(
            String dropForeignKeyTemplateFileName) {
        this.dropForeignKeyTemplateFileName = dropForeignKeyTemplateFileName;
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 一意キーを削除するDDLのテンプレートファイル名
     */
    public String getDropUniqueKeyTemplateFileName() {
        return dropUniqueKeyTemplateFileName;
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropUniqueKeyTemplateFileName
     *            一意キーを削除するDDLのテンプレートファイル名
     */
    public void setDropUniqueKeyTemplateFileName(
            String dropUniqueKeyTemplateFileName) {
        this.dropUniqueKeyTemplateFileName = dropUniqueKeyTemplateFileName;
    }

    /**
     * エンティティクラスのパッケージ名を返します。
     * 
     * @return エンティティクラスのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * 対象とするエンティティ名の正規表現を返します。
     * 
     * @return 対象とするエンティティ名の正規表現
     */
    public String getEntityNamePattern() {
        return entityNamePattern;
    }

    /**
     * 対象とするエンティティ名の正規表現を設定します。
     * 
     * @param entityNamePattern
     *            対象とするエンティティ名の正規表現
     */
    public void setEntityNamePattern(String entityNamePattern) {
        this.entityNamePattern = entityNamePattern;
    }

    /**
     * 対象としないエンティティ名の正規表現を返します。
     * 
     * @return 対象としないエンティティ名の正規表現
     */
    public String getIgnoreEntityNamePattern() {
        return ignoreEntityNamePattern;
    }

    /**
     * 対象としないエンティティ名の正規表現を設定します。
     * 
     * @param ignoreEntityNamePattern
     *            対象としないエンティティ名の正規表現
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        this.ignoreEntityNamePattern = ignoreEntityNamePattern;
    }

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * マイグレーションのディレクトリを返します。
     * 
     * @return マイグレーションのディレクトリ
     */
    public File getMigrateDir() {
        return migrateDir;
    }

    /**
     * マイグレーションのディレクトリを設定します。
     * 
     * @param migrateDir
     *            マイグレーションのディレクトリ
     */
    public void setMigrateDir(File migrateDir) {
        this.migrateDir = migrateDir;
    }

    /**
     * DDLファイルのエンコーディングを返します。
     * 
     * @return DDLファイルのエンコーディング
     */
    public String getDdlFileEncoding() {
        return ddlFileEncoding;
    }

    /**
     * DDLファイルのエンコーディングを設定します。
     * 
     * @param ddlFileEncoding
     *            DDLファイルのエンコーディング
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        this.ddlFileEncoding = ddlFileEncoding;
    }

    /**
     * テンプレートファイルのエンコーディングを返します。
     * 
     * @return テンプレートファイルのエンコーディング
     */
    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを返します。
     * 
     * @return テンプレートファイルを格納するプライマリディレクトリ
     */
    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        this.templateFilePrimaryDir = templateFilePrimaryDir;
    }

    /**
     * スキーマ情報を格納する完全なテーブル名を返します。
     * 
     * @return スキーマ情報を格納する完全なテーブル名
     */
    public String getSchemaInfoFullTableName() {
        return schemaInfoFullTableName;
    }

    /**
     * スキーマ情報を格納する完全なテーブル名を設定します。
     * 
     * @param schemaInfoFullTableName
     *            スキーマ情報を格納する完全なテーブル名
     */
    public void setSchemaInfoFullTableName(String schemaInfoFullTableName) {
        this.schemaInfoFullTableName = schemaInfoFullTableName;
    }

    /**
     * スキーマのバージョン番号を格納するカラム名を返します。
     * 
     * @return スキーマのバージョン番号を格納するカラム名
     */
    public String getSchemaInfoColumnName() {
        return schemaInfoColumnName;
    }

    /**
     * スキーマのバージョン番号を格納するカラム名を設定します。
     * 
     * @param schemaInfoColumnName
     *            スキーマのバージョン番号を格納するカラム名
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        this.schemaInfoColumnName = schemaInfoColumnName;
    }

    /**
     * DDL情報ファイル名を返します。
     * 
     * @return DDL情報ファイル
     */
    public File getDdlInfoFile() {
        return ddlInfoFile;
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        this.ddlInfoFile = ddlInfoFile;
    }

    /**
     * バージョン番号のパターンを返します。
     * 
     * @return バージョン番号のパターン
     */
    public String getVersionNoPattern() {
        return versionNoPattern;
    }

    /**
     * バージョン番号のパターンを設定します。
     * 
     * @param versionNoPattern
     *            バージョン番号のパターン
     */
    public void setVersionNoPattern(String versionNoPattern) {
        this.versionNoPattern = versionNoPattern;
    }

    /**
     * ダンプディレクトリ名を返します。
     * 
     * @return ダンプディレクトリ名
     */
    public String getDumpDirName() {
        return dumpDirName;
    }

    /**
     * ダンプディレクトリ名を設定します。
     * 
     * @param dumpDirName
     *            ダンプディレクトリ名
     */
    public void setDumpDirName(String dumpDirName) {
        this.dumpDirName = dumpDirName;
    }

    /**
     * テーブルオプションを返します。
     * 
     * @return テーブルオプション
     */
    public String getTableOption() {
        return tableOption;
    }

    /**
     * テーブルオプションを設定します。
     * 
     * @param tableOption
     *            テーブルオプション
     */
    public void setTableOption(String tableOption) {
        this.tableOption = tableOption;
    }

    /**
     * ダンプファイルのエンコーディングを返します。
     * 
     * @return ダンプファイルのエンコーディング
     */
    public String getDumpFileEncoding() {
        return dumpFileEncoding;
    }

    /**
     * ダンプファイルのエンコーディングを設定します。
     * 
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        this.dumpFileEncoding = dumpFileEncoding;
    }

    /**
     * データをダンプする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return データをダンプする場合{@code true}、しない場合{@code false}
     */
    public boolean isDump() {
        return dump;
    }

    /**
     * データをダンプする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param dump
     *            データをダンプする場合{@code true}、しない場合{@code false}
     */
    public void setDump(boolean dump) {
        this.dump = dump;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        ddlVersionDirectory = createDdlVersionDirectory();
        ddlVersionIncrementer = createDdlVersionIncrementer();
        ddlModelFactory = createDdlModelFactory();
        generator = createGenerator();
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        dumper = createDumper();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() throws Throwable {
        ddlVersionIncrementer.increment(new DdlVersionIncrementer.Callback() {

            public void execute(final File createDir, File dropDir,
                    int versionNo) {

                final DatabaseDesc databaseDesc = databaseDescFactory
                        .getDatabaseDesc();
                DdlModel model = ddlModelFactory.getDdlModel(databaseDesc,
                        versionNo);
                generateCreateDdl(model, createDir);
                generateDropDdl(model, dropDir);

                if (!dump) {
                    return;
                }
                sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

                    public void execute(SqlExecutionContext context) {
                        dumper.dump(context, databaseDesc, new File(createDir,
                                dumpDirName));
                    }
                });

            }
        });
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * 作成用のDDLファイルを生成します。
     * 
     * @param model
     *            データモデル
     * @param createDir
     *            ファイルを生成するディレクトリ
     */
    protected void generateCreateDdl(DdlModel model, File createDir) {
        GenerationContext tableContext = createGenerationContext(model,
                createDir, createTableDdlFileName, createTableTemplateFileName);
        generator.generate(tableContext);

        GenerationContext uniqueKeyContext = createGenerationContext(model,
                createDir, createUniqueKeyDdlFileName,
                createUniqueKeyTemplateFileName);
        generator.generate(uniqueKeyContext);

        GenerationContext foreignKeyContext = createGenerationContext(model,
                createDir, createForeignKeyDdlFileName,
                createForeignKeyTemplateFileName);
        generator.generate(foreignKeyContext);

        GenerationContext sequenceContext = createGenerationContext(model,
                createDir, createSequenceDdlFileName,
                createSequenceTemplateFileName);
        generator.generate(sequenceContext);
    }

    /**
     * 削除用のDDLファイルを生成します。
     * 
     * @param model
     *            データモデル
     * @param dropDir
     *            ファイルを生成するディレクトリ
     */
    protected void generateDropDdl(DdlModel model, File dropDir) {
        GenerationContext tableContext = createGenerationContext(model,
                dropDir, dropTableDdlFileName, dropTableTemplateFileName);
        generator.generate(tableContext);

        GenerationContext uniqueKeyContext = createGenerationContext(model,
                dropDir, dropUniqueKeyDdlFileName,
                dropUniqueKeyTemplateFileName);
        generator.generate(uniqueKeyContext);

        GenerationContext foreignKeyContext = createGenerationContext(model,
                dropDir, dropForeignKeyDdlFileName,
                dropForeignKeyTemplateFileName);
        generator.generate(foreignKeyContext);

        GenerationContext sequenceContext = createGenerationContext(model,
                dropDir, dropSequenceDdlFileName, dropSequenceTemplateFileName);
        generator.generate(sequenceContext);
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), jdbcManager
                .getEntityMetaFactory(), entityNamePattern,
                ignoreEntityNamePattern);
    }

    /**
     * {@link DatabaseDescFactory}の実装を作成します。
     * 
     * @return {@link DatabaseDescFactory}の実装
     */
    protected DatabaseDescFactory createDatabaseDescFactory() {
        return new DatabaseDescFactoryImpl(jdbcManager.getEntityMetaFactory(),
                entityMetaReader, dialect);
    }

    /**
     * {@link DdlVersionDirectory}の実装を作成します。
     * 
     * @return {@link DdlVersionDirectory}の実装
     */
    protected DdlVersionDirectory createDdlVersionDirectory() {
        return new DdlVersionDirectoryImpl(migrateDir, ddlInfoFile,
                versionNoPattern);
    }

    /**
     * {@link DdlVersionIncrementer}の実装を作成します。
     * 
     * @return {@link DdlVersionIncrementer}の実装
     */
    protected DdlVersionIncrementer createDdlVersionIncrementer() {
        List<String> createFileNameList = Arrays.asList(createTableDdlFileName,
                createUniqueKeyDdlFileName, createSequenceDdlFileName,
                createForeignKeyDdlFileName, dumpDirName);
        List<String> dropFileNameList = Arrays.asList(dropTableDdlFileName,
                dropUniqueKeyDdlFileName, dropSequenceDdlFileName,
                dropForeignKeyDdlFileName);
        return new DdlVersionIncrementerImpl(ddlVersionDirectory,
                createFileNameList, dropFileNameList);
    }

    /**
     * {@link DdlModelFactory}の実装を作成します。
     * 
     * @return {@link DdlModelFactory}の実装
     */
    protected DdlModelFactory createDdlModelFactory() {
        return new DdlModelFactoryImpl(dialect, statementDelimiter,
                schemaInfoFullTableName, schemaInfoColumnName, tableOption);
    }

    /**
     * {@link Dumper}の実装を作成します。
     * 
     * @return {@link Dumper}の実装
     */
    protected Dumper createDumper() {
        return new DumperImpl(dialect, dumpFileEncoding);
    }

    /**
     * {@link SqlUnitExecutor}の実装を返します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return new SqlUnitExecutorImpl(jdbcManager.getDataSource(), false);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            データベースのモデル
     * @param dir
     *            生成するファイルの出力先ディレクトリ
     * @param fileName
     *            ファイルの名前
     * @param templateName
     *            テンプレートファイルの名前
     * @return
     */
    protected GenerationContext createGenerationContext(Object model, File dir,
            String fileName, String templateName) {
        return new GenerationContextImpl(model, dir, new File(dir, fileName),
                templateName, ddlFileEncoding, true);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}

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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.DdlModel;
import org.seasar.extension.jdbc.gen.DdlModelFactory;
import org.seasar.extension.jdbc.gen.DdlVersion;
import org.seasar.extension.jdbc.gen.Dumper;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.TableDescFactory;
import org.seasar.extension.jdbc.gen.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.TableDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.NextVersionDirExistsRuntimeException;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.DdlModelFactoryImpl;
import org.seasar.extension.jdbc.gen.sql.DumperImpl;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContextImpl;
import org.seasar.extension.jdbc.gen.util.ExclusionFilenameFilter;
import org.seasar.extension.jdbc.gen.util.FileUtil;
import org.seasar.extension.jdbc.gen.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.gen.util.VersionUtil;
import org.seasar.extension.jdbc.gen.version.DdlVersionImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * DDLのSQLファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからDDLのSQLファイルを生成します。 そのため、
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
 * 
 * @author taedium
 */
public class GenerateDdlCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(GenerateDdlCommand.class);

    /** クラスパスのルートとなるディレクトリ */
    protected File classpathDir;

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** テーブルを作成するDDLファイル名 */
    protected String createTableDdlFileName = "010-create-table.sql";

    /** 一意キーを作成するDDLファイル名 */
    protected String createUniqueKeyDdlFileName = "020-create-uniquekey.sql";

    /** 外部キーを作成するDDLファイル名 */
    protected String createForeignKeyDdlFileName = "030-create-foreignkey.sql";

    /** シーケンスを生成するDDLファイル */
    protected String createSequenceDdlFileName = "040-create-sequence.sql";

    /** テーブルを削除するDDLファイル名 */
    protected String dropTableDdlFileName = "040-drop-table.sql";

    /** 一意キーを削除するDDLファイル名 */
    protected String dropUniqueKeyDdlFileName = "030-drop-uniquekey.sql";

    /** 外部キーを削除するDDLファイル名 */
    protected String dropForeignKeyDdlFileName = "020-drop-foreignkey.sql";

    /** シーケンスを削除するDDLファイル名 */
    protected String dropSequenceDdlFileName = "010-drop-sequence.sql";

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

    /** 環境名 */
    protected String env = "ut";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

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
    protected File ddlVersionFile = new File("db", "ddl-version.txt");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** スキーマ作成用のSQLファイルを格納するディレクトリ名 */
    protected String createDirName = "create";

    /** スキーマ削除用のSQLファイルを格納するディレクトリ名 */
    protected String dropDirName = "drop";

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    protected String dumpTemplateFileName = "dump/dump.ftl";

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** 方言 */
    protected GenDialect dialect;

    protected DataSource dataSource;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** テーブル記述のファクトリ */
    protected TableDescFactory tableDescFactory;

    /** データベースのモデルのファクトリ */
    protected DdlModelFactory ddlModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** DDLのバージョン */
    protected DdlVersion ddlVersion;

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlCommand() {
    }

    /**
     * クラスパスのルートとなるディレクトリを返します。
     * 
     * @return クラスパスのルートとなるディレクトリ
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
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
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
     * 環境名を返します。
     * 
     * @return 環境名
     */
    public String getEnv() {
        return env;
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
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
     * DDLのバージョンファイル名を返します。
     * 
     * @return DDLのバージョンファイル
     */
    public File getDdlVersionFile() {
        return ddlVersionFile;
    }

    /**
     * DDLのバージョンファイルを設定します。
     * 
     * @param ddlVersionFile
     *            DDLのバージョンファイル
     */
    public void setDdlVersionFile(File ddlVersionFile) {
        this.ddlVersionFile = ddlVersionFile;
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

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dataSource = jdbcManager.getDataSource();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        ddlVersion = createDdlVersion();
        entityMetaReader = createEntityMetaReader();
        tableDescFactory = createTableDescFactory();
        ddlModelFactory = createDdlModelFactory();
        generator = createGenerator();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
    }

    @Override
    protected void doExecute() throws Throwable {
        int versionNo = ddlVersion.getVersionNo();
        File versionDir = new File(migrateDir, VersionUtil.toString(versionNo,
                versionNoPattern));
        if (versionNo == 0) {
            File createDir = new File(versionDir, createDirName);
            createDir.mkdirs();
            File dropDir = new File(versionDir, dropDirName);
            dropDir.mkdirs();
        }

        int nextVersionNo = versionNo + 1;
        File nextVersionDir = new File(migrateDir, VersionUtil.toString(
                nextVersionNo, versionNoPattern));
        if (nextVersionDir.exists()) {
            throw new NextVersionDirExistsRuntimeException(nextVersionDir
                    .getPath(), ddlVersionFile.getPath());
        }

        try {
            if (versionDir.exists()) {
                FileUtil.copyDirectory(versionDir, nextVersionDir,
                        new ExclusionFilenameFilter());
            }
            generate(nextVersionDir, nextVersionNo);
            ddlVersion.setVersionNo(nextVersionNo);
        } catch (Throwable t) {
            if (nextVersionDir.exists()) {
                FileUtil.deleteDirectory(nextVersionDir);
            }
            throw t;
        }
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    /**
     * DDLファイルを生成します。
     * 
     * @param nextVersionDir
     *            次のバージョンのディレクトリ
     * @param nextVersionNo
     *            次のバージョン番号
     */
    protected void generate(File nextVersionDir, int nextVersionNo) {
        List<TableDesc> tableDescList = getTableDescList();
        DdlModel model = ddlModelFactory.getDdlModel(tableDescList,
                nextVersionNo);

        File createDir = new File(nextVersionDir, createDirName);
        generateCreateDdl(model, createDir);

        File dropDir = new File(nextVersionDir, dropDirName);
        generateDropDdl(model, dropDir);

        File dumpDir = new File(createDir, "025-dump");
        Dumper dumper = createDumper(dumpDir, tableDescList);
        SqlExecutionContext context = createSqlExecutionContext();
        try {
            dumper.dump(context);
        } finally {
            if (!context.getExceptionList().isEmpty()) {
                for (Exception e : context.getExceptionList()) {
                    logger.error(e.getMessage());
                }
                throw context.getExceptionList().get(0);
            }
        }
    }

    protected List<TableDesc> getTableDescList() {
        List<EntityMeta> entityMetaList = entityMetaReader.read();
        List<TableDesc> tableDescList = new ArrayList<TableDesc>();
        for (EntityMeta entityMeta : entityMetaList) {
            TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
            if (!tableDescList.contains(tableDesc)) {
                tableDescList.add(tableDesc);
            }
            for (TableDesc idTableDesc : tableDesc.getIdTableDescList()) {
                if (!tableDescList.contains(idTableDesc)) {
                    tableDescList.add(idTableDesc);
                }
            }
        }
        return tableDescList;
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
                rootPackageName, entityPackageName), entityMetaFactory,
                entityNamePattern, ignoreEntityNamePattern);
    }

    /**
     * {@link TableDescFactory}の実装を作成します。
     * 
     * @return {@link TableDescFactory}の実装
     */
    protected TableDescFactory createTableDescFactory() {
        ColumnDescFactory colFactory = new ColumnDescFactoryImpl(dialect);
        PrimaryKeyDescFactory pkFactory = new PrimaryKeyDescFactoryImpl(dialect);
        UniqueKeyDescFactory ukFactory = new UniqueKeyDescFactoryImpl();
        ForeignKeyDescFactory fkFactory = new ForeignKeyDescFactoryImpl(
                entityMetaFactory);
        SequenceDescFactory seqFactory = new SequenceDescFactoryImpl(dialect);
        IdTableDescFactory idTabFactory = new IdTableDescFactoryImpl(dialect,
                colFactory, pkFactory, ukFactory);
        return new TableDescFactoryImpl(colFactory, pkFactory, ukFactory,
                fkFactory, seqFactory, idTabFactory);
    }

    /**
     * {@link DdlVersion}の実装を作成します。
     * 
     * @return {@link DdlVersion}の実装
     */
    protected DdlVersion createDdlVersion() {
        return new DdlVersionImpl(ddlVersionFile);
    }

    /**
     * {@link DdlModelFactory}の実装を作成します。
     * 
     * @return {@link DdlModelFactory}の実装
     */
    protected DdlModelFactory createDdlModelFactory() {
        return new DdlModelFactoryImpl(dialect, statementDelimiter,
                schemaInfoFullTableName, schemaInfoColumnName);
    }

    protected Dumper createDumper(File dumpDir, List<TableDesc> tableDescList) {
        return new DumperImpl(dumpDir, dumpFileEncoding, dumpTemplateFileName,
                generator, dialect, tableDescList);
    }

    protected SqlExecutionContext createSqlExecutionContext() {
        return new SqlExecutionContextImpl(DataSourceUtil
                .getConnection(dataSource), false);
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

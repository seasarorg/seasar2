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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.event.GenDdlListenerImpl;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.model.DdlModel;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.TableModelFactory;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementer;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.container.SingletonS2Container;
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
 * このコマンドは次の10種類のDDLを生成します。
 * <ul>
 * <li>テーブルを作成するDDL</li>
 * <li>テーブルを削除するDDL</li>
 * <li>一意キーを作成するDDL</li>
 * <li>一意キーを削除するDDL</li>
 * <li>外部キーを作成するDDL</li>
 * <li>外部キーを削除するDDL</li>
 * <li>シーケンスを作成するDDL</li>
 * <li>シーケンスを削除するDDL</li>
 * <li>補助的オブジェクトを作成するDDL</li>
 * <li>補助的ブジェクトを削除するDDL</li>
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

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** テーブルを作成するDDLを格納するディレクトリ名 */
    protected String createTableDirName = "010-table";

    /** 一意キーを作成するDDLを格納するディレクトリ名 */
    protected String createUniqueKeyDirName = "020-uniquekey";

    /** シーケンスを作成するDDLを格納するディレクトリ名 */
    protected String createSequenceDirName = "030-sequence";

    /** 外部キーを作成するDDLを格納するディレクトリ名 */
    protected String createForeignKeyDirName = "050-foreignkey";

    /** 補助的オブジェクトを作成するDDLを格納するディレクトリ名 */
    protected String createAuxiliaryDirName = "060-auxiliary";

    /** 補助的オブジェクトを削除するDDLを格納するディレクトリ名 */
    protected String dropAuxiliaryDirName = "050-auxiliary";

    /** テーブルを削除するDDLを格納するディレクトリ名 */
    protected String dropTableDirName = "040-table";

    /** 一意キーを削除するDDLを格納するディレクトリ名 */
    protected String dropUniqueKeyDirName = "030-uniquekey";

    /** シーケンスを作成するDDLを削除するディレクトリ名 */
    protected String dropSequenceDirName = "020-sequence";

    /** 外部キーを作成するDDLを削除するディレクトリ名 */
    protected String dropForeignKeyDirName = "010-foreignkey";

    /** DDLファイルのエンコーディング */
    protected String ddlFileEncoding = "UTF-8";

    /** SQLのキーワードの大文字小文字を変換するかどうかを示す値 */
    protected SqlKeywordCaseType sqlKeywordCaseType = SqlKeywordCaseType.ORIGINALCASE;

    /** SQLの識別子の大文字小文字を変換するかどうかを示す値 */
    protected SqlIdentifierCaseType sqlIdentifierCaseType = SqlIdentifierCaseType.ORIGINALCASE;

    /** データをダンプする場合{@code true}、しない場合{@code false} */
    protected boolean dump = true;

    /** ダンプディレクトリ名 */
    protected String dumpDirName = "040-dump";

    /** ダンプファイルのエンコーディング */
    protected String dumpFileEncoding = "UTF-8";

    /** テーブルを作成するDDLのテンプレートファイル名 */
    protected String createTableTemplateFileName = "sql/create-table.ftl";

    /** 一意キーを作成するDDLのテンプレートファイル名 */
    protected String createUniqueKeyTemplateFileName = "sql/create-uniquekey.ftl";

    /** シーケンスを生成するDDLのテンプレートファイル名 */
    protected String createSequenceTemplateFileName = "sql/create-sequence.ftl";

    /** 外部キーを作成するDDLのテンプレートファイル名 */
    protected String createForeignKeyTemplateFileName = "sql/create-foreignkey.ftl";

    /** 補助的オブジェクトを生成するDDLのテンプレートファイル名 */
    protected String createAuxiliaryTemplateFileName = "sql/create-auxiliary.ftl";

    /** テーブルを削除するDDLのテンプレートファイル名 */
    protected String dropTableTemplateFileName = "sql/drop-table.ftl";

    /** 一意キーを削除するDDLのテンプレートファイル名 */
    protected String dropUniqueKeyTemplateFileName = "sql/drop-uniquekey.ftl";

    /** シーケンスを削除するDDLのテンプレートファイル名 */
    protected String dropSequenceTemplateFileName = "sql/drop-sequence.ftl";

    /** 外部キーを削除するDDLのテンプレートファイル名 */
    protected String dropForeignKeyTemplateFileName = "sql/drop-foreignkey.ftl";

    /** 補助的オブジェクトを削除するDDLのテンプレートファイル名 */
    protected String dropAuxiliaryTemplateFileName = "sql/drop-auxiliary.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** マイグレーションのディレクトリ */
    protected File migrateDir = new File("db", "migrate");

    /** DDLの情報ファイル */
    protected File ddlInfoFile = new File("db", "ddl-info.txt");

    /** バージョン番号のパターン */
    protected String versionNoPattern = "0000";

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** テーブルオプション */
    protected String tableOption = null;

    /** エンティティクラスのコメントをDDLに適用する場合@{true} */
    protected boolean applyJavaCommentToDdl = false;

    /** Javaファイルのソースディレクトリのリスト */
    protected List<File> javaFileSrcDirList = new ArrayList<File>();
    {
        File defaultSrcDir = new File(new File("src", "main"), "java");
        javaFileSrcDirList.add(defaultSrcDir);
    }

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 外部キーを自動生成する場合{@code true}、しない場合{@code false} */
    protected boolean autoGenerateForeignKey = true;

    /** DDLを生成する理由を示すコメント */
    protected String comment = "";

    /** {@link GenDialect}の実装クラス名 */
    protected String genDialectClassName = null;

    /** {@link GenDdlListener}の実装クラス名 */
    protected String genDdlListenerClassName = GenDdlListenerImpl.class
            .getName();

    /** トランザクション内で実行する場合{@code true}、そうでない場合{@code false} */
    protected boolean transactional = false;

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** テーブルモデルのファクトリ */
    protected TableModelFactory tableModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** DDLのバージョンを管理するディレクトリツリー */
    protected DdlVersionDirectoryTree ddlVersionDirectoryTree;

    /** バージョンディレクトリやファイルが生成されたイベントを受け取るためのリスナー */
    protected GenDdlListener genDdlListener;

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
     * シーケンスを生成するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを生成するDDLのテンプレートファイル名
     */
    public String getCreateSequenceTemplateFileName() {
        return createSequenceTemplateFileName;
    }

    /**
     * シーケンスを生成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを生成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        this.createSequenceTemplateFileName = createSequenceTemplateFileName;
    }

    /**
     * 補助的オブジェクトを生成するDDLのテンプレートファイル名を返します。
     * 
     * @return 補助的オブジェクトを生成するDDLのテンプレートファイル名
     */
    public String getCreateAuxiliaryTemplateFileName() {
        return createAuxiliaryTemplateFileName;
    }

    /**
     * 補助的オブジェクトを生成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createAuxiliaryTemplateFileName
     *            補助的オブジェクトを生成するDDLのテンプレートファイル名
     */
    public void setCreateAuxiliaryTemplateFileName(
            String createAuxiliaryTemplateFileName) {
        this.createAuxiliaryTemplateFileName = createAuxiliaryTemplateFileName;
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
     * 対象とするエンティティクラス名の正規表現を返します。
     * 
     * @return 対象とするエンティティクラス名の正規表現
     */
    public String getEntityClassNamePattern() {
        return entityClassNamePattern;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        this.entityClassNamePattern = entityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を返します。
     * 
     * @return 対象としないエンティティクラス名の正規表現
     */
    public String getIgnoreEntityClassNamePattern() {
        return ignoreEntityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        this.ignoreEntityClassNamePattern = ignoreEntityClassNamePattern;
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
     * DDL情報ファイルを返します。
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
     * エンティティクラスのコメントをDDLに適用する場合@{true}を返します。
     * 
     * @return エンティティクラスのコメントをDDLに適用する場合@{true}
     */
    public boolean isApplyJavaCommentToDdl() {
        return applyJavaCommentToDdl;
    }

    /**
     * エンティティクラスのコメントをDDLに適用する場合@{true}を設定します。
     * 
     * @param applyJavaCommentToDdl
     *            エンティティクラスのコメントをDDLに適用する場合@{true}
     */
    public void setApplyJavaCommentToDdl(boolean applyJavaCommentToDdl) {
        this.applyJavaCommentToDdl = applyJavaCommentToDdl;
    }

    /**
     * Javaファイルのソースディレクトリのリストを返します。
     * 
     * @return Javaファイルのソースディレクトリのリスト
     */
    public List<File> getJavaFileSrcDirList() {
        return javaFileSrcDirList;
    }

    /**
     * Javaファイルのソースディレクトリのリストを設定します。
     * 
     * @param javaFileSrcDirList
     *            Javaファイルのソースディレクトリのリスト
     */
    public void setJavaFileSrcDirList(List<File> javaFileSrcDirList) {
        this.javaFileSrcDirList = javaFileSrcDirList;
    }

    /**
     * Javaファイルのエンコーディングを返します。
     * 
     * @return Javaファイルのエンコーディング
     */
    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    /**
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
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

    /**
     * SQLのキーワードの大文字小文字を変換するかどうかを示す値を返します。
     * 
     * @return SQLのキーワードの大文字小文字を変換するかどうかを示す値
     */
    public SqlKeywordCaseType getSqlKeywordCaseType() {
        return sqlKeywordCaseType;
    }

    /**
     * SQLのキーワードの大文字小文字を変換するかどうかを示す値を設定します。
     * 
     * @param sqlKeywordCaseType
     *            SQLのキーワードの大文字小文字を変換するかどうかを示す値
     */
    public void setSqlKeywordCaseType(SqlKeywordCaseType sqlKeywordCaseType) {
        this.sqlKeywordCaseType = sqlKeywordCaseType;
    }

    /**
     * SQLの識別子の大文字小文字を変換するかどうかを示す値を返します。
     * 
     * @return SQLの識別子の大文字小文字を変換するかどうかを示す値
     */
    public SqlIdentifierCaseType getSqlIdentifierCaseType() {
        return sqlIdentifierCaseType;
    }

    /**
     * SQLの識別子の大文字小文字を変換するかどうかを示す値を設定します。
     * 
     * @param sqlIdentifierCaseType
     *            SQLの識別子の大文字小文字を変換するかどうかを示す値
     */
    public void setSqlIdentifierCaseType(
            SqlIdentifierCaseType sqlIdentifierCaseType) {
        this.sqlIdentifierCaseType = sqlIdentifierCaseType;
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

    /**
     * テーブルを作成するDDLを格納するディレクトリ名を返します。
     * 
     * @return テーブルを作成するDDLを格納するディレクトリ名
     */
    public String getCreateTableDirName() {
        return createTableDirName;
    }

    /**
     * テーブルを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createTableDirName
     *            テーブルを作成するDDLを格納するディレクトリ名
     */
    public void setCreateTableDirName(String createTableDirName) {
        this.createTableDirName = createTableDirName;
    }

    /**
     * 一意キーを作成するDDLを格納するディレクトリ名を返します。
     * 
     * @return 一意キーを作成するDDLを格納するディレクトリ名
     */
    public String getCreateUniqueKeyDirName() {
        return createUniqueKeyDirName;
    }

    /**
     * 一意キーを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createUniqueKeyDirName
     *            一意キーを作成するDDLを格納するディレクトリ名
     */
    public void setCreateUniqueKeyDirName(String createUniqueKeyDirName) {
        this.createUniqueKeyDirName = createUniqueKeyDirName;
    }

    /**
     * シーケンスを作成するDDLを格納するディレクトリ名を返します。
     * 
     * @return シーケンスを作成するDDLを格納するディレクトリ名
     */
    public String getCreateSequenceDirName() {
        return createSequenceDirName;
    }

    /**
     * シーケンスを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createSequenceDirName
     *            シーケンスを作成するDDLを格納するディレクトリ名
     */
    public void setCreateSequenceDirName(String createSequenceDirName) {
        this.createSequenceDirName = createSequenceDirName;
    }

    /**
     * 外部キーを作成するDDLを格納するディレクトリ名を返します。
     * 
     * @return 外部キーを作成するDDLを格納するディレクトリ名
     */
    public String getCreateForeignKeyDirName() {
        return createForeignKeyDirName;
    }

    /**
     * 外部キーを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createForeignKeyDirName
     *            外部キーを作成するDDLを格納するディレクトリ名
     */
    public void setCreateForeignKeyDirName(String createForeignKeyDirName) {
        this.createForeignKeyDirName = createForeignKeyDirName;
    }

    /**
     * テーブルを削除するDDLを格納するディレクトリ名を返します。
     * 
     * @return テーブルを削除するDDLを格納するディレクトリ名
     */
    public String getDropTableDirName() {
        return dropTableDirName;
    }

    /**
     * テーブルを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropTableDirName
     *            テーブルを削除するDDLを格納するディレクトリ名
     */
    public void setDropTableDirName(String dropTableDirName) {
        this.dropTableDirName = dropTableDirName;
    }

    /**
     * 一意キーを削除するDDLを格納するディレクトリ名を返します。
     * 
     * @return 一意キーを削除するDDLを格納するディレクトリ名
     */
    public String getDropUniqueKeyDirName() {
        return dropUniqueKeyDirName;
    }

    /**
     * 一意キーを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropUniqueKeyDirName
     *            一意キーを削除するDDLを格納するディレクトリ名
     */
    public void setDropUniqueKeyDirName(String dropUniqueKeyDirName) {
        this.dropUniqueKeyDirName = dropUniqueKeyDirName;
    }

    /**
     * シーケンスを作成するDDLを削除するディレクトリ名を返します。
     * 
     * @return シーケンスを作成するDDLを削除するディレクトリ名
     */
    public String getDropSequenceDirName() {
        return dropSequenceDirName;
    }

    /**
     * シーケンスを作成するDDLを削除するディレクトリ名を設定します。
     * 
     * @param dropSequenceDirName
     *            シーケンスを作成するDDLを削除するディレクトリ名
     */
    public void setDropSequenceDirName(String dropSequenceDirName) {
        this.dropSequenceDirName = dropSequenceDirName;
    }

    /**
     * 外部キーを作成するDDLを削除するディレクトリ名を返します。
     * 
     * @return 外部キーを作成するDDLを削除するディレクトリ名
     */
    public String getDropForeignKeyDirName() {
        return dropForeignKeyDirName;
    }

    /**
     * 外部キーを作成するDDLを削除するディレクトリ名を設定します。
     * 
     * @param dropForeignKeyDirName
     *            外部キーを作成するDDLを削除するディレクトリ名
     */
    public void setDropForeignKeyDirName(String dropForeignKeyDirName) {
        this.dropForeignKeyDirName = dropForeignKeyDirName;
    }

    /**
     * {@link GenDdlListener}の実装クラス名を返します。
     * 
     * @return {@link GenDdlListener}の実装クラス名
     */
    public String getGenDdlListenerClassName() {
        return genDdlListenerClassName;
    }

    /**
     * {@link GenDdlListener}の実装クラス名を設定します。
     * 
     * @param genDdlListenerClassName
     *            {@link GenDdlListener}の実装クラス名
     */
    public void setGenDdlListenerClassName(String genDdlListenerClassName) {
        this.genDdlListenerClassName = genDdlListenerClassName;
    }

    /**
     * DDLを生成する理由を示すコメントを返します。
     * 
     * @return DDLを生成する理由を示すコメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * DDLを生成する理由を示すコメントを設定します。
     * 
     * @param comment
     *            DDLを生成する理由を示すコメント
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 外部キーを自動生成する場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 外部キーを自動生成する場合{@code true}、しない場合{@code false}
     */
    public boolean isAutoGenerateForeignKey() {
        return autoGenerateForeignKey;
    }

    /**
     * 外部キーを自動生成する場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param autoGenerateForeignKey
     *            外部キーを自動生成する場合{@code true}、しない場合{@code false}
     */
    public void setAutoGenerateForeignKey(boolean autoGenerateForeignKey) {
        this.autoGenerateForeignKey = autoGenerateForeignKey;
    }

    /**
     * 補助的オブジェクトを作成するDDLを格納するディレクトリ名を返します。
     * 
     * @return 補助的オブジェクトを作成するDDLを格納するディレクトリ名
     */
    public String getCreateAuxiliaryDirName() {
        return createAuxiliaryDirName;
    }

    /**
     * 補助的オブジェクトを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createAuxiliaryDirName
     *            補助的オブジェクトを作成するDDLを格納するディレクトリ名
     */
    public void setCreateAuxiliaryDirName(String createAuxiliaryDirName) {
        this.createAuxiliaryDirName = createAuxiliaryDirName;
    }

    /**
     * 補助的オブジェクトを削除するDDLを格納するディレクトリ名を返します。
     * 
     * @return 補助的オブジェクトを削除するDDLを格納するディレクトリ名
     */
    public String getDropAuxiliaryDirName() {
        return dropAuxiliaryDirName;
    }

    /**
     * 補助的オブジェクトを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropAuxiliaryDirName
     *            補助的オブジェクトを削除するDDLを格納するディレクトリ名
     */
    public void setDropAuxiliaryDirName(String dropAuxiliaryDirName) {
        this.dropAuxiliaryDirName = dropAuxiliaryDirName;
    }

    /**
     * 補助的オブジェクトを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 補助的オブジェクトを削除するDDLのテンプレートファイル名
     */
    public String getDropAuxiliaryTemplateFileName() {
        return dropAuxiliaryTemplateFileName;
    }

    /**
     * 補助的オブジェクトを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropAuxiliaryTemplateFileName
     *            補助的オブジェクトを削除するDDLのテンプレートファイル名
     */
    public void setDropAuxiliaryTemplateFileName(
            String dropAuxiliaryTemplateFileName) {
        this.dropAuxiliaryTemplateFileName = dropAuxiliaryTemplateFileName;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public boolean isTransactional() {
        return transactional;
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    @Override
    protected void doInit() {
        dialect = getGenDialect(genDialectClassName);
        genDdlListener = ReflectUtil.newInstance(GenDdlListener.class,
                genDdlListenerClassName);
        if (transactional) {
            userTransaction = SingletonS2Container
                    .getComponent(UserTransaction.class);
        }
        valueTypeProvider = createValueTypeProvider();
        ddlVersionDirectoryTree = createDdlVersionDirectoryTree();
        ddlVersionIncrementer = createDdlVersionIncrementer();
        tableModelFactory = createTableModelFactory();
        generator = createGenerator();
        entityMetaReader = createEntityMetaReader();
        databaseDescFactory = createDatabaseDescFactory();
        sqlUnitExecutor = createSqlUnitExecutor();
        dumper = createDumper();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() throws Throwable {
        ddlVersionIncrementer.increment(comment,
                new DdlVersionIncrementerCallback());
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * DDLを生成します。
     * 
     * @param model
     *            DDLのモデル
     * @param dir
     *            生成するファイルの出力先ディレクトリ
     * @param templateName
     *            テンプレートファイルの名前
     */
    protected void generateDdl(DdlModel model, ManagedFile dir,
            String templateName) {
        GenerationContext context = createGenerationContext(model, dir,
                templateName);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            DDLのモデル
     * @param dir
     *            生成するファイルの出力先ディレクトリ
     * @param fileName
     *            ファイルの名前
     * @param templateName
     *            テンプレートファイルの名前
     * @return {@link GenerationContext}の実装
     */
    protected GenerationContext createGenerationContext(DdlModel model,
            ManagedFile dir, String templateName) {
        String fileName = model.getCanonicalTableName() + ".sql";
        ManagedFile file = dir.createChild(fileName);
        file.createNewFile();
        return factory.createGenerationContext(this, model, file.asFile(),
                templateName, ddlFileEncoding, true);
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return factory.createEntityMetaReader(this, classpathDir, ClassUtil
                .concatName(rootPackageName, entityPackageName), jdbcManager
                .getEntityMetaFactory(), entityClassNamePattern,
                ignoreEntityClassNamePattern, applyJavaCommentToDdl,
                javaFileSrcDirList, javaFileEncoding);
    }

    /**
     * {@link DatabaseDescFactory}の実装を作成します。
     * 
     * @return {@link DatabaseDescFactory}の実装
     */
    protected DatabaseDescFactory createDatabaseDescFactory() {
        return factory.createDatabaseDescFactory(this, jdbcManager
                .getEntityMetaFactory(), entityMetaReader, dialect,
                valueTypeProvider, autoGenerateForeignKey);
    }

    /**
     * {@link DdlVersionDirectoryTree}の実装を作成します。
     * 
     * @return {@link DdlVersionDirectoryTree}の実装
     */
    protected DdlVersionDirectoryTree createDdlVersionDirectoryTree() {
        return factory.createDdlVersionDirectoryTree(this, migrateDir,
                ddlInfoFile, versionNoPattern, env, false);
    }

    /**
     * {@link DdlVersionIncrementer}の実装を作成します。
     * 
     * @return {@link DdlVersionIncrementer}の実装
     */
    protected DdlVersionIncrementer createDdlVersionIncrementer() {
        List<String> createDirNameList = Arrays.asList(createTableDirName,
                createUniqueKeyDirName, createSequenceDirName,
                createForeignKeyDirName, dumpDirName, createAuxiliaryDirName);
        List<String> dropDirNameList = Arrays.asList(dropTableDirName,
                dropUniqueKeyDirName, dropSequenceDirName,
                dropForeignKeyDirName, dropAuxiliaryDirName);
        return factory.createDdlVersionIncrementer(this,
                ddlVersionDirectoryTree, genDdlListener, dialect, jdbcManager
                        .getDataSource(), createDirNameList, dropDirNameList);
    }

    /**
     * {@link TableModelFactory}の実装を作成します。
     * 
     * @return {@link TableModelFactory}の実装
     */
    protected TableModelFactory createTableModelFactory() {
        return factory.createTableModelFactory(this, dialect, jdbcManager
                .getDataSource(), sqlIdentifierCaseType, sqlKeywordCaseType,
                statementDelimiter, tableOption, applyJavaCommentToDdl);
    }

    /**
     * {@link Dumper}の実装を作成します。
     * 
     * @return {@link Dumper}の実装
     */
    protected Dumper createDumper() {
        return factory.createDumper(this, dialect, dumpFileEncoding);
    }

    /**
     * {@link SqlUnitExecutor}の実装を返します。
     * 
     * @return {@link SqlUnitExecutor}の実装
     */
    protected SqlUnitExecutor createSqlUnitExecutor() {
        return factory.createSqlUnitExecutor(this, jdbcManager.getDataSource(),
                userTransaction, true);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return factory.createGenerator(this, templateFileEncoding,
                templateFilePrimaryDir);
    }

    /**
     * {@link ValueTypeProvider}の実装を作成します。
     * 
     * @return {@link ValueTypeProvider}の実装
     */
    protected ValueTypeProvider createValueTypeProvider() {
        return factory.createValueTypeProvider(this, jdbcManager.getDialect());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * {@link DdlVersionIncrementer.Callback}の実装クラスです。
     * 
     * @author taedium
     */
    protected class DdlVersionIncrementerCallback implements
            DdlVersionIncrementer.Callback {

        /** ディレクトリ名をキー、createディレクトリの子ディレクトリを値とするマップ */
        protected Map<String, ManagedFile> createDirChildMap = new HashMap<String, ManagedFile>(
                5);

        /** ディレクトリ名をキー、dropディレクトリの子ディレクトリを値とするマップ */
        protected Map<String, ManagedFile> dropDirChildMap = new HashMap<String, ManagedFile>(
                5);

        /** createディレクトリ */
        protected ManagedFile createDir;

        /** dropディレクトリ */
        protected ManagedFile dropDir;

        public void execute(DdlVersionDirectory versionDirectory) {
            createDir = versionDirectory.getCreateDirectory();
            dropDir = versionDirectory.getDropDirectory();
            final DatabaseDesc databaseDesc = databaseDescFactory
                    .getDatabaseDesc();

            for (TableDesc tableDesc : databaseDesc.getTableDescList()) {
                TableModel model = tableModelFactory.getTableModel(tableDesc);
                generateTableDdl(model);
                generateUniqueKeyDdl(model);
                generateForeignKeyDdl(model);
                generateSequenceDdl(model);
                generateAuxiliaryDdl(model);
            }

            if (dump) {
                sqlUnitExecutor.execute(new SqlUnitExecutor.Callback() {

                    public void execute(SqlExecutionContext context) {
                        dumper.dump(context, databaseDesc, new File(createDir
                                .asFile(), dumpDirName));
                    }
                });
            }

            clear();
        }

        /**
         * テーブルのDDLを生成します。
         * 
         * @param model
         *            テーブルモデル
         */
        protected void generateTableDdl(TableModel model) {
            generateDdl(model, getCreateDirChild(createTableDirName),
                    createTableTemplateFileName);
            generateDdl(model, getDropDirChild(dropTableDirName),
                    dropTableTemplateFileName);
        }

        /**
         * 一意キーのDDLを生成します。
         * 
         * @param model
         *            テーブルモデル
         */
        protected void generateUniqueKeyDdl(TableModel model) {
            if (model.getUniqueKeyModelList().isEmpty()) {
                return;
            }
            generateDdl(model, getCreateDirChild(createUniqueKeyDirName),
                    createUniqueKeyTemplateFileName);
            generateDdl(model, getDropDirChild(dropUniqueKeyDirName),
                    dropUniqueKeyTemplateFileName);
        }

        /**
         * 外部キーのDDLを生成します。
         * 
         * @param model
         *            テーブルモデル
         */
        protected void generateForeignKeyDdl(TableModel model) {
            if (model.getForeignKeyModelList().isEmpty()) {
                return;
            }
            generateDdl(model, getCreateDirChild(createForeignKeyDirName),
                    createForeignKeyTemplateFileName);
            generateDdl(model, getDropDirChild(dropForeignKeyDirName),
                    dropForeignKeyTemplateFileName);
        }

        /**
         * シーケンスのDDLを生成します。
         * 
         * @param model
         *            テーブルモデル
         */
        protected void generateSequenceDdl(TableModel model) {
            if (model.getSequenceModelList().isEmpty()) {
                return;
            }
            generateDdl(model, getCreateDirChild(createSequenceDirName),
                    createSequenceTemplateFileName);
            generateDdl(model, getDropDirChild(dropSequenceDirName),
                    dropSequenceTemplateFileName);
        }

        /**
         * 補助的オブジェクトのDDLを生成します。
         * 
         * @param model
         *            テーブルモデル
         */
        protected void generateAuxiliaryDdl(TableModel model) {
            generateDdl(model, getCreateDirChild(createAuxiliaryDirName),
                    createAuxiliaryTemplateFileName);
            generateDdl(model, getDropDirChild(dropAuxiliaryDirName),
                    dropAuxiliaryTemplateFileName);
        }

        /**
         * createディレクトリの子ディレクトリを返します。
         * 
         * @param childName
         *            子ディレクトリの名前
         * @return createディレクトリの子ディレクトリ
         */
        protected ManagedFile getCreateDirChild(String childName) {
            ManagedFile child = createDirChildMap.get(childName);
            if (child != null) {
                return child;
            }
            child = createDir.createChild(childName);
            createDirChildMap.put(childName, child);
            return child;
        }

        /**
         * dropディレクトリの子ディレクトリを返します。
         * 
         * @param childName
         *            子ディレクトリの名前
         * @return dropディレクトリの子ディレクトリ
         */
        protected ManagedFile getDropDirChild(String childName) {
            ManagedFile child = dropDirChildMap.get(childName);
            if (child != null) {
                return child;
            }
            child = dropDir.createChild(childName);
            dropDirChildMap.put(childName, child);
            return child;
        }

        /**
         * クリア処理をします。
         */
        protected void clear() {
            deleteDirIfEmpty(createDirChildMap.values());
            deleteDirIfEmpty(dropDirChildMap.values());
            createDirChildMap.clear();
            dropDirChildMap.clear();
        }

        /**
         * ディレクトリが空の場合に削除します。
         * 
         * @param dirs
         *            ディレクトリのコレクション
         */
        protected void deleteDirIfEmpty(Collection<ManagedFile> dirs) {
            for (ManagedFile dir : dirs) {
                if (!dir.hasChild()) {
                    dir.delete();
                }
            }
        }

    }
}

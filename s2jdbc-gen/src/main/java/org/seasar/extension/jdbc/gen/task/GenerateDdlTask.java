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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;
import java.util.StringTokenizer;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.internal.command.GenerateDdlCommand;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;

/**
 * DDLのSQLファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateDdlCommand
 */
public class GenerateDdlTask extends AbstractTask {

    /** コマンド */
    protected GenerateDdlCommand command = new GenerateDdlCommand();

    @Override
    protected Command getCommand() {
        return command;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * {@link Factory}の実装クラス名を設定します。
     * 
     * @param factoryClassName
     *            {@link Factory}の実装クラス名
     */
    public void setFactoryClassName(String factoryClassName) {
        command.setFactoryClassName(factoryClassName);
    }

    /**
     * SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型を設定します。
     * 
     * @param sqlKeywordCase
     *            SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型
     */
    public void setSqlKeywordCase(SqlKeywordCase sqlKeywordCase) {
        String value = sqlKeywordCase.getValue().toUpperCase();
        command.setSqlKeywordCaseType(SqlKeywordCaseType.valueOf(value));
    }

    /**
     * SQLの識別子の大文字小文字を変換するかどうかを示す列挙型を設定します。
     * 
     * @param sqlIdentifierCase
     *            SQLの識別子の大文字小文字を変換するかどうかを示す列挙型
     */
    public void setSqlIdentifierCase(SqlIdentifierCase sqlIdentifierCase) {
        String value = sqlIdentifierCase.getValue().toUpperCase();
        command.setSqlIdentifierCaseType(SqlIdentifierCaseType.valueOf(value));
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createForeignKeyTemplateFileName
     *            外部キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateForeignKeyTemplateFileName(
            String createForeignKeyTemplateFileName) {
        command
                .setCreateForeignKeyTemplateFileName(createForeignKeyTemplateFileName);
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createUniqueKeyTemplateFileName
     *            一意キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateUniqueKeyTemplateFileName(
            String createUniqueKeyTemplateFileName) {
        command
                .setCreateUniqueKeyTemplateFileName(createUniqueKeyTemplateFileName);
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropForeignKeyTemplateFileName
     *            外部キーを削除するDDLのテンプレートファイル名
     */
    public void setDropForeignKeyTemplateFileName(
            String dropForeignKeyTemplateFileName) {
        command
                .setDropForeignKeyTemplateFileName(dropForeignKeyTemplateFileName);
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropUniqueKeyTemplateFileName
     *            一意キーを削除するDDLのテンプレートファイル名
     */
    public void setDropUniqueKeyTemplateFileName(
            String dropUniqueKeyTemplateFileName) {
        command.setDropUniqueKeyTemplateFileName(dropUniqueKeyTemplateFileName);
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを作成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        command
                .setCreateSequenceTemplateFileName(createSequenceTemplateFileName);
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createTableTemplateFileName
     *            テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        command.setCreateTableTemplateFileName(createTableTemplateFileName);
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSequenceTemplateFileName
     *            シーケンスを削除するDDLのテンプレートファイル名
     */
    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        command.setDropSequenceTemplateFileName(dropSequenceTemplateFileName);
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropTableTemplateFileName
     *            テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        command.setDropTableTemplateFileName(dropTableTemplateFileName);
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    /**
     * マイグレーションのディレクトリを設定します。
     * 
     * @param migrateDir
     *            マイグレーションのディレクトリ
     */
    public void setMigrateDir(File migrateDir) {
        command.setMigrateDir(migrateDir);
    }

    /**
     * DDLファイルのエンコーディングを設定します。
     * 
     * @param ddlFileEncoding
     *            DDLファイルのエンコーディング
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        command.setDdlFileEncoding(ddlFileEncoding);
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlVersionFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        command.setEntityClassNamePattern(entityClassNamePattern);
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
    }

    /**
     * ダンプディレクトリ名を設定します。
     * 
     * @param dumpDirName
     *            ダンプディレクトリ名
     */
    public void setDumpDirName(String dumpDirName) {
        command.setDumpDirName(dumpDirName);
    }

    /**
     * テーブルオプションを設定します。
     * 
     * @param tableOption
     *            テーブルオプション
     */
    public void setTableOption(String tableOption) {
        command.setTableOption(tableOption);
    }

    /**
     * バージョン番号のパターンを設定します。
     * 
     * @param versionNoPattern
     *            バージョン番号のパターン
     */
    public void setVersionNoPattern(String versionNoPattern) {
        command.setVersionNoPattern(versionNoPattern);
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
    }

    /**
     * ダンプファイルのエンコーディングを設定します。
     * 
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * データをダンプする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param dump
     *            データをダンプする場合{@code true}、しない場合{@code false}
     */
    public void setDump(boolean dump) {
        command.setDump(dump);
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        command.setGenDialectClassName(genDialectClassName);
    }

    /**
     * 外部キーを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createForeignKeyDirName
     *            The createForeignKeyDirName to set.
     */
    public void setCreateForeignKeyDirName(String createForeignKeyDirName) {
        command.setCreateForeignKeyDirName(createForeignKeyDirName);
    }

    /**
     * シーケンスを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createSequenceDirName
     *            シーケンスを作成するDDLを格納するディレクトリ名
     */
    public void setCreateSequenceDirName(String createSequenceDirName) {
        command.setCreateSequenceDirName(createSequenceDirName);
    }

    /**
     * テーブルを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createTableDirName
     *            テーブルを作成するDDLを格納するディレクトリ名
     */
    public void setCreateTableDirName(String createTableDirName) {
        command.setCreateTableDirName(createTableDirName);
    }

    /**
     * 一意キーを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createUniqueKeyDirName
     *            一意キーを作成するDDLを格納するディレクトリ名
     */
    public void setCreateUniqueKeyDirName(String createUniqueKeyDirName) {
        command.setCreateUniqueKeyDirName(createUniqueKeyDirName);
    }

    /**
     * 外部キーを作成するDDLを削除するディレクトリ名を設定します。
     * 
     * @param dropForeignKeyDirName
     *            外部キーを作成するDDLを削除するディレクトリ名
     */
    public void setDropForeignKeyDirName(String dropForeignKeyDirName) {
        command.setDropForeignKeyDirName(dropForeignKeyDirName);
    }

    /**
     * シーケンスを作成するDDLを削除するディレクトリ名を設定します。
     * 
     * @param dropSequenceDirName
     *            シーケンスを作成するDDLを削除するディレクトリ名
     */
    public void setDropSequenceDirName(String dropSequenceDirName) {
        command.setDropSequenceDirName(dropSequenceDirName);
    }

    /**
     * テーブルを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropTableDirName
     *            テーブルを削除するDDLを格納するディレクトリ名
     */
    public void setDropTableDirName(String dropTableDirName) {
        command.setDropTableDirName(dropTableDirName);
    }

    /**
     * 一意キーを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropUniqueKeyDirName
     *            一意キーを削除するDDLを格納するディレクトリ名
     */
    public void setDropUniqueKeyDirName(String dropUniqueKeyDirName) {
        command.setDropUniqueKeyDirName(dropUniqueKeyDirName);
    }

    /**
     * {@link GenDdlListener}の実装クラス名を設定します。
     * 
     * @param genDdlListenerClassName
     *            {@link GenDdlListener}の実装クラス名
     */
    public void setGenDdlListenerClassName(String genDdlListenerClassName) {
        command.setGenDdlListenerClassName(genDdlListenerClassName);
    }

    /**
     * エンティティクラスのコメントをDDLに適用する場合@{true}を設定します。
     * 
     * @param applyJavaCommentToDdl
     *            エンティティクラスのコメントをDDLに適用する場合@{true}
     */
    public void setApplyJavaCommentToDdl(boolean applyJavaCommentToDdl) {
        command.setApplyJavaCommentToDdl(applyJavaCommentToDdl);
    }

    /**
     * Javaファイルのソースディレクトリをカンマまたは空白で区切って設定します。
     * 
     * @param javaFileSrcDirs
     *            複数のJavaファイルのソースディレクトリ
     */
    public void setJavaFileSrcDirs(String javaFileSrcDirs) {
        command.getJavaFileSrcDirList().clear();
        if (javaFileSrcDirs != null && javaFileSrcDirs.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(javaFileSrcDirs,
                    ", \t\n\r\f", false);
            while (tokenizer.hasMoreTokens()) {
                File dir = new File(getProject().getBaseDir(), tokenizer
                        .nextToken());
                command.getJavaFileSrcDirList().add(dir);
            }
        }
    }

    /**
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    /**
     * DDLを生成する理由を示すコメントを設定します。
     * 
     * @param comment
     *            DDLを生成する理由を示すコメント
     */
    public void setComment(String comment) {
        command.setComment(comment);
    }

    /**
     * 外部キーを自動生成する場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param autoGenerateForeignKey
     *            外部キーを自動生成する場合{@code true}、しない場合{@code false}
     */
    public void setAutoGenerateForeignKey(boolean autoGenerateForeignKey) {
        command.setAutoGenerateForeignKey(autoGenerateForeignKey);
    }

    /**
     * 補助的オブジェクトを作成するDDLを格納するディレクトリ名を設定します。
     * 
     * @param createAuxiliaryDirName
     *            補助的オブジェクトを作成するDDLを格納するディレクトリ名
     */
    public void setCreateAuxiliaryDirName(String createAuxiliaryDirName) {
        command.setCreateAuxiliaryDirName(createAuxiliaryDirName);
    }

    /**
     * 補助的オブジェクトを生成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createAuxiliaryTemplateFileName
     *            補助的オブジェクトを生成するDDLのテンプレートファイル名
     */
    public void setCreateAuxiliaryTemplateFileName(
            String createAuxiliaryTemplateFileName) {
        command
                .setCreateAuxiliaryTemplateFileName(createAuxiliaryTemplateFileName);
    }

    /**
     * 補助的オブジェクトを削除するDDLを格納するディレクトリ名を設定します。
     * 
     * @param dropAuxiliaryDirName
     *            補助的オブジェクトを削除するDDLを格納するディレクトリ名
     */
    public void setDropAuxiliaryDirName(String dropAuxiliaryDirName) {
        command.setDropAuxiliaryDirName(dropAuxiliaryDirName);
    }

    /**
     * 補助的オブジェクトを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropAuxiliaryTemplateFileName
     *            補助的オブジェクトを削除するDDLのテンプレートファイル名
     */
    public void setDropAuxiliaryTemplateFileName(
            String dropAuxiliaryTemplateFileName) {
        command.setDropAuxiliaryTemplateFileName(dropAuxiliaryTemplateFileName);
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        command.setTransactional(transactional);
    }

}

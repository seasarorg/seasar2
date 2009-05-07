/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.internal.command.GenerateDdlCommand;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;

/**
 * DDLのSQLファイルを生成するゴールです。
 * 
 * @author hakoda-te-kun
 * @see GenerateDdlCommand
 *
 * @goal gen-ddl-task
 */
public class GenerateDdlTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected GenerateDdlCommand command = new GenerateDdlCommand();

	/**
	 * 設定ファイルのパスを設定します。
	 * 
	 * @parameter
	 */
	private String configPath;

	/**
	 * 環境名を設定します。
	 * 
	 * @parameter
	 */
	private String env;

	/**
	 * {@link JdbcManager}のコンポーネント名を設定します。
	 * 
	 * @parameter
	 */
	private String jdbcManagerName;

	/**
	 * {@link Factory}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String factoryClassName;

	/**
	 * SQLのキーワードの大文字小文字を変換するかどうかを示す列挙型を設定します。
	 * 
	 * @parameter
	 */
	private String sqlKeywordCase;

	/**
	 * SQLの識別子の大文字小文字を変換するかどうかを示す列挙型を設定します。
	 * 
	 * @parameter
	 */
	private String sqlIdentifierCase;

	/**
	 * クラスパスのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File classpathDir;

	/**
	 * 外部キーを作成するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String createForeignKeyTemplateFileName;

	/**
	 * 一意キーを作成するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String createUniqueKeyTemplateFileName;

	/**
	 * 外部キーを削除するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String dropForeignKeyTemplateFileName;

	/**
	 * 一意キーを削除するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String dropUniqueKeyTemplateFileName;

	/**
	 * シーケンスを作成するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String createSequenceTemplateFileName;

	/**
	 * テーブルを作成するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String createTableTemplateFileName;

	/**
	 * シーケンスを削除するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String dropSequenceTemplateFileName;

	/**
	 * テーブルを削除するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String dropTableTemplateFileName;

	/**
	 * エンティティクラスのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String entityPackageName;

	/**
	 * ルートパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String rootPackageName;

	/**
	 * マイグレーションのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File migrateDir;

	/**
	 * DDLファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String ddlFileEncoding;

	/**
	 * DDL情報ファイルを設定します。
	 * 
	 * @parameter
	 */
	private File ddlVersionFile;

	/**
	 * テンプレートファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String templateFileEncoding;

	/**
	 * SQLステートメントの区切り文字を設定します。
	 * 
	 * @parameter
	 */
	private Character statementDelimiter;

	/**
	 * テンプレートファイルを格納するプライマリディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File templateFilePrimaryDir;

	/**
	 * 対象とするエンティティクラス名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String entityClassNamePattern;

	/**
	 * 対象としないエンティティクラス名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String ignoreEntityClassNamePattern;

	/**
	 * ダンプディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dumpDirName;

	/**
	 * テーブルオプションを設定します。
	 * 
	 * @parameter
	 */
	private String tableOption;

	/**
	 * バージョン番号のパターンを設定します。
	 * 
	 * @parameter
	 */
	private String versionNoPattern;

	/**
	 * DDL情報ファイルを設定します。
	 * 
	 * @parameter
	 */
	private File ddlInfoFile;

	/**
	 * ダンプファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String dumpFileEncoding;

	/**
	 * データをダンプする場合{@code true}、しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean dump;

	/**
	 * {@link GenDialect}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String genDialectClassName;

	/**
	 * 外部キーを作成するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String createForeignKeyDirName;

	/**
	 * シーケンスを作成するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String createSequenceDirName;

	/**
	 * テーブルを作成するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String createTableDirName;

	/**
	 * 一意キーを作成するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String createUniqueKeyDirName;

	/**
	 * 外部キーを作成するDDLを削除するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dropForeignKeyDirName;

	/**
	 * シーケンスを作成するDDLを削除するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dropSequenceDirName;

	/**
	 * テーブルを削除するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dropTableDirName;

	/**
	 * 一意キーを削除するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dropUniqueKeyDirName;

	/**
	 * {@link GenDdlListener}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String genDdlListenerClassName;

	/**
	 * エンティティクラスのコメントをDDLに適用する場合@{true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean applyJavaCommentToDdl;

	/**
	 * Javaファイルのソースディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private Set<File> javaFileSrcDirs;

	/**
	 * Javaファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String javaFileEncoding;

	/**
	 * DDLを生成する理由を示すコメントを設定します。
	 * 
	 * @parameter
	 */
	private String comment;

	/**
	 * 外部キーを自動生成する場合{@code true}、しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean autoGenerateForeignKey;

	/**
	 * 補助的オブジェクトを作成するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String createAuxiliaryDirName;

	/**
	 * 補助的オブジェクトを生成するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String createAuxiliaryTemplateFileName;

	/**
	 * 補助的オブジェクトを削除するDDLを格納するディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dropAuxiliaryDirName;

	/**
	 * 補助的オブジェクトを削除するDDLのテンプレートファイル名を設定します。
	 * 
	 * @parameter
	 */
	private String dropAuxiliaryTemplateFileName;

	@Override
	protected Command getCommand() {
		return command;
	}

	@Override
	protected void doExecute() {
		if (configPath != null)
			command.setConfigPath(configPath);
		if (env != null)
			command.setEnv(env);
		if (jdbcManagerName != null)
			command.setJdbcManagerName(jdbcManagerName);
		if (factoryClassName != null)
			command.setFactoryClassName(factoryClassName);
		if (sqlKeywordCase != null) {
			final String value = sqlKeywordCase.toUpperCase();
			command.setSqlKeywordCaseType(SqlKeywordCaseType.valueOf(value));
		}
		if (sqlIdentifierCase != null) {
			final String value = sqlIdentifierCase.toUpperCase();
			command.setSqlIdentifierCaseType(SqlIdentifierCaseType.valueOf(value));
		}
		if (classpathDir != null)
			command.setClasspathDir(classpathDir);
		if (createForeignKeyTemplateFileName != null)
			command.setCreateForeignKeyTemplateFileName(createForeignKeyTemplateFileName);
		if (createUniqueKeyTemplateFileName != null)
			command.setCreateUniqueKeyTemplateFileName(createUniqueKeyTemplateFileName);
		if (dropForeignKeyTemplateFileName != null)
			command.setDropForeignKeyTemplateFileName(dropForeignKeyTemplateFileName);
		if (dropUniqueKeyTemplateFileName != null)
			command.setDropUniqueKeyTemplateFileName(dropUniqueKeyTemplateFileName);
		if (createSequenceTemplateFileName != null)
			command.setCreateSequenceTemplateFileName(createSequenceTemplateFileName);
		if (createTableTemplateFileName != null)
			command.setCreateTableTemplateFileName(createTableTemplateFileName);
		if (dropSequenceTemplateFileName != null)
			command.setDropSequenceTemplateFileName(dropSequenceTemplateFileName);
		if (dropTableTemplateFileName != null)
			command.setDropTableTemplateFileName(dropTableTemplateFileName);
		if (entityPackageName != null)
			command.setEntityPackageName(entityPackageName);
		if (rootPackageName != null)
			command.setRootPackageName(rootPackageName);
		if (migrateDir != null)
			command.setMigrateDir(migrateDir);
		if (ddlFileEncoding != null)
			command.setDdlFileEncoding(ddlFileEncoding);
		if (ddlVersionFile != null)
			command.setDdlInfoFile(ddlVersionFile); // 以前のバージョンとの互換性を保つため？
		if (templateFileEncoding != null)
			command.setTemplateFileEncoding(templateFileEncoding);
		if (statementDelimiter != null)
			command.setStatementDelimiter(statementDelimiter);
		if (templateFilePrimaryDir != null)
			command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
		if (entityClassNamePattern != null)
			command.setEntityClassNamePattern(entityClassNamePattern);
		if (ignoreEntityClassNamePattern != null)
			command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
		if (dumpDirName != null)
			command.setDumpDirName(dumpDirName);
		if (tableOption != null)
			command.setTableOption(tableOption);
		if (versionNoPattern != null)
			command.setVersionNoPattern(versionNoPattern);
		if (ddlInfoFile != null)
			command.setDdlInfoFile(ddlInfoFile);
		if (dumpFileEncoding != null)
			command.setDumpFileEncoding(dumpFileEncoding);
		if (dump != null)
			command.setDump(dump);
		if (genDialectClassName != null)
			command.setGenDialectClassName(genDialectClassName);
		if (createForeignKeyDirName != null)
			command.setCreateForeignKeyDirName(createForeignKeyDirName);
		if (createSequenceDirName != null)
			command.setCreateSequenceDirName(createSequenceDirName);
		if (createTableDirName != null)
			command.setCreateTableDirName(createTableDirName);
		if (createUniqueKeyDirName != null)
			command.setCreateUniqueKeyDirName(createUniqueKeyDirName);
		if (dropForeignKeyDirName != null)
			command.setDropForeignKeyDirName(dropForeignKeyDirName);
		if (dropSequenceDirName != null)
			command.setDropSequenceDirName(dropSequenceDirName);
		if (dropTableDirName != null)
			command.setDropTableDirName(dropTableDirName);
		if (dropUniqueKeyDirName != null)
			command.setDropUniqueKeyDirName(dropUniqueKeyDirName);
		if (genDdlListenerClassName != null)
			command.setGenDdlListenerClassName(genDdlListenerClassName);
		if (applyJavaCommentToDdl != null)
			command.setApplyJavaCommentToDdl(applyJavaCommentToDdl);
		if (javaFileSrcDirs != null) {
			command.getJavaFileSrcDirList().clear();
			command.getJavaFileSrcDirList().addAll(javaFileSrcDirs);
		}
		if (javaFileEncoding != null)
			command.setJavaFileEncoding(javaFileEncoding);
		if (comment != null)
			command.setComment(comment);
		if (autoGenerateForeignKey != null)
			command.setAutoGenerateForeignKey(autoGenerateForeignKey);
		if (createAuxiliaryDirName != null)
			command.setCreateAuxiliaryDirName(createAuxiliaryDirName);
		if (createAuxiliaryTemplateFileName != null)
			command.setCreateAuxiliaryTemplateFileName(createAuxiliaryTemplateFileName);
		if (dropAuxiliaryDirName != null)
			command.setDropAuxiliaryDirName(dropAuxiliaryDirName);
		if (dropAuxiliaryTemplateFileName != null)
			command.setDropAuxiliaryTemplateFileName(dropAuxiliaryTemplateFileName);
	}

	@Override
	protected List<File> getAdditionalClasspath() {
		final List<File> dirs = new ArrayList<File>();
		if (classpathDir != null)
			dirs.add(classpathDir);
		return dirs;
	}
}

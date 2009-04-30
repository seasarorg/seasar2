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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.MigrateCommand;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;

/**
 * @goal migrate-task
 */
public class MigrateTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected MigrateCommand command = new MigrateCommand();

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
	 * SQLブロックの区切り文字を設定します。
	 * 
	 * @parameter
	 */
	private String blockDelimiter;

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
	private File ddlInfoFile;

	/**
	 * エラー発生時に処理を中止する場合{@code true}、中止しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean haltOnError;

	/**
	 * マイグレーションのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File migrateDir;

	/**
	 * スキーマのバージョン番号を格納するカラム名を設定します。
	 * 
	 * @parameter
	 */
	private String schemaInfoColumnName;

	/**
	 * スキーマ情報を格納するテーブル名を設定します。
	 * 
	 * @parameter
	 */
	private String schemaInfoFullTableName;

	/**
	 * SQLステートメントの区切り文字を設定します。
	 * 
	 * @parameter
	 */
	private Character statementDelimiter;

	/**
	 * マイグレーション先のバージョンを設定します。
	 * 
	 * @parameter
	 */
	private String version;

	/**
	 * バージョン番号のパターンを設定します。
	 * 
	 * @parameter
	 */
	private String versionNoPattern;

	/**
	 * クラスパスのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File classpathDir;

	/**
	 * ダンプファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String dumpFileEncoding;

	/**
	 * 対象とするエンティティクラス名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String entityClassNamePattern;

	/**
	 * エンティティクラスのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String entityPackageName;

	/**
	 * 対象としないエンティティクラス名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String ignoreEntityClassNamePattern;

	/**
	 * ルートパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String rootPackageName;

	/**
	 * データをロードする際のバッチサイズを設定します。
	 * 
	 * @parameter
	 */
	private Integer loadBatchSize;

	/**
	 * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean transactional;

	/**
	 * {@link GenDialect}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String genDialectClassName;

	/**
	 * 環境名をバージョンに適用する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean applyEnvToVersion;

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
		if (blockDelimiter != null)
			command.setBlockDelimiter(blockDelimiter);
		if (ddlFileEncoding != null)
			command.setDdlFileEncoding(ddlFileEncoding);
		if (ddlInfoFile != null)
			command.setDdlInfoFile(ddlInfoFile);
		if (haltOnError != null)
			command.setHaltOnError(haltOnError);
		if (migrateDir != null)
			command.setMigrateDir(migrateDir);
		if (schemaInfoColumnName != null)
			command.setSchemaInfoColumnName(schemaInfoColumnName);
		if (schemaInfoFullTableName != null)
			command.setSchemaInfoFullTableName(schemaInfoFullTableName);
		if (statementDelimiter != null)
			command.setStatementDelimiter(statementDelimiter);
		if (version != null)
			command.setVersion(version);
		if (versionNoPattern != null)
			command.setVersionNoPattern(versionNoPattern);
		if (classpathDir != null)
			command.setClasspathDir(classpathDir);
		if (dumpFileEncoding != null)
			command.setDumpFileEncoding(dumpFileEncoding);
		if (entityClassNamePattern != null)
			command.setEntityClassNamePattern(entityClassNamePattern);
		if (entityPackageName != null)
			command.setEntityPackageName(entityPackageName);
		if (ignoreEntityClassNamePattern != null)
			command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
		if (rootPackageName != null)
			command.setRootPackageName(rootPackageName);
		if (loadBatchSize != null)
			command.setLoadBatchSize(loadBatchSize);
		if (transactional != null)
			command.setTransactional(transactional);
		if (genDialectClassName != null)
			command.setGenDialectClassName(genDialectClassName);
		if (applyEnvToVersion != null)
			command.setApplyEnvToVersion(applyEnvToVersion);
	}

	@Override
	protected List<File> getAdditionalClasspath() {
		final List<File> dirs = new ArrayList<File>();
		if (classpathDir != null)
			dirs.add(classpathDir);
		return dirs;
	}
}

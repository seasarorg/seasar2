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

import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.GenerateEntityCommand;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;

/**
 * @goal gen-entity-task
 */
public class GenerateEntityTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected final GenerateEntityCommand command = new GenerateEntityCommand();

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
	 * エンティティクラスのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String entityPackageName;

	/**
	 * エンティティクラスのテンプレート名を設定します。
	 * 
	 * @parameter
	 */
	private String entityTemplateFileName;

	/**
	 * 生成するJavaファイルの出力先ディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File javaFileDestDir;

	/**
	 * Javaファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String javaFileEncoding;

	/**
	 * 上書きをする場合{@code true}、しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean overwrite;

	/**
	 * ルートパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String rootPackageName;

	/**
	 * スキーマ名を設定します。
	 * 
	 * @parameter
	 */
	private String schemaName;

	/**
	 * Javaコード生成の対象とするテーブル名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String tableNamePattern;

	/**
	 * Javaコード生成の対象としないテーブル名の正規表現を設定します。
	 * 
	 * @parameter
	 */
	private String ignoreTableNamePattern;

	/**
	 * テンプレートファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String templateFileEncoding;

	/**
	 * テンプレートファイルを格納するプライマリディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File templateFilePrimaryDir;

	/**
	 * バージョンカラム名のパターンを設定します。
	 * 
	 * @parameter
	 */
	private String versionColumnNamePattern;

	/**
	 * 単語を複数系に変換するための辞書ファイルを設定します。
	 * 
	 * @parameter
	 */
	private File pluralFormFile;

	/**
	 * カタログ名を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showCatalogName;

	/**
	 * カラム定義を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showColumnDefinition;

	/**
	 * カラム名を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showColumnName;

	/**
	 * {@link JoinColumn}を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showJoinColumn;

	/**
	 * スキーマ名を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showSchemaName;

	/**
	 * テーブル名を表示する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean showTableName;

	/**
	 * {@link GenDialect}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String genDialectClassName;

	/**
	 * エンティティの識別子の生成方法を示す列挙型を設定します。
	 * 
	 * @parameter
	 */
	private String idGeneration; // IdGenarationで受け取ることはできないので、Stringで代用

	/**
	 * エンティティの識別子の初期値を設定します。
	 * 
	 * @parameter
	 */
	private Integer initialValue;

	/**
	 * エンティティの識別子の割り当てサイズを設定します。
	 * 
	 * @parameter
	 */
	private Integer allocationSize;

	/**
	 * エンティティのスーパークラスの名前を設定します。
	 * 
	 * @parameter
	 */
	private String entitySuperclassName;

	/**
	 * エンティティクラスでアクセサを使用する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean useAccessor;

	/**
	 * データベースのコメントをJavaコードに適用する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean applyDbCommentToJava;

	/**
	 * {@link TemporalType}を使用する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean useTemporalType;

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
		if (entityPackageName != null)
			command.setEntityPackageName(entityPackageName);
		if (entityTemplateFileName != null)
			command.setEntityTemplateFileName(entityTemplateFileName);
		if (javaFileDestDir != null)
			command.setJavaFileDestDir(javaFileDestDir);
		if (javaFileEncoding != null)
			command.setJavaFileEncoding(javaFileEncoding);
		if (overwrite != null)
			command.setOverwrite(overwrite);
		if (rootPackageName != null)
			command.setRootPackageName(rootPackageName);
		if (schemaName != null)
			command.setSchemaName(schemaName);
		if (tableNamePattern != null)
			command.setTableNamePattern(tableNamePattern);
		if (ignoreTableNamePattern != null)
			command.setIgnoreTableNamePattern(ignoreTableNamePattern);
		if (templateFileEncoding != null)
			command.setTemplateFileEncoding(templateFileEncoding);
		if (templateFilePrimaryDir != null)
			command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
		if (versionColumnNamePattern != null)
			command.setVersionColumnNamePattern(versionColumnNamePattern);
		if (pluralFormFile != null)
			command.setPluralFormFile(pluralFormFile);
		if (showCatalogName != null)
			command.setShowCatalogName(showCatalogName);
		if (showColumnDefinition != null)
			command.setShowColumnDefinition(showColumnDefinition);
		if (showColumnName != null)
			command.setShowColumnName(showColumnName);
		if (showJoinColumn != null)
			command.setShowJoinColumn(showJoinColumn);
		if (showSchemaName != null)
			command.setShowSchemaName(showSchemaName);
		if (showTableName != null)
			command.setShowTableName(showTableName);
		if (genDialectClassName != null)
			command.setGenDialectClassName(genDialectClassName);
		if (idGeneration != null) {
			final GenerationType generationType;
			if (!idGeneration.equals("assigned")) {
				generationType = GenerationType.valueOf(idGeneration.toUpperCase());
			} else {
				generationType = null;
			}
			command.setGenerationType(generationType);
		}
		if (initialValue != null)
			command.setInitialValue(initialValue);
		if (allocationSize != null)
			command.setAllocationSize(allocationSize);
		if (entitySuperclassName != null)
			command.setEntitySuperclassName(entitySuperclassName);
		if (useAccessor != null)
			command.setUseAccessor(useAccessor);
		if (applyDbCommentToJava != null)
			command.setApplyDbCommentToJava(applyDbCommentToJava);
		if (useTemporalType != null)
			command.setUseTemporalType(useTemporalType);
	}
}

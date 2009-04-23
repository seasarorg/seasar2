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
import org.seasar.extension.jdbc.gen.internal.command.GenerateNamesCommand;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;

/**
 * @goal gen-names-task
 */
public class GenerateNamesMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected GenerateNamesCommand command = new GenerateNamesCommand();

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
	 * クラスパスのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File classpathDir;

	/**
	 * エンティティクラスのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String entityPackageName;

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
	 * テンプレートファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String templateFileEncoding;

	/**
	 * テンプレートファイルを格納したプライマリディレクトリを設定します。
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
	 * 名前クラス名のサフィックスを設定します。
	 * 
	 * @parameter
	 */
	private String namesClassNameSuffix;

	/**
	 * 名前クラスのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String namesPackageName;

	/**
	 * 名前クラスのテンプレート名を設定します。
	 * 
	 * @parameter
	 */
	private String namesTemplateFileName;

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
		if (classpathDir != null)
			command.setClasspathDir(classpathDir);
		if (entityPackageName != null)
			command.setEntityPackageName(entityPackageName);
		if (javaFileDestDir != null)
			command.setJavaFileDestDir(javaFileDestDir);
		if (javaFileEncoding != null)
			command.setJavaFileEncoding(javaFileEncoding);
		if (overwrite != null)
			command.setOverwrite(overwrite);
		if (rootPackageName != null)
			command.setRootPackageName(rootPackageName);
		if (templateFileEncoding != null)
			command.setTemplateFileEncoding(templateFileEncoding);
		if (templateFilePrimaryDir != null)
			command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
		if (entityClassNamePattern != null)
			command.setEntityClassNamePattern(entityClassNamePattern);
		if (ignoreEntityClassNamePattern != null)
			command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
		if (namesClassNameSuffix != null)
			command.setNamesClassNameSuffix(namesClassNameSuffix);
		if (namesPackageName != null)
			command.setNamesPackageName(namesPackageName);
		if (namesTemplateFileName != null)
			command.setNamesTemplateFileName(namesTemplateFileName);
	}

	@Override
	protected List<File> getAdditionalClasspath() {
		final List<File> dirs = new ArrayList<File>();
		if (classpathDir != null)
			dirs.add(classpathDir);
		return dirs;
	}
}

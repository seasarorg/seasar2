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
import java.io.IOException;
import java.util.Set;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.command.GenerateSqlFileTestCommand;
import org.seasar.extension.jdbc.gen.internal.factory.Factory;

/**
 * SQLファイルに対するテストクラスのJavaファイルを生成するゴールです。
 * 
 * @author hakoda-te-kun
 * @see GenerateSqlFileTestCommand
 *
 * @goal gen-sqlfiletest-task
 */
public class GenerateSqlFileTestTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected GenerateSqlFileTestCommand command = new GenerateSqlFileTestCommand();

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
	 * エンティティのパッケージ名を設定します。
	 * 
	 * @parameter
	 */
	private String entityPackageName;

	/**
	 * テストクラスのテンプレート名を設定します。
	 * 
	 * @parameter
	 */
	private String templateFileName;

	/**
	 * Javaファイルのエンコーディングを設定します。
	 * 
	 * @parameter
	 */
	private String javaFileEncoding;

	/**
	 * 生成するJavaファイルの出力先ディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File javaFileDestDir;

	/**
	 * 上書きをする場合{@code true}、しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean overwrite;

	/**
	 * ルートパッケージ名を返します。
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
	 * テンプレートファイルを格納するプライマリディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File templateFilePrimaryDir;

	/**
	 * 生成するテストクラスの単純名を設定します。
	 * 
	 * @parameter
	 */
	private String shortClassName;

	/**
	 * テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean useS2junit4;

	/**
	 * 対象となるSQLファイルを設定します。
	 * 
	 * @parameter
	 */
	private FileSet sqlFileSet;

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
		if (templateFileName != null)
			command.setTemplateFileName(templateFileName);
		if (javaFileEncoding != null)
			command.setJavaFileEncoding(javaFileEncoding);
		if (javaFileDestDir != null)
			command.setJavaFileDestDir(javaFileDestDir);
		if (overwrite != null)
			command.setOverwrite(overwrite);
		if (rootPackageName != null)
			command.setRootPackageName(rootPackageName);
		if (templateFileEncoding != null)
			command.setTemplateFileEncoding(templateFileEncoding);
		if (templateFilePrimaryDir != null)
			command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
		if (shortClassName != null)
			command.setShortClassName(shortClassName);
		if (useS2junit4 != null)
			command.setUseS2junit4(useS2junit4);
		if (sqlFileSet != null) {
			try {
				final Set<File> files = sqlFileSet.getFiles();
				getLog().warn(files.toString());
				command.getSqlFileSet().addAll(files);
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}

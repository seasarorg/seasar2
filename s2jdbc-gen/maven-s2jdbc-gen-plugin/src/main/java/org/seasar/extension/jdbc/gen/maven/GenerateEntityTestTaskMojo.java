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

import org.seasar.extension.jdbc.gen.internal.command.AbstractCommand;
import org.seasar.extension.jdbc.gen.internal.command.GenerateEntityTestCommand;

/**
 * エンティティに対するテストクラスのJavaファイルを生成するゴールです。
 * 
 * @author hakoda-te-kun
 * @see GenerateEntityTestCommand
 * 
 * @goal gen-entitytest-task
 */
public class GenerateEntityTestTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected GenerateEntityTestCommand command = new GenerateEntityTestCommand();

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
	 * テストクラス名のサフィックスを設定します。
	 * 
	 * @parameter
	 */
	private String testClassNameSuffix;

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
	 * テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean useS2junit4;

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
	 * 名前クラスを使用する場合{@code true}、しない場合{@code false}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean useNamesClass;

	@Override
	protected AbstractCommand getCommand() {
		return command;
	}

	@Override
	protected void setCommandSpecificParameters() {
		if (classpathDir != null)
			command.setClasspathDir(classpathDir);
		if (entityPackageName != null)
			command.setEntityPackageName(entityPackageName);
		if (testClassNameSuffix != null)
			command.setTestClassNameSuffix(testClassNameSuffix);
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
		if (entityClassNamePattern != null)
			command.setEntityClassNamePattern(entityClassNamePattern);
		if (ignoreEntityClassNamePattern != null)
			command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
		if (useS2junit4 != null)
			command.setUseS2junit4(useS2junit4);
		if (namesClassNameSuffix != null)
			command.setNamesClassNameSuffix(namesClassNameSuffix);
		if (namesPackageName != null)
			command.setNamesPackageName(namesPackageName);
		if (useNamesClass != null)
			command.setUseNamesClass(useNamesClass);
	}

	@Override
	protected List<File> getAdditionalClasspath() {
		final List<File> dirs = new ArrayList<File>();
		if (classpathDir != null)
			dirs.add(classpathDir);
		return dirs;
	}
}
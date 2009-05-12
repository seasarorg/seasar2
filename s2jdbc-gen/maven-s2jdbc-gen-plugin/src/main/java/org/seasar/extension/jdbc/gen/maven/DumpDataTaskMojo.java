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

import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.AbstractCommand;
import org.seasar.extension.jdbc.gen.internal.command.DumpDataCommand;

/**
 * エンティティに対応するデータベースのデータをテキストファイルにダンプするゴールです。
 * 
 * @author hakoda-te-kun
 * @see DumpDataCommand
 * 
 * @goal dump-data-task
 */
public class DumpDataTaskMojo extends AbstractS2JdbcGenMojo {

	/** コマンド */
	protected DumpDataCommand command = new DumpDataCommand();

	/**
	 * クラスパスのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File classpathDir;

	/**
	 * ダンプディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File dumpDir;

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
	 * {@link GenDialect}の実装クラス名を設定します。
	 * 
	 * @parameter
	 */
	private String genDialectClassName;

	/**
	 * DDL情報ファイルを設定します。
	 * 
	 * @parameter
	 */
	private File ddlInfoFile;

	/**
	 * ダンプディレクトリ名を設定します。
	 * 
	 * @parameter
	 */
	private String dumpDirName;

	/**
	 * マイグレーションのディレクトリを設定します。
	 * 
	 * @parameter
	 */
	private File migrateDir;

	/**
	 * バージョン番号のパターンを設定します。
	 * 
	 * @parameter
	 */
	private String versionNoPattern;

	/**
	 * 環境名をバージョンに適用する場合{@code true}を設定します。
	 * 
	 * @parameter
	 */
	private Boolean applyEnvToVersion;

	@Override
	protected AbstractCommand getCommand() {
		return command;
	}

	@Override
	protected void setCommandSpecificParameters() {
		if (classpathDir != null)
			command.setClasspathDir(classpathDir);
		if (dumpDir != null)
			command.setDumpDir(dumpDir);
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
		if (genDialectClassName != null)
			command.setGenDialectClassName(genDialectClassName);
		if (ddlInfoFile != null)
			command.setDdlInfoFile(ddlInfoFile);
		if (dumpDirName != null)
			command.setDumpDirName(dumpDirName);
		if (migrateDir != null)
			command.setMigrateDir(migrateDir);
		if (versionNoPattern != null)
			command.setVersionNoPattern(versionNoPattern);
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

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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.command.GenerateSqlFileConstantsCommand;
import org.seasar.extension.jdbc.gen.internal.command.GenerateSqlFileTestCommand;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantNamingRule;

/**
 * SQLファイルの定数クラスののJavaファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateSqlFileTestCommand
 */
public class GenerateSqlFileConstantsTask extends AbstractTask {

    /** コマンド */
    protected GenerateSqlFileConstantsCommand command = new GenerateSqlFileConstantsCommand();

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
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * サブパッケージ名を設定します。
     * 
     * @param subPackageName
     *            サブパッケージ名
     */
    public void setSubPackageName(String subPackageName) {
        command.setSubPackageName(subPackageName);
    }

    /**
     * テストクラスのテンプレート名を設定します。
     * 
     * @param templateFileName
     *            テストクラスのテンプレート名
     */
    public void setTemplateFileName(String templateFileName) {
        command.setTemplateFileName(templateFileName);
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
     * 生成するJavaファイルの出力先ディレクトリを設定します。
     * 
     * @param javaFileDestDir
     *            生成するJavaファイルの出力先ディレクトリ
     */
    public void setJavaFileDestDir(File javaFileDestDir) {
        command.setJavaFileDestDir(javaFileDestDir);
    }

    /**
     * 上書きをする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param overwrite
     *            上書きをする場合{@code true}、しない場合{@code false}
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
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
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    /**
     * 生成するテストクラスの単純名を設定します。
     * 
     * @param shortClassName
     *            生成するテストクラスの単純名
     */
    public void setShortClassName(String shortClassName) {
        command.setShortClassName(shortClassName);
    }

    /**
     * {@link SqlFileConstantNamingRule}の実装クラス名を設定します。
     * 
     * @param sqlFileConstantNamingRuleClassName
     *            {@link SqlFileConstantNamingRule}の実装クラス名
     */
    public void setSqlFileConstantNamingRuleClassName(
            String sqlFileConstantNamingRuleClassName) {
        command
                .setSqlFileConstantNamingRuleClassName(sqlFileConstantNamingRuleClassName);
    }

    /**
     * すでに値が設定された{@link FileSet}を追加します。
     * 
     * @param sqlFileSet
     *            すでに値が設定された{@link FileSet}
     */
    public void addConfiguredSqlFileSet(FileSet sqlFileSet) {
        DirectoryScanner scanner = sqlFileSet.getDirectoryScanner(getProject());
        File baseDir = sqlFileSet.getDir();
        for (String fileName : scanner.getIncludedFiles()) {
            command.getSqlFileSet().add(new File(baseDir, fileName));
        }
    }

}

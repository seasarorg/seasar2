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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.command.GenerateNamesCommand;
import org.seasar.extension.jdbc.gen.internal.command.GenerateServiceCommand;

/**
 * エンティティに対する名前クラスのJavaファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateServiceCommand
 */
public class GenerateNamesTask extends AbstractTask {

    /** コマンド */
    protected GenerateNamesCommand command = new GenerateNamesCommand();

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
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */

    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
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
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
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
     * ルートパッケージ名を設定します。
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
     * テンプレートファイルを格納したプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納したプライマリディレクトリ
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
     * 名前クラス名のサフィックスを設定します。
     * 
     * @param namesClassNameSuffix
     *            名前クラス名のサフィックス
     */
    public void setNamesClassNameSuffix(String namesClassNameSuffix) {
        command.setNamesClassNameSuffix(namesClassNameSuffix);
    }

    /**
     * 名前クラスのパッケージ名を設定します。
     * 
     * @param namesPackageName
     *            名前クラスのパッケージ名
     */
    public void setNamesPackageName(String namesPackageName) {
        command.setNamesPackageName(namesPackageName);
    }

    /**
     * 名前クラスのテンプレート名を設定します。
     * 
     * @param namesTemplateFileName
     *            名前クラスのテンプレート名
     */
    public void setNamesTemplateFileName(String namesTemplateFileName) {
        command.setNamesTemplateFileName(namesTemplateFileName);
    }

    /**
     * 名前集約クラスを生成する場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param generateNamesAggregateClass
     *            名前集約クラスを生成する場合{@code true}、しない場合{@code false}
     */
    public void setGenerateNamesAggregateClass(
            boolean generateNamesAggregateClass) {
        command.setGenerateNamesAggregateClass(generateNamesAggregateClass);
    }

    /**
     * 名前集約クラスの単純名を設定します。
     * 
     * @param namesAggregateShortClassName
     *            名前集約クラスの単純名
     */
    public void setNamesAggregateShortClassName(
            String namesAggregateShortClassName) {
        command.setNamesAggregateShortClassName(namesAggregateShortClassName);
    }

    /**
     * 名前集約クラスのテンプレート名を設定します。
     * 
     * @param namesAggregateTemplateFileName
     *            名前集約クラスのテンプレート名
     */
    public void setNamesAggregateTemplateFileName(
            String namesAggregateTemplateFileName) {
        command
                .setNamesAggregateTemplateFileName(namesAggregateTemplateFileName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}

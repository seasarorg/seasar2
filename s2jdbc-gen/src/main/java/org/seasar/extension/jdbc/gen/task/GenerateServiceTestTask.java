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
import org.seasar.extension.jdbc.gen.internal.command.GenerateServiceTestCommand;

/**
 * サービスに対するテストクラスのJavaファイルを生成する{@link Task}です。
 * 
 * @author taedium
 * @see GenerateServiceTestCommand
 */
public class GenerateServiceTestTask extends AbstractTask {

    /** コマンド */
    protected GenerateServiceTestCommand command = new GenerateServiceTestCommand();

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
     * エンティティのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * テストクラス名のサフィックスを設定します。
     * 
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     */
    public void setTestClassNameSuffix(String testClassNameSuffix) {
        command.setTestClassNameSuffix(testClassNameSuffix);
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
     * アプリケーション用の設定ファイルのパスを設定します。
     * 
     * @param appConfigPath
     *            アプリケーション用の設定ファイルのパス
     */
    public void setAppConfigPath(String appConfigPath) {
        command.setAppConfigPath(appConfigPath);
    }

    /**
     * サービスクラス名のサフィックスを設定します。
     * 
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     */
    public void setServiceClassNameSuffix(String serviceClassNameSuffix) {
        command.setServiceClassNameSuffix(serviceClassNameSuffix);
    }

    /**
     * サービスクラスのパッケージ名を設定します。
     * 
     * @param servicePackageName
     *            サービスクラスのパッケージ名
     */
    public void setServicePackageName(String servicePackageName) {
        command.setServicePackageName(servicePackageName);
    }

    /**
     * テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を設定します。
     * 
     * @param useS2junit4
     *            テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
    public void setUseS2junit4(boolean useS2junit4) {
        command.setUseS2junit4(useS2junit4);
    }
}

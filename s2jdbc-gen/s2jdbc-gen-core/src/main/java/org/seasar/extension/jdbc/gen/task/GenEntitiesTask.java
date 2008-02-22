/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import org.seasar.extension.jdbc.gen.command.GenEntitiesCommand;

/**
 * エンティティのJavaファイルを生成するタスクです。
 * 
 * @author taedium
 */
public class GenEntitiesTask extends GenTask {

    /** diconファイル */
    protected String diconFile;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** ルートパッケージ名 */
    protected String rootPackageName;

    /** エンティティパッケージ名 */
    protected String entityPackageName;

    /** エンティティ基底クラスのパッケージ名 */
    protected String entityBasePackageName;

    /** エンティティ基底クラス名のサフィックス */
    protected String entityBaseClassNameSuffix;

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateDir;

    /** テンプレートのエンコーディング */
    protected String templateEncoding;

    /** 生成Javaファイル出力先ディレクトリ */
    protected File destDir;

    /** Javaコードのエンコーディング */
    protected String javaCodeEncoding;

    /** スキーマの名前 */
    protected String schemaName;

    /** バージョン用カラムの名前 */
    protected String versionColumnName;

    /** テーブル名のパターン */
    protected String tableNamePattern;

    /**
     * インスタンスを構築します。
     */
    public GenEntitiesTask() {
        super(GenEntitiesCommand.class.getName());
    }

    /**
     * diconファイルを取得します。
     * 
     * @return diconファイル
     */
    public String getDiconFile() {
        return diconFile;
    }

    /**
     * diconファイルを設定します。
     * 
     * @param diconFile
     *            daiconファイル
     */
    public void setDiconFile(String diconFile) {
        this.diconFile = diconFile;
    }

    /**
     * {@link JdbcManager}のコンポーネント名です。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * ルートパッケージ名を取得します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * エンティティパッケージ名を取得します。
     * 
     * @return エンティティパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * エンティティ基底クラスのパッケージ名を取得します。
     * 
     * @return エンティティ基底クラスのパッケージ名
     */
    public String getEntityBasePackageName() {
        return entityBasePackageName;
    }

    /**
     * エンティティ基底クラスのパッケージ名を設定します。
     * 
     * @param entityBasePackageName
     *            エンティティ基底クラスのパッケージ名
     */
    public void setEntityBasePackageName(String entityBasePackageName) {
        this.entityBasePackageName = entityBasePackageName;
    }

    /**
     * エンティティ基底クラス名のサフィックスを取得します。
     * 
     * @return エンティティ基底クラス名のサフィックス
     */
    public String getEntityBaseClassNameSuffix() {
        return entityBaseClassNameSuffix;
    }

    /**
     * エンティティ基底クラス名のサフィックスを設定します。
     * 
     * @param entityBaseClassNameSuffix
     *            エンティティ基底クラス名のサフィックス
     */
    public void setEntityBaseClassNameSuffix(String entityBaseClassNameSuffix) {
        this.entityBaseClassNameSuffix = entityBaseClassNameSuffix;
    }

    /**
     * テンプレートファイルを格納するディレクトリ
     * 
     * @return テンプレートファイルを格納するディレクトリを取得します。
     */
    public File getTemplateDir() {
        return templateDir;
    }

    /**
     * テンプレートファイルを格納するディレクトリを設定します。
     * 
     * @param templateDir
     *            テンプレートファイルを格納するディレクトリ
     */
    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    /**
     * テンプレートのエンコーディングを取得します。
     * 
     * @return テンプレートのエンコーディング
     */
    public String getTemplateEncoding() {
        return templateEncoding;
    }

    /**
     * テンプレートのエンコーディングを設定します。
     * 
     * @param templateEncoding
     *            テンプレートのエンコーディング
     */
    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    /**
     * 生成Javaファイル出力先ディレクトリを取得します。
     * 
     * @return 生成Javaファイル出力先ディレクトリ
     */
    public File getDestDir() {
        return destDir;
    }

    /**
     * 生成Javaファイル出力先ディレクトリを設定します。
     * 
     * @param destDir
     *            生成Javaファイル出力先ディレクトリ
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Javaコードのエンコーディングを取得します。
     * 
     * @return Javaコードのエンコーディング
     */
    public String getJavaCodeEncoding() {
        return javaCodeEncoding;
    }

    /**
     * Javaコードのエンコーディングを設定します。
     * 
     * @param javaCodeEncoding
     *            Javaコードのエンコーディング
     */
    public void setJavaCodeEncoding(String javaCodeEncoding) {
        this.javaCodeEncoding = javaCodeEncoding;
    }

    /**
     * スキーマ名を取得します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * バージョン用カラム名を取得します。
     * 
     * @return バージョン用カラム名を設定します。
     */
    public String getVersionColumnName() {
        return versionColumnName;
    }

    /**
     * バージョン用カラム名を設定します。
     * 
     * @param versionColumnName
     *            バージョン用カラム名を設定します。
     */
    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

    /**
     * 正規表現で表されたテーブル名のパターンを取得します。
     * 
     * @return テーブル名のパターン
     */
    public String getTableNamePattern() {
        return tableNamePattern;
    }

    /**
     * 正規表現で表されたテーブル名のパターンを正規表現で設定します。
     * 
     * @param tableNamePattern
     *            テーブル名のパターン
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

}

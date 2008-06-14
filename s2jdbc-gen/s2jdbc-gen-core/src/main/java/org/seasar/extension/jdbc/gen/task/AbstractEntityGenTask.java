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

/**
 * @author taedium
 * 
 */
public abstract class AbstractEntityGenTask extends AbstractGenTask {

    /** diconファイル */
    protected String diconFile;

    /** ルートパッケージ名 */
    protected String rootPackageName;

    /** エンティティパッケージ名 */
    protected String entityPackageName;

    /** エンティティ基底クラスのパッケージ名 */
    protected String entityBasePackageName;

    /** エンティティ基底クラス名のプレフィックス */
    protected String entityBaseClassNamePrefix;

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateDir;

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateName;

    /** エンティティ基底クラスのテンプレート名 */
    protected String entityBaseTemplateName;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File destDir;

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding;

    /** スキーマの名前 */
    protected String schemaName;

    /** バージョン用カラムの名前 */
    protected String versionColumnName;

    /** テーブル名のパターン */
    protected String tableNamePattern;

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public AbstractEntityGenTask(String commandClassName) {
        super(commandClassName);
    }

    /**
     * diconファイルを返します。
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
     * ルートパッケージ名を返します。
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
     * エンティティパッケージ名を返します。
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
     * エンティティ基底クラスのパッケージ名を返します。
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
     * エンティティ基底クラス名のサフィックスを返します。
     * 
     * @return エンティティ基底クラス名のサフィックス
     */
    public String getEntityBaseClassNamePrefix() {
        return entityBaseClassNamePrefix;
    }

    /**
     * エンティティ基底クラス名のサフィックスを設定します。
     * 
     * @param entityBaseClassNameSuffix
     *            エンティティ基底クラス名のサフィックス
     */
    public void setEntityBaseClassNamePrefix(String entityBaseClassNameSuffix) {
        this.entityBaseClassNamePrefix = entityBaseClassNameSuffix;
    }

    /**
     * テンプレートファイルを格納するディレクトリ
     * 
     * @return テンプレートファイルを格納するディレクトリを返します。
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
     * エンティティクラスのテンプレート名を返します。
     * 
     * @return エンティティクラスのテンプレート名
     */

    public String getEntityTemplateName() {
        return entityTemplateName;
    }

    /**
     * エンティティクラスのテンプレート名を設定します。
     * 
     * @param entityTemplateName
     *            エンティティクラスのテンプレート名
     */
    public void setEntityTemplateName(String entityTemplateName) {
        this.entityTemplateName = entityTemplateName;
    }

    /**
     * エンティティ基底クラスのテンプレート名を返します。
     * 
     * @return エンティティ基底クラスのテンプレート名
     */
    public String getEntityBaseTemplateName() {
        return entityBaseTemplateName;
    }

    /**
     * エンティティ基底クラスのテンプレート名
     * 
     * @param entityBaseTemplateName
     *            エンティティ基底クラスのテンプレート名
     */
    public void setEntityBaseTemplateName(String entityBaseTemplateName) {
        this.entityBaseTemplateName = entityBaseTemplateName;
    }

    /**
     * テンプレートのエンコーディングを返します。
     * 
     * @return テンプレートのエンコーディング
     */
    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    /**
     * テンプレートのエンコーディングを設定します。
     * 
     * @param templateEncoding
     *            テンプレートのエンコーディング
     */
    public void setTemplateFileEncoding(String templateEncoding) {
        this.templateFileEncoding = templateEncoding;
    }

    /**
     * 生成Javaファイル出力先ディレクトリを返します。
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
     * Javaコードのエンコーディングを返します。
     * 
     * @return Javaコードのエンコーディング
     */
    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    /**
     * Javaコードのエンコーディングを設定します。
     * 
     * @param javaCodeEncoding
     *            Javaコードのエンコーディング
     */
    public void setJavaFileEncoding(String javaCodeEncoding) {
        this.javaFileEncoding = javaCodeEncoding;
    }

    /**
     * スキーマ名を返します。
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
     * バージョン用カラム名を返します。
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
     * 正規表現で表されたテーブル名のパターンを返します。
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

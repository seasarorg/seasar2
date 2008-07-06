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

import org.seasar.extension.jdbc.gen.command.EntityGenCommand;

/**
 * S2JDBC用のエンティティを生成するタスクです。
 * 
 * @author taedium
 */
public class EntityGenTask extends AbstractTask {

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File destDir;

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding;

    /** 条件クラス名のサフィックス */
    protected String conditionClassNameSuffix;

    /** 条件クラスのパッケージ名 */
    protected String conditionPackageName;

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateName;

    /** 条件クラスのテンプレート名 */
    protected String conditionTemplateName;

    /** 条件クラスを生成する場合{@link Boolean#TRUE } */
    protected Boolean generateConditionClass;

    /** スキーマ名 */
    protected String schemaName;

    /** Javaコードの生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern;

    /** バージョンカラムの名前 */
    protected String versionColumnName;

    /**
     * インスタンスを構築します。
     */
    public EntityGenTask() {
        this(EntityGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public EntityGenTask(String commandClassName) {
        super(commandClassName);
    }

    public File getDestDir() {
        return destDir;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
    }

    public String getConditionClassNameSuffix() {
        return conditionClassNameSuffix;
    }

    public void setConditionClassNameSuffix(String conditionClassNameSuffix) {
        this.conditionClassNameSuffix = conditionClassNameSuffix;
    }

    public String getConditionPackageName() {
        return conditionPackageName;
    }

    public void setConditionPackageName(String conditionPackageName) {
        this.conditionPackageName = conditionPackageName;
    }

    public String getEntityTemplateName() {
        return entityTemplateName;
    }

    public void setEntityTemplateName(String entityTemplateName) {
        this.entityTemplateName = entityTemplateName;
    }

    public String getConditionTemplateName() {
        return conditionTemplateName;
    }

    public void setConditionTemplateName(String conditionTemplateName) {
        this.conditionTemplateName = conditionTemplateName;
    }

    public Boolean getGenerateConditionClass() {
        return generateConditionClass;
    }

    public void setGenerateConditionClass(Boolean generateConditionClass) {
        this.generateConditionClass = generateConditionClass;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableNamePattern() {
        return tableNamePattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public String getVersionColumnName() {
        return versionColumnName;
    }

    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

}

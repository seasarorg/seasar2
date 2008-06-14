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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.S2JdbcEntityGenCommand;

/**
 * S2JDBC用のエンティティを生成するタスクです。
 * 
 * @author taedium
 */
public class S2JdbcEntityGenTask extends AbstractEntityGenTask {

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** エンティティ条件クラスのパッケージ名 */
    protected String entityConditionPackageName;

    /** エンティティ条件基底クラスのパッケージ名 */
    protected String entityConditionBasePackageName;

    /** エンティティ条件クラス名のサフィックス */
    protected String entityConditionClassNameSuffix;

    /** エンティティ条件基底クラス名のプレフィックス */
    protected String entityConditionBaseClassNamePrefix;

    /** エンティティ条件基底クラス名のサフィックス */
    protected String entityConditionBaseClassNameSuffix;

    /** エンティティ条件クラスのテンプレート名 */
    protected String entityConditionTemplateName;

    /** エンティティ条件基底クラスのテンプレート名 */
    protected String entityConditionBaseTemplateName;

    /** 条件クラスを生成する場合{@code true} */
    protected Boolean generateCondition;

    /**
     * インスタンスを構築します。
     */
    public S2JdbcEntityGenTask() {
        this(S2JdbcEntityGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public S2JdbcEntityGenTask(String commandClassName) {
        super(commandClassName);
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
     * エンティティ条件クラスのパッケージ名を返します。
     * 
     * @return エンティティ条件クラスのパッケージ名
     */
    public String getEntityConditionPackageName() {
        return entityConditionPackageName;
    }

    /**
     * エンティティ条件クラスのパッケージ名を設定します。
     * 
     * @param entityConditionPackageName
     *            エンティティ条件クラスのパッケージ名
     */
    public void setEntityConditionPackageName(String entityConditionPackageName) {
        this.entityConditionPackageName = entityConditionPackageName;
    }

    /**
     * エンティティ条件基底クラスのパッケージ名を返します。
     * 
     * @return エンティティ条件基底クラスのパッケージ名
     */
    public String getEntityConditionBasePackageName() {
        return entityConditionBasePackageName;
    }

    /**
     * エンティティ条件基底クラスのパッケージ名
     * 
     * @param entityConditionBasePackageName
     *            エンティティ条件基底クラスのパッケージ名
     */
    public void setEntityConditionBasePackageName(
            String entityConditionBasePackageName) {
        this.entityConditionBasePackageName = entityConditionBasePackageName;
    }

    /**
     * エンティティ条件クラス名のサフィックスを設定します。
     * 
     * @param entityConditionClassNameSuffix
     *            エンティティ条件クラス名のサフィックス
     */
    public void setEntityConditionClassNameSuffix(
            String entityConditionClassNameSuffix) {
        this.entityConditionClassNameSuffix = entityConditionClassNameSuffix;
    }

    /**
     * エンティティ条件基底クラス名のプレフィックスを返します。
     * 
     * @return エンティティ条件基底クラス名のプレフィックス
     */
    public String getEntityConditionBaseClassNamePrefix() {
        return entityConditionBaseClassNamePrefix;
    }

    /**
     * エンティティ条件基底クラス名のプレフィックスを設定します。
     * 
     * @param entityConditionBaseClassNamePrefix
     *            エンティティ条件基底クラス名のプレフィックス
     */
    public void setEntityConditionBaseClassNamePrefix(
            String entityConditionBaseClassNamePrefix) {
        this.entityConditionBaseClassNamePrefix = entityConditionBaseClassNamePrefix;
    }

    /**
     * エンティティ条件基底クラス名のサフィックスを返します。
     * 
     * @return エンティティ条件基底クラス名のサフィックス
     */
    public String getEntityConditionBaseClassNameSuffix() {
        return entityConditionBaseClassNameSuffix;
    }

    /**
     * エンティティ条件基底クラス名のサフィックスを設定します。
     * 
     * @param entityConditionBaseClassNameSuffix
     *            エンティティ条件基底クラス名のサフィックス
     */
    public void setEntityConditionBaseClassNameSuffix(
            String entityConditionBaseClassNameSuffix) {
        this.entityConditionBaseClassNameSuffix = entityConditionBaseClassNameSuffix;
    }

    /**
     * エンティティ条件クラスのテンプレート名を設定します。
     * 
     * @return エンティティ条件クラスのテンプレート名
     */
    public String getEntityConditionTemplateName() {
        return entityConditionTemplateName;
    }

    /**
     * エンティティ条件クラスのテンプレート名を設定します。
     * 
     * @param entityConditionTemplateName
     *            エンティティ条件クラスのテンプレート名
     */
    public void setEntityConditionTemplateName(
            String entityConditionTemplateName) {
        this.entityConditionTemplateName = entityConditionTemplateName;
    }

    /**
     * エンティティ条件基底クラスのテンプレート名を設定します。
     * 
     * @return エンティティ条件基底クラスのテンプレート名
     */
    public String getEntityConditionBaseTemplateName() {
        return entityConditionBaseTemplateName;
    }

    /**
     * エンティティ条件基底クラスのテンプレート名を設定します。
     * 
     * @param entityConditionBaseTemplateName
     *            エンティティ条件基底クラスのテンプレート名
     */
    public void setEntityConditionBaseTemplateName(
            String entityConditionBaseTemplateName) {
        this.entityConditionBaseTemplateName = entityConditionBaseTemplateName;
    }

    /**
     * エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}を返します。
     * 
     * @return エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}
     */
    public Boolean isGenerateCondition() {
        return generateCondition;
    }

    /**
     * エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}を設定します。
     * 
     * @param generateCondition
     *            エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}
     */
    public void setGenerateCondition(Boolean generateCondition) {
        this.generateCondition = generateCondition;
    }

}

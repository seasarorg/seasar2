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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;

/**
 * テストクラスのモデルです。
 * 
 * @author taedium
 */
public class EntityTestModel extends TestClassModel {

    /** 識別子の式のリスト */
    protected List<String> idExpressionList = new ArrayList<String>();

    /** 関連名のリスト */
    protected List<String> associationNameList = new ArrayList<String>();

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** 設定ファイルのパス */
    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** 名前モデル */
    protected NamesModel namesModel;

    /**
     * エンティティクラスの単純名を返します。
     * 
     * @return エンティティクラスの単純名
     */
    public String getShortEntityClassName() {
        return shortEntityClassName;
    }

    /**
     * エンティティクラスの単純名を設定します。
     * 
     * @param shortEntityClassName
     *            エンティティクラスの単純名
     */
    public void setShortEntityClassName(String shortEntityClassName) {
        this.shortEntityClassName = shortEntityClassName;
    }

    /**
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を返します。
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
     * 識別子の式のリストを返します。
     * 
     * @return 識別子の式のリスト
     */
    public List<String> getIdExpressionList() {
        return Collections.unmodifiableList(idExpressionList);
    }

    /**
     * 識別子の式のリストを設定します。
     * 
     * @param idExpression
     *            識別子の式のリスト
     */
    public void addIdExpression(String idExpression) {
        idExpressionList.add(idExpression);
    }

    /**
     * 関連名のリストを返します。
     * 
     * @return 関連名のリスト
     */
    public List<String> getAssociationNameList() {
        return Collections.unmodifiableList(associationNameList);
    }

    /**
     * 関連名を追加します。
     * 
     * @param associationName
     *            関連名
     */
    public void addAssociationName(String associationName) {
        associationNameList.add(associationName);
    }

    /**
     * 名前モデルを返します。
     * 
     * @return 名前モデル
     */
    public NamesModel getNamesModel() {
        return namesModel;
    }

    /**
     * 名前モデルを設定します。
     * 
     * @param namesModel
     *            名前モデル
     */
    public void setNamesModel(NamesModel namesModel) {
        this.namesModel = namesModel;
    }

}

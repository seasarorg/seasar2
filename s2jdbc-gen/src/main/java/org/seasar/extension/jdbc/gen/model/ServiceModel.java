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
import org.seasar.extension.jdbc.PropertyMeta;

/**
 * サービスモデルです。
 * 
 * @author taedium
 */
public class ServiceModel extends ClassModel {

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** {@link JdbcManager}のコンポーネントをsetterメソッドでDIする場合{@code true} */
    protected boolean jdbcManagerSetterNecessary;

    /** エンティティクラスの単純名 */
    protected String shortEntityClassName;

    /** スーパークラスの単純名 */
    protected String shortSuperclassName;

    /** 名前モデル */
    protected NamesModel namesModel;

    /** 識別子のプロパティメタデータのリスト */
    protected List<PropertyMeta> idPropertyMetaList = new ArrayList<PropertyMeta>();

    /** バージョンを表すプロパティメタデータ */
    protected PropertyMeta versionPropertyMeta;

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
     * {@link JdbcManager}のコンポーネントをsetterメソッドでDIする場合{@code true}を返します。
     * 
     * @return {@link JdbcManager}のコンポーネントをsetterメソッドでDIする場合{@code true}
     */
    public boolean isJdbcManagerSetterNecessary() {
        return jdbcManagerSetterNecessary;
    }

    /**
     * {@link JdbcManager}のコンポーネントをsetterメソッドでDIする場合{@code true}を設定します。
     * 
     * @param jdbcManagerSetterNecessary
     *            {@link JdbcManager}のコンポーネントをsetterメソッドでDIする場合{@code true}
     */
    public void setJdbcManagerSetterNecessary(boolean jdbcManagerSetterNecessary) {
        this.jdbcManagerSetterNecessary = jdbcManagerSetterNecessary;
    }

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
     * スーパークラスの単純名を返します。
     * 
     * @return スーパークラスの単純名
     */
    public String getShortSuperclassName() {
        return shortSuperclassName;
    }

    /**
     * スーパークラスの単純名を設定します。
     * 
     * @param shortSuperclassName
     *            スーパークラスの単純名
     */
    public void setShortSuperclassName(String shortSuperclassName) {
        this.shortSuperclassName = shortSuperclassName;
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

    /**
     * 識別子のプロパティメタデータを追加します。
     * 
     * @param idPropertyMeta
     *            識別子のプロパティメタデータ
     */
    public void addIdPropertyMeta(PropertyMeta idPropertyMeta) {
        idPropertyMetaList.add(idPropertyMeta);
    }

    /**
     * 識別子のプロパティメタデータのリストを返します。
     * 
     * @return 識別子のプロパティメタデータのリスト
     */
    public List<PropertyMeta> getIdPropertyMetaList() {
        return Collections.unmodifiableList(idPropertyMetaList);
    }

    /**
     * バージョンを表すプロパティメタデータを返します。
     * 
     * @return バージョンを表すプロパティメタデータ
     */
    public PropertyMeta getVersionPropertyMeta() {
        return versionPropertyMeta;
    }

    /**
     * バージョンを表すプロパティメタデータを設定します。
     * 
     * @param versionPropertyMeta
     *            バージョンを表すプロパティメタデータ
     */
    public void setVersionPropertyMeta(PropertyMeta versionPropertyMeta) {
        this.versionPropertyMeta = versionPropertyMeta;
    }

}

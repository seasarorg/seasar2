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
package org.seasar.extension.jdbc;

/**
 * 結合メタデータです。
 * 
 * @author higa
 */
public class JoinMeta {

    /**
     * 結合名です。
     */
    protected String name;

    /**
     * 結合タイプです。
     */
    protected JoinType joinType;

    /**
     * フェッチするかどうかです。
     */
    protected boolean fetch = true;

    /**
     * 付加的な結合条件です。
     */
    protected String condition;

    /**
     * 付加的な結合条件のパラメータです。
     */
    protected Object[] conditionParams;

    /**
     * 付加的な結合条件のプロパティ名です。
     */
    protected String[] conditionPropertyNames;

    /**
     * {@link JoinMeta}を作成します。
     */
    public JoinMeta() {
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合するプロパティ名
     */
    public JoinMeta(String name) {
        this(name, JoinType.LEFT_OUTER);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合するプロパティ名
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     */
    public JoinMeta(String name, String condition, Object[] conditionParams) {
        this(name, JoinType.LEFT_OUTER, condition, conditionParams);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合するプロパティ名
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     * @param conditionPropertyNames
     *            付加的な結合条件のプロパティ名
     */
    public JoinMeta(String name, String condition, Object[] conditionParams,
            String[] conditionPropertyNames) {
        this(name, JoinType.LEFT_OUTER, condition, conditionParams,
                conditionPropertyNames);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     */
    public JoinMeta(String name, JoinType joinType) {
        this(name, joinType, true);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     */
    public JoinMeta(String name, JoinType joinType, String condition,
            Object[] conditionParams) {
        this(name, joinType, true, condition, conditionParams);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     * @param conditionPropertyNames
     *            付加的な結合条件のプロパティ名
     */
    public JoinMeta(String name, JoinType joinType, String condition,
            Object[] conditionParams, String[] conditionPropertyNames) {
        this(name, joinType, true, condition, conditionParams,
                conditionPropertyNames);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param fetch
     *            フェッチするかどうか
     */
    public JoinMeta(String name, boolean fetch) {
        this(name, JoinType.LEFT_OUTER, fetch);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param fetch
     *            フェッチするかどうか
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     */
    public JoinMeta(String name, boolean fetch, String condition,
            Object[] conditionParams) {
        this(name, JoinType.LEFT_OUTER, fetch, condition, conditionParams);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param fetch
     *            フェッチするかどうか
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     * @param conditionPropertyNames
     *            付加的な結合条件のプロパティ名
     */
    public JoinMeta(String name, boolean fetch, String condition,
            Object[] conditionParams, String[] conditionPropertyNames) {
        this(name, JoinType.LEFT_OUTER, fetch, condition, conditionParams,
                conditionPropertyNames);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            フェッチするかどうか
     */
    public JoinMeta(String name, JoinType joinType, boolean fetch) {
        setName(name);
        setJoinType(joinType);
        setFetch(fetch);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            フェッチするかどうか
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     */
    public JoinMeta(String name, JoinType joinType, boolean fetch,
            String condition, Object[] conditionParams) {
        setName(name);
        setJoinType(joinType);
        setFetch(fetch);
        setCondition(condition);
        setConditionParams(conditionParams);
    }

    /**
     * {@link JoinMeta}を作成します。
     * 
     * @param name
     *            結合名
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            フェッチするかどうか
     * @param condition
     *            付加的な結合条件
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     * @param conditionPropertyNames
     *            付加的な結合条件のプロパティ名
     */
    public JoinMeta(String name, JoinType joinType, boolean fetch,
            String condition, Object[] conditionParams,
            String[] conditionPropertyNames) {
        setName(name);
        setJoinType(joinType);
        setFetch(fetch);
        setCondition(condition);
        setConditionParams(conditionParams);
        setConditionPropertyNames(conditionPropertyNames);
    }

    /**
     * 結合名を返します。
     * 
     * @return 結合するプロパティ名
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * 結合名を設定します。
     * </p>
     * <p>
     * ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     * </p>
     * 
     * @param name
     *            結合名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 結合タイプを返します。
     * 
     * @return 結合タイプ
     */
    public JoinType getJoinType() {
        return joinType;
    }

    /**
     * 結合タイプを設定します。
     * 
     * @param joinType
     *            結合タイプ
     */
    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    /**
     * フェッチするかどうかを返します。
     * 
     * @return フェッチするかどうか
     */
    public boolean isFetch() {
        return fetch;
    }

    /**
     * フェッチするかどうかを設定します。
     * 
     * @param fetch
     *            フェッチするかどうか
     */
    public void setFetch(boolean fetch) {
        this.fetch = fetch;
    }

    /**
     * 付加的な結合条件を返します。
     * 
     * @return 付加的な結合条件
     */
    public String getCondition() {
        return condition;
    }

    /**
     * 付加的な結合条件を設定します。
     * 
     * @param condition
     *            付加的な結合条件
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 付加的な結合条件のパラメータを返します。
     * 
     * @return 付加的な結合条件のパラメータ
     */
    public Object[] getConditionParams() {
        return conditionParams;
    }

    /**
     * 付加的な結合条件のパラメータを設定します。
     * 
     * @param conditionParams
     *            付加的な結合条件のパラメータ
     */
    public void setConditionParams(Object[] conditionParams) {
        this.conditionParams = conditionParams;
    }

    /**
     * 付加的な結合条件のプロパティ名を返します。
     * 
     * @return 付加的な結合条件のプロパティ名
     */
    public String[] getConditionPropertyNames() {
        return conditionPropertyNames;
    }

    /**
     * 付加的な結合条件のプロパティ名を設定します。
     * 
     * @param conditionPropertyNames
     *            付加的な結合条件のプロパティ名
     */
    public void setConditionPropertyNames(String[] conditionPropertyNames) {
        this.conditionPropertyNames = conditionPropertyNames;
    }

}

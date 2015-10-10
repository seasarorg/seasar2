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
package org.seasar.extension.jdbc.service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.GenericUtil;

/**
 * サービスを作るベースクラスです。
 * 
 * @author higa
 * @param <T>
 *            エンティティの型
 * 
 */
public abstract class S2AbstractService<T> {

    /**
     * JDBCマネージャです。
     */
    @Resource
    protected JdbcManager jdbcManager;

    /**
     * エンティティのクラスです。
     */
    protected Class<T> entityClass;

    /**
     * SQLファイルのパスのプレフィックスです。
     */
    protected String sqlFilePathPrefix;

    /**
     * コンストラクタです。
     * 
     */
    @SuppressWarnings("unchecked")
    public S2AbstractService() {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(getClass());
        for (Class<?> c = getClass(); c != Object.class; c = c.getSuperclass()) {
            if (c.getSuperclass() == S2AbstractService.class) {
                Type type = c.getGenericSuperclass();
                Type[] arrays = GenericUtil.getGenericParameter(type);
                setEntityClass((Class<T>) GenericUtil.getActualClass(arrays[0],
                        map));
                break;
            }
        }
    }

    /**
     * コンストラクタです。
     * 
     * @param entityClass
     *            エンティティのクラス
     */
    public S2AbstractService(Class<T> entityClass) {
        setEntityClass(entityClass);
    }

    /**
     * 自動検索を返します。
     * 
     * @return 自動検索
     */
    protected AutoSelect<T> select() {
        return jdbcManager.from(entityClass);
    }

    /**
     * すべてのエンティティを検索します。
     * 
     * @return すべてのエンティティ
     */
    public List<T> findAll() {
        return select().getResultList();
    }

    /**
     * 条件付で検索します。
     * 
     * @param conditions
     *            条件
     * 
     * @return エンティティのリスト
     * @see AutoSelect#where(Map)
     */
    public List<T> findByCondition(BeanMap conditions) {
        return select().where(conditions).getResultList();
    }

    /**
     * 件数を返します。
     * 
     * @return 件数
     */
    public long getCount() {
        return select().getCount();
    }

    /**
     * エンティティを挿入します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int insert(T entity) {
        return jdbcManager.insert(entity).execute();
    }

    /**
     * エンティティを更新します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int update(T entity) {
        return jdbcManager.update(entity).execute();
    }

    /**
     * エンティティを削除します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int delete(T entity) {
        return jdbcManager.delete(entity).execute();
    }

    /**
     * SQLファイル検索を返します。
     * 
     * @param <T2>
     *            戻り値のJavaBeansの型
     * @param baseClass
     *            戻り値のJavaBeansのクラス
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @return SQLファイル検索
     */
    protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
            String path) {
        return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path);
    }

    /**
     * SQLファイル検索を返します。
     * 
     * @param <T2>
     *            戻り値のJavaBeansの型
     * @param baseClass
     *            戻り値のJavaBeansのクラス
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @param parameter
     *            <p>
     *            パラメータ。
     *            </p>
     *            <p>
     *            パラメータが1つしかない場合は、値を直接指定します。 パラメータが複数ある場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせます。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>ｂyte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLファイル検索
     */
    protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
            String path, Object parameter) {
        return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path,
                parameter);
    }

    /**
     * SQLファイル更新を返します。
     * 
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @return SQLファイル更新
     */
    protected SqlFileUpdate updateBySqlFile(String path) {
        return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path);
    }

    /**
     * SQLファイル更新を返します。
     * 
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @param parameter
     *            パラメータ用のJavaBeans
     * 
     * @return SQLファイル更新
     */
    protected SqlFileUpdate updateBySqlFile(String path, Object parameter) {
        return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path, parameter);
    }

    /**
     * エンティティのクラスを設定します。
     * 
     * @param entityClass
     *            エンティティのクラス
     */
    protected void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        sqlFilePathPrefix = "META-INF/sql/"
                + StringUtil.replace(entityClass.getName(), ".", "/") + "/";
    }
}
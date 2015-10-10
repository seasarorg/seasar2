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
package org.seasar.extension.dataset.states;

/**
 * SQLを実行するためのコンテキストです。
 * 
 * @author higa
 * 
 */
public class SqlContext {

    private String sql;

    private Object[] args;

    private Class[] argTypes;

    /**
     * {@link SqlContext}を作成します。
     */
    public SqlContext() {
    }

    /**
     * {@link SqlContext}を作成します。
     * 
     * @param sql
     *            SQL
     * @param args
     *            引数
     * @param argTypes
     *            引数の型
     */
    public SqlContext(String sql, Object[] args, Class[] argTypes) {
        setSql(sql);
        setArgs(args);
        setArgTypes(argTypes);
    }

    /**
     * 引数を返します。
     * 
     * @return 引数
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 引数を設定します。
     * 
     * @param args
     *            引数
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * 引数の型を返します。
     * 
     * @return 引数の型
     */
    public Class[] getArgTypes() {
        return argTypes;
    }

    /**
     * 引数の型を設定します。
     * 
     * @param argTypes
     *            引数の型
     */
    public void setArgTypes(Class[] argTypes) {
        this.argTypes = argTypes;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

    /**
     * SQLを設定します。
     * 
     * @param sql
     *            SQL
     */
    public void setSql(String sql) {
        this.sql = sql;
    }
}

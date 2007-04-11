/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLを実行するためのクラスです。
 * 
 * @author higa
 * 
 */
public class SqlStatement {

    private StringBuilder sqlBuf = new StringBuilder(300);

    private List<Object> argList = new ArrayList<Object>();

    private List<Class<?>> argClassList = new ArrayList<Class<?>>();

    /**
     * SQLを返します。
     * 
     * @return
     */
    public String getSql() {
        return sqlBuf.toString();
    }

    /**
     * SQLを追加します。
     * 
     * @param sql
     */
    public void addSql(String sql) {
        sqlBuf.append(sql);
    }

    /**
     * 引数の配列を返します。
     * 
     * @return
     */
    public Object[] getArgs() {
        return argList.toArray();
    }

    /**
     * 引数の<code>Class</code>の配列を返します。
     * 
     * @return
     */
    public Class[] getArgClasses() {
        return argClassList.toArray(new Class[argClassList.size()]);
    }

    /**
     * 引数を追加します。
     * 
     * @param arg
     * @throws NullPointerException
     *             argがnullの場合。
     */
    public void addArg(Object arg) {
        if (arg == null) {
            throw new NullPointerException("arg");
        }
        argList.add(arg);
        argClassList.add(arg.getClass());
    }

    /**
     * nullな引数を追加します。
     * 
     * @param argClass
     * @throws NullPointerException
     *             argClassがnullの場合。
     */
    public void addNullArg(Class<?> argClass) {
        if (argClass == null) {
            throw new NullPointerException("argClass");
        }
        argList.add(null);
        argClassList.add(argClass);
    }
}

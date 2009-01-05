/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.extension.dao.helper;

import java.lang.reflect.Method;

/**
 * Dao用のHelperインターフェースです。
 * 
 * @author higa
 * 
 */
public interface DaoHelper {

    /**
     * Daoのインターフェースを返します。
     * 
     * @param daoClass
     *            Daoのクラス
     * @return Daoのインターフェース
     */
    Class getDaoInterface(Class daoClass);

    /**
     * データソース名を返します。
     * 
     * @param daoClass
     *            Daoクラス
     * @return データソース名
     */
    String getDataSourceName(Class daoClass);

    /**
     * Daoのメソッドに対するSQLファイルの中身を返します。
     * 
     * @param daoClass
     *            Daoクラス
     * @param method
     *            メソッド名
     * @param suffix
     *            データベースサフィックス
     * @return Daoのメソッドに対するSQLファイルの中身
     */
    String getSqlBySqlFile(Class daoClass, Method method, String suffix);
}
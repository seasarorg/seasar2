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
 * SQLのログを表すインターフェースです。
 * 
 * @author taedium
 */
public interface SqlLog {

    /**
     * 未加工のSQLを返します。
     * <p>
     * このメソッドが返すSQLはバインド変数<code>?</code>を含みます。
     * </p>
     * 
     * @return 未加工のSQL
     */
    String getRawSql();

    /**
     * バインド変数を実際の値で置き換えた完全なSQLを返します。
     * 
     * @return 完全なSQL
     */
    String getCompleteSql();

    /**
     * SQLにバインドされる値の配列を返します。
     * 
     * @return SQLにバインドされる値の配列
     */
    Object[] getBindArgs();

    /**
     * SQLにバインドされる値の型の配列を返します。
     * 
     * @return SQLにバインドされる値の型の配列
     */
    Class[] getBindArgTypes();

}

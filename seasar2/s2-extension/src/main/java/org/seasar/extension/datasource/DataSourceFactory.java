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
package org.seasar.extension.datasource;

import javax.sql.DataSource;

/**
 * データソース用のファクトリです。
 * 
 * @author higa
 * 
 */
public interface DataSourceFactory {

    /**
     * 動的なデータソース名を返します。
     * 
     * @return データソース名
     */
    String getSelectableDataSourceName();

    /**
     * 動的なデータソース名を設定します。データソース名は {@link ThreadLocal}で管理されます。
     * 
     * @param name
     *            データソース名
     */
    void setSelectableDataSourceName(String name);

    /**
     * 名前が設定されている場合は名前をそのまま返し、設定されていない場合は {@link #getSelectableDataSourceName()}の結果を返します。
     * 
     * @param name
     *            dao.nameのようなdaoの後ろのサブパッケージ名
     * @return データソース名
     */
    String getDataSourceName(String name);

    /**
     * データソースを返します。
     * 
     * @param name
     *            dao.nameのようなdaoの後ろのサブパッケージ名
     * @return データソース
     */
    DataSource getDataSource(String name);
}
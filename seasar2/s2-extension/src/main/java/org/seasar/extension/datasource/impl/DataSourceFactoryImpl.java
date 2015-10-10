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
package org.seasar.extension.datasource.impl;

import javax.sql.DataSource;

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;

/**
 * {@link DataSourceFactory}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class DataSourceFactoryImpl implements DataSourceFactory {

    /**
     * 動的なデータソース名を管理します。
     */
    protected ThreadLocal selectableDataSourceName = new ThreadLocal();

    /**
     * S2コンテナです。
     */
    protected S2Container container;

    public String getSelectableDataSourceName() {
        return (String) selectableDataSourceName.get();
    }

    public void setSelectableDataSourceName(String name) {
        selectableDataSourceName.set(name);
    }

    public String getDataSourceName(String name) {
        if (!StringUtil.isEmpty(name)) {
            return name;
        }
        return getSelectableDataSourceName();
    }

    /**
     * @return S2コンテナ
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * @param container
     *            S2コンテナ
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    public DataSource getDataSource(String name) {
        return (DataSource) container.getRoot().getComponent(
                getDataSourceComponentName(name));
    }

    /**
     * データソースのコンポーネント名を返します。
     * 
     * @param name
     *            dao.nameのようなdaoの後ろのサブパッケージ名
     * @return コンポーネント名
     */
    protected String getDataSourceComponentName(String name) {
        String dsName = getDataSourceName(name);
        if (StringUtil.isEmpty(dsName)) {
            return "dataSource";
        }
        return dsName + "DataSource";
    }
}

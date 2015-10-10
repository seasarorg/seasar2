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
package org.seasar.extension.jdbc.impl;

import org.seasar.extension.jdbc.SqlLog;

/**
 * {@link SqlLog}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlLogImpl implements SqlLog {

    private String rawSql;

    private String completeSql;

    private Object[] bindArgs;

    private Class[] bindArgTypes;

    /**
     * インスタンスを構築します。
     * 
     * @param rawSql
     *            未加工のSQL
     * @param completeSql
     *            完全なSQL
     * @param bindArgs
     *            SQLにバインドされる値の配列
     * @param bindArgTypes
     *            SQLにバインドされる値の型の配列
     */
    public SqlLogImpl(String rawSql, String completeSql, Object[] bindArgs,
            Class[] bindArgTypes) {
        this.rawSql = rawSql;
        this.completeSql = completeSql;
        this.bindArgs = bindArgs;
        this.bindArgTypes = bindArgTypes;
    }

    public Object[] getBindArgs() {
        return bindArgs;
    }

    public Class[] getBindArgTypes() {
        return bindArgTypes;
    }

    public String getCompleteSql() {
        return completeSql;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String toString() {
        return rawSql;
    }
}

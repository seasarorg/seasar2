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
package org.seasar.extension.jdbc.gen.internal.provider;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;

/**
 * {@link ValueTypeProvider}の実装クラスです。
 * 
 * @author taedium
 */
public class ValueTypeProviderImpl implements ValueTypeProvider {

    /** S2JDBCのDBMS方言 */
    protected DbmsDialect dbmsDialect;

    /**
     * インスタンスを構築します。
     * 
     * @param dbmsDialect
     *            S2JDBCのDBMS方言
     */
    public ValueTypeProviderImpl(DbmsDialect dbmsDialect) {
        this.dbmsDialect = dbmsDialect;
    }

    public ValueType provide(PropertyMeta propertyMeta) {
        return dbmsDialect.getValueType(propertyMeta);
    }

}

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
package org.seasar.extension.jdbc.gen.internal.sqltype;

import org.seasar.extension.jdbc.gen.internal.util.ColumnUtil;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;

/**
 * {@link SqlType}の抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractSqlType implements SqlType {

    /** データ型 */
    protected String dataType;

    /**
     * インスタンスを構築します。
     */
    protected AbstractSqlType() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dataType
     *            データ型
     */
    protected AbstractSqlType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType(int length, int precision, int scale,
            boolean identity) {
        return ColumnUtil.formatDataType(dataType, length, precision, scale);
    }

}

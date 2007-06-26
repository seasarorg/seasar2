/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.util;

import java.sql.Types;

import javax.persistence.TemporalType;

/**
 * {@link TemporalType}のユーティリティクラスです。
 * 
 * @author koichik
 */
public class TemporalTypeUtil {

    /**
     * JDBCのSQL型を適切な{@link TemporalType}に変換します。
     * 
     * @param sqlType
     *            SQL型
     * @return <code>sqlType</code>を変換できる場合は{@link TemporalType}、変換できない場合は<code>null</code>
     */
    public static TemporalType fromSqlTypeToTemporalType(final int sqlType) {
        switch (sqlType) {
        case Types.DATE:
            return TemporalType.DATE;
        case Types.TIME:
            return TemporalType.TIME;
        case Types.TIMESTAMP:
            return TemporalType.TIMESTAMP;
        }
        return null;
    }

    /**
     * {@link TemporalType}をJDBCのSQL型に変換します。
     * 
     * @param temporalType
     *            時制を表わす型
     * @return <code>temporalType</code>を変換できる場合は適切なJDBCのSQL型、変換できない場合は{@link Types#OTHER}
     */
    public static int fromTemporalTypeToSqlType(final TemporalType temporalType) {
        if (temporalType == TemporalType.DATE) {
            return Types.DATE;
        } else if (temporalType == TemporalType.TIME) {
            return Types.TIME;
        } else if (temporalType == TemporalType.TIMESTAMP) {
            return Types.TIMESTAMP;
        }
        return Types.OTHER;
    }

}

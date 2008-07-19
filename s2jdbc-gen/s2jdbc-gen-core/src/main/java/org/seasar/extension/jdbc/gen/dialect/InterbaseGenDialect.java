/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.dialect;

import java.sql.Types;

import javax.persistence.GenerationType;

/**
 * Interbaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class InterbaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public InterbaseGenDialect() {
        dbTypeMap.put(Types.BIGINT, InterbaseDbType.BIGINT);
        dbTypeMap.put(Types.BINARY, InterbaseDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, InterbaseDbType.BOOLEAN);
        dbTypeMap.put(Types.CLOB, InterbaseDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, InterbaseDbType.DECIMAL);
        dbTypeMap.put(Types.DOUBLE, InterbaseDbType.DOUBLE);
        dbTypeMap.put(Types.TINYINT, InterbaseDbType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    /**
     * Interbase用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class InterbaseDbType extends StandardDbType {

        private static DbType BIGINT = new InterbaseDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,0)", precision);
            }
        };

        private static DbType BINARY = new InterbaseDbType("blob");

        private static DbType BOOLEAN = new InterbaseDbType("smallint");

        private static DbType CLOB = new InterbaseDbType("blob sub_type 1");

        private static DbType DECIMAL = new InterbaseDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("number(%d,%d)", precision, scale);
            }
        };

        private static DbType DOUBLE = new InterbaseDbType("double precision");

        private static DbType TINYINT = new InterbaseDbType("smallint");

        /**
         * インスタンスを構築します。
         */
        protected InterbaseDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected InterbaseDbType(String definition) {
            super(definition);
        }
    }
}

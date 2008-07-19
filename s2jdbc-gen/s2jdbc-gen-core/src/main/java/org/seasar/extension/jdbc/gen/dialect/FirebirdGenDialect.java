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
 * Firebirdの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class FirebirdGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public FirebirdGenDialect() {
        dbTypeMap.put(Types.BIGINT, FirebirdDbType.BIGINT);
        dbTypeMap.put(Types.BINARY, FirebirdDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, FirebirdDbType.BOOLEAN);
        dbTypeMap.put(Types.CLOB, FirebirdDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, FirebirdDbType.DECIMAL);
        dbTypeMap.put(Types.DOUBLE, FirebirdDbType.DOUBLE);
        dbTypeMap.put(Types.TINYINT, FirebirdDbType.TINYINT);

    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    /**
     * Firebird用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class FirebirdDbType extends StandardDbType {

        private static DbType BIGINT = new FirebirdDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,0)", precision);
            }
        };

        private static DbType BINARY = new FirebirdDbType("blob");

        private static DbType BOOLEAN = new FirebirdDbType("smallint");

        private static DbType CLOB = new FirebirdDbType("blob sub_type 1");

        private static DbType DECIMAL = new FirebirdDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("number(%d,%d)", precision, scale);
            }
        };

        private static DbType DOUBLE = new FirebirdDbType("double precision");

        private static DbType TINYINT = new FirebirdDbType("smallint");

        /**
         * インスタンスを構築します。
         */
        protected FirebirdDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected FirebirdDbType(String definition) {
            super(definition);
        }
    }
}

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
 * Sybaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class SybaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public SybaseGenDialect() {
        dbTypeMap.put(Types.BIGINT, SybaseDbType.BIGINT);
        dbTypeMap.put(Types.BINARY, SybaseDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, SybaseDbType.BOOLEAN);
        dbTypeMap.put(Types.BLOB, SybaseDbType.BLOB);
        dbTypeMap.put(Types.CLOB, SybaseDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, SybaseDbType.DECIMAL);
        dbTypeMap.put(Types.DOUBLE, SybaseDbType.DOUBLE);
        dbTypeMap.put(Types.INTEGER, SybaseDbType.INTEGER);
        dbTypeMap.put(Types.DATE, SybaseDbType.DATE);
        dbTypeMap.put(Types.TIME, SybaseDbType.TIME);
        dbTypeMap.put(Types.TIMESTAMP, SybaseDbType.TIMESTAMP);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "go";
    }

    /**
     * Sybase用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class SybaseDbType extends StandardDbType {

        private static DbType BIGINT = new SybaseDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,0)", precision);
            }
        };

        private static DbType BINARY = new SybaseDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varbinary(%d)", length);
            }

        };

        private static DbType BOOLEAN = new SybaseDbType("tinyint");

        private static DbType BLOB = new SybaseDbType("image");

        private static DbType CLOB = new SybaseDbType("text");

        private static DbType DECIMAL = new SybaseDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static DbType DOUBLE = new SybaseDbType("double precision");

        private static DbType INTEGER = new SybaseDbType("int");

        private static DbType DATE = new SybaseDbType("datetime");

        private static DbType TIME = new SybaseDbType("datetime");

        private static DbType TIMESTAMP = new SybaseDbType("datetime");

        /**
         * インスタンスを構築します。
         */
        protected SybaseDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected SybaseDbType(String definition) {
            super(definition);
        }
    }

}

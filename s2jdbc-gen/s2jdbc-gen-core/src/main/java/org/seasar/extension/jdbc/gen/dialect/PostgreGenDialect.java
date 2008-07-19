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
 * PostgreSQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class PostgreGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public PostgreGenDialect() {
        dbTypeMap.put(Types.BIGINT, PostgreDbType.BIGINT);
        dbTypeMap.put(Types.BINARY, PostgreDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, PostgreDbType.BOOLEAN);
        dbTypeMap.put(Types.BLOB, PostgreDbType.BLOB);
        dbTypeMap.put(Types.CLOB, PostgreDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, PostgreDbType.DECIMAL);
        dbTypeMap.put(Types.DOUBLE, PostgreDbType.DOUBLE);
        dbTypeMap.put(Types.FLOAT, PostgreDbType.FLOAT);
        dbTypeMap.put(Types.INTEGER, PostgreDbType.INTEGER);
        dbTypeMap.put(Types.SMALLINT, PostgreDbType.SMALLINT);
        dbTypeMap.put(Types.TINYINT, PostgreDbType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return dataType + " start with " + allocationSize + " increment by "
                + initValue;
    }

    /**
     * PostgreSQL用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class PostgreDbType extends StandardDbType {

        private static DbType BIGINT = new PostgreDbType("int8");

        private static DbType BINARY = new PostgreDbType("bytea");

        private static DbType BOOLEAN = new PostgreDbType("bool");

        private static DbType BLOB = new PostgreDbType("oid");

        private static DbType CLOB = new PostgreDbType("text");

        private static DbType DECIMAL = new PostgreDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static DbType DOUBLE = new PostgreDbType("float8");

        private static DbType FLOAT = new PostgreDbType("float4");

        private static DbType INTEGER = new PostgreDbType("int4");

        private static DbType SMALLINT = new PostgreDbType("int2");

        private static DbType TINYINT = new PostgreDbType("int2");

        /**
         * インスタンスを構築します。
         */
        protected PostgreDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected PostgreDbType(String definition) {
            super(definition);
        }
    }
}

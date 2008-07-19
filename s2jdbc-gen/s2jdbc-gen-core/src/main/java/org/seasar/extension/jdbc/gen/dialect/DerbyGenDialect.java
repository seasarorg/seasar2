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
 * Derbyの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class DerbyGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public DerbyGenDialect() {
        dbTypeMap.put(Types.BINARY, DerbyDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, DerbyDbType.BOOLEAN);
        dbTypeMap.put(Types.BLOB, DerbyDbType.BLOB);
        dbTypeMap.put(Types.CLOB, DerbyDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, DerbyDbType.DECIMAL);
        dbTypeMap.put(Types.TINYINT, DerbyDbType.TINYINT);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    /**
     * Derby用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class DerbyDbType extends StandardDbType {

        private static DbType BINARY = new DerbyDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varchar(%d) for bit data", length);
            }
        };

        private static DbType BOOLEAN = new DerbyDbType("smallint");

        private static DbType BLOB = new DerbyDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("blob(%d)", length);
            }
        };

        private static DbType CLOB = new DerbyDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("clob(%d)", length);
            }
        };

        private static DbType DECIMAL = new DerbyDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static DbType TINYINT = new DerbyDbType("smallint");

        /**
         * インスタンスを構築します。
         */
        protected DerbyDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected DerbyDbType(String definition) {
            super(definition);
        }
    }
}

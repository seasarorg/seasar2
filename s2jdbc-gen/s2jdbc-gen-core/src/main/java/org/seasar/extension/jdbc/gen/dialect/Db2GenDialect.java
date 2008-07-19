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
 * DB2の方言を扱うクラスです。
 * 
 * @author taedium
 */
public class Db2GenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public Db2GenDialect() {
        dbTypeMap.put(Types.BINARY, Db2DbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, Db2DbType.BOOLEAN);
        dbTypeMap.put(Types.BLOB, Db2DbType.BLOB);
        dbTypeMap.put(Types.CLOB, Db2DbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, Db2DbType.DECIMAL);
        dbTypeMap.put(Types.TINYINT, Db2DbType.TINYINT);
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
        return "as " + dataType + " start with " + allocationSize
                + " increment by " + initValue;
    }

    /**
     * DB2用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class Db2DbType extends StandardDbType {

        private static DbType BINARY = new Db2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varchar(%d) for bit data", length);
            }
        };

        private static DbType BOOLEAN = new Db2DbType("smallint");

        private static DbType BLOB = new Db2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("blob(%d)", length);
            }
        };

        private static DbType CLOB = new Db2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("clob(%d)", length);
            }
        };

        private static DbType DECIMAL = new Db2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static DbType TINYINT = new Db2DbType("smallint");

        /**
         * インスタンスを構築します。
         */
        protected Db2DbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected Db2DbType(String definition) {
            super(definition);
        }
    }
}

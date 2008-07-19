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
 * MySQLの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MysqlGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public MysqlGenDialect() {
        dbTypeMap.put(Types.BINARY, MysqlDbType.BINARY);
        dbTypeMap.put(Types.BLOB, MysqlDbType.BLOB);
        dbTypeMap.put(Types.CLOB, MysqlDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, MysqlDbType.DECIMAL);
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    /**
     * MySQL用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class MysqlDbType extends StandardDbType {

        private static DbType BINARY = new MysqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                if (length <= 0xFF) {
                    return "tinyblob";
                } else if (length <= 0xFFFF) {
                    return "blob";
                } else if (length <= 0xFFFFFF) {
                    return "mediumblob";
                }
                return "longblob";
            }
        };

        private static DbType BLOB = new MysqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                if (length <= 0xFF) {
                    return "tinyblob";
                } else if (length <= 0xFFFF) {
                    return "blob";
                } else if (length <= 0xFFFFFF) {
                    return "mediumblob";
                }
                return "longblob";
            }
        };

        private static DbType CLOB = new MysqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                if (length <= 0xFF) {
                    return "tinytext";
                } else if (length <= 0xFFFF) {
                    return "text";
                } else if (length <= 0xFFFFFF) {
                    return "mediumtext";
                }
                return "longtext";
            }
        };

        private static DbType DECIMAL = new MysqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected MysqlDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected MysqlDbType(String definition) {
            super(definition);
        }
    }
}

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
import java.util.Arrays;

import javax.persistence.GenerationType;

/**
 * MS SQL Serverの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class MssqlGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public MssqlGenDialect() {
        dbTypeMap.put(Types.BINARY, MssqlDbType.BINARY);
        dbTypeMap.put(Types.BOOLEAN, MssqlDbType.BOOLEAN);
        dbTypeMap.put(Types.BLOB, MssqlDbType.BLOB);
        dbTypeMap.put(Types.CLOB, MssqlDbType.CLOB);
        dbTypeMap.put(Types.DECIMAL, MssqlDbType.DECIMAL);
        dbTypeMap.put(Types.DOUBLE, MssqlDbType.DOUBLE);
        dbTypeMap.put(Types.INTEGER, MssqlDbType.INTEGER);
        dbTypeMap.put(Types.DATE, MssqlDbType.DATE);
        dbTypeMap.put(Types.TIME, MssqlDbType.TIME);
        dbTypeMap.put(Types.TIMESTAMP, MssqlDbType.TIMESTAMP);

        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getOpenQuote() {
        return "[";
    }

    @Override
    public String getCloseQuote() {
        return "]";
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "go";
    }

    /**
     * MS SQL Server用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class MssqlDbType extends StandardDbType {

        private static DbType BINARY = new MssqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("varbinary(%d)", length);
            }
        };

        private static DbType BOOLEAN = new MssqlDbType("bit");

        private static DbType BLOB = new MssqlDbType("image");

        private static DbType CLOB = new MssqlDbType("text");

        private static DbType DECIMAL = new MssqlDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("numeric(%d,%d)", precision, scale);
            }
        };

        private static DbType DOUBLE = new MssqlDbType("double precision");

        private static DbType INTEGER = new MssqlDbType("int");

        private static DbType DATE = new MssqlDbType("datetime");

        private static DbType TIME = new MssqlDbType("datetime");

        private static DbType TIMESTAMP = new MssqlDbType("datetime");

        /**
         * インスタンスを構築します。
         */
        protected MssqlDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected MssqlDbType(String definition) {
            super(definition);
        }
    }
}
